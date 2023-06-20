
# Biblioteca de Geração de Json
### Desenvolvido por : Duarte Guerreiro e Hugo Cristino

Esta página tem como principal objetivo fornecer instruções/tutoriais de como utilizar a biblioteca nas várias vertentes (simples, visitors, reflexão/anotações, observadores).

# Json Simples
De forma a otimizar a organização das diferentes classes existentes no modelo, optámos por criar um interface (JsonValue), que contém as variáveis que são comuns a todas as outras classes específicas.  Tendo também em conta as diferenças de operações que seriam possíveis de realizar, decidimos criar duas outras classes:
- JsonComplex : superclass de JsonObject e JsonArray
- JsonPrimitive : superclass de JsonString, JsonNumber, JsonBoolean e JsonNull

De seguida apresenta-se a forma de criar diferentes tipos de elementos Json.

```kotlin
val uc = JsonString("PA")
val ects = JsonNumber(10)
val hasExam = JsonBoolean(false)
val dateExame = JsonNull()
```

Para criar um objeto Json que vai conter todos os elementos anteriormente criados, devemos primeiro criar o "root", e de seguida, utilizar a função add(), sendo o primeiro parâmetro o identificador e o segundo o objeto JsonValue:

```kotlin
//Objeto principal que vai conter todos os elementos
val json = JsonObject()

json.add("uc", uc)
json.add("ects", ects)
json.add("hasExam", hasExam)
json.add("dateExam", dateExame)
```

De forma a criar um objeto JsonArray e posteriormente adicioná-lo à estrutura do Json criada acima:


```kotlin
//criar JsonArray
val perguntas = JsonArray()

//criar elementos do array 
val pergunta1 = JsonString("a)")
val pergunta2 = JsonString("b)")

//adicionar elementos ao array através da funçao add() da classe JsonArray
perguntas.add(pergunta1)
perguntas.add(pergunta2)

//adicionar array à estrutura 
json.add("perguntas", perguntas)
```
### Json Textual
Se desejarmos visualizar o modelo do nosso Json em formato textual, deveremos correr a seguinte linha de código, que irá gerar o output que se pode observar imediatamente abaixo:

```kotlin
println(json.getJsonContent())
```

```json
{
	"uc" : "PA",
	"ects" : 10,
	"hasExam" : false,
	"dateExam" : null,
	"perguntas" : ["a)", "b)"]
}

```
# Visitors

### Criação da estrutura Json
Antes de introduzirmos as operações que se podem realizar através do padrão de desenho Visitor, vamos passar a criar o objeto Json de forma a que seja igual ao apresentado no enunciado da 1a parte do projeto.

```kotlin
//criar estrutura principal
val json = JsonObject()


//criar elementos primitivos
val uc = JsonString("PA")
val ects = JsonNumber(10)
val dataExame = JsonNull()
json.add("uc", uc)
json.add("ects", ects)
json.add("data-exame", dataExame)

//criar o array de inscritos
val inscritosArray = JsonArray()

//cada um dos inscritos é um JsonObject, composto por outros JsonValue's
val inscrito1 = JsonObject()
val inscrito1nome = JsonString("Dave Farley")
val inscrito1numero = JsonNumber(101101)
val inscrito1int = JsonBoolean(true)
inscrito1.add("numero", inscrito1numero)
inscrito1.add("nome", inscrito1nome)
inscrito1.add("internacional", inscrito1int)

val inscrito2 = JsonObject()
val inscrito2nome = JsonString("Martin Fowler")
val inscrito2numero = JsonNumber(101102)
val inscrito2int = JsonBoolean(true)
inscrito2.add("numero", inscrito2numero)
inscrito2.add("nome", inscrito2nome)
inscrito2.add("internacional", inscrito2int)

val inscrito3 = JsonObject()
val inscrito3nome = JsonString("André Santos")
val inscrito3numero = JsonNumber(26503)
val inscrito3int = JsonBoolean(false)
inscrito3.add("numero", inscrito3numero)
inscrito3.add("nome", inscrito3nome)
inscrito3.add("internacional", inscrito3int)

//adicionar todos os inscritos ao array
inscritosArray.add(inscrito1)
inscritosArray.add(inscrito2)
inscritosArray.add(inscrito3)

//adicionar o array à estrutura
json.add("inscritos",inscritosArray)
```

Output do Json Textual:
```json
{
	"uc" : "PA",
	"ects" : 6,
	"data-exame" : null,
	"inscritos" : [
{ 
	"numero" : 101101,
	"nome" : "Dave Farley",
	"internacional" : true 
},
{ 
	"numero" : 101102,
	"nome" : "Martin Fowler",
	"internacional" : true 
},
{ 
	"numero" : 26503,
	"nome" : "André Santos",
	"internacional" : false 
}
]
}
```


O modelo oferece, através do padrão Visitor, uma forma de varrimento permitindo efetuar várias operações, expostas de seguida.

### Efetuar pesquisas, como por exemplo:

