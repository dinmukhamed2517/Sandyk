package kz.sdk.sandykprot.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.sdk.sandykprot.database.AppDatabase
import kz.sdk.sandykprot.database.ClassEntity
import kz.sdk.sandykprot.database.StudentEntity

class StudentViewModel(application: Application) : AndroidViewModel(application) {
    private val studentDao = AppDatabase.getDatabase(application).studentDao()

    // Add new students, ensuring that each student has a unique ID within the class
    fun addStudents(className: String, studentNames: List<String>) {
        viewModelScope.launch {
            // Fetch the current maximum studentIdInClass for the given class
            val maxStudentIdInClass = studentDao.getMaxStudentIdInClass(className) ?: 0

            // Assign class-specific student IDs
            val students = studentNames.mapIndexed { index, studentName ->
                StudentEntity(
                    className = className,
                    studentName = studentName,
                    studentIdInClass = maxStudentIdInClass + index + 1 // Increment ID within the class
                )
            }

            studentDao.insertStudents(students)  // Insert the students
        }
    }
    fun getClassesWithStudents(): LiveData<List<ClassEntity>> {
        val classLiveData = MediatorLiveData<List<ClassEntity>>()
        classLiveData.addSource(getAllStudents()) { students ->
            val classes = students.groupBy { it.className }.map { entry ->
                ClassEntity(entry.key, entry.value)
            }
            classLiveData.value = classes
        }
        return classLiveData
    }

    fun getStudentsByClass(className: String): LiveData<List<StudentEntity>> {
        return studentDao.getStudentsByClass(className)
    }

    fun getAllStudents(): LiveData<List<StudentEntity>> {
        return studentDao.getAllStudents()
    }

    // Delete a student
    fun deleteStudent(student: StudentEntity) {
        viewModelScope.launch {
            studentDao.deleteStudent(student)
        }
    }
    fun getAllClassNames():LiveData<List<String>>{
        return studentDao.getAllClassNames()
    }
}

