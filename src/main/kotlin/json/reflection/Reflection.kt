package json.reflection

import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.KProperty
import kotlin.reflect.full.*
//import kotlin.reflect.full.findAnnotations

//Annotations
@Target(AnnotationTarget.PROPERTY)
annotation class ExcludeProperty

@Target(AnnotationTarget.PROPERTY)
annotation class CustomizableIdentifier(val newIdentifier: String)

@Target(AnnotationTarget.PROPERTY)
annotation class ForceString


// obtem lista de atributos pela ordem do construtor primario
val KClass<*>.dataClassFields: List<KProperty<*>>
    get() {
        require(isData) { "instance must be data class" }
        return primaryConstructor!!.parameters.map { p ->
            declaredMemberProperties.find { it.name == p.name }!!
        }
    }

// saber se um KClassifier é um enumerado
val KClassifier?.isEnum: Boolean
    get() = this is KClass<*> && this.isSubclassOf(Enum::class)




fun main(){
    val fields: List<KProperty<*>> = Exam::class.dataClassFields
    println(fields)

    val exam = Exam("PA", 123,20, null, true,
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

}



