package kz.sdk.sandykprot.screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import kz.sdk.sandykprot.R
import kz.sdk.sandykprot.base.BaseFragment
import kz.sdk.sandykprot.databinding.FragmentAirportBinding
import kz.sdk.sandykprot.viewmodel.StudentViewModel
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class AirportFragment : BaseFragment<FragmentAirportBinding>(FragmentAirportBinding::inflate) {

    private val studentViewModel: StudentViewModel by viewModels()
    private var selectedClass: String? = null
    private var studentNames = listOf<String>()

    override fun onBindView() {
        super.onBindView()

        studentViewModel.getAllClassNames().observe(viewLifecycleOwner) { classList ->
            setupClassDropdown(classList)
        }

        binding.actionBtn.setOnClickListener {
            chooseRandomStudentAndDisplay()
        }

        binding.chooseBtn.setOnClickListener {
            findNavController().navigate(R.id.action_airportFragment_to_chooseFragment)
        }

        binding.spinnerBtn.setOnClickListener {
            findNavController().navigate(R.id.action_airportFragment_to_spinFragment)
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
                loadStudentsForClass(selectedClass!!)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                showCustomCancelDialog("Ошибка", "Выберите определенный класс")
            }
        }
    }

    private fun loadStudentsForClass(className: String) {
        studentViewModel.getStudentsByClass(className).observe(viewLifecycleOwner) { students ->
            if (students.isNotEmpty()) {
                studentNames = students.map { it.studentName }
            } else {
                studentNames = emptyList()
                showCustomCancelDialog("Ошибка", "В этом классе нет студентов")
            }
        }
    }

    private fun chooseRandomStudentAndDisplay() {
        if (studentNames.isNotEmpty()) {
            val randomStudent = studentNames[Random.nextInt(studentNames.size)]  // Pick a random student from the list
            displayChosenStudentWithDelay(randomStudent)
        } else {
            showCustomCancelDialog("Ошибка", "В этом классе нет студентов для выбора")
        }
    }

    private fun displayChosenStudentWithDelay(name: String) {
        val lettersLayout = binding.lettersLayout

        lettersLayout.removeAllViews()

        lifecycleScope.launch {
            for (char in name) {
                val letterView = LayoutInflater.from(requireContext())
                    .inflate(R.layout.item_letter, lettersLayout, false) as TextView
                letterView.text = char.toString()
                lettersLayout.addView(letterView)

                delay(700L)
            }

            val maxLetters = 10
            for (i in name.length until maxLetters) {
                val underscoreView = LayoutInflater.from(requireContext())
                    .inflate(R.layout.item_letter, lettersLayout, false) as TextView
                underscoreView.text = "_"
                lettersLayout.addView(underscoreView)

                delay(700L)
            }
        }
    }
}
