package json.reflection

data class Exam(
    @CustomizableIdentifier("unidadeCurricular")
    val uc: String,
    val credits: Int,
    @ForceString
    val maximumGrade: Int, //ForceString here
//    @ForceString
    val examDate: Nothing? = null,
//    @ForceString
    val isImportant: Boolean,
    @ExcludeProperty
    val inEnglish: Boolean,
    val enrolled: List<Any>,      //List<Map<String, Any>>
    val requirements: Map<String, Any>
//    val teacher: Teacher //to add value object
)

data class Teacher(
    val name:String
)