package kz.sdk.sandykprot.screens

import android.animation.ValueAnimator
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.animation.addListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kz.sdk.sandykprot.R
import kz.sdk.sandykprot.base.BaseFragment
import kz.sdk.sandykprot.databinding.FragmentSpinBinding
import kz.sdk.sandykprot.viewmodel.StudentViewModel

class SpinFragment : BaseFragment<FragmentSpinBinding>(FragmentSpinBinding::inflate) {

    private val studentViewModel: StudentViewModel by viewModels()
    private var selectedClass: String? = null
    private var studentNames = listOf<String>()
    private val maxStudents = 8

    override fun onBindView() {
        super.onBindView()




        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.divideBtn.setOnClickListener {
            findNavController().navigate(R.id.action_spinFragment_to_divideFragment)
        }
        binding.airportBtn.setOnClickListener {
            findNavController().navigate(R.id.action_spinFragment_to_airportFragment)
        }
        binding.byListBtn.setOnClickListener {
            findNavController().navigate(R.id.action_spinFragment_to_chooseFragment)
        }
        binding.listBtn.setOnClickListener {
            findNavController().navigate(R.id.action_spinFragment_to_listFragment)
        }
        studentViewModel.getAllClassNames().observe(viewLifecycleOwner) { classList ->
            setupClassDropdown(classList)
        }

        binding.actionBtn.setOnClickListener {
            spinWheel()
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
                studentNames = students.map { it.studentName }.take(maxStudents)
                setupWheel()
            } else {
                studentNames = emptyList()
                showCustomCancelDialog("Ошибка", "В этом классе нет студентов")
            }
        }
    }

    private fun setupWheel() {
        binding.customWheelView.setSegments(studentNames)
    }

    private fun spinWheel() {
        if (studentNames.isNotEmpty()) {
            val randomAngle = (360..720).random()
            val finalAngle = randomAngle + (360 * 3)

            val animator = ValueAnimator.ofFloat(binding.customWheelView.getCurrentAngle(),
                finalAngle.toFloat()
            ).apply {
                duration = 3000
                interpolator = DecelerateInterpolator()
                addUpdateListener {
                    binding.customWheelView.setAngle(it.animatedValue as Float)
                }
                start()
            }

            animator.addListener(onEnd = {
                val selectedSegment = binding.customWheelView.getSegmentAtAngle()
                val chosenStudent = studentNames[selectedSegment]
                showCustomDialog("Успешно", "Выбранный студент: $chosenStudent")
            })
        } else {
            showCustomCancelDialog("Ошибка", "Колесо пусто, выберите класс.")
        }
    }
}
