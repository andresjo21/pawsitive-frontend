package cr.una.pawsitive.view.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import cr.una.pawsitive.adapter.PostAdapter
import cr.una.pawsitive.databinding.FragmentHomeBinding
import cr.una.pawsitive.viewmodel.PostViewModel
import cr.una.pawsitive.viewmodel.StatePost
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import cr.una.pawsitive.repository.FavouriteRepository
import cr.una.pawsitive.service.FavouriteService
import cr.una.pawsitive.viewmodel.PostViewModelFactory

class Home : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val postViewModel: PostViewModel by activityViewModels{
        PostViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Create an instance of FavouriteRepository
        val favouriteRepository = FavouriteRepository(FavouriteService.getInstance())

        val adapter = PostAdapter(postViewModel,favouriteRepository, viewLifecycleOwner)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val rootView = binding.root

        // Asignar el administrador de diseño al RecyclerView
        binding.rvPost.layoutManager = LinearLayoutManager(requireContext())

        binding.rvPost.adapter = adapter

        postViewModel.findAllPosts()

        postViewModel.state.observe(viewLifecycleOwner) { state ->

            Log.i("HomeFragment", "State: $state")
            when (state) {
                StatePost.Loading -> {
                    // TODO: Si necesitas hacer algo durante la carga
                    Log.i("HomeFragment", "Inside Loading")
                }
                is StatePost.Error -> {
                    // TODO: Si necesitas hacer algo en caso de error
                    Log.i("HomeFragment", "Inside Error")
                }
                is StatePost.SuccessList -> {
                    state.postList?.let { adapter.setPostList(it) }
                }
                else -> {
                    // TODO: No se cargó ningún estado
                }
            }

        }


        return rootView
    }
}