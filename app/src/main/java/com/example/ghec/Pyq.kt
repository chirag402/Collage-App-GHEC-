package com.example.ghec

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class PyqFragment : Fragment() {

    private lateinit var storageReference: StorageReference
    private val PICK_FILE_REQUEST_CODE = 1234

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pyq, container, false)

        val spinnerSemester: Spinner = view.findViewById(R.id.spinner_semester)
        val spinnerBranch: Spinner = view.findViewById(R.id.spinner_branch)
        val spinnerSubject: Spinner = view.findViewById(R.id.spinner_subject)
        val buttonGetPapers: Button = view.findViewById(R.id.button_get_papers)
        val buttonUploadPapers: Button = view.findViewById(R.id.button_upload_papers)

        // Initialize Firebase Storage
        storageReference = FirebaseStorage.getInstance().reference

        // Initialize spinners
        val semesters = arrayOf("Semester 1", "Semester 2", "Semester 3", "Semester 4", "Semester 5", "Semester 6", "Semester 7", "Semester 8")
        val branches = arrayOf("Applied Science", "Electrical", "Computer", "Civil")

        val semesterAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, semesters)
        semesterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSemester.adapter = semesterAdapter

        val branchAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, branches)
        branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBranch.adapter = branchAdapter

        // Initialize subject spinner with a mutable list
        val subjects = mutableListOf<String>()
        val subjectAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, subjects)
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSubject.adapter = subjectAdapter

        // Update subjects based on selected semester
        spinnerSemester.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedSemester = parent.getItemAtPosition(position).toString()
                updateSubjects(selectedSemester, subjectAdapter)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Set click listener for Get Question Papers button
        buttonGetPapers.setOnClickListener {
            val semester = spinnerSemester.selectedItem as String
            val branch = spinnerBranch.selectedItem as String
            val subject = spinnerSubject.selectedItem as String

            val fileName = "${branch.replace(" ", "_")}_${semester.replace(" ", "_")}_${subject.replace(" ", "_")}.pdf"
            downloadFile(fileName)
        }

        // Set click listener for Upload Question Papers button
        buttonUploadPapers.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf" // Assuming question papers are PDFs
            startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { fileUri ->
                uploadFile(fileUri)
            }
        }
    }

    private fun uploadFile(fileUri: Uri) {
        val semester = (view?.findViewById<Spinner>(R.id.spinner_semester)?.selectedItem as String).replace(" ", "_")
        val branch = (view?.findViewById<Spinner>(R.id.spinner_branch)?.selectedItem as String).replace(" ", "_")
        val subject = (view?.findViewById<Spinner>(R.id.spinner_subject)?.selectedItem as String).replace(" ", "_")
        val fileName = "${branch}_${semester}_${subject}.pdf"
        val fileRef = storageReference.child("question_papers/$fileName")

        fileRef.putFile(fileUri)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "File Uploaded Successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "File Upload Failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun downloadFile(fileName: String) {
        val fileRef = storageReference.child("question_papers/$fileName")
        val localFile = File(requireContext().cacheDir, fileName)

        fileRef.getFile(localFile)
            .addOnSuccessListener {
                // Open the downloaded file
                val fileUri = FileProvider.getUriForFile(requireContext(), "com.example.ghec.fileprovider", localFile)
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(fileUri, "application/pdf")
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                try {
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(requireContext(), "No application available to view PDF", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "File Download Failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateSubjects(selectedSemester: String, adapter: ArrayAdapter<String>) {
        val subjects = when (selectedSemester) {
            "Semester 1", "Semester 2" -> listOf("Physics", "Basic Electrical", "Communication Skills", "EVS", "Maths 1", "Maths 2", "Programming with C++", "Human Values", "Chemistry", "Basic Electronics")
            else -> listOf() // Add subjects for other semesters as needed
        }
        adapter.clear()
        adapter.addAll(subjects)
        adapter.notifyDataSetChanged()
    }
}
