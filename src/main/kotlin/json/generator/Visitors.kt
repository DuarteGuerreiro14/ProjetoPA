package json.generator

interface Visitor{
    fun visit(jsonElement: JsonValue,  key: String){ } //Boolean = true
//    fun visit(jsonStructure: Json){ } //Boolean = true
//    fun endVisit(jsonStructure: Json){ } //Boolean = true
    fun endVisit(jsonObject: JsonObject){ } //Boolean = true
    //    visit will be equal for all primitive types(?)
    fun visit(jsonNumber: JsonNumber, key: String){ } //Boolean = true
    fun visit(jsonString: JsonString, key: String){ } //Boolean = true
    fun visit(jsonBoolean: JsonBoolean, key: String){ } //Boolean = true
    fun visit(jsonNull: JsonNull, key: String){ } //Boolean = true
    fun visit(jsonObject: JsonObject, key: String){ } //Boolean = true
    fun visit(jsonArray: JsonArray, key: String){ } //Boolean = true
//    fun visit(jsonValues: List<Pair<String,JsonValue>>, key: String) //to use in getKey

//    fun endVisit(directory: DirectoryElement) { }
//    fun visit(file: FileElement) { }
}