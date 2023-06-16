package json.generator

class FindObjectsWithKeys(private val keysList: List<String>) : Visitor{

    private val listOfValues = mutableListOf<JsonValue>()
//    private val listOfValues = mutableListOf<Any>()


//    fun getListOfObjects(): MutableList<Any> {
    fun getListOfObjects(): MutableList<JsonValue> {
        return listOfValues
    }


//    override fun visit(jsonArray: JsonArray, key: String) {
//
//            jsonArray.elements.forEach {
//                if (it is JsonObject) {
//                    it.accept(this)
//                }
//            }
//    }


    override fun visit(jsonObject: JsonObject, key: String) {

//        val allKeysPresent = keysList.all { jsonObject.properties.containsKey(it) }
        val allKeysPresent = keysList.all { jsonObject.value.containsKey(it) }
        if (allKeysPresent){
            listOfValues.add(jsonObject)
        }
    }

}
