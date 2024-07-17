package com.example.ghec

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AttendanceViewModel : ViewModel() {

    private val studentsPresent = mutableMapOf<String, MutableSet<String>>() // Map of class IDs to sets of roll numbers
    private val firestore = FirebaseFirestore.getInstance()

    // Fetch roll number from Firestore using student ID
    private suspend fun getStudentRollNo(studentId: String): String? {
        return try {
            val doc = firestore.collection("students").document(studentId).get().await()
            doc.getString("rollNumber")
        } catch (e: Exception) {
            null
        }
    }

    // Handle submission of roll number by students
    suspend fun submitRollNo(classId: String, rollNo: String, studentId: String) {
        // Extract the last two digits
        val rollNoLastTwoDigits = rollNo.takeLast(2)

        // Fetch the student's full roll number
        val studentRollNo = getStudentRollNo(studentId)

        // Verify if the last two digits match
        if (studentRollNo != null && rollNoLastTwoDigits == studentRollNo.takeLast(2)) {
            studentsPresent.getOrPut(classId) { mutableSetOf() }.add(studentRollNo)
        }
    }

    // Method to get the list of absent students for a specific class
    fun getAbsentStudents(classId: String, callback: (List<String>) -> Unit) {
        // Define the list of all roll numbers for the class (replace with real data)
        val allStudentRollNumbers = generateRollNumbersForClass(classId)

        // Determine absent students
        val absentStudents = allStudentRollNumbers.filter { rollNo -> !studentsPresent[classId]?.contains(rollNo)!!
            ?: false }
        // Execute the callback with the list of absent student roll numbers
        callback(absentStudents)
    }

    // Generate roll numbers for a specific class (replace with real data)
    private fun generateRollNumbersForClass(classId: String): List<String> {
        // Example: Generating roll numbers for classes with format "2301050521", adjust as needed
        return (1..76).map { "$classId${it.toString().padStart(2, '0')}" }
    }
}


