package json.mvc

//import javafx.scene.Parent
import json.generator.*
import java.awt.Color
import java.awt.Component
import java.awt.GridLayout
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*
import javax.swing.border.Border

class JsonEditor(private val model: JsonObject, val controller: Controller) : JPanel(){

    private var scrollPane : JScrollPane
    private var panel: JPanel

    private val observers: MutableList<EditorObservers> = mutableListOf()
    fun addObserver(o:EditorObservers) = observers.add(o)
    init {

        model.addObserver(object : JsonValueObserver  {
            override fun addedJsonValue(identifier: String, jsonValue: JsonValue) {
                revalidate()
                repaint()
            }

            override fun modifiedJsonValue(jsonValueOld: JsonValue , jsonValueNew: JsonValue) {
//                TODO(" modificar os valores do json")
            }

            override fun removedJsonValue(jsonValue: JsonValue) {
            }
        })

        layout = GridLayout()
        border = BorderFactory.createEmptyBorder(10, 50, 0, 50)
        panel = getPanel()

        scrollPane = JScrollPane(panel).apply {
            horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
            verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        }
        add(scrollPane)
    }


    private fun getPanel(): JPanel=
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)

            model.value.forEach { (identifier, jsonValue) ->
                if(jsonValue !is JsonArray){
                    add(testWidget(identifier, jsonValue))
                }
                else{
                    traverseJsonArray(identifier, jsonValue, this)
                }
            }
        }


    private fun traverseJsonArray(identifier: String, jsonArray: JsonArray, panel: JPanel): JPanel =
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            alignmentX = Component.LEFT_ALIGNMENT

            val objectsPanel = JPanel()
            val outerBorder: Border = BorderFactory.createEmptyBorder(0, 0, 0, 0)
            objectsPanel.layout = GridLayout(jsonArray.value.size, 1)

            jsonArray.value.forEachIndexed { index, jsonValue ->

                val elementPanel: JPanel

                if (jsonValue is JsonObject){
                    elementPanel = traverseJsonObject(jsonValue)

                    elementPanel.border = BorderFactory.createCompoundBorder(
                        outerBorder,
                        BorderFactory.createLineBorder(if (index % 2 == 0) Color.GRAY else Color.DARK_GRAY, 6)
                    )
                }

                else{
                    elementPanel = testWidget(value = jsonValue)
                    elementPanel.border = BorderFactory.createCompoundBorder(
                        outerBorder,
                        BorderFactory.createLineBorder(if (index % 2 == 0) Color.GRAY else Color.DARK_GRAY, 4)
                    )
                }


                objectsPanel.add(elementPanel)

            } //end foreach

            JPanel().apply {
                layout = BoxLayout(this, BoxLayout.X_AXIS)
                border = BorderFactory.createEmptyBorder(0, 0, 0, 0)
                add(testWidget(identifier = identifier))
                add(objectsPanel)
                panel.add(this)
            }

        }


    private fun traverseJsonObject(jsonObject: JsonObject): JPanel=
//        TODO()
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            jsonObject.value.forEach { add(testWidget(it.key, it.value)) }
        }



    private fun testWidget(identifier: String? = null, value: JsonValue? = null): JPanel =
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = Component.LEFT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT

            val identifierLabel: JLabel
            val text: JTextField

            if (identifier != null) {
                identifierLabel = JLabel(identifier)
                add(identifierLabel)
            }

            if (value != null) {
                text = JTextField(value.value.toString())

                text.addActionListener {

                }

                text.addFocusListener(object : FocusAdapter() {
                    override fun focusLost(e: FocusEvent) {
                        println("perdeu foco: ${text.text},  " + value)

                        if(text.text != value.value.toString()) {

                            var oldJsonValue: JsonValue = value
                            println("inside if" + oldJsonValue)

                            val jsonValueText = getJsonValueText(text.text)
//                            value = oldJsonValue
                            val newJsonValue = model.getJsonValue(jsonValueText)
//                            model.value[identifier.toString()] = newJsonValue
//                            println(model.getJsonContent())
//                            controller.updateModel(model)
                            testWidget(identifier, newJsonValue)
                            observers.forEach { it.modifiedJsonValue(oldJsonValue.parent!!,oldJsonValue, newJsonValue) }
//                            testWidget(identifier, newJsonValue)
                        }

                    }
                })
                add(text)
            }
        }


    fun getJsonValueText(text:String) : Any?{
        if(text.toIntOrNull() != null){
            return text.toInt()
        }
        else if(text == "true" || text == "false"){
            return text.toBoolean()
        }
        else if(text == "null") {
            return null
        }
        return text
    }

//    private fun testWidget2(key: String? = null, value: String? = null): JPanel =
//        JPanel().apply {
//            layout = BoxLayout(this, BoxLayout.X_AXIS)
//            alignmentX = Component.LEFT_ALIGNMENT
//            alignmentY = Component.TOP_ALIGNMENT
//
//            if (key!= null){ add(JLabel(key)) } //identifier
//            val text: JTextField                //jsonValue
//
//            if(value!= null) {
//                text = JTextField(value)
//
//                text.addFocusListener(object : FocusAdapter() {
//                    override fun focusLost(e: FocusEvent) {
//                        println("perdeu foco: ${text.text}")
//                    }
//                })
//                add(text)
//            }
//
//
//            // menu
//            addMouseListener(object : MouseAdapter() {
//                override fun mouseClicked(e: MouseEvent) {
//                    if (SwingUtilities.isRightMouseButton(e)) {
//                        val menu = JPopupMenu("Message")
//                        val addButton = JButton("add property")
//                        addButton.addActionListener {
//                            val identifier : String? = JOptionPane.showInputDialog("Write property name")
//                            add(testWidget2(identifier, "?"))
//                            menu.isVisible = false
//                            revalidate()
////                            frame.repaint()
//                            repaint()
//                        }
//                        val deleteButton = JButton("delete property $key")
//                        deleteButton.addActionListener {
//                            components.forEach {
//                                remove(it)
//                            }
//                            menu.isVisible = false
//                            revalidate()
////                            frame.repaint()
//                            repaint()
//                        }
//                        menu.add(addButton);
//                        menu.add(deleteButton)
//                        menu.show(this@apply, 100, 100);
//                    }
//                }
//            })
//
//
//        }
}

interface EditorObservers {
    fun addedJsonValue(parent: JsonComplex,identifier: String, jsonValue: JsonValue)
    fun modifiedJsonValue(parent: JsonComplex, jsonValueOld: JsonValue, jsonValueNew: JsonValue)
    fun removedJsonValue(parent: JsonComplex, jsonValue: JsonValue)
}