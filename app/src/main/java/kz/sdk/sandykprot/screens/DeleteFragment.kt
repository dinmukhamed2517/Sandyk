package kz.sdk.sandykprot.screens

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import kz.sdk.sandykprot.adapters.StudentAdapter
import kz.sdk.sandykprot.base.BaseFragment
import kz.sdk.sandykprot.databinding.FragmentDeleteBinding
import kz.sdk.sandykprot.viewmodel.StudentViewModel

class DeleteFragment : BaseFragment<FragmentDeleteBinding>(FragmentDeleteBinding::inflate) {

    private val studentViewModel: StudentViewModel by viewModels()
    private lateinit var studentAdapter: StudentAdapter
    private val args:DeleteFragmentArgs by navArgs()

    override fun onBindView() {
        super.onBindView()

        studentAdapter = StudentAdapter()


        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.recyclerViewStudents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = studentAdapter
        }

        studentViewModel.getStudentsByClass(args.className).observe(viewLifecycleOwner) { students ->
            studentAdapter.submitList(students)
        }

        studentAdapter.setOnItemClickListener { student ->

            showDeleteConfirmationDialog(student.studentName){
                studentViewModel.deleteStudent(student)
            }
        }
    }
}