package json.generator

interface Visitor{
    fun visit(jsonValue: JsonValue,  key: String){ }
    fun endVisit(jsonObject: JsonObject){ }
    //    visit will be equal for all primitive types - change
    fun visit(jsonNumber: JsonNumber, key: String){ }
    fun visit(jsonString: JsonString, key: String){ }
    fun visit(jsonBoolean: JsonBoolean, key: String){ }
    fun visit(jsonNull: JsonNull, key: String){ }
    fun visit(jsonObject: JsonObject, key: String){ }
    fun visit(jsonArray: JsonArray, key: String){ }

}