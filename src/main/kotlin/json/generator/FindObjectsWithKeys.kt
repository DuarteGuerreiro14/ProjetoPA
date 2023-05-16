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

//            jsonObject.properties.forEach {
//                // possible change - change this to make it as it is for if inside visit jsonArray, so that every element is treated as JsonValue of specific type
//                if (it.key in keysList){
//                    println("${it.key} found inside $keysList!!!!")
////                    listOfValues.add(it.value) //it.value is JsonValue
//                }
//            }
        }

}
