package json.mvc

import json.generator.*
import java.awt.Component
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.event.*
import javax.swing.*


//fun json.mvc.main() {
//    json.mvc.Editor().open()
//}

class Controller(private var model: JsonObject) {
    val instance = this

    val frame = JFrame("JSON Object Editor").apply {
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        layout = GridLayout(0, 2)
        size = Dimension(900, 600)

        var editor = JsonEditor(model, instance)
        editor.addObserver(object : EditorObservers{
            override fun modifiedJsonValue(parent: JsonComplex, jsonValueOld: JsonValue, jsonValueNew: JsonValue) {
                println("inside modified")
                println(model.getJsonContent())
                println("\n______________________\n")

                if((jsonValueOld.parent == model)) {
                    model.modify(jsonValueOld, jsonValueNew)
                }

                else{
                    print("hereeeeeeee")
                    if(jsonValueOld.parent is JsonArray){

                        (jsonValueOld.parent as JsonArray).modify(jsonValueOld, jsonValueNew)
                    }
                }

                println(model.getJsonContent())

            }

            override fun addedJsonValue(parent: JsonComplex, identifier: String, jsonValue: JsonValue) {
//                TODO("Not yet implemented")
            }

            override fun removedJsonValue(parent: JsonComplex, jsonValue: JsonValue) {
//                TODO("Not yet implemented")
            }
        })

//        editor = JsonEditor(model, instance)
        // Adding views to the frame - Json editor and viewer
        val editorPanel: JPanel = JPanel().apply {
//            add(undoBtn)
            add(editor)
        }


        val view = JsonView(model)

        add(editorPanel)
        add(view)
    }

    fun updateModel(model: JsonObject){
        this.model = model
//        frame.revalidate()
//        frame.repaint()
//        val view = JsonView(this.model)
        Controller(this.model).open()
    }

    fun open() {
        frame.isVisible = true
    }

    fun testPanel(): JPanel =
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            alignmentX = Component.LEFT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT

            // menu
            addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        val menu = JPopupMenu("Message")
                        val add = JButton("add")
                        add.addActionListener {
                            val text = JOptionPane.showInputDialog("text")
                            add(testWidget(text, "?"))
                            menu.isVisible = false
                            revalidate()
                            frame.repaint()
                        }
                        val del = JButton("delete all")
                        del.addActionListener {
                            components.forEach {
                                remove(it)
                            }
                            menu.isVisible = false
                            revalidate()
                            frame.repaint()
                        }
                        menu.add(add);
                        menu.add(del)
                        menu.show(this@apply, 100, 100);
                    }
                }
            })
        }


    fun testWidget(key: String, value: String): JPanel =
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = Component.LEFT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT

            add(JLabel(key))
            val text = JTextField(value)
            text.addFocusListener(object : FocusAdapter() {
                override fun focusLost(e: FocusEvent) {
                    println("perdeu foco: ${text.text}")
                }
            })
            add(text)
        }
}


fun getInitialJson(): JsonObject {

    val json = JsonObject()
    json.add("uc", "PA")
    json.add("ects", 123)
    json.add("data-exame", null)
    json.add("inscritos", listOf(
        mapOf(
            "numero" to 10,
            "nome" to "Dave",
            "internacional" to true),
        mapOf(
            "numero" to 11,
            "nome" to "Joao",
            "internacional" to false),
        mapOf(
            "numero" to 11,
            "nome" to "Andre",
            "internacional" to false)))

    val jsonArray = JsonArray()
    jsonArray.add(JsonString("MEI"))
    jsonArray.add(JsonString("METI"))
    jsonArray.add(JsonString("MEGI"))
    json.add("cursos", jsonArray)

    return json
}


fun main() {

    val json = getInitialJson()
    Controller(json).open()
}





