package json.generator

class FindValuesByKey(private val keyName: String) : Visitor{

    private val listOfValues = mutableListOf<JsonValue>()


    fun getListOfValues(): MutableList<JsonValue> {
        return listOfValues
    }

    override fun visit(jsonString: JsonString, key: String) {
        if (key == keyName) {
            listOfValues.add(jsonString)
        }
    }

    override fun visit(jsonNumber: JsonNumber, key: String) {
        if (key == keyName) {
            listOfValues.add(jsonNumber)
        }
    }

    override fun visit(jsonBoolean: JsonBoolean, key: String) {
        if (key == keyName) {
            listOfValues.add(jsonBoolean)
        }
    }

    override fun visit(jsonNull: JsonNull, key: String) {
        if (key == keyName) {
            listOfValues.add(jsonNull)
        }
    }

    override fun visit(jsonArray: JsonArray, key: String) {
        if (key == keyName) {
            listOfValues.add(jsonArray)
        }
    }


    override fun visit(jsonObject: JsonObject, key: String) {
        if (key == keyName) {
            listOfValues.add(jsonObject)
        }
    }
}