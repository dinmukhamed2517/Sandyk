package kz.sdk.sandykprot.screens

import androidx.navigation.fragment.findNavController
import kz.sdk.sandykprot.R
import kz.sdk.sandykprot.base.BaseFragment
import kz.sdk.sandykprot.databinding.FragmentChooseBinding

class ChooseFragment:BaseFragment<FragmentChooseBinding>(FragmentChooseBinding::inflate) {
    override fun onBindView() {
        super.onBindView()

        binding.listBtn.setOnClickListener {
            findNavController().navigate(R.id.action_chooseFragment_to_listFragment)
        }
        binding.divideBtn.setOnClickListener {
            findNavController().navigate(R.id.action_chooseFragment_to_divideFragment)
        }
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}