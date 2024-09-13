package kz.sdk.sandykprot.screens

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController
import kz.sdk.sandykprot.R
import kz.sdk.sandykprot.adapters.StudentChoiceAdapter
import kz.sdk.sandykprot.base.BaseFragment
import kz.sdk.sandykprot.database.StudentEntity
import kz.sdk.sandykprot.databinding.FragmentChooseBinding
import kz.sdk.sandykprot.viewmodel.StudentViewModel

class ChooseFragment : BaseFragment<FragmentChooseBinding>(FragmentChooseBinding::inflate) {
    private val studentViewModel: StudentViewModel by viewModels()
    private lateinit var studentAdapter: StudentChoiceAdapter
    private lateinit var selectedClass: String

    override fun onBindView() {
        super.onBindView()

        studentAdapter = StudentChoiceAdapter()
        binding.listStudents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = studentAdapter
        }

        studentViewModel.getAllClassNames().observe(viewLifecycleOwner) { classList ->
            setupClassDropdown(classList)
        }

        binding.listBtn.setOnClickListener {
            findNavController().navigate(R.id.action_chooseFragment_to_listFragment)
        }
        binding.divideBtn.setOnClickListener {
            findNavController().navigate(R.id.action_chooseFragment_to_divideFragment)
        }
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.spinnerBtn.setOnClickListener {
            findNavController().navigate(R.id.action_chooseFragment_to_spinFragment)
        }

        binding.airportBtn.setOnClickListener {
            findNavController().navigate(R.id.action_chooseFragment_to_airportFragment)
        }

        binding.actionBtn.setOnClickListener {
            val selectedStudent = studentAdapter.getSelectedStudent()
            if (selectedStudent != null) {
                studentAdapter.deleteSelectedStudent()
            }
            showCustomDialog("Успешно", "Ученик ${studentAdapter.studentName} выбран")

        }
    }

    private fun setupClassDropdown(classList: List<String>) {
        val spinner = binding.spinnerClasses

        val adapter = ArrayAdapter(requireContext(), R.layout.item_spinner_custom, classList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedClass = parent.getItemAtPosition(position) as String
                loadStudentsForClass(selectedClass)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                showCustomCancelDialog("Ошибка", "Выберите определенный класс")
            }
        }
    }

    private fun loadStudentsForClass(className: String) {
        studentViewModel.getStudentsByClass(className).observe(viewLifecycleOwner) { students ->
            updateStudentList(students)
        }
    }

    private fun updateStudentList(students: List<StudentEntity>) {
        studentAdapter.submitList(students)
    }

}
