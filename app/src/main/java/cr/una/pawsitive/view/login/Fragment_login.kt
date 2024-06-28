package cr.una.pawsitive.view.login

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo  // Añadir este import
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cr.una.pawsitive.R
import cr.una.pawsitive.databinding.FragmentLoginBinding
import cr.una.pawsitive.model.LoggedInUserView
import cr.una.pawsitive.model.LoginRequest
import cr.una.pawsitive.view.MainActivity
import cr.una.pawsitive.view.register.fragment_register
import cr.una.pawsitive.viewmodel.LoginViewModel
import cr.una.pawsitive.viewmodel.LoginViewModelFactory

class fragment_login : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class.java]

        // Observa el estado del formulario de inicio de sesión
        loginViewModel.loginFormState.observe(viewLifecycleOwner, Observer {
            val loginState = it ?: return@Observer

            binding.layoutLogin.loginButton.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                binding.layoutLogin.editTextText.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                binding.layoutLogin.editTextTextPassword.error = getString(loginState.passwordError)
            }
        })

        // Observa la respuesta del inicio de sesión
        loginViewModel.loginResponse.observe(viewLifecycleOwner, Observer {
            val loginResult = it ?: return@Observer

            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
        })

        // Configura los eventos de texto cambiado
        binding.layoutLogin.editTextText.afterTextChanged {
            loginViewModel.loginDataChanged(
                LoginRequest(
                    username = binding.layoutLogin.editTextText.text.toString(),
                    password = binding.layoutLogin.editTextTextPassword.text.toString()
                )
            )
        }

        binding.layoutLogin.editTextTextPassword.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    LoginRequest(
                        username = binding.layoutLogin.editTextText.text.toString(),
                        password = binding.layoutLogin.editTextTextPassword.text.toString()
                    )
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(
                        LoginRequest(
                            username = binding.layoutLogin.editTextText.text.toString(),
                            password = binding.layoutLogin.editTextTextPassword.text.toString()
                        )
                    )
                }
                false
            }
        }

        // Configura el evento onClick para el botón de inicio de sesión
        binding.layoutLogin.loginButton.setOnClickListener {
            loginViewModel.login(
                LoginRequest(
                    username = binding.layoutLogin.editTextText.text.toString(),
                    password = binding.layoutLogin.editTextTextPassword.text.toString()
                )
            )
        }


        // Configura el evento onClick para el botón de administrador
//        binding.adminButton.setOnClickListener {
//            Log.i("Fragment_login", "Admin button clicked")
//            try {
//                (activity?.application as App).isAdmin = true
//                val intent = Intent(activity, MainActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                startActivity(intent)
//            } catch (e: Exception) {
//                Log.e("Fragment_login", "Error in fragment transaction", e)
//            }
//        }

        // Configura el evento onClick para el botón de registro desde el inicio de sesión
        binding.signUpFormLoginButton.setOnClickListener {
            Log.i("Fragment_login", "Sign up from login button clicked")
            try {
                val fragment = fragment_register()
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView, fragment)
                transaction.addToBackStack(null)
                transaction.commit()
            } catch (e: Exception) {
                Log.e("Fragment_login", "Error in fragment transaction", e)
            }
        }

        return binding.root
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        activity?.setResult(RESULT_OK)
        activity?.finish()

        (activity?.application as cr.una.pawsitive.utils.MyApplication).isAdmin = false

        model.authorities.forEach {
            Log.i("Fragment_login", "Authority: ${it.authority}")
            if (it.authority == "admin") {
                (activity?.application as cr.una.pawsitive.utils.MyApplication).isAdmin = true
                val intent = Intent(activity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                return@forEach  // Exit the loop after starting MainActivity
            }
        }

        // If not admin or no admin authority found, start MainActivity normally
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(context, errorString, Toast.LENGTH_SHORT).show()
    }
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}