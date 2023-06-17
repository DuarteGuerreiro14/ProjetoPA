//package json.generator
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.Assertions.*
//
//import java.io.File
//
////import kotlin.test.assertEquals
//class TestClass {
//
//
//    @Test
//    fun testSimpleJson() {
////        assertEquals("xls", getExtension("rand.xls"))
//        val expectedJson = """
//            {
//            	"uc" : "PA",
//            	"ects" : 123,
//            	"data-exame" : null,
//            	"importante" : true,
//            	"perguntas" : ["a", "b", "c"]
//            }
//        """.trimIndent()
//
//        val json = Json(
//            Pair("uc", "PA"),
//            Pair("ects", 123),
//            Pair("data-exame", null),
//            Pair("importante", true),
//            Pair("perguntas", listOf("a", "b", "c"))
//        )
//
//        assertEquals(expectedJson, json.getJsonContent())
//    }
//
//    @Test
//    fun testListOfObjects(){
//
//        val expectedJson = """
//           {
//           	"inscritos" : [
//           {
//           	"numero" : 10,
//           	"nome" : "Dave",
//           	"internacional" : true },
//           {
//           	"numero" : 11,
//           	"nome" : "Joao",
//           	"internacional" : false }
//           ]
//           }
//        """.trimIndent()
//
//        val json = Json(
//            Pair("inscritos", listOf(
//                mapOf(
//                    "numero" to 10,
//                    "nome" to "Dave",
//                    "internacional" to true),
//                mapOf(
//                    "numero" to 11,
//                    "nome" to "Joao",
//                    "internacional" to false)))
//        )
//
//        assertEquals(expectedJson, json.getJsonContent())
//        println(json.getJsonContent())
//
//    }
//
//    @Test
//    fun testListInsideList(){
//
//        val expectedJson = """
//           {
//           	"perguntas" : ["a", false, null, 57, "teste", ["list inside list", 98, null]]
//           }
//        """.trimIndent()
//
//        val json = Json(
//        Pair("perguntas", listOf("a", false, null, 57, "teste", listOf("list inside list", 98, null))))
//
//        assertEquals(expectedJson, json.getJsonContent())
//        println(json.getJsonContent())
//
//    }
//
//    @Test
//    fun testFunctionjsonArrayHasObject(){
//
//    }
//
//
//}