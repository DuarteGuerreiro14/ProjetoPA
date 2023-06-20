import json.generator.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TestVisitors {


    //create main structure
    val json = JsonObject()


    //create primitive elements
    val uc = JsonString("PA")
    val ects = JsonNumber(10)
    val dataExame = JsonNull()

    //create inscritos array
    private val inscritosArray = JsonArray()

    //each element of inscritos is a JsonObject, composed by other JsonValue's
    val inscrito1 = JsonObject()
    val inscrito1nome = JsonString("Dave Farley")
    val inscrito1numero = JsonNumber(101101)
    val inscrito1int = JsonBoolean(true)

    val inscrito2 = JsonObject()
    val inscrito2nome = JsonString("Martin Fowler")
    val inscrito2numero = JsonNumber(101102)
    val inscrito2int = JsonBoolean(true)

    val inscrito3 = JsonObject()
    val inscrito3nome = JsonString("André Santos")
    val inscrito3numero = JsonNumber(26503)
    val inscrito3int = JsonBoolean(false)


    init {

    json.add("uc", uc)
    json.add("ects", ects)
    json.add("data-exame", dataExame)

    inscrito1.add("numero", inscrito1numero)
    inscrito1.add("nome", inscrito1nome)
    inscrito1.add("internacional", inscrito1int)

    inscrito2.add("numero", inscrito2numero)
    inscrito2.add("nome", inscrito2nome)
    inscrito2.add("internacional", inscrito2int)

    inscrito3.add("numero", inscrito3numero)
    inscrito3.add("nome", inscrito3nome)
    inscrito3.add("internacional", inscrito3int)

    //add all inscritos to the array
    inscritosArray.add(inscrito1)
    inscritosArray.add(inscrito2)
    inscritosArray.add(inscrito3)

    //add the array to the structure
    json.add("inscritos",inscritosArray)
}

    @Test
    fun `test getting values by their key`(){

        val valuesFromKeyUC = json.getValuesFromKey("uc")
        val expectedValuesFromKeyUc = listOf(uc)
        assertEquals(valuesFromKeyUC, expectedValuesFromKeyUc)

        val valuesFromKeyNumero = json.getValuesFromKey("numero")
        val expectedValuesFromKeyNumero = listOf(inscrito1numero, inscrito2numero, inscrito3numero)
        assertEquals(valuesFromKeyNumero, expectedValuesFromKeyNumero)


    }

    @Test
    fun `test getting objects with keys`(){

        val objectsWithKeys = json.getObjectsWithKeys(listOf("numero","nome"))
        val expectedObjectsWithKeys = listOf(inscrito1, inscrito2, inscrito3)
        assertEquals(objectsWithKeys, expectedObjectsWithKeys)

        //this lists should not be equal , since we are sending as parameter a wrong key
        val objectsWithKeys2 = json.getObjectsWithKeys(listOf("numero","nome", "internaci"))
        val expectedObjectsWithKeys2 = listOf(inscrito1, inscrito2, inscrito3)
        assertNotEquals(objectsWithKeys2, expectedObjectsWithKeys2)

    }

    @Test
    fun `test key has value type`(){

        val numerohasNumberType = json.keyHasValueType("numero", JsonNumber::class)
        assertTrue(numerohasNumberType)

        val internacionalhasBooleanType = json.keyHasValueType("internacional", JsonBoolean::class)
        assertTrue(internacionalhasBooleanType)

    }

    @Test
    fun `test array has defined structure`(){

        val arrayHasDefinedStructure = json.arrayHasDefinedStructure("inscritos")
        assertTrue(arrayHasDefinedStructure)

    }


}