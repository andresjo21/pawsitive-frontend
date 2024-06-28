package cr.una.pawsitive.view.user_profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import cr.una.pawsitive.R
import cr.una.pawsitive.databinding.FragmentUserProfileBinding
import cr.una.pawsitive.view.LoginActivity
import cr.una.pawsitive.view.MainActivity
import cr.una.pawsitive.viewmodel.LoginViewModel
import cr.una.pawsitive.viewmodel.LoginViewModelFactory
import cr.una.pawsitive.viewmodel.StateUser
import cr.una.pawsitive.viewmodel.UserViewModel
import cr.una.pawsitive.viewmodel.UserViewModelFactory

class fragment_user_profile : Fragment() {
    private var _binding: FragmentUserProfileBinding? = null
    private lateinit var loginViewModel: LoginViewModel
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by activityViewModels{
        UserViewModelFactory()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)

        // LoginViewModelFactory
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class.java]

        userViewModel.state.observe(viewLifecycleOwner) { state ->
            with(binding.root) {
                when (state) {
                    StateUser.Loading -> {}
                    is StateUser.Error -> {}
                    is StateUser.Success -> {
                        state.user?.let {
                            binding.textViewMail.text = it.email
                        }
                    }
                    else -> {}
                }
            }
        }

        binding.buttonLogOut.setOnClickListener {
            // Logout with viewModel
            loginViewModel.logout()
            Log.i("Logout", "User has logged out")
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            //finish the current activity
            activity?.finish()
            startActivity(intent)
        }

        userViewModel.getUser()

        return binding.root
    }
}