- obter todos os valores guardados em propriedades com identificador específico, por exemplo, numero:

```kotlin
val jsonValuesWithKeyNumero: List<JsonValue> = json.getValuesFromKey("numero")
println(jsonValuesWithKeyNumero) 
```
```text
Output: [JsonNumber(value=101101), JsonNumber(value=101102), JsonNumber(value=26503)]
```

- obter todos os objetos que têm determinadas propriedades, por exemplo, numero e nome:

```kotlin
val jsonObjectsWithKeys: List<JsonValue> = json.getObjectsWithKeys(listOf("numero", "nome"))
println(jsonObjectsWithKeys)
```
```text
Output: [JsonObject(value={numero=JsonNumber(value=101101), nome=JsonString(value=Dave Farley), internacional=JsonBoolean(value=true)}),
         JsonObject(value={numero=JsonNumber(value=101102), nome=JsonString(value=Martin Fowler), internacional=JsonBoolean(value=true)}), 
         JsonObject(value={numero=JsonNumber(value=26503), nome=JsonString(value=André Santos), internacional=JsonBoolean(value=false)})]
```

### Verificar que o modelo obedece a determinada estrutura, por exemplo:
- uma propriedade apenas tem um tipo de valores, por exemplo numero apenas tem como valores números inteiros:

```kotlin
val keyNumeroHasValueType: Boolean = json.keyHasValueType("numero", JsonNumber::class)
println(keyNumeroHasValueType)
```
```text
Output: true
```

- uma propriedade consiste num array onde todos os objetos têm a mesma estutura, por exemplo array de inscritos:
```kotlin
val inscritosHasDefinedStructure = json.arrayHasDefinedStructure("inscritos")
println(inscritosHasDefinedStructure)
```
```text
Output: true
```

# Inferência por reflexão

A biblioteca possui a funcionalidade de instanciar automaticamente o modelo por reflexão de quaisquer objetos de valor (data class).

## Anotações
De forma a adaptar a instanciação, foram definidas 3 anotações:

- ExcludeProperty - permite excluir propriedades da instanciação
- CustomizableIdentifier - permite utilizar identificadores personalizados (e não os das classes)
- ForceString - força alguns valores a serem considerados strings JSON

```kotlin
//Annotations
@Target(AnnotationTarget.PROPERTY)
annotation class ExcludeProperty

@Target(AnnotationTarget.PROPERTY)
annotation class CustomizableIdentifier(val newIdentifier: String)

@Target(AnnotationTarget.PROPERTY)
annotation class ForceString
```

Para exemplificar a instanciação por reflexão utilizando também as anotações previamente definidas, utilizaremos a seguinte classe de dados, e respetivo objeto criado:

```kotlin
data class Exam(
    @CustomizableIdentifier("unidadeCurricular")
    val uc: String,
    val credits: Int,
    @ForceString
    val maximumGrade: Int,
    val examDate: Nothing? = null,
    val isImportant: Boolean,
    @ExcludeProperty
    val inEnglish: Boolean,
    val enrolled: List<Any>,
    val requirements: Map<String, Any>
)

val exam = Exam("PA", 123,20, null, true, false,
        listOf(
                mapOf(
                    "numero" to 10,
                    "nome" to "Dave",
                    "internacional" to true),
                mapOf(
                    "numero" to 11,
                    "nome" to "Joao",
                    "internacional" to false),
                mapOf(
                    "numero" to 11,
                    "nome" to "Joao",
                    "internacional" to false)),
        mapOf("nota_minima" to 10,"projeto" to true)
    )
```

É possível verificar que:
- a propriedade uc tem um identificador personalizado, pelo que é expectável que o identificador final no objeto JsonObject criado seja "unidadeCurricular" em detrimento de "uc"
- a propriedades maximumGrade, apesar de ser do tipo inteiro, deve ser forçado a comportar-se como String
- a propriedade inEnglish deve ser ignorada no momento da criação do objeto

## Instanciação do modelo

No próximo passo, e para confirmar os pontos acima, deve ser chamada a função createJson() que recebe como parâmetro um objeto de valor.

```kotlin
val json = createJson(exam)
println(json.getJsonContent())
```
```json
Output: 
{
	"unidadeCurricular" : "PA",
	"credits" : 123,
	"maximumGrade" : "20",
	"examDate" : null,
	"isImportant" : true,
	"enrolled" : [
{ 
	"numero" : 10,
	"nome" : "Dave",
	"internacional" : true 
},
{ 
	"numero" : 11,
	"nome" : "Joao",
	"internacional" : false 
},
{ 
	"numero" : 11,
	"nome" : "Joao",
	"internacional" : false 
}
],
	"requirements" : { 
	"nota_minima" : 10,
	"projeto" : true 
}
}
```

Confirma-se que "unidadeCurricular" foi definida como identificador, a propriedade maximumGrade foi adicionada à estrutura como string ao invés de inteiro, e , por fim, a propriedade inEnglish foi excluída da instanciação

# Observadores