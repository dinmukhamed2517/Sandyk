package kz.sdk.sandykprot.screens

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import kz.sdk.sandykprot.adapters.StudentAdapter
import kz.sdk.sandykprot.base.BaseFragment
import kz.sdk.sandykprot.databinding.FragmentAddBinding
import kz.sdk.sandykprot.viewmodel.StudentViewModel

class AddFragment:BaseFragment<FragmentAddBinding>(FragmentAddBinding::inflate) {


    private val studentViewModel: StudentViewModel by viewModels()

    private val args:AddFragmentArgs by navArgs()
    private lateinit var studentAdapter: StudentAdapter

    override fun onBindView() {
        super.onBindView()




        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }



        binding.addBtn.setOnClickListener {
            val newStudentName = binding.etStudentName.text.toString().trim()
            val className = args.className

            if (newStudentName.isNotEmpty()) {
                studentViewModel.addStudents(className, listOf(newStudentName))
                binding.etStudentName.text?.clear()
                showCustomDialog("Успешно", "Ученик добавлен")
            }
            else{
                showCustomCancelDialog("Ошибка", "В заполненном источнике информации обнаружена ошибка. Пожалуйста, заполните заново.")
            }
        }
    }

}