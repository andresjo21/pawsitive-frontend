package cr.una.pawsitive.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cr.una.pawsitive.R
import cr.una.pawsitive.databinding.ActivityLoginBinding
import cr.una.pawsitive.databinding.ActivityMainBinding
import cr.una.pawsitive.model.LoggedInUserView
import cr.una.pawsitive.model.LoginRequest
import cr.una.pawsitive.view.login.fragment_login
import cr.una.pawsitive.viewmodel.LoginViewModel
import cr.una.pawsitive.viewmodel.LoginViewModelFactory

class LoginActivity : AppCompatActivity() {
    // Definition of the binding variable
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // With View Binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
//
//        val fragment = binding.fragmentContainerView.getFragment<Fragment>()
//        val loginButton = fragment.view?.findViewById<Button>(R.id.loginButton)
//        val username = fragment.view?.findViewById<EditText>(R.id.editTextText)
//        val password = fragment.view?.findViewById<EditText>(R.id.editTextTextPassword)
//
//        // LoginViewModelFactory
//        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class.java]
//
//        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
//            val loginState = it ?: return@Observer
//            // disable login button unless both username / password is valid
//            loginButton?.isEnabled = loginState.isDataValid
//            if (loginState.usernameError != null) {
//                username?.error = getString(loginState.usernameError)
//            }
//            if (loginState.passwordError != null) {
//                password?.error = getString(loginState.passwordError)
//            }
//        })
//
//        loginViewModel.loginResponse.observe(this@LoginActivity, Observer {
//            val loginResult = it ?: return@Observer
//
//            if (loginResult.error != null) {
//                showLoginFailed(loginResult.error)
//            }
//            if (loginResult.success != null) {
//                updateUiWithUser(loginResult.success)
//            }
//            setResult(Activity.RESULT_OK)
//        })
//
//        username?.afterTextChanged {
//            loginViewModel.loginDataChanged(
//                LoginRequest(
//                    username = username.text.toString(),
//                    password = password?.text.toString()
//                )
//            )
//        }
//
//        password?.apply {
//            afterTextChanged {
//                loginViewModel.loginDataChanged(
//                    LoginRequest(
//                        username = username?.text.toString(),
//                        password = password.text.toString()
//                    )
//                )
//            }
//
//            setOnEditorActionListener { _, actionId, _ ->
//                when (actionId) {
//                    EditorInfo.IME_ACTION_DONE ->
//                        loginViewModel.login(
//                            LoginRequest(
//                                username = username?.text.toString(),
//                                password = password.text.toString()
//                            )
//                        )
//                }
//                false
//            }
//
//            loginButton?.setOnClickListener {
//                loginViewModel.login(
//                    LoginRequest(
//                        username = username?.text.toString(),
//                        password = password.text.toString()
//                    )
//                )
//            }
//        }
//
//        loginViewModel.loginDataChanged(
//            LoginRequest(
//                username = username?.text.toString(),
//                password = password?.text.toString()
//            )
//        )
    }

    /**
     * Success login to redirect the app to the next Screen
     */
    private fun updateUiWithUser(model: LoggedInUserView) {
        //Complete and destroy login activity once successful
        finish()

        // Initiate successful logged in experience
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    /**
     * Unsuccessful login
     */
    private fun showLoginFailed(@StringRes errorString: Int) {

        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
        // return login activity

    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}