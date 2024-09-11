package kz.sdk.sandykprot.screens

import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kz.sdk.sandykprot.R
import kz.sdk.sandykprot.adapters.ClassAdapter
import kz.sdk.sandykprot.adapters.StudentAdapter
import kz.sdk.sandykprot.base.BaseFragment
import kz.sdk.sandykprot.database.StudentEntity
import kz.sdk.sandykprot.databinding.FragmentListBinding
import kz.sdk.sandykprot.viewmodel.StudentViewModel

class ListFragment : BaseFragment<FragmentListBinding>(FragmentListBinding::inflate) {

    private val studentViewModel: StudentViewModel by viewModels()

    private lateinit var classAdapter: ClassAdapter

    override fun onBindView() {
        super.onBindView()

        classAdapter = ClassAdapter(findNavController())

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.chooseBtn.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_chooseFragment)
        }
        binding.divideBtn.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_divideFragment)
        }

        binding.recyclerViewClasses.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = classAdapter
        }

        studentViewModel.getClassesWithStudents().observe(viewLifecycleOwner) { classList ->
            classAdapter.submitList(classList)
        }

        binding.saveBtn.setOnClickListener {
            val className = binding.etClass.text.toString().trim()
            val studentNames = binding.etStudents.text.toString().split(",").map { it.trim() }

            if (className.isNotEmpty() && studentNames.isNotEmpty()) {
                studentViewModel.addStudents(className, studentNames)
                binding.etClass.text?.clear()
                binding.etStudents.text?.clear()
                showCustomDialog("Успешно прошло", "Заполненные данные успешно сохранены")
            }
            else{
                showCustomCancelDialog("Ошибка", "В заполненном источнике информации обнаружена ошибка. Пожалуйста, заполните заново.")
            }
        }
    }

}