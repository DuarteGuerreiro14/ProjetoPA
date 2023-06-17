//package json.generator
//
//import kotlin.reflect.KClass
//
//
////change to consider if no key is sent
//class Json(vararg properties: Pair<String,Any?>): JsonValue {
//
//    override var parent: JsonComplex? = null
//    override val observers: MutableList<*>
//        get() {
//            TODO()
//        }
//
//    //    private val values = mutableListOf<JsonValue>()//list that will store all the
//    val jsonValues = mutableListOf<Pair<String, JsonValue>>()//list that will store all the
//
//    //loop through all the key-value pairs of Json Object
//    init {
//        properties.forEach { (name, value) ->
//            this.addValue(Pair(name, getJsonValue(value)))
//        }
//    }
//
//    //create JsonValue object after getting the data type
//    private fun getJsonValue(value: Any?): JsonValue{
//        return when (value) {
//            is Int -> JsonNumber(value)
//            is String -> JsonString(value)
//            is Boolean -> JsonBoolean(value)
//            is List<*> -> createJsonArray(value)
//            is Map<*, *> -> createJsonObject(value)
//            null -> JsonNull(null)
//            else -> JsonNull(null) //TODO
//        }
//    }
//
//     //create JsonArray object
//     fun createJsonArray(jsonElements : List<Any?>): JsonArray{
//        val elements = mutableListOf<JsonValue>()
//        jsonElements.forEach {
//    //                elements.add(it as JsonValue)
//                elements.add(getJsonValue(it))
//        }
//
////        val temporaryJsonArray = JsonArray(elements)
////        elements.forEach { it.parent = temporaryJsonArray }
//        return JsonArray(elements)
//    }
//
//    //create JsonObject object
//     fun createJsonObject(jsonProperties : Map<*,*>): JsonObject{
//        val properties = mutableMapOf<String, JsonValue>()
//        jsonProperties.forEach {
//            properties[it.key as String] = getJsonValue(it.value)
//        }
//        return JsonObject(properties)
//    }
//
//
//    //    private fun addValue(keyValuePair: Pair<String, JsonValue>) {
//    fun addValue(keyValuePair: Pair<String, JsonValue>) {
//        jsonValues.add(keyValuePair)
//        val jsonValue = keyValuePair.second
//
////        if (jsonValue is JsonComplex){
//            if(jsonValue is JsonArray) {
//                jsonValue.value.forEach {
//                    it.parent = jsonValue
//                }
//            }
//
//            else if(jsonValue is JsonObject){
//                jsonValue.value.forEach {
//                    it.value.parent = jsonValue
//                }
//            }
////        }
//    }
//
//    fun printParents(){
//        jsonValues.forEach {
//            if(it.second.parent != null){
//                println("Parent ${it.second.parent} ")
//            }
//        }
//    }
//
//
//    override fun accept(visitor: Visitor, key: String) {
////        TODO()
//        visitor.visit(this)
//        jsonValues.forEach {
//            it.second.accept(visitor, it.first) //make the JsonValue
//        }
//        visitor.endVisit(this)
//    }
//
//
//    fun getJsonContent(): String {
////        TODO()
//        return jsonValues.joinToString(
//            separator = ",",
//            prefix = "{",
//            postfix = "\n}",
//            transform = ::generateJsonContent
//        )
//    }
//
//    //    private fun generateJsonContent(pair: Pair<String, Any>): String {
//    private fun generateJsonContent(pair: Pair<String, JsonValue>): String {
//        var jsonContent = "\n\t\"${pair.first}\" : "
//
//        if (pair.second is JsonString) {
//            jsonContent += "\"${(pair.second as JsonString).value}\""
//        } else if (pair.second is JsonNumber) {
//            jsonContent += "${(pair.second as JsonNumber).value}"
//        } else if (pair.second is JsonBoolean) {
//            jsonContent += "${(pair.second as JsonBoolean).value}"
//        } else if (pair.second is JsonNull) {
//            jsonContent += "null"
//        } else if (pair.second is JsonObject) {
////            jsonContent += handleObject((pair.second as JsonObject).properties)
//            jsonContent += handleObject((pair.second as JsonObject).value)
//        } else if (pair.second is JsonArray) {
//
////            if (isListOfMaps(((pair.second as JsonArray).elements as List<Any>))) {
//            if (isListOfMaps(((pair.second as JsonArray).value as List<Any>))) {
//                jsonContent += generateListOfMaps(pair.second as JsonArray)
//            }
//
//            else {
////                jsonContent += handleList((pair.second as JsonArray).elements as List<Any>)
//                jsonContent += handleList((pair.second as JsonArray).value as List<Any>)
////                jsonContent += (pair.second as JsonArray).elements.forEach(generateJsonContent())
//            }
//        }
//
//        return jsonContent
//    }
//
//
//    private fun handleList(listElements: List<Any?>): String {
//        var jsonListContent = listElements.joinToString(separator = ", ", prefix = "[", postfix = "]") {
//            when (it) {
//                is JsonString -> "\"${it.value}\""
//                is JsonNumber -> "${it.value}"
//                is JsonBoolean -> "${it.value}"
//                is JsonNull -> "null"
////                is List<Any?> -> handleList(it)
//                is JsonArray -> handleList(it as List<Any?>)
//                else -> {
//                    ""
//                }
//            }
//        }
//
//        return jsonListContent
//    }
//
//
//    private fun generateListOfMaps(jsonArray: JsonArray): String {
//        val elementsJson =
////            jsonArray.elements.map { handleObject(it as Map<String, Any>) }.joinToString(separator = ",\n")
////            jsonArray.elements.map { handleObject((it as JsonObject).properties) }.joinToString(separator = ",\n")
//            jsonArray.value.map { handleObject((it as JsonObject).value) }.joinToString(separator = ",\n")
//        return "[\n$elementsJson\n]"
//    }
//
//
//    private fun handleObject(properties: Map<String, Any>): String {
//
//        val jsonContentList = properties.entries.map { pair ->
//            when (val type = pair.value) {
////                is Int -> generateJsonContent(pair.key to JsonNumber(value))
////                is String -> generateJsonContent(pair.key to JsonString(value))
////                is Boolean -> generateJsonContent(pair.key to JsonBoolean(value))
////                is List<*> -> generateJsonContent(pair.key to JsonArray(value as List<JsonValue>))
////                is Map<*, *> -> generateJsonContent(pair.key to JsonObject(value as Map<String, JsonValue>))
////                null -> generateJsonContent(pair.key to JsonNull(null))
////                else -> TODO()
//
//
//                //isto pode ser alterado para ser feito diretamente no if
//                is JsonNumber -> generateJsonContent(pair.key to JsonNumber(type.value))
//                is JsonString -> generateJsonContent(pair.key to JsonString(type.value))
//                is JsonBoolean -> generateJsonContent(pair.key to JsonBoolean(type.value))
////                is JsonArray -> generateJsonContent(pair.key to JsonArray(type.elements))
//                is JsonArray -> generateJsonContent(pair.key to JsonArray(type.value))
//                is JsonObject -> generateJsonContent(pair.key to JsonObject(type.value))
//                is JsonNull -> generateJsonContent(pair.key to JsonNull(type.value))
//                else -> TODO()
//            }
//        }
//        return "{ ${jsonContentList.joinToString(",")} }"
//
//    }
//
//    private fun isListOfMaps(list: List<Any>): Boolean {
////        return list.all { it is Map<*, *> }
//        return list.all { it is JsonObject}
//    }
//
//
//    //    if json array has objects, we have to handle identation
//    private fun jsonArrayHasObject(jsonArray: JsonArray): Boolean {
////        jsonArray.elements.forEach { element ->
//        jsonArray.value.forEach { element ->
//            if (element is JsonObject) {
//                return true
//            } else if (element is JsonArray) {
//                jsonArrayHasObject(element) //recursividade
//            }
//        }
//        return false
//
//    }
//
//    //    function to get all the values from a specific key
//    fun getValuesFromKey(keyName: String): MutableList<JsonValue> {
//        val visitor = FindValuesByKey(keyName)
//        this.accept(visitor)
////        print(visitor.getListOfValues())
//        return visitor.getListOfValues()
//    }
//
////    fun getObjectsWithKeys(keysList: List<String>): MutableList<Any> {
//    fun getObjectsWithKeys(keysList: List<String>): MutableList<JsonValue> {
//        //need to implement a first part to check if the json structure has at least one object (JsonObject or object inside JsonArray)
//        val visitor = FindObjectsWithKeys(keysList)
//        this.accept(visitor)
//        return visitor.getListOfObjects()
//    }
//
//    //    fun keyHasValueType(keyName: String, jsonValueType : KClass<T>){
//    fun <T : JsonValue> keyHasValueType(keyName: String, jsonValueType : KClass<T>): Boolean {
//        val keyValuesList = getValuesFromKey(keyName)
//        return keyValuesList.all { jsonValueType.isInstance(it) }
//    }
//
//
//    fun arrayHasDefinedStructure(keyName: String): Boolean {
//        val keyValuesList = (getValuesFromKey(keyName)[0] as JsonArray)
////        val firstObject = keyValuesList.elements[0] as Map<String, Any>
////        val firstObject = keyValuesList.elements[0] as JsonObject
//        val firstObject = keyValuesList.value[0] as JsonObject
////        put this in a loop in case keyValuesList has more than one element
////        println(keyValuesList)
//        var previousStructure = mutableMapOf<String, String>()
//        var currentStructure = mutableMapOf<String, String>()
//
//        //more efficient: detect for each key, if a key (or value type) is different from the current, "break" immediately with false
//
////        for ((key, value) in firstObject.properties) {
//        for ((key, value) in firstObject.value) {
//            val valueType = value::class.simpleName ?: "Unknown"
//            previousStructure[key] = valueType
//        }
//
//
//        println(previousStructure)
//
////        keyValuesList.elements.forEach { currentObject ->
//        keyValuesList.value.forEach { currentObject ->
//
//            currentStructure = mutableMapOf<String, String>()
//
//
////            for ((key, value) in (currentObject as JsonObject).properties) {
//            for ((key, value) in (currentObject as JsonObject).value) {
//                val valueType = value::class.simpleName ?: "Unknown"
//                currentStructure[key as String] = valueType
//            }
//
//            println("previous" + previousStructure)
//            println("current" + currentStructure + "\n")
//
//            if(previousStructure != currentStructure)
//                return false
//
//            previousStructure = currentStructure
////            currentStructure = mutableMapOf<String, String>()
//
//        }
//
////        println(currentStructure)
//
//
//        return true
//
////        return keyValuesList{}
//
////        keyValuesList.elements.forEach {
////            println(it)
//////            println(it!!::class.java.typeName)
////        }
////        (keyValuesList[0] as JsonArray).elements.forEach {
////            println(it)
////        }
//    }
//}
//
//
//
//    fun main(){
//
//
//        val json = Json(
//            Pair("uc", "PA"),
//            Pair("ects", 123),
//            Pair("data-exame", null),
//            Pair("importante", true),
//            Pair("perguntas", listOf("a", "b", "c", null)),
////            Pair("inscritos", listOf("um", "dois", 7)),
//            Pair("inscritos", listOf(
//                mapOf(
//                "numero" to 10,
//                "nome" to "Dave",
//                "internacional" to true),
//                mapOf(
//                "numero" to 11,
//                "nome" to "Joao",
//                "internacional" to false),
//                mapOf(
//                    "numero" to 11,
//                    "nome" to "Joao",
//                    "internacional" to false)))
//        )
//
//
////        println(json.keyHasValueType("ects", JsonNumber::class))
////        println(json.arrayHasDefinedStructure("inscritos"))
//
//
//
//    println(json.jsonValues)
//    json.printParents()
//    println(json.getJsonContent())
////    println(json2.jsonValues)
////    println(json2.getJsonContent())
//
////    val visitorTest = object : Visitor{}
//////    println(json.accept(visitorTest))
////    println(json.getValuesFromKey("uc"))
//    println(json.getValuesFromKey("numero"))
////    println(json.getValuesFromKey("nome"))
////    println(json.getValuesFromKey("data-exame"))
////    println(json.getValuesFromKey("internacional"))
////    println(json.getValuesFromKey("inscritos"))
//
//    println(json.getObjectsWithKeys(listOf("numero", "internacional", "nome"))) //this will not work because "nom" is not an identifier
////    println(json.getObjectsWithKeys(listOf("numero", "internacional", "nome", "teste_para_falhar")))
////    println(json.getObjectsWithKeys(listOf("numero", "internacional")))
//
//
//
//    }
