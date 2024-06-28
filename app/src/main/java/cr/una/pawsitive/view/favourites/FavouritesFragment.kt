package cr.una.pawsitive.view.favourites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import cr.una.pawsitive.adapter.FavouriteAdapter
import cr.una.pawsitive.databinding.FragmentFavouritesBinding
import cr.una.pawsitive.repository.FavouriteRepository
import cr.una.pawsitive.service.FavouriteService
import cr.una.pawsitive.viewmodel.PostViewModel
import cr.una.pawsitive.viewmodel.PostViewModelFactory
import cr.una.pawsitive.viewmodel.favourite.FavouriteViewModel
import cr.una.pawsitive.viewmodel.favourite.FavouriteViewModelFactory
import cr.una.pawsitive.viewmodel.favourite.StateFavourite

class FavouritesFragment : Fragment() {
    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!
    private val favouriteViewModel: FavouriteViewModel by activityViewModels {
        FavouriteViewModelFactory()
    }
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
        val adapter = FavouriteAdapter(postViewModel, favouriteRepository, viewLifecycleOwner)

        // Inflate the layout for this fragment
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)

        val rootView = binding.root

        //Connect the recycle view with the adapter
        binding.rvFavourite.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavourite.adapter = adapter

        favouriteViewModel.findAllFavouritePosts()
        favouriteViewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                StateFavourite.Loading -> {
                    // TODO: If you need to do something during loading
                }
                is StateFavourite.Error -> {
                    // TODO: If you need to do something in case of error
                }
                is StateFavourite.SuccessList -> {
                    state.favouriteList?.let { adapter.setFavouritesList(it) }
                }
                else -> {
                    // TODO: No se cargó ningún estado
                }
            }
        }
        return rootView
    }
}