package kz.sdk.sandykprot.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val className: String,
    val studentName: String,
    var studentIdInClass: Int
)