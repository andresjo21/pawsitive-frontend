package cr.una.pawsitive.view.new_post

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import cr.una.pawsitive.BuildConfig
import cr.una.pawsitive.R
import cr.una.pawsitive.databinding.FragmentNewPostBinding
import cr.una.pawsitive.model.AnimalTypeNewPost
import cr.una.pawsitive.model.BreedNewPost
import cr.una.pawsitive.model.NewPost
import cr.una.pawsitive.model.PetNewPost
import cr.una.pawsitive.model.PostType
import cr.una.pawsitive.viewmodel.AnimalTypeViewModel
import cr.una.pawsitive.viewmodel.AnimalTypeViewModelFactory
import cr.una.pawsitive.viewmodel.BreedViewModel
import cr.una.pawsitive.viewmodel.BreedViewModelFactory
import cr.una.pawsitive.viewmodel.PostTypeViewModel
import cr.una.pawsitive.viewmodel.PostTypeViewModelFactory
import cr.una.pawsitive.viewmodel.PostViewModel
import cr.una.pawsitive.viewmodel.PostViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class fragment_new_post : Fragment() {
    // Definition of the binding variable
    private var _binding: FragmentNewPostBinding? = null
    private val binding get() = _binding!!

    // View model
    private lateinit var postViewModel: PostViewModel
    private lateinit var postTypeViewModel: PostTypeViewModel
    private lateinit var animalTypeViewModel: AnimalTypeViewModel
    private lateinit var breedViewModel: BreedViewModel

    private lateinit var postTypes: List<PostType>
    private lateinit var postTypeSelected: PostType

    private lateinit var animalTypes: List<AnimalTypeNewPost>
    private lateinit var animalTypeSelected: AnimalTypeNewPost

    private lateinit var breeds: List<BreedNewPost>
    private lateinit var breedSelected: BreedNewPost

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPostBinding.inflate(inflater, container, false)

        // PostViewModelFactory
        postViewModel =
            ViewModelProvider(this, PostViewModelFactory())[PostViewModel::class.java]
        postTypeViewModel =
            ViewModelProvider(this, PostTypeViewModelFactory())[PostTypeViewModel::class.java]
        animalTypeViewModel =
            ViewModelProvider(this, AnimalTypeViewModelFactory())[AnimalTypeViewModel::class.java]
        breedViewModel =
            ViewModelProvider(this, BreedViewModelFactory())[BreedViewModel::class.java]

        postTypeViewModel.findAllPostTypes()
        animalTypeViewModel.findAllAnimalTypes()
        breedViewModel.findAllBreeds()

        // Observer method to bind postType data
        postTypeViewModel.postTypeList.observe(viewLifecycleOwner) {
            postTypes = it
            // access the spinner
            val adapter: ArrayAdapter<PostType> = ArrayAdapter<PostType>(
                activity?.applicationContext!!,
                android.R.layout.simple_spinner_item,
                postTypes
            )

            binding.spinnerFor.adapter = adapter

            binding.spinnerFor.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?, position: Int, id: Long
                ) {
                    if (parent != null) {
                        if (parent.selectedItem != null) {
                            postTypeSelected = parent.selectedItem as PostType
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // write code to perform some action
                }
            }
        }

        // Observer method to bind animalType data
        animalTypeViewModel.animalTypeList.observe(viewLifecycleOwner) {
            animalTypes = it
            // access the spinner
            val adapter: ArrayAdapter<AnimalTypeNewPost> = ArrayAdapter<AnimalTypeNewPost>(
                activity?.applicationContext!!,
                android.R.layout.simple_spinner_item,
                animalTypes
            )

            binding.spinnerAnimal.adapter = adapter

            binding.spinnerAnimal.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?, position: Int, id: Long
                    ) {
                        if (parent != null) {
                            if (parent.selectedItem != null) {
                                animalTypeSelected = parent.selectedItem as AnimalTypeNewPost
                                breedViewModel.getBreedsByAnimalType(animalTypeSelected.name ?: "")
                            }
                        }

                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        // write code to perform some action
                    }
                }
        }

        // Observer method to bind breed data
        breedViewModel.breedList.observe(viewLifecycleOwner) {
            breeds = it
            // access the spinner
            val adapter: ArrayAdapter<BreedNewPost> = ArrayAdapter<BreedNewPost>(
                activity?.applicationContext!!,
                android.R.layout.simple_spinner_item,
                breeds
            )

            binding.spinnerBreed.adapter = adapter

            binding.spinnerBreed.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?, position: Int, id: Long
                    ) {
                        if (parent != null) {
                            if (parent.selectedItem != null) {
                                breedSelected = parent.selectedItem as BreedNewPost
                            }
                        }

                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        // write code to perform some action
                    }
                }
        }

        binding.textAgeDate.setOnClickListener {
            val datePickerBuilder: MaterialDatePicker.Builder<Long> = MaterialDatePicker
                .Builder
                .datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .setTitleText("Select birth date")
            val datePicker = datePickerBuilder.build()
            datePicker.show(parentFragmentManager, "DATE_PICKER")

            datePicker.addOnPositiveButtonClickListener {
                val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                utc.timeInMillis = it
                //+1: The first month of the year in the Gregorian and Julian calendars is JANUARY which is 0
                val stringData =
                    "${utc.get(Calendar.DAY_OF_MONTH)}/${utc.get(Calendar.MONTH) + 1}/${
                        utc.get(Calendar.YEAR)
                    }"
                binding.textAgeDate.setText(stringData)
            }
        }

        binding.btnPublish.setOnClickListener {
            val formatter = SimpleDateFormat(BuildConfig.DATE_FORMAT)
            val date = formatter.parse(binding.textAgeDate.text.toString())!!
            val pet = PetNewPost(
                name = binding.editTextName.text.toString(),
                address = binding.editTextAddress.text.toString(),
                about = binding.editTextAbout.text.toString(),
                age = date,
                breed = BreedNewPost(
                    id = breedSelected.id,
                    name = breedSelected.name,
                    animalType = AnimalTypeNewPost(
                        id = breedSelected.animalType.id,
                        name = breedSelected.animalType.name
                    )
                )
            )
            val post = NewPost(
                show = true,
                postType = postTypeSelected,
                pet = pet
            )
            postViewModel.createPost(post)
            findNavController().navigate(R.id.homeScreen)
        }

        // Inflate the layout for this fragment
        return binding.root
    }
}