package json.reflection

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
