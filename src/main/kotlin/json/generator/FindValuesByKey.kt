package json.generator

class FindValuesByKey(private val keyName: String) : Visitor{

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
            listOfValues.add(jsonNull)
        }
    }

    override fun visit(jsonArray: JsonArray, key: String) {
        if (key == keyName) {
            println("found list for key $keyName!!!!")
            listOfValues.add(jsonArray)
        }
        //        else {
//            jsonArray.elements.forEach {
//                if (it is JsonObject) {
//                    it.accept(this)
//                }
//            }
//        }
    }


    override fun visit(jsonObject: JsonObject, key: String) {
        if (key == keyName) {
            println("found list for key $keyName!!!!")
            listOfValues.add(jsonObject)
        }
//        else {
//            jsonObject.properties.forEach {
//                // possible change - change this to make it as it is for if inside visit jsonArray, so that every element is treated as JsonValue of specific type
//                if (it.key == keyName) {
//                    println("found element inside object for key $keyName!!!!")
//                    listOfValues.add(it.value) //it.value is JsonValue
//                }
//            }
//        }
    }
}