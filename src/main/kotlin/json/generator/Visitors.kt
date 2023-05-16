package json.generator

interface Visitor{
    fun visit(jsonElement: JsonValue,  key: String){ } //Boolean = true
    fun visit(jsonStructure: Json){ } //Boolean = true
    fun endVisit(jsonStructure: Json){ } //Boolean = true
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

class FindValuesByKey(val keyName: String) : Visitor{

    private val listOfValues = mutableListOf<JsonValue>()
//    val listOfValues = mutableListOf<Int>(1,2)


    fun getListOfValues(): MutableList<JsonValue> {
        return listOfValues
    }

    override fun visit(jsonString: JsonString, key: String) {
        if (key == keyName) {
            println("found string for key $keyName!!!!")
            listOfValues.add(jsonString)
        }
    }

    override fun visit(jsonNumber: JsonNumber, key: String) {
        if (key == keyName) {
            println("found number for key $keyName!!!!")
            listOfValues.add(jsonNumber)
        }
    }

    override fun visit(jsonBoolean: JsonBoolean, key: String) {
        if (key == keyName) {
            println("found boolean for key $keyName!!!!")
            listOfValues.add(jsonBoolean)
        }
    }

    override fun visit(jsonNull: JsonNull, key: String) {
        if (key == keyName) {
            println("found null for key $keyName!!!!")
        }
    }

    override fun visit(jsonArray: JsonArray, key: String) {
        if (key == keyName) {
            println("found list for key $keyName!!!!")
            listOfValues.add(jsonArray)
        } else {
            jsonArray.elements.forEach {
                if (it is Map<*, *>) {
//                            val o = JsonObject("", it as Map<String, JsonValue>)
                    val o = JsonObject(it as Map<String, JsonValue>)
//                            println("$it is object inside list")
                    o.accept(this)
                }
            }
        }
    }


    override fun visit(jsonObject: JsonObject, key: String) {
        if (key == keyName) {
            println("found list for key $keyName!!!!")
        } else {
            jsonObject.properties.forEach {
                // possible change - change this to make it as it is for if inside visit jsonArray, so that every element is treated as JsonValue of specific type
                if (it.key == keyName) {
                    println("found element inside object for key $keyName!!!!")
                    listOfValues.add(it.value) //it.value is JsonValue
                }
            }
        }
    }
}