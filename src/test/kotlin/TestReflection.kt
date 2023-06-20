import json.generator.*
import json.reflection.CustomizableIdentifier
import json.reflection.ExcludeProperty
import json.reflection.ForceString
import json.reflection.createJson
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TestReflection {


    data class Exam(
        @CustomizableIdentifier("unidadeCurricular")
        val uc: String,
        val credits: Int,
        @ForceString
        val maximumGrade: Int,
        val examDate: Nothing? = null,
        val isImportant: Boolean,
        @ExcludeProperty
        val inEnglish: Boolean,
        val enrolled: List<Any>,
        val requirements: Map<String, Any>
    )



    @Test
    fun `test object creation with reflection`(){

        val exam = Exam("PA", 123,20, null, true, false,
            listOf(
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
                    "nome" to "Joao",
                    "internacional" to false)),
            mapOf("nota_minima" to 10,"projeto" to true)
        )


        val json = createJson(exam)
        val expectedJson = """
            {
            	"unidadeCurricular" : "PA",
            	"credits" : 123,
            	"maximumGrade" : "20",
            	"examDate" : null,
            	"isImportant" : true,
            	"enrolled" : [
            { 
            	"numero" : 10,
            	"nome" : "Dave",
            	"internacional" : true 
            },
            { 
            	"numero" : 11,
            	"nome" : "Joao",
            	"internacional" : false 
            },
            { 
            	"numero" : 11,
            	"nome" : "Joao",
            	"internacional" : false 
            }
            ],
            	"requirements" : { 
            	"nota_minima" : 10,
            	"projeto" : true 
            }
            }
        """.trimIndent()

        assertEquals(json.getJsonContent(), expectedJson)


    }
}