////import json.generator.Json
//import json.generator.JsonObject
//import java.awt.Component
//import java.awt.Dimension
//import java.awt.GridLayout
//import java.awt.event.*
//import javax.swing.*
//
//
//
//
//
//class json.mvc.Editor(private val model: JsonObject) {
//    val frame = JFrame("JSON Object json.mvc.Editor").apply {
//        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
//        layout = GridLayout(0, 2)
//        size = Dimension(900, 600)
//
//        val left = JPanel()
//        left.layout = GridLayout()
//        val scrollPane = JScrollPane(testPanel()).apply {
//            horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
//            verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
//        }
//        left.add(scrollPane)
//        add(left)
//
//        val right = JPanel()
//        right.layout = GridLayout()
//        val srcArea = JTextArea()
//        srcArea.tabSize = 2
////        srcArea.text = "TODO"
//
//        srcArea.text = model.getJsonContent()
//        right.add(srcArea)
//        add(right)
//    }
//
//    fun open() {
//        frame.isVisible = true
//    }
//
//    fun testPanel(): JPanel =
//        JPanel().apply {
//            layout = BoxLayout(this, BoxLayout.Y_AXIS)
//            alignmentX = Component.LEFT_ALIGNMENT
//            alignmentY = Component.TOP_ALIGNMENT
//
//            add(testWidget("A", "um"))
//            add(testWidget("B", "dois"))
//            add(testWidget("C", "tres"))
//
////            // menu
////            val menu = JPopupMenu("Options")
////            val add = JMenuItem("Add")
////            add.addActionListener {
////                val text = JOptionPane.showInputDialog("Enter a value")
////                if (text != null && text.isNotEmpty()) {
////                    add(testWidget(text, "?"))
////                    revalidate()
////                    frame.repaint()
////                }
////            }
////            menu.add(add)
////
////            val deleteAll = JMenuItem("Delete All")
////            deleteAll.addActionListener {
////                removeAll()
////                revalidate()
////                frame.repaint()
////            }
////            menu.add(deleteAll)
//
//            // menu
//            addMouseListener(object : MouseAdapter() {
//                override fun mouseClicked(e: MouseEvent) {
//                    if (SwingUtilities.isRightMouseButton(e)) {
//                        val menu = JPopupMenu("Message")
//                        val add = JButton("add")
//                        add.addActionListener {
//                            val text = JOptionPane.showInputDialog("text")
//                            add(testWidget(text, "?"))
//                            menu.isVisible = false
//                            revalidate()
//                            frame.repaint()
//                        }
//                        val del = JButton("delete all")
//                        del.addActionListener {
//                            components.forEach {
//                                remove(it)
//                            }
//                            menu.isVisible = false
//                            revalidate()
//                            frame.repaint()
//                        }
//                        menu.add(add);
//                        menu.add(del)
//                        menu.show(this@apply, 100, 100);
//                    }
//                }
//            })
//        }
//
//
//    fun testWidget(key: String, value: String): JPanel =
//        JPanel().apply {
//            layout = BoxLayout(this, BoxLayout.X_AXIS)
//            alignmentX = Component.LEFT_ALIGNMENT
//            alignmentY = Component.TOP_ALIGNMENT
//
//            add(JLabel(key))
//            val text = JTextField(value)
//            text.addFocusListener(object : FocusAdapter() {
//                override fun focusLost(e: FocusEvent) {
//                    println("perdeu foco: ${text.text}")
//                }
//            })
//            add(text)
//        }
//}
//
//fun json.mvc.getInitialJson(): Json {
//
//    return Json(
//        Pair("uc", "PA"),
//        Pair("ects", 123),
//        Pair("data-exame", null),
//        Pair("importante", true),
//        Pair("perguntas", listOf("a", "b", "c")),
////            Pair("inscritos", listOf("um", "dois", 7)),
//        Pair(
//            "inscritos", listOf(
//                mapOf<String, Any>(
//                    "numero" to 10,
//                    "nome" to "Dave",
//                    "internacional" to true
//                ),
//                mapOf<String, Any>(
//                    "numero" to 11,
//                    "nome" to "Joao",
//                    "internacional" to false
//                ),
//                mapOf<String, Any>(
//                    "numero" to 11,
//                    "nome" to "Joao",
//                    "internacional" to false
//                )
//            )
//        )
//    )
//}
//
//
//fun json.mvc.main() {
//
//    val json = json.mvc.getInitialJson()
//    json.mvc.Editor(json).open()
//}



