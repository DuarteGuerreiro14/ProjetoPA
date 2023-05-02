package json.generator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

import java.io.File

//import kotlin.test.assertEquals
class TestClass {

    @Test
    fun testJUnit() {
//        assertEquals("xls", getExtension("rand.xls"))
        assertEquals(3, 3)
    }

    @Test
    fun testSimpleJson() {
//        assertEquals("xls", getExtension("rand.xls"))
        val expectedJson = """
            {
            	"uc" : "PA",
            	"ects" : 123,
            	"data-exame" : null,
            	"importante" : true,
            	"perguntas" : [a,b,c]
            }
        """.trimIndent()

        val json = Json(
            Pair("uc", "PA"),
            Pair("ects", 123),
            Pair("data-exame", null),
            Pair("importante", true),
            Pair("perguntas", listOf('a', 'b', 'c'))
        )

//        println(json.jsonValues)
//        println(json.getJsonContent())
        assertEquals(expectedJson, json.getJsonContent())
    }


}