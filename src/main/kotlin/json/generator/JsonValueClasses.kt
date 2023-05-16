package json.generator

sealed class JsonValue {
    abstract fun accept(visitor: Visitor, key: String = "")
}

data class JsonObject(val properties: Map<String, JsonValue>) : JsonValue() {
    override fun accept(visitor: Visitor, key: String) {
        visitor.visit(this, key)
    }
}

data class JsonArray(val elements: List<Any?>) : JsonValue() {
    override fun accept(visitor: Visitor, key: String) {
        visitor.visit(this, key)
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