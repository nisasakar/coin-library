package com.example.coinlibrary.ui.login

import android.os.Bundle
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.coinlibrary.R
import com.example.coinlibrary.compose.BaseFragment
import com.example.coinlibrary.compose.viewBinding
import com.example.coinlibrary.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentLogin : BaseFragment(R.layout.fragment_login) {
    private val binding: FragmentLoginBinding by viewBinding(FragmentLoginBinding::bind)

    override fun observeVariables() = Unit

    override fun initUI(savedInstanceState: Bundle?) {
        binding.apply {
            btnLogin.setOnClickListener {
                if (etEmail.text.toString().isNotEmpty() && etPassword.text.toString().isNotEmpty()
                ) {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(
                        etEmail.text.toString(),
                        etPassword.text.toString()
                    )
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful)
                                findNavController().navigate(R.id.action_fragmentLogin_to_fragmentMain)
                            else
                                Toast.makeText(
                                    requireContext(),
                                    "Invalid email or password",
                                    Toast.LENGTH_SHORT
                                ).show()

                        }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Please fill the empty fields",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}