import json.generator.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TestSimpleJson {

    private val expectedJson = """
            {
            	"uc" : "PA",
            	"ects" : 123,
            	"data-exame" : null,
            	"importante" : true,
            	"perguntas" : ["a", "b", "c"]
            }
        """.trimIndent()


    @Test
    fun `test json without creating JsonValues`(){

        val json = JsonObject()
        json.add("uc", "PA")
        json.add("ects", 123)
        json.add("data-exame", null)
        json.add("importante", true)
        json.add("perguntas", listOf("a", "b", "c"))

        assertEquals(expectedJson, json.getJsonContent())
    }

    @Test
    fun `test json after creating JsonValues`(){

        val json = JsonObject()

        val uc = JsonString("PA")
        val ects = JsonNumber(123)
        val dateExame = JsonNull()
        val importante = JsonBoolean(false)

        val perguntas = JsonArray()
        val pergunta1 = JsonString("a")
        val pergunta2 = JsonString("b")
        val pergunta3 = JsonString("c")
        perguntas.add(pergunta1)
        perguntas.add(pergunta2)
        perguntas.add(pergunta3)

        json.add("uc", uc)
        json.add("ects", ects)
        json.add("data-exame", dateExame)
        json.add("importante", true)
        json.add("perguntas", perguntas)

        assertEquals(expectedJson, json.getJsonContent())
    }
}