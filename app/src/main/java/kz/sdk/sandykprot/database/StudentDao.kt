package kz.sdk.sandykprot.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete

@Dao
interface StudentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudents(students: List<StudentEntity>)

    @Delete
    suspend fun deleteStudent(student: StudentEntity)

    @Query("SELECT * FROM students WHERE className = :className")
    fun getStudentsByClass(className: String): LiveData<List<StudentEntity>>

    @Query("SELECT * FROM students")
    fun getAllStudents(): LiveData<List<StudentEntity>>

    @Query("SELECT MAX(studentIdInClass) FROM students WHERE className = :className")
    suspend fun getMaxStudentIdInClass(className: String): Int?

    @Query("SELECT DISTINCT className FROM students")
    fun getAllClassNames():LiveData<List<String>>
}
