package json.generator

//sealed class JsonValue {
//    abstract fun accept(visitor: Visitor, key: String = "")
//}

//interface that represents all the Json Objects
sealed interface JsonValue{
    val value: Any?
        get() = null
    var parent: JsonComplex?
    val observers: MutableList<*>

    fun accept(visitor: Visitor, key: String = "")
}

//Json Complex is the class for JsonObject and JsonArray objects
abstract class JsonComplex : JsonValue {
//    abstract fun modify(old: JsonValue, new: JsonValue)
    override val observers: MutableList<String> = mutableListOf()
}

//Json Primitive is the class for JsonNumber, JsonString, JsonBoolean and JsonNull
abstract class JsonPrimitive : JsonValue{
    override val observers: MutableList<String> = mutableListOf()
    override var parent: JsonComplex? = null
}

//data class JsonObject(val properties: Map<String, JsonValue>) : JsonComplex() {
data class JsonObject(override val value: MutableMap<String, JsonValue>) : JsonComplex() {
//    override val value: MutableMap<String, JsonValue> = mutableMapOf()
    override var parent: JsonComplex? = null

    override fun accept(visitor: Visitor, key: String) {
//        visitor.visit(this, key)
//        properties.forEach { (identifier, jsonValue) ->
//            jsonValue.accept(visitor, identifier)
//        }

        if (visitor is FindObjectsWithKeys){
            visitor.visit(this, key)
        }
        else if(visitor is FindValuesByKey){
            //insert loop for each das properties above
            value.forEach { (identifier, jsonValue) ->
                jsonValue.accept(visitor, identifier)
            }
        }
    }
}

data class JsonArray(override val value: MutableList<JsonValue>) : JsonComplex() {
// class JsonArray : JsonComplex() {

//    override val value: MutableList<JsonValue> = mutableListOf()
    override var parent: JsonComplex? = null

    override fun accept(visitor: Visitor, key: String) {
//        visitor.visit(this, key)
        value.forEach {
            it.accept(visitor, key)
        }
    }
}

data class JsonString(override val value: String) : JsonPrimitive() {
    override fun accept(visitor: Visitor, key: String) {
        visitor.visit(this, key)
    }
}

data class JsonNumber(override val value: Number) : JsonPrimitive() {
    override fun accept(visitor: Visitor, key: String) {
        visitor.visit(this, key)
    }
}

data class JsonBoolean(override val value: Boolean) : JsonPrimitive() {
    override fun accept(visitor: Visitor, key: String) {
        visitor.visit(this, key)
    }
}

data class JsonNull(override val value: Nothing? = null) : JsonPrimitive() {
    override fun accept(visitor: Visitor, key: String) {
        visitor.visit(this, key)
    }
}