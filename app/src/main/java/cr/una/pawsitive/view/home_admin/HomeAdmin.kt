package cr.una.pawsitive.view.home_admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import cr.una.pawsitive.R
import androidx.fragment.app.viewModels
import cr.una.pawsitive.viewmodel.PostViewModel
import cr.una.pawsitive.adapter.AdminPostAdapter
import cr.una.pawsitive.view.other_user_profile.other_user_profile
import androidx.recyclerview.widget.LinearLayoutManager
import cr.una.pawsitive.adapter.PostAdapter
import cr.una.pawsitive.databinding.FragmentHomeAdminBinding
import cr.una.pawsitive.model.BackendOption
import cr.una.pawsitive.service.ServiceBuilder
import cr.una.pawsitive.service.ServiceManager
import cr.una.pawsitive.viewmodel.PostViewModelFactory
import cr.una.pawsitive.viewmodel.StatePost


class HomeAdmin : Fragment() {
    private var _binding: FragmentHomeAdminBinding? = null
    private val binding get() = _binding!!
    //viewModel
    private val postViewModel: PostViewModel by activityViewModels{
        PostViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //adapter
        val adapter = AdminPostAdapter()
        // Inflate the layout for this fragment
        _binding = FragmentHomeAdminBinding.inflate(inflater, container, false)

        val rootView = binding.root

        // Asignar el administrador de diseño al RecyclerView
        binding.rvPostAdmin.layoutManager = LinearLayoutManager(requireContext())

        binding.rvPostAdmin.adapter = adapter

        // create options array with label and value
        val arrayBackends = arrayOf(
            BackendOption("Heroku", "https://pawsitive-v01-28a4957aae0f.herokuapp.com/"),
            BackendOption("Docker on Google Cloud", "http://34.72.177.223:8081/")
        )

        //get spinner and set the options
        binding.spinnerBackendSelector.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, arrayBackends)

        //set the listener for the spinner to log the selected option for now
        binding.spinnerBackendSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedOption = arrayBackends[position]
                Log.i("HomeAdminFragment", "Selected option: $selectedOption")
                Log.i("HomeAdminFragment", "Selected option value: ${selectedOption.value}")
                //update base url in the service builder
                ServiceManager.updateBaseUrl(selectedOption.value)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.i("HomeAdminFragment", "Nothing selected")
            }
        }

        postViewModel.findAllPosts()

        postViewModel.state.observe(viewLifecycleOwner) { state ->

            Log.i("HomeAdminFragment", "State: $state")
            when (state) {
                StatePost.Loading -> {
                    // TODO: Si necesitas hacer algo durante la carga
                    Log.i("HomeAdminFragment", "Inside Loading")
                }
                is StatePost.Error -> {
                    // TODO: Si necesitas hacer algo en caso de error
                    Log.i("HomeAdminFragment", "Inside Error")
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