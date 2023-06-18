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
//                    TODO()
//                    add(testWidget2(identifier, jsonValue.value.toString()))
//                    add(testWidget(identifier, jsonValue.value.toString()))
                    add(testWidget(identifier, jsonValue))
                }
                else{
//                    add(testWidget2(identifier, jsonValue.value.toString()))
                    traverseJsonArray(identifier, jsonValue, this)
//                    add(testWidget2(identifier, identifier))
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
//                    elementPanel = testWidget2(value = jsonValue.value.toString())
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
//            jsonObject.value.forEach { add(testWidget(it.key, it.value.value.toString())) }
            jsonObject.value.forEach { add(testWidget(it.key, it.value)) }
        }


//    private fun testWidget(identifier: String? = null, value: String? = null): JPanel =
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
            } //identifier
                            //jsonValue

            if (value != null) {
//                text = JTextField(value)
                text = JTextField(value.value.toString())

                text.addActionListener {

                }

                text.addFocusListener(object : FocusAdapter() {
                    override fun focusLost(e: FocusEvent) {
                        println("perdeu foco: ${text.text},  " + value)

//                        println(text.text + value.value.toString())
                        if(text.text != value.value.toString()) {

                            //initialize oldJsonValue
//                            var oldJsonValue: JsonValue = JsonNull()
                            var oldJsonValue: JsonValue = value
                            //adicionar verificação de parent
//                            if (value.parent == model) {
//                                oldJsonValue = model.value[identifier.toString()]!! //change
//                            }
//                            else if( value.parent is JsonArray){
//
//                            }


                            val newJsonValue = model.getJsonValue(text.text)
//                            model.value[identifier.toString()] = newJsonValue
//                            println(model.getJsonContent())
//                            controller.updateModel(model)
                            observers.forEach { it.modifiedJsonValue(oldJsonValue.parent!!,oldJsonValue, newJsonValue) }
//                            testWidget(identifier, newJsonValue)
                        }

                    }
                })
                add(text)
            }
        }


    private fun testWidget2(key: String? = null, value: String? = null): JPanel =
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = Component.LEFT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT

            if (key!= null){ add(JLabel(key)) } //identifier
            val text: JTextField                //jsonValue

            if(value!= null) {
                text = JTextField(value)

                text.addFocusListener(object : FocusAdapter() {
                    override fun focusLost(e: FocusEvent) {
                        println("perdeu foco: ${text.text}")
                    }
                })
                add(text)
            }


            // menu
            addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        val menu = JPopupMenu("Message")
                        val addButton = JButton("add property")
                        addButton.addActionListener {
                            val identifier : String? = JOptionPane.showInputDialog("Write property name")
                            add(testWidget2(identifier, "?"))
                            menu.isVisible = false
                            revalidate()
//                            frame.repaint()
                            repaint()
                        }
                        val deleteButton = JButton("delete property $key")
                        deleteButton.addActionListener {
                            components.forEach {
                                remove(it)
                            }
                            menu.isVisible = false
                            revalidate()
//                            frame.repaint()
                            repaint()
                        }
                        menu.add(addButton);
                        menu.add(deleteButton)
                        menu.show(this@apply, 100, 100);
                    }
                }
            })


        }
}

interface EditorObservers {
    fun addedJsonValue(parent: JsonComplex,identifier: String, jsonValue: JsonValue)
    fun modifiedJsonValue(parent: JsonComplex, jsonValueOld: JsonValue, jsonValueNew: JsonValue)
    fun removedJsonValue(parent: JsonComplex, jsonValue: JsonValue)
}