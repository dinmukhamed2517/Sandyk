package kz.sdk.sandykprot.screens

import androidx.navigation.fragment.findNavController
import kz.sdk.sandykprot.R
import kz.sdk.sandykprot.adapters.GroupAdapter
import kz.sdk.sandykprot.base.BaseFragment
import kz.sdk.sandykprot.database.GroupEntity
import kz.sdk.sandykprot.databinding.FragmentDivideBinding
import kz.sdk.sandykprot.viewmodel.StudentViewModel
import androidx.fragment.app.viewModels
import android.widget.ArrayAdapter
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.GridLayoutManager
import kz.sdk.sandykprot.database.StudentEntity

class DivideFragment : BaseFragment<FragmentDivideBinding>(FragmentDivideBinding::inflate) {

    private val studentViewModel: StudentViewModel by viewModels()
    private var studentNames = listOf<StudentEntity>()
    private var selectedClass: String? = null
    private var selectedGroupCount: Int = 2

    override fun onBindView() {
        super.onBindView()

        setupClassDropdown()
        setupGroupCountDropdown()

        binding.listBtn.setOnClickListener {
            findNavController().navigate(R.id.action_divideFragment_to_listFragment)
        }
        binding.chooseBtn.setOnClickListener {
            findNavController().navigate(R.id.action_divideFragment_to_chooseFragment)
        }
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.actionBtn.setOnClickListener {
            divideStudentsIntoGroups()
        }
    }

    private fun setupClassDropdown() {
        studentViewModel.getAllClassNames().observe(viewLifecycleOwner) { classList ->
            val spinner = binding.spinnerClasses
            val adapter = ArrayAdapter(requireContext(), R.layout.item_spinner_custom, classList)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    selectedClass = parent.getItemAtPosition(position) as String
                    loadStudentsForClass(selectedClass!!)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    showCustomCancelDialog("Ошибка", "Выберите класс")
                }
            }
        }
    }

    private fun setupGroupCountDropdown() {
        val groupCounts = listOf(2, 3, 4, 5, 6)
        val adapter = ArrayAdapter(requireContext(), R.layout.item_spinner_custom, groupCounts)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGroupCount.adapter = adapter

        binding.spinnerGroupCount.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedGroupCount = parent.getItemAtPosition(position) as Int
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                showCustomCancelDialog("Ошибка", "Выберите количество групп")
            }
        }
    }

    private fun loadStudentsForClass(className: String) {
        studentViewModel.getStudentsByClass(className).observe(viewLifecycleOwner) { students ->
            if (students.isNotEmpty()) {
                studentNames = students
            } else {
                studentNames = emptyList()
                showCustomCancelDialog("Ошибка", "В этом классе нет студентов")
            }
        }
    }

    private fun divideStudentsIntoGroups() {
        if (studentNames.isNotEmpty()) {
            val shuffledStudents = studentNames.shuffled()
            val groups = List(selectedGroupCount) { mutableListOf<StudentEntity>() }

            shuffledStudents.forEachIndexed { index, student ->
                groups[index % selectedGroupCount].add(student)
            }

            displayGroups(groups)
        } else {
            showCustomCancelDialog("Ошибка", "Нет студентов для разделения на группы")
        }
    }

    private fun displayGroups(groups: List<MutableList<StudentEntity>>) {
        val groupEntities = groups.mapIndexed { index, students ->
            GroupEntity("Группа ${index + 1}", students)
        }

        binding.listStudents.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = GroupAdapter().apply {
                submitList(groupEntities)
            }
        }
    }
}
