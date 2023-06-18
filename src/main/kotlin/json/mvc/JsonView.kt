package json.mvc

//import json.generator.Json
import json.generator.JsonObject
import json.generator.JsonValue
import json.generator.JsonValueObserver
import java.awt.GridLayout
import javax.swing.JPanel
import javax.swing.JTextArea

class JsonView(private val model: JsonObject) : JPanel() {

//    private val observers = mutableListOf<JsonViewObserver>()

    private val jsonTextArea = JTextArea()

    init {
        layout = GridLayout()
        jsonTextArea.tabSize = 2
        jsonTextArea.text = model.getJsonContent()
        jsonTextArea.isEditable = false

        model.addObserver(object: JsonValueObserver{
            override fun addedJsonValue(identifier: String, jsonValue: JsonValue) {
//                super.addedJsonValue(identifier, jsonValue)
            }

            override fun modifiedJsonValue(jsonValueOld: JsonValue, jsonValueNew: JsonValue) {
//                super.modifiedJsonValue(jsonValueOld, jsonValueNew)
                jsonTextArea.text = model.getJsonContent()
//                jsonTextArea.repaint()
            }

            override fun removedJsonValue(jsonValue: JsonValue) {
//                super.removedJsonValue(jsonValue)
            }
        })
//        iterateJsonObject(model)
        add(jsonTextArea)

    }
//    private fun addObserverToJsonStructured(){

//}

}

//interface JsonViewObserver{
//
//}
