package com.example.ghec


import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class AttendanceFragment : Fragment() {

    private val viewModel: AttendanceViewModel by activityViewModels()
    private lateinit var generateCodeButton: Button
    private lateinit var attendanceStatusTextView: TextView
    private lateinit var rollNoEditText: EditText
    private lateinit var codeEditText: EditText
    private lateinit var submitCodeButton: Button
    private lateinit var teacherNameEditText: EditText
    private lateinit var studentNameEditText: EditText
    private lateinit var classIdEditText: EditText // Add this to handle class ID

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_attendence, container, false)

        // Initialize UI elements
        generateCodeButton = view.findViewById(R.id.generate_code_button)
        attendanceStatusTextView = view.findViewById(R.id.attendance_status_text_view)
        rollNoEditText = view.findViewById(R.id.roll_no_edit_text)
        codeEditText = view.findViewById(R.id.code_edit_text)
        submitCodeButton = view.findViewById(R.id.submit_code_button)
        teacherNameEditText = view.findViewById(R.id.teacher_name_edit_text)
        studentNameEditText = view.findViewById(R.id.student_name_edit_text)
        classIdEditText = view.findViewById(R.id.class_id_edit_text) // Add this

        generateCodeButton.setOnClickListener {
            generateCode()
        }

        submitCodeButton.setOnClickListener {
            submitCode()
        }

        return view
    }

    private fun generateCode() {
        val generatedCode = Random.nextInt(10, 100)
        val teacherName = teacherNameEditText.text.toString()
        val classId = classIdEditText.text.toString() // Get class ID

        // Handle code generation logic
        attendanceStatusTextView.text = "Generated Code: $generatedCode"

        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Update UI if needed
            }

            override fun onFinish() {
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.getAbsentStudents(classId) { absentStudents ->
                        val absentStudentsText = absentStudents.joinToString(", ")
                        attendanceStatusTextView.text = "Absent Students: $absentStudentsText"
                    }
                }
            }
        }.start()
    }

    private fun submitCode() {
        val rollNo = rollNoEditText.text.toString()
        val enteredCode = codeEditText.text.toString()
        val studentName = studentNameEditText.text.toString()
        val classId = classIdEditText.text.toString() // Get class ID

        if (enteredCode.isNotBlank() && rollNo.isNotBlank() && studentName.isNotBlank()) {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.submitRollNo(classId, rollNo, studentName)
            }
        }
    }
}
