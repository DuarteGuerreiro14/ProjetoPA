package json.generator

import kotlin.reflect.KClass

//interface that represents all the Json Objects
interface JsonValue{
    val value: Any?
        get() = null
    var parent: JsonComplex?
    val observers: MutableList<JsonValueObserver>

    fun accept(visitor: Visitor, key: String = "")
    fun addObserver(observer: JsonValueObserver) = observers.add(observer)
    fun removeObserver(observer: JsonValueObserver) = observers.remove(observer)
}

//Json Complex is the class for JsonObject and JsonArray objects
abstract class JsonComplex : JsonValue {
    override val observers: MutableList<JsonValueObserver> = mutableListOf()

   abstract fun modify (jsonValueOld : JsonValue, jsonValueNew : JsonValue)
   abstract fun remove(jsonValue: JsonValue)
}
//Json Primitive is the class for JsonNumber, JsonString, JsonBoolean and JsonNull
abstract class JsonPrimitive : JsonValue{
    override val observers: MutableList<JsonValueObserver> = mutableListOf()
    override var parent: JsonComplex? = null
}
class JsonObject : JsonComplex() {
    override var value: MutableMap<String, JsonValue> = mutableMapOf()
    override var parent: JsonComplex? = null

    override fun toString(): String {
        return "JsonObject(value=$value)"
    }
    override fun accept(visitor: Visitor, key: String) {
        //do this on the function
        if (visitor is FindObjectsWithKeys){
            this.value.forEach { (_, jsonValue) ->
            if(jsonValue is JsonComplex) {
                if (jsonValue is JsonObject) visitor.visit(jsonValue, key)
                    if(jsonValue is JsonArray){
                    jsonValue.value.forEach {
                        if (it is JsonObject) visitor.visit(it, key)
                    }
                }

            }
            }
        }
        else if(visitor is FindValuesByKey){
            visitor.visit(this, key)
            //insert loop for each das properties above
            value.forEach { (identifier, jsonValue) ->
                jsonValue.accept(visitor, identifier)
            }
        }
        visitor.endVisit(this)
    }

    fun add(identifier: String, jsonValue: Any?){
        var jsonValueToAdd = jsonValue
        //if is not in JsonValue format, transform it
        if (jsonValueToAdd !is JsonValue){
            jsonValueToAdd = getJsonValue(jsonValue)
        }
        this.value[identifier] = jsonValueToAdd
        jsonValueToAdd.parent = this //the JsonObject is the parent of the added JsonValue
    }

    override fun remove (jsonValue: JsonValue) {
        var key = ""
            this.value.forEach {
                if (it.value.value == jsonValue) {
                    key = it.key
                }
            }
        this.value.remove(key)

        if(this.value.isEmpty()) {
            jsonValue.parent?.remove(this)
        }
    }

    override fun modify(jsonValueOld: JsonValue, jsonValueNew: JsonValue) {
        TODO("Not yet implemented")
    }
    private fun getJsonValue(value: Any?): JsonValue{
        return when (value) {
            is Int -> JsonNumber(value)
            is String -> JsonString(value)
            is Boolean -> JsonBoolean(value)
            is List<*> -> createJsonArray(value)
            is Map<*, *> -> createJsonObject(value)
            null -> JsonNull()
            else -> JsonNull() //TODO
        }
    }
    fun getJsonContent(): String {
//        TODO()
        return this.value.toList().joinToString(
            separator = ",",
            prefix = "{",
            postfix = "\n}",
            transform = ::generateJsonContent
        )
    }
    private fun generateJsonContent(pair: Pair<String, JsonValue>): String {
        var jsonContent = "\n\t\"${pair.first}\" : "
        val jsonValue = pair.second

        if(jsonValue is JsonComplex){
            if(jsonValue is JsonObject) jsonContent += handleObject(jsonValue.value)
            else if(jsonValue is JsonArray){
                jsonContent += if (jsonValue.isListOfMaps) generateListOfMaps(jsonValue)
                else handleList(jsonValue.value as List<Any>)
            }
        }
        else if(jsonValue is JsonPrimitive){
            if(jsonValue is JsonString){
                jsonContent += "\"${(pair.second as JsonString).value}\""
            }
            else if(jsonValue is JsonNull){
                jsonContent += "null"
            }
            else{
                jsonContent += "${jsonValue.value}"
            }
        }
        return jsonContent
    }

    private fun handleList(listElements: List<Any?>): String {
        var jsonListContent = listElements.joinToString(separator = ", ", prefix = "[", postfix = "]") {
            when (it) {
                is JsonString -> "\"${it.value}\""
                is JsonNumber -> "${it.value}"
                is JsonBoolean -> "${it.value}"
                is JsonNull -> "null"
                is JsonArray -> handleList(it as List<Any?>)
                else -> {
                    ""
                }
            }
        }
        return jsonListContent
    }
    private fun handleObject(value: Map<String, JsonValue>): String {

        val jsonContentList = value.entries.map { pair ->
            generateJsonContent(pair.key to pair.value)
        }
        return "{ ${jsonContentList.joinToString(",")} \n}"
    }


    private fun generateListOfMaps(jsonArray: JsonArray): String {
        val elementsJson =
            jsonArray.value.map { handleObject((it as JsonObject).value) }.joinToString(separator = ",\n")
        return "[\n$elementsJson\n]"
    }

    //create JsonArray object
    fun createJsonArray(jsonElements : List<Any?>): JsonArray{
        val jsonArray = JsonArray()
        jsonElements.forEach {
            var jsonValueToAdd = it
            //if is not in JsonValue format, transform it
            if (jsonValueToAdd !is JsonValue){
                jsonValueToAdd = getJsonValue(it)
            }
            jsonArray.add(jsonValueToAdd)
        }
        return jsonArray
    }

    //create JsonObject object
    fun createJsonObject(jsonProperties : Map<*,*>): JsonObject{
        val jsonObject = JsonObject()
        jsonProperties.forEach {
            jsonObject.add(it.key as String, it.value)
        }
        return jsonObject
    }

    //    function to get all the values from a specific key
    fun getValuesFromKey(keyName: String): MutableList<JsonValue> {
        val visitor = FindValuesByKey(keyName)
        this.accept(visitor)
        return visitor.getListOfValues()
    }
    fun <T : JsonValue> keyHasValueType(keyName: String, jsonValueType : KClass<T>): Boolean {
        val keyValuesList = getValuesFromKey(keyName)
        return keyValuesList.all { jsonValueType.isInstance(it) }
    }
    fun arrayHasDefinedStructure(keyName: String): Boolean {
        val objectsList = (getValuesFromKey(keyName))
        println(getValuesFromKey(keyName)[0])
        val firstObject = objectsList[0]
        println(firstObject)
        var previousStructure = mutableMapOf<String, String>()
        var currentStructure = mutableMapOf<String, String>()

        //more efficient: detect for each key, if a key (or value type) is different from the current, "break" immediately with false

//        for ((key, value) in firstObject.properties) {
        for ((key, value) in (firstObject as JsonObject).value) {
            val valueType = value::class.simpleName ?: "Unknown"
            previousStructure[key] = valueType
        }


        println(previousStructure)

        objectsList.forEach { currentObject ->

            currentStructure = mutableMapOf<String, String>()

            for ((key, value) in (currentObject as JsonObject).value) {
                val valueType = value::class.simpleName ?: "Unknown"
                currentStructure[key as String] = valueType
            }

            println("previous" + previousStructure)
            println("current" + currentStructure + "\n")

            if(previousStructure != currentStructure)
                return false

            previousStructure = currentStructure
//            currentStructure = mutableMapOf<String, String>()

        }
        return true

    }

    fun getObjectsWithKeys(keysList: List<String>): MutableList<JsonValue> {
        //need to implement a first part to check if the json structure has at least one object (JsonObject or object inside JsonArray)
        val visitor = FindObjectsWithKeys(keysList)
        this.accept(visitor)

        return visitor.getListOfObjects()
    }


}

class JsonArray : JsonComplex() {
// class JsonArray : JsonComplex() {

    override val value: MutableList<JsonValue> = mutableListOf()
    override var parent: JsonComplex? = null
    val isListOfMaps: Boolean
        get() = this.value.all { it is JsonObject }

    override fun toString(): String {
        return "JsonArray(value=$value)"
    }

    override fun accept(visitor: Visitor, key: String) {
//        visitor.visit(this, key)
        value.forEach {
            it.accept(visitor, key)
        }
    }

    fun add(jsonValue: JsonValue) {
        this.value.add(jsonValue)
        jsonValue.parent = this
    }

    override fun remove(jsonValue: JsonValue) {
        this.value.remove(jsonValue)
        observers.forEach { it.removedJsonValue(jsonValue) }
            println(this.parent)
        if (this.value.isEmpty()) {

            this.parent?.remove(this)

            //println(jsonValue.parent.toString() + "123")

        }
    }
        override fun modify(jsonValueOld: JsonValue, jsonValueNew: JsonValue) {
            TODO("Not yet implemented")
        }

    }

    class JsonString(override val value: String) : JsonPrimitive() {
        override fun accept(visitor: Visitor, key: String) {
            visitor.visit(this, key)
        }

        override fun toString(): String {
            return "JsonString(value=$value)"
        }
    }

    class JsonNumber(override val value: Number) : JsonPrimitive() {
        override fun accept(visitor: Visitor, key: String) {
            visitor.visit(this, key)
        }

        override fun toString(): String {
            return "JsonNumber(value=$value)"
        }
    }

    class JsonBoolean(override val value: Boolean) : JsonPrimitive() {
        override fun accept(visitor: Visitor, key: String) {
            visitor.visit(this, key)
        }

        override fun toString(): String {
            return "JsonBoolean(value=$value)"
        }
    }

    class JsonNull : JsonPrimitive() {
        override val value: Nothing? = null
        override fun accept(visitor: Visitor, key: String) {
            visitor.visit(this, key)
        }

        override fun toString(): String {
            return "JsonNull(value=$value)"
        }
    }
//class JsonNull(override val value: Nothing? = null) : JsonPrimitive() {
//    override fun accept(visitor: Visitor, key: String) {
//        visitor.visit(this, key)
//    }
//}

    fun main() {
        val json = JsonObject()
        json.add("uc", "PA")
        json.add("ects", 123)
        json.add("data-exame", null)
        json.add(
            "inscritos", listOf(
                mapOf(
                    "numero" to 10,
                    "nome" to "Dave",
                    "internacional" to true
                ),
                mapOf(
                    "numero" to 11,
                    "nome" to "Joao",
                    "internacional" to false
                ),
                mapOf(
                    "numero" to 11,
                    "nome" to "Andre",
                    "internacional" to false
                )
            )
        )

        val jsonArray = JsonArray()
        var jValue = JsonString("MEI")
        jsonArray.add(jValue)
       // jsonArray.add(JsonString("METI"))
        //jsonArray.add(JsonString("MEGI"))
        json.add("cursos", jsonArray)
        print(json.getJsonContent())
        jsonArray.remove(jValue)

        print("#########################################################")
        println(json.getJsonContent())
//    println(json.getValuesFromKey("cursos"))
//    println(json.arrayHasDefinedStructure("inscritos"))
////    println(json.getValuesFromKey("numero").forEach{ println(it.value)})
//    println(json.keyHasValueType("numero", JsonNumber::class))
       // println(json.getObjectsWithKeys(listOf("numero", "internacional", "nome")))
//    println(json.getObjectsWithKeys(listOf("numero")))
    }

    interface JsonValueObserver {
        fun addedJsonValue(identifier: String, jsonValue: JsonValue) {}
        fun modifiedJsonValue(jsonValueOld: JsonValue, jsonValueNew: JsonValue) {}
        fun removedJsonValue(jsonValue: JsonValue) {}
    }
