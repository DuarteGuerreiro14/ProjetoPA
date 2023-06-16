package json.mvc

import json.generator.Json
import java.awt.GridLayout
import javax.swing.JPanel
import javax.swing.JTextArea

class JsonView(private val model: Json) : JPanel() {

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
