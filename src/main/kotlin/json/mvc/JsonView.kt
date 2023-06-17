package json.mvc

//import json.generator.Json
import json.generator.JsonObject
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
        add(jsonTextArea)

//        iterateJsonObject(model)
    }

}

//interface JsonViewObserver{
//
//}
