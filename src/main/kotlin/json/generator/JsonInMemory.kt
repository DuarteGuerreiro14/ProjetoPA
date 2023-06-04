package json.generator

import kotlin.reflect.KClass


//change to consider if no key is sent
class Json(vararg properties: Pair<String,Any?>): JsonValue() {

    //    private val values = mutableListOf<JsonValue>()//list that will store all the
    val jsonValues = mutableListOf<Pair<String, JsonValue>>()//list that will store all the

    //loop through all the key-value pairs of Json Object
    init {
        properties.forEach { (name, value) ->
            when (value) {
                is Int -> this.addValue(Pair(name, JsonNumber(value)))
                is String -> this.addValue(Pair(name, JsonString(value)))
                is Boolean -> this.addValue(Pair(name, JsonBoolean(value)))
//                is List<*> -> this.addValue(Pair(name,JsonArray(value as List<JsonValue>)))
                is List<*> -> this.addValue(Pair(name, JsonArray(value as List<Any>)))
                is Map<*, *> -> this.addValue(Pair(name, JsonObject(value as Map<String, JsonValue>)))
//                is Map<*, *> -> value.forEach{
//                    this.addValue(Pair(it.key as String, it.value) as Pair<String, JsonValue>)}
                null -> this.addValue(Pair(name, JsonNull(null)))
//                else -> TODO()
            }
        }
    }

    override fun accept(visitor: Visitor, key: String) {
//        TODO()
        visitor.visit(this)
        jsonValues.forEach {
            it.second.accept(visitor, it.first) //make the JsonValue
        }
        visitor.endVisit(this)
    }


//    private fun addValue(keyValuePair: Pair<String, JsonValue>) {
    fun addValue(keyValuePair: Pair<String, JsonValue>) {
        jsonValues.add(keyValuePair)
    }


    fun getJsonContent(): String {
//        TODO()
        return jsonValues.joinToString(
            separator = ",",
            prefix = "{",
            postfix = "\n}",
            transform = ::generateJsonContent
        )
    }

    //    private fun generateJsonContent(pair: Pair<String, Any>): String {
    private fun generateJsonContent(pair: Pair<String, JsonValue>): String {
        var jsonContent = "\n\t\"${pair.first}\" : "

        if (pair.second is JsonString) {
            jsonContent += "\"${(pair.second as JsonString).value}\""
        } else if (pair.second is JsonNumber) {
            jsonContent += "${(pair.second as JsonNumber).value}"
        } else if (pair.second is JsonBoolean) {
            jsonContent += "${(pair.second as JsonBoolean).value}"
        } else if (pair.second is JsonNull) {
            jsonContent += "null"
        } else if (pair.second is JsonObject) {
            jsonContent += handleObject((pair.second as JsonObject).properties)
        } else if (pair.second is JsonArray) {

            if (isListOfMaps(((pair.second as JsonArray).elements as List<Any>))) {
                jsonContent += generateListOfMaps(pair.second as JsonArray)
            }

//            else if(jsonArrayHasObject((pair.second as JsonArray))){ //if the array has at least one Object ({})
//                jsonContent+= handleObject((pair.second as JsonObject).properties)
//                jsonContent+= handleObject((pair.second as JsonObject).properties)
//            }
            else {
                jsonContent += handleList((pair.second as JsonArray).elements as List<Any>)
            }
        }

        return jsonContent
    }


    fun handleList(listElements: List<Any?>): String {
        var jsonListContent = listElements.joinToString(separator = ", ", prefix = "[", postfix = "]") {
            when (it) {
                is String -> "\"${it}\""
                is Int -> "$it"
                is Boolean -> "$it"
                null -> "null"
                is List<Any?> -> handleList(it)
                else -> {
                    ""
                }
            }
        }

        return jsonListContent
    }


    private fun generateListOfMaps(jsonArray: JsonArray): String {
        val elementsJson =
            jsonArray.elements.map { handleObject(it as Map<String, Any>) }.joinToString(separator = ",\n")
        return "[\n$elementsJson\n]"
    }


    private fun handleObject(properties: Map<String, Any>): String {

        val jsonContentList = properties.entries.map { pair ->
            when (val value = pair.value) {
                is Int -> generateJsonContent(pair.key to JsonNumber(value))
                is String -> generateJsonContent(pair.key to JsonString(value))
                is Boolean -> generateJsonContent(pair.key to JsonBoolean(value))
                is List<*> -> generateJsonContent(pair.key to JsonArray(value as List<JsonValue>))
                is Map<*, *> -> generateJsonContent(pair.key to JsonObject(value as Map<String, JsonValue>))
                null -> generateJsonContent(pair.key to JsonNull(null))
                else -> TODO()
            }
        }
        return "{ ${jsonContentList.joinToString(",")} }"

    }

