package json.generator

sealed class JsonValue {
    abstract fun accept(visitor: Visitor, key: String = "")
}

data class JsonObject(val properties: Map<String, JsonValue>) : JsonValue() {
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
            properties.forEach { (identifier, jsonValue) ->
                jsonValue.accept(visitor, identifier)
            }
        }
    }
}

data class JsonArray(val elements: List<JsonValue>) : JsonValue() {
    override fun accept(visitor: Visitor, key: String) {
//        visitor.visit(this, key)
        elements.forEach {
            it.accept(visitor, key)
        }
    }
}

data class JsonString(val value: String) : JsonValue() {
    override fun accept(visitor: Visitor, key: String) {
        visitor.visit(this, key)
    }
}

data class JsonNumber(val value: Number) : JsonValue() {
    override fun accept(visitor: Visitor, key: String) {
        visitor.visit(this, key)
    }
}

data class JsonBoolean(val value: Boolean) : JsonValue() {
    override fun accept(visitor: Visitor, key: String) {
        visitor.visit(this, key)
    }
}

data class JsonNull(val value: Nothing? = null) : JsonValue() {
    override fun accept(visitor: Visitor, key: String) {
        visitor.visit(this, key)
    }
}