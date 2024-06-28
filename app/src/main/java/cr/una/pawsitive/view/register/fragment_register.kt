package cr.una.pawsitive.view.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cr.una.pawsitive.R
import cr.una.pawsitive.databinding.FragmentRegisterBinding
import cr.una.pawsitive.model.UserSignUp
import cr.una.pawsitive.view.login.fragment_login
import cr.una.pawsitive.viewmodel.SignUpViewModel
import cr.una.pawsitive.viewmodel.SignUpViewModelFactory

class fragment_register : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var signUpViewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        val viewModelFactory = SignUpViewModelFactory()
        signUpViewModel = ViewModelProvider(this, viewModelFactory).get(SignUpViewModel::class.java)

        signUpViewModel.signUpResponse.observe(viewLifecycleOwner, Observer { signUpSuccess ->
            signUpSuccess?.let {
                if (it) {
                    showToast("Registro Exitoso, realice el login desde la página principal")
                    clearInputFields()
                } else {
                    val errorMessage = signUpViewModel.errorMessage.value ?: "Error desconocido"
                    Log.e("SignUp", "Error en el registro: $errorMessage")
                    showToast("Error en el registro: $errorMessage")
                }
            }
        })

        binding.signUpButton.setOnClickListener {
            val user = getUserDataFromInputs()
            signUpViewModel.signUp(user)
        }

        binding.layoutLogin.loginButton.setOnClickListener {
            navigateToLoginFragment()
        }

        return binding.root
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun getUserDataFromInputs(): UserSignUp {
        val username = binding.editTextText2.text.toString()
        val firstName = binding.firstNameTV.text.toString()
        val lastName = binding.lastNameTV.text.toString()
        val email = binding.layoutLogin.editTextText.text.toString()
        val password = binding.layoutLogin.editTextTextPassword.text.toString()

        return UserSignUp(email, username, password, firstName, lastName)
    }

    private fun clearInputFields() {
        binding.editTextText2.text.clear()
        binding.firstNameTV.text.clear()
        binding.lastNameTV.text.clear()
        binding.layoutLogin.editTextText.text.clear()
        binding.layoutLogin.editTextTextPassword.text.clear()
    }

    private fun navigateToLoginFragment() {
        try {
            val fragment = fragment_login()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        } catch (e: Exception) {
            Log.e("Fragment_register", "Error in fragment transaction", e)
        }
    }
}
