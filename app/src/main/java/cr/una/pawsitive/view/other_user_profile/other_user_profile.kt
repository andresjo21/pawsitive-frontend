package cr.una.pawsitive.view.other_user_profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import cr.una.pawsitive.R
import cr.una.pawsitive.adapter.PostAdapter.Companion.USER_EMAIL
import cr.una.pawsitive.databinding.FragmentOtherUserProfileBinding
import cr.una.pawsitive.viewmodel.PostViewModel
import cr.una.pawsitive.viewmodel.StatePost


class other_user_profile : Fragment() {
    private var _binding: FragmentOtherUserProfileBinding? = null
    private val binding get() = _binding!!

    private val postViewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOtherUserProfileBinding.inflate(inflater, container, false)

        val userEmail: String = arguments?.getString(USER_EMAIL) ?: "0"

        binding.textViewMail.text = userEmail

        // Inflate the layout for this fragment
        return binding.root
    }

}