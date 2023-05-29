package json.generator

class FindObjectsWithKeys(val keysList: List<String>) : Visitor{

//    private val listOfValues = mutableListOf<JsonValue>()
    private val listOfValues = mutableListOf<Any>()
//    val listOfValues = mutableListOf<Int>(1,2)


    fun getListOfObjects(): MutableList<Any> {
        return listOfValues
    }


    override fun visit(jsonArray: JsonArray, key: String) {

            jsonArray.elements.forEach {
                //as soon the code is optimized, this if should look like -> if(it is JsonObject)
                if (it is Map<*, *>) {
                    val o = JsonObject(it as Map<String, JsonValue>)
                    o.accept(this)
                }
            }

    }


    override fun visit(jsonObject: JsonObject, key: String) {

        val allKeysPresent = keysList.all { jsonObject.properties.containsKey(it) }
        if (allKeysPresent){
            listOfValues.add(jsonObject)
        }

    }

}
