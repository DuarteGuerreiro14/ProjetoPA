package json.generator

class FindObjectsWithKeys(private val keysList: List<String>) : Visitor{

    private val listOfValues = mutableListOf<JsonValue>()

    fun getListOfObjects(): MutableList<JsonValue> {
        return listOfValues
    }


    override fun visit(jsonObject: JsonObject, key: String) {
        val allKeysPresent = keysList.all { jsonObject.value.containsKey(it) }
        if (allKeysPresent){
            listOfValues.add(jsonObject)
        }
    }
}
