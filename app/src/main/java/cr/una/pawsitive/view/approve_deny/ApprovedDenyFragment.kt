package cr.una.pawsitive.view.approve_deny

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.animateDpAsState
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import cr.una.pawsitive.R
import cr.una.pawsitive.adapter.PostAdapter
import cr.una.pawsitive.databinding.FragmentApprovedDenyBinding
import cr.una.pawsitive.repository.FavouriteRepository
import cr.una.pawsitive.service.FavouriteService
import cr.una.pawsitive.viewmodel.PostViewModel
import cr.una.pawsitive.view.other_user_profile.other_user_profile
class ApprovedDenyFragment : Fragment() {

    // Definition of the binding variable
    private lateinit var binding : FragmentApprovedDenyBinding
    private val postViewModel: PostViewModel by viewModels()

    // Definition of the adapter variable
    private lateinit var adapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentApprovedDenyBinding.inflate(inflater, container, false)

        // Create an instance of FavouriteRepository
        val favouriteRepository = FavouriteRepository(FavouriteService.getInstance())

        // Create an instance of PostAdapter
        adapter = PostAdapter(postViewModel, favouriteRepository, viewLifecycleOwner)

        //Connect the recycle view with the adapter
        binding.rvAprvDny.layoutManager = LinearLayoutManager(context)
        binding.rvAprvDny.adapter = adapter



        postViewModel.findAllPosts()

        return binding.root
    }
}