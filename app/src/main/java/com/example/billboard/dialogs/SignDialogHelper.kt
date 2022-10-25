package com.example.billboard.dialogs

import android.app.AlertDialog
import android.view.View
import android.widget.Toast
import com.example.billboard.consts.DialogConsts
import com.example.billboard.activities.MainActivity
import com.example.billboard.R
import com.example.billboard.accountHelper.AccountHelper
import com.example.billboard.databinding.SignDialogBinding

class SignDialogHelper(act: MainActivity) {

    private val act = act
    val accHelper = AccountHelper(act)

    fun createSignDialog(index: Int) {
        val builder = AlertDialog.Builder(act)
        val rootView = SignDialogBinding.inflate(act.layoutInflater)
        builder.setView(rootView.root)
        val dialog = builder.create()

        setDialogState(index, rootView)

        rootView.dialogButton.setOnClickListener {
            setOnClickButtonSignInOrUp(index, rootView, dialog)
        }

        rootView.forgetPasswordButton.setOnClickListener {
            rootView.googleSignInButton.visibility = View.GONE
            setOnClickResetPassword(rootView)
        }

        rootView.googleSignInButton.setOnClickListener {
            accHelper.singInWithGoogle()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setOnClickResetPassword(binding: SignDialogBinding) {
        binding.tvTitleDialog.visibility = View.GONE
        binding.tvDialogEmailMessage.visibility = View.VISIBLE
        binding.password.visibility = View.GONE
        binding.dialogButton.visibility = View.GONE
        binding.forgetPasswordButton.text = act.resources.getString(R.string.send_verification)
        binding.forgetPasswordButton.visibility = View.VISIBLE
        if (binding.email.text.isNotEmpty()) {
            accHelper.forgotPassword(binding.email.text.toString())
        } else {
            Toast.makeText(
                act,
                "Введите ваш E-mail",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setDialogState(index: Int, binding: SignDialogBinding) {
        if (index == DialogConsts.SIGN_IN_STATE) {
            binding.tvTitleDialog.text = act.resources.getString(R.string.sign_in)
            binding.dialogButton.text = act.resources.getString(R.string.sign_in_act)
            binding.forgetPasswordButton.visibility = View.VISIBLE
        } else {
            binding.tvTitleDialog.text = act.resources.getString(R.string.sign_up)
            binding.dialogButton.text = act.resources.getString(R.string.sign_up_act)
        }
    }

    private fun setOnClickButtonSignInOrUp(index: Int, binding: SignDialogBinding, dialog: AlertDialog?) {
        if (index == DialogConsts.SIGN_UP_STATE) {
            accHelper.signUpWithEmail(
                binding.email.text.toString(),
                binding.password.text.toString()
            )
            dialog?.dismiss()
        }
        else {
            accHelper.signInWithEmail(
                binding.email.text.toString(),
                binding.password.text.toString()
            )
            dialog?.dismiss()
        }
    }
}