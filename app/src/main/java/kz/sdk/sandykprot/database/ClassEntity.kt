package kz.sdk.sandykprot.database

data class ClassEntity(
    val className: String,
    val students: List<StudentEntity>
)