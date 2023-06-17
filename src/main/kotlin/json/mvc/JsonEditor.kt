package json.mvc

import json.generator.JsonArray
import json.generator.JsonObject
import java.awt.Color
import java.awt.Component
import java.awt.GridLayout
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*
import javax.swing.border.Border

class JsonEditor(private val model: JsonObject) : JPanel(){

    private var scrollPane : JScrollPane
    private var panel: JPanel

    init {
//        val left = JPanel()
        layout = GridLayout()
        border = BorderFactory.createEmptyBorder(10, 50, 0, 50)

//        panel = JPanel()
        panel = getPanel()


        scrollPane = JScrollPane(panel).apply {
            horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
            verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        }
        add(scrollPane)
//        add(left)
    }

    private fun getPanel(): JPanel=
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)

            model.value.forEach { (identifier, jsonValue) ->
                if(jsonValue !is JsonArray){
//                    TODO()
//                    add(testWidget2(identifier, jsonValue.value.toString()))
                    add(testWidget2(identifier, jsonValue.value.toString()))
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
                    elementPanel = testWidget2(value = jsonValue.value.toString())
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
                add(testWidget2(key = identifier))
                add(objectsPanel)
                panel.add(this)
            }

        }


    private fun traverseJsonObject(jsonObject: JsonObject): JPanel=
//        TODO()
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            jsonObject.value.forEach { add(testWidget2(it.key, it.value.value.toString())) }
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