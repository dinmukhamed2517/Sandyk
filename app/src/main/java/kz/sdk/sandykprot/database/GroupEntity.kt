package kz.sdk.sandykprot.database

data class GroupEntity(
    val groupName: String,
    val students: List<StudentEntity>
)