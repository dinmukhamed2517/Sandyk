package kz.sdk.sandykprot.screens

import androidx.navigation.fragment.findNavController
import kz.sdk.sandykprot.R
import kz.sdk.sandykprot.base.BaseFragment
import kz.sdk.sandykprot.databinding.FragmentDivideBinding

class DivideFragment:BaseFragment<FragmentDivideBinding>(FragmentDivideBinding::inflate) {
    override fun onBindView() {
        super.onBindView()

        binding.listBtn.setOnClickListener {
            findNavController().navigate(R.id.action_divideFragment_to_listFragment)
        }
        binding.chooseBtn.setOnClickListener {
            findNavController().navigate(R.id.action_divideFragment_to_chooseFragment)
        }
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }



}