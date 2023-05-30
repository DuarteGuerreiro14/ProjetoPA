package json.reflection

data class Exam(
    val uc: String,
    val credits: Int,
    @ForceString
    val maximumGrade: Int, //ForceString here
    val examDate: Nothing? = null,
    val isImportant: Boolean,
    val enrolled: List<Any>,      //List<Map<String, Any>>
    val requirements: Map<String, Any>
//    val teacher: Teacher //to add value object
)

data class Teacher(
    val name:String
)