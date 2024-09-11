package kz.sdk.sandykprot.base

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.airbnb.lottie.LottieAnimationView
import kz.sdk.sandykprot.R
import java.lang.Exception
import java.lang.RuntimeException


typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T


abstract class BaseFragment<VB : ViewBinding>(private val inflate: Inflate<VB>) : Fragment() {
    private var _binding: VB? = null

    val binding get() = _binding ?: throw RuntimeException()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            onInit()
            onBindView()
            bindViewModel()
        } catch (e: Exception) {
            Log.e("OnViewCreated", "Exception by view binding: ${e.message}")
        }
    }

    protected fun showCustomDialog(title: String, content: String) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_success_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val animationView: LottieAnimationView = dialog.findViewById(R.id.animation)
        val titleTextView: TextView = dialog.findViewById(R.id.title)
        val contentTextView: TextView = dialog.findViewById(R.id.content)

        animationView.playAnimation()
        titleTextView.text = title
        contentTextView.text = content
        dialog.show()
        val button: Button = dialog.findViewById(R.id.ok_btn)
        button.setOnClickListener {
            dialog.dismiss()
        }
    }

    protected fun showCustomCancelDialog(title: String, content: String) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_cancel_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val animationView: LottieAnimationView = dialog.findViewById(R.id.cancel_animation)
        val titleTextView: TextView = dialog.findViewById(R.id.cancel_title)
        val contentTextView: TextView = dialog.findViewById(R.id.cancel_content)

        animationView.playAnimation()
        titleTextView.text = title
        contentTextView.text = content
        dialog.show()
        val button: Button = dialog.findViewById(R.id.cancel_ok_btn)
        button.setOnClickListener {
            dialog.dismiss()
        }
    }
    fun showDeleteConfirmationDialog(studentName: String, onDeleteConfirmed: () -> Unit) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_confirm_delete, null)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()

        val btnYes = dialogView.findViewById<Button>(R.id.btn_yes)
        val btnNo = dialogView.findViewById<Button>(R.id.btn_no)

        btnYes.setOnClickListener {
            onDeleteConfirmed()
            dialog.dismiss()
        }

        btnNo.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    open fun onInit() {}
    open fun onBindView() {}
    open fun bindViewModel() {}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}