    private fun isListOfMaps(list: List<Any>): Boolean {
        return list.all { it is Map<*, *> }
    }


    //    if json array has objects, we have to handle identation
    private fun jsonArrayHasObject(jsonArray: JsonArray): Boolean {
        jsonArray.elements.forEach { element ->
            if (element is JsonObject) {
                return true
            } else if (element is JsonArray) {
                jsonArrayHasObject(element) //recursividade
            }
        }
        return false

    }

    //    function to get all the values from a specific key
    fun getValuesFromKey(keyName: String): MutableList<JsonValue> {
        val visitor = FindValuesByKey(keyName)
        this.accept(visitor)
//        print(visitor.getListOfValues())
        return visitor.getListOfValues()
    }

    fun getObjectsWithKeys(keysList: List<String>): MutableList<Any> {
        //need to implement a first part to check if the json structure has at least one object (JsonObject or object inside JsonArray)
        val visitor = FindObjectsWithKeys(keysList)
        this.accept(visitor)
        return visitor.getListOfObjects()
    }

    //    fun keyHasValueType(keyName: String, jsonValueType : KClass<T>){
    fun <T : JsonValue> keyHasValueType(keyName: String, jsonValueType : KClass<T>): Boolean {
        val keyValuesList = getValuesFromKey(keyName)
        return keyValuesList.all { jsonValueType.isInstance(it) }
    }


    fun arrayHasDefinedStructure(keyName: String): Boolean {
        val keyValuesList = (getValuesFromKey(keyName)[0] as JsonArray)
        val firstObject = keyValuesList.elements[0] as Map<String, Any>
//        put this in a loop in case keyValuesList has more than one element
//        println(keyValuesList)
        var previousStructure = mutableMapOf<String, String>()
        var currentStructure = mutableMapOf<String, String>()

        //more efficient: detect for each key, if a key (or value type) is different from the current, "break" immediately with false

        for ((key, value) in firstObject) {
            val valueType = value::class.simpleName ?: "Unknown"
            previousStructure[key] = valueType
        }


        println(previousStructure)

        keyValuesList.elements.forEach { currentObject ->

            currentStructure = mutableMapOf<String, String>()


            for ((key, value) in currentObject as Map<String, Any>) {
                val valueType = value::class.simpleName ?: "Unknown"
                currentStructure[key] = valueType
            }

            println("previous" + previousStructure)
            println("current" + currentStructure + "\n")

            if(previousStructure != currentStructure)
                return false

            previousStructure = currentStructure
//            currentStructure = mutableMapOf<String, String>()

        }

//        println(currentStructure)


        return true

//        return keyValuesList{}

//        keyValuesList.elements.forEach {
//            println(it)
////            println(it!!::class.java.typeName)
//        }
//        (keyValuesList[0] as JsonArray).elements.forEach {
//            println(it)
//        }
    }
}



    fun main(){


        val json = Json(
            Pair("uc", "PA"),
            Pair("ects", 123),
            Pair("data-exame", null),
            Pair("importante", true),
            Pair("perguntas", listOf("a", "b", "c")),
//            Pair("inscritos", listOf("um", "dois", 7)),
            Pair("inscritos", listOf(
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
                    "internacional" to false)))
        )


//        println(json.keyHasValueType("ects", JsonNumber::class))
        println(json.arrayHasDefinedStructure("inscritos"))


//        val json = Json(
//            Pair("uc", "PA"),
//            Pair("ects", 123),
//            Pair("data-exame", null),
//            Pair("importante", true),
//            Pair("perguntas", listOf("a", "b", "c")),
////            Pair("inscritos", listOf("um", "dois", 7)),
//            Pair("inscritos",
//                mapOf(
//                    "numero" to 10,
//                    "nome" to "Dave",
//                    "internacional" to true)
//        ))

//        val json2 = Json(
//            Pair("uc", "PA"),
//            Pair("ects", 123))


//    println(json.jsonValues)
//    println(json.getJsonContent())
//    println(json2.jsonValues)
//    println(json2.getJsonContent())

//    val visitorTest = object : Visitor{}
////    println(json.accept(visitorTest))
//    println(json.getValuesFromKey("uc"))
//    println(json.getValuesFromKey("numero"))
//    println(json.getValuesFromKey("internacional"))
//    println(json.getValuesFromKey("inscritos"))

//    println(json.getObjectsWithKeys(listOf("numero", "internacional", "nom"))) //this will not work because "nom" is not an identifier
//    println(json.getObjectsWithKeys(listOf("numero", "internacional", "nome", "teste_para_falhar")))
//    println(json.getObjectsWithKeys(listOf("numero", "internacional", "nome")))



    }
