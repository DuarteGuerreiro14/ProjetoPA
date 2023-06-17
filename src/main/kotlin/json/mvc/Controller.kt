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

class Editor(private val model: JsonObject) {
    val frame = JFrame("JSON Object Editor").apply {
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        layout = GridLayout(0, 2)
        size = Dimension(900, 600)

        val editor = JsonEditor(model)

        // Adding views to the frame - Json editor and viewer
        val editorPanel: JPanel = JPanel().apply {
//            add(undoBtn)
            add(editor)
        }
        val view = JsonView(model)

        add(editorPanel)
        add(view)



//        val left = JPanel()
//        left.layout = GridLayout()
//        val scrollPane = JScrollPane(testPanel()).apply {
//            horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
//            verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
//        }
//        left.add(scrollPane)
//        add(left)



//        val right = JPanel()
//        right.layout = GridLayout()
//        val srcArea = JTextArea()
//        srcArea.tabSize = 2
//        srcArea.text = model.getJsonContent()
//        right.add(srcArea)
//        add(right)
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
    Editor(json).open()
}





