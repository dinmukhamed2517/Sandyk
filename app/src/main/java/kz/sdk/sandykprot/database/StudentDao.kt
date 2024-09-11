package kz.sdk.sandykprot.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete

@Dao
interface StudentDao {

    // Insert multiple students
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudents(students: List<StudentEntity>)

    // Delete a student
    @Delete
    suspend fun deleteStudent(student: StudentEntity)

    // Get students by class name
    @Query("SELECT * FROM students WHERE className = :className")
    fun getStudentsByClass(className: String): LiveData<List<StudentEntity>>

    // Get all students (for global operations)
    @Query("SELECT * FROM students")
    fun getAllStudents(): LiveData<List<StudentEntity>>

    // Get the maximum student ID within a class (for generating new student IDs)
    @Query("SELECT MAX(studentIdInClass) FROM students WHERE className = :className")
    suspend fun getMaxStudentIdInClass(className: String): Int?
}
