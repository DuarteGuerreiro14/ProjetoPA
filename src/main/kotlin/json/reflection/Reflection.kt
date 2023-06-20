package json.reflection

import json.generator.*
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.KProperty
import kotlin.reflect.full.*
//import kotlin.reflect.full.findAnnotations

//Annotations
@Target(AnnotationTarget.PROPERTY)
annotation class ExcludeProperty

@Target(AnnotationTarget.PROPERTY)
annotation class CustomizableIdentifier(val newIdentifier: String)

@Target(AnnotationTarget.PROPERTY)
annotation class ForceString


//obtains list of attributes ordered as in the main constructor
val KClass<*>.dataClassFields: List<KProperty<*>>
    get() {
        require(isData) { "instance must be data class" }
        return primaryConstructor!!.parameters.map { p ->
            declaredMemberProperties.find { it.name == p.name }!!
        }
    }

val KClassifier?.isEnum: Boolean
    get() = this is KClass<*> && this.isSubclassOf(Enum::class)


fun createJson(obj:Any): JsonObject {

    val clazz = obj::class
    val jsonObject = JsonObject()

    clazz.dataClassFields.forEach {

        //if current property is not to exclude
        if (!it.hasAnnotation<ExcludeProperty>()) {

            //get the name of the identifier if it is customizable
            val identifier = if(it.hasAnnotation<CustomizableIdentifier>()) it.findAnnotation<CustomizableIdentifier>()?.newIdentifier else it.name

            if (it.returnType.toString() == "kotlin.String" || it.hasAnnotation<ForceString>()) { //or has annotation forceString
                jsonObject.add(identifier!!, JsonString(it.call(obj).toString()))
            }
            else if (it.returnType.toString() == "kotlin.Int") {
                jsonObject.add(identifier!!, JsonNumber(it.call(obj) as Int))
            }
            else if (it.returnType.toString() == "kotlin.Boolean") {
                jsonObject.add(identifier!!, JsonBoolean(it.call(obj) as Boolean))
            }
            else if (it.returnType.toString() == "kotlin.Nothing?") {
                jsonObject.add(identifier!!, JsonNull())
            }
            else if (it.returnType.toString().contains("kotlin.collections.List")) {
                jsonObject.add(identifier!!, jsonObject.createJsonArray(it.call(obj) as List<Any?>))
            }
            else if (it.returnType.toString().contains("kotlin.collections.Map")) {
                jsonObject.add(identifier!!, jsonObject.createJsonObject(it.call(obj) as Map<*, *>))
            }

        }
    }

    return jsonObject

}



fun main(){
    val fields: List<KProperty<*>> = Exam::class.dataClassFields
    println(fields)

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
    println(json.getJsonContent())

}



