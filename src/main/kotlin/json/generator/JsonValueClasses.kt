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
                if (it.value == jsonValue) {
                    key = it.key
                }
            }
        this.value.remove(key)

        if(this.value.isEmpty()) {
            jsonValue.parent?.remove(this)
        }
    }

    override fun modify(jsonValueOld: JsonValue, jsonValueNew: JsonValue) {
        var key = this.value.entries.find { it.value == jsonValueOld }?.key
        if(key != null) {
            this.value.put(key, jsonValueNew)
        }
        jsonValueNew.parent = this
        observers.forEach { it.modifiedJsonValue(jsonValueOld, jsonValueNew) }
    }


     fun getJsonValue(value: Any?): JsonValue{
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

    //function to check if a key is composed of JsonValue objects of an exclusive type
    fun <T : JsonValue> keyHasValueType(keyName: String, jsonValueType : KClass<T>): Boolean {
        val keyValuesList = getValuesFromKey(keyName)
        return keyValuesList.all { jsonValueType.isInstance(it) }
    }

    //function to verify if an array has a defined structure (ex: all the JsonObjects inside a list have the same properties)
    fun arrayHasDefinedStructure(keyName: String): Boolean {
        val objectsList = (getValuesFromKey(keyName))
        val firstObject = objectsList[0]

        var previousStructure = mutableMapOf<String, String>()
        var currentStructure = mutableMapOf<String, String>()


        for ((key, value) in (firstObject as JsonObject).value) {
            val valueType = value::class.simpleName ?: "Unknown"
            previousStructure[key] = valueType
        }

        objectsList.forEach { currentObject ->
            currentStructure = mutableMapOf<String, String>()

            for ((key, value) in (currentObject as JsonObject).value) {
                val valueType = value::class.simpleName ?: "Unknown"
                currentStructure[key as String] = valueType
            }

            if(previousStructure != currentStructure)
                return false
            previousStructure = currentStructure

        }

        return true

    }

    fun getObjectsWithKeys(keysList: List<String>): MutableList<JsonValue> {

        val visitor = FindObjectsWithKeys(keysList)
        this.accept(visitor)

        return visitor.getListOfObjects()
    }


}

class JsonArray : JsonComplex() {

    override val value: MutableList<JsonValue> = mutableListOf()
    override var parent: JsonComplex? = null
    val isListOfMaps: Boolean
        get() = this.value.all { it is JsonObject }

    override fun toString(): String {
        return "JsonArray(value=$value)"
    }

    override fun accept(visitor: Visitor, key: String) {
        value.forEach {
            it.accept(visitor, key)
        }
    }

    fun add(jsonValue: JsonValue) {
        this.value.add(jsonValue)
        jsonValue.parent = this
    }

    override fun remove(jsonValue: JsonValue) {

        var parentKeyToRemove = ""
        (parent as JsonObject).value.forEach { (identifier, currentJsonValue) ->
            if(currentJsonValue is JsonArray){
                (currentJsonValue as JsonArray).value.forEach {
                    if(it == jsonValue) parentKeyToRemove = identifier
                }
            }
        }
        this.value.remove(jsonValue)

        observers.forEach { it.removedJsonValue(jsonValue) }
        if (this.value.isEmpty()) {
            (parent as JsonObject).value.remove(parentKeyToRemove)
        }
    }


    override fun modify(jsonValueOld: JsonValue, jsonValueNew: JsonValue) {
        var listAux = mutableListOf<JsonValue>()
        listAux.addAll(this.value)
        listAux.forEachIndexed { index, jsonValue ->
            if(jsonValue == jsonValueOld) {
                this.value[index] = jsonValueNew
                jsonValueNew.parent = this
                observers.forEach { it.modifiedJsonValue(jsonValueOld,jsonValueNew) }
            }
        }

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


    fun main() {

        val json = JsonObject()

        val jsonPA = JsonString("PA")

        json.add("uc", jsonPA)
        json.add("ects", 6)
        json.add("data-exame", null)


        val inscritosArray = JsonArray()

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

        inscritosArray.add(inscrito1)
        inscritosArray.add(inscrito2)
        inscritosArray.add(inscrito3)

        json.add("inscritos",inscritosArray)
        println(json.getJsonContent())

    }

    interface JsonValueObserver {
        fun addedJsonValue(identifier: String, jsonValue: JsonValue) {}
        fun modifiedJsonValue(jsonValueOld: JsonValue, jsonValueNew: JsonValue) {}
        fun removedJsonValue(jsonValue: JsonValue) {}
    }
