package json.generator

sealed class JsonValue {
    data class JsonObject(val properties: Map<String, JsonValue>) : JsonValue()
    data class JsonArray(val elements: List<JsonValue>) : JsonValue()
    data class JsonString(val value: String) : JsonValue()
    data class JsonNumber(val value: Number) : JsonValue()
    data class JsonBoolean(val value: Boolean) : JsonValue()
//    object JsonNull : JsonValue()
    data class JsonNull(val value: Nothing? = null) : JsonValue()

}


//change to consider if no key is sent
class Json(vararg properties: Pair<String,Any?>){

//    private val values = mutableListOf<JsonValue>()//list that will store all the
    val jsonValues = mutableListOf<Pair<String,JsonValue>>()//list that will store all the

    //loop through all the key-value pairs of Json Object
    init {
        properties.forEach {(name, value)->
            when(value){
                is Int -> this.addValue(Pair(name, JsonValue.JsonNumber(value)))
                is String -> this.addValue(Pair(name,JsonValue.JsonString(value)))
                is Boolean -> this.addValue(Pair(name,JsonValue.JsonBoolean(value)))
                is List<*> -> this.addValue(Pair(name,JsonValue.JsonArray(value as List<JsonValue>)))
                is Map<*, *> -> this.addValue(Pair(name, JsonValue.JsonObject(value as Map<String, JsonValue>)))
//                is Map<*, *> -> value.forEach{
//                    this.addValue(Pair(it.key as String, it.value) as Pair<String, JsonValue>)}
                null -> this.addValue(Pair(name,JsonValue.JsonNull(null)))
                else -> TODO()
            }
        }
    }

    fun addValue(keyValuePair:  Pair<String,JsonValue>){
        jsonValues.add(keyValuePair)
    }


    fun getJsonContent(): String {
//        TODO()
        return jsonValues.joinToString(separator = ",", prefix = "{", postfix = "\n}", transform = ::generateJsonContent)
    }

//    private fun generateJsonContent(pair: Pair<String, Any>): String {
    private fun generateJsonContent(pair: Pair<String, JsonValue>): String {
        return  if (pair.second is JsonValue.JsonString)
            "\n\t\"${pair.first}\" : \"${(pair.second as JsonValue.JsonString).value}\""
        else if(pair.second is JsonValue.JsonNumber)
            "\n\t\"${pair.first}\" : ${(pair.second as JsonValue.JsonNumber).value}"
        else if(pair.second is JsonValue.JsonArray)
//            if(jsonArrayHasObject(pair.second as JsonValue.JsonArray))
//                TODO()
//            else
            "\n\t\"${pair.first}\" : ${((pair.second as JsonValue.JsonArray).elements.joinToString(separator = ",", prefix = "[", postfix = "]"))}"
        else if(pair.second is JsonValue.JsonObject)
//            "\n\t\"${pair.first}\" : ${(pair.second as JsonValue.JsonObject).properties.toList().joinToString(separator = ",", prefix = "{", postfix = "\n}", transform = generateJsonContent(Pair(pair.second.)))}"
              "\n\t\"${pair.first}\" : ${handleObject((pair.second as JsonValue.JsonObject).properties)}"
//              "\n\t\"${pair.first}\" : ${(pair.second as JsonValue.JsonObject).properties.entries.joinToString()}"
//            "\n\t\"${pair.first}\" : ${(pair.second as JsonValue.JsonObject).properties.entries.joinToString(separator = ",", prefix = "{", postfix = "\n}", transform = {generateJsonContent(Pair(it.key, it.value))})}"
//                 generateJsonContent(Pair(it.first, it.second))
//                "${it.key} - ${it.value}"
//               hey hey"
//            }}"

//            "\n\t\"${pair.first}\" : {" +
//                    (pair.second as JsonValue.JsonObject).properties.entries.joinToString(",") { (key, value) ->
//                        generateJsonContent(key to (value as JsonValue))
//                    } + "\n\t}"

        else if(pair.second is JsonValue.JsonBoolean)
            "\n\t\"${pair.first}\" : ${(pair.second as JsonValue.JsonBoolean).value}"
        else if(pair.second is JsonValue.JsonNull)
            "\n\t\"${pair.first}\" : null"
        else {
            ""
        }
//
//        return "\n${pair.first} : ${when (pair.second){
//            is JsonValue.JsonObject -> pair.second.properties.toString()
//            is JsonValue.JsonArray -> pair.second.elements.toString()
//        }}"

//        return "\n${pair.first} : ${when (pair.second) {
//            is JsonValue.JsonObject -> pair.second.properties.toString()
//            is JsonValue.JsonArray -> pair.second.elements.toString()
//            is JsonValue.JsonString -> pair.second.value
//            is JsonValue.JsonNumber -> pair.second.value.toString()
//            is JsonValue.JsonBoolean -> pair.second.value.toString()
//            is JsonValue.JsonNull -> "null"
//        }}"
    }

    private fun handleObject(properties: Map<String, Any>): String {
        var objectString = ""
        for(pair in properties){
//            objectString + generateJsonContent(pair.)
//            println(generateJsonContent(Pair(pair.key, pair.value)))
            when(pair.value) {
                is Int -> println(generateJsonContent(Pair(pair.key, JsonValue.JsonNumber(pair.value as Int))))
                is String -> println(generateJsonContent(Pair(pair.key, JsonValue.JsonString(pair.value as String))))
                is Boolean -> println(generateJsonContent(Pair(pair.key, JsonValue.JsonBoolean(pair.value as Boolean))))
                is List<*> -> println(generateJsonContent(Pair(pair.key, JsonValue.JsonArray(pair.value as List<JsonValue>))))
                is Map<*, *> -> println(generateJsonContent(Pair(pair.key,JsonValue.JsonObject(pair.value as Map<String, JsonValue>))))
//                is Map<*, *> -> value.forEach{
//                    this.addValue(Pair(it.key as String, it.value) as Pair<String, JsonValue>)}
                null -> println(generateJsonContent(Pair(pair.key, JsonValue.JsonNull(null))))
                else -> TODO()
            }
        }
        return "hello"

    }

    //    if json array has objects, we have to handle identation
    private fun jsonArrayHasObject(jsonArray: JsonValue.JsonArray): Boolean {
        jsonArray.elements.forEach{ element ->
            if(element is JsonValue.JsonObject){
                return true
            }
            else if(element is JsonValue.JsonArray){
                jsonArrayHasObject(element) //recursividade uhhhh
            }
        }
        return false

    }

}


fun main(){
    val json = Json(
        Pair("uc", "PA"),
        Pair("ects", 123),
        Pair("data-exame", null),
        Pair("importante", true),
        Pair("perguntas", listOf('a', 'b', 'c')),
        Pair("inscritos", mapOf(
            "numero" to 10,
            "nome" to "Dave",
            "internacional" to true))
    )

    println(json.jsonValues)
    println(json.getJsonContent())
}

//        {
//            "uc": "PA",
//            "ects" : 6.0,
//            "data-exame" : null
//            "perguntas"  : ['a', 'b', 'c']
//        }