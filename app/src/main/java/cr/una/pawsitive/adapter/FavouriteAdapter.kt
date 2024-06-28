package cr.una.pawsitive.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import cr.una.pawsitive.R
import cr.una.pawsitive.databinding.FavouriteItemBinding
import cr.una.pawsitive.model.AiResponse
import cr.una.pawsitive.model.AnimalTypeAiRequest
import cr.una.pawsitive.model.BreedAiRequest
import cr.una.pawsitive.model.PetAiRequest
import cr.una.pawsitive.model.PostModel
import cr.una.pawsitive.viewmodel.PostItemViewModel
import cr.una.pawsitive.viewmodel.PostViewModel
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.util.Date

class FavouriteAdapter(
    private val postViewModel: PostViewModel,
    private val favouriteRepository: cr.una.pawsitive.repository.FavouriteRepository,
    private val viewLifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<FavouriteViewHolder>(){
    // Set of posts that are being observed
    private val observedPosts = mutableSetOf<Int>()
    var favourites = mutableListOf<PostModel>()
    // PostItemViewModel for coroutines
    var postViewModels = mutableListOf<PostItemViewModel>()

    fun setFavouritesList(favourites: List<PostModel>){
        this.favourites.clear()
        this.favourites.addAll(favourites.toMutableList())
        // Create a PostItemViewModel for each post
        postViewModels.clear()
        favourites.forEach { post ->
            postViewModels.add(PostItemViewModel(postViewModel.postRepository, favouriteRepository))
        }
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FavouriteItemBinding.inflate(inflater, parent, false)

        return FavouriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        try {
            val favourite = favourites[position]
            holder.binding.petNameLabel.text = favourite.pet.name
            holder.binding.aboutLabel.text = favourite.pet.about
            holder.binding.animalLabel.text = favourite.pet.breed.animalType.name
            holder.binding.addresLabel.text = favourite.pet.address
            holder.binding.breedLabel.text = favourite.pet.breed.name
            holder.binding.ageLabel.text = cr.una.pawsitive.utils.calculateAge(favourite.pet.age)
            holder.binding.forLabel.text = favourite.postType.name
            holder.binding.CostsTexArea.text = favourite.aiResponse?.costs
            holder.binding.CaresTexArea.text = favourite.aiResponse?.cares
            holder.binding.DetailsTexArea.text = favourite.aiResponse?.details

            //contact button
            val textView = holder.binding.contactButton
            textView.text = favourite.user.email
            //listener for the button
            textView.setOnClickListener(){
                val bundle = bundleOf(USER_EMAIL to favourite.user.email)

                holder.itemView.findNavController().navigate(
                    R.id.action_favouritesScreen_to_other_user_profile, bundle
                )
            }

            // listenner para el favoriteButton
            val favoriteButton = holder.binding.favoriteButton
            favoriteButton.setOnClickListener(){
                Log.i("FavouritesAdapter", "Favorite Button clicked")
                //delete the post from favourites
                postViewModels[position].deleteFavouritePost(favourite.id)
                // remove the post from the list
                favourites.removeAt(position)
                // notify the adapter about the removed item
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, favourites.size)
            }

            // Ai button Listenner
            val aiButton = holder.binding.AiButton
            aiButton.setOnClickListener(){
                Log.i("PostAdapter", "Ai Button clicked")
                //get pet from the post
                val pet = favourites[position].pet
                Log.i("PostAdapter", "Pet: $pet")
                favourites[position].isCardVisible = !favourites[position].isCardVisible
                notifyItemChanged(position)

                // Create PetAiRequest object
                val petAiRequest = PetAiRequest(pet.name, pet.address, pet.about, pet.age, AnimalTypeAiRequest(pet.breed.animalType.name,
                    BreedAiRequest(pet.breed.name)
                )
                )

//            val petAiRequest = PetAiRequest("Buddy", "123 Main St", "Friendly and playful",
//                "2022-02-15T00:00:00.000Z",
//                AnimalTypeAiRequest("Dog", BreedAiRequest("Golden Retriever")))


                // Call getAiInformation in PostViewModel
                // get ai information only if the card is visible and not already loaded
                var post = favourites[position]
                if(post.isCardVisible && post.aiResponse == null) {
                    // show the 3 progress bars
                    holder.binding.progressBarCares.visibility = View.VISIBLE
                    holder.binding.progressBarCosts.visibility = View.VISIBLE
                    holder.binding.progressBarDetails.visibility = View.VISIBLE
                    // call getAiInformation in PostItemViewModel
                    postViewModels[position].getAiInformation(petAiRequest)
                }else{
                    // hide the 3 progress bars
                    holder.binding.progressBarCares.visibility = View.GONE
                    holder.binding.progressBarCosts.visibility = View.GONE
                    holder.binding.progressBarDetails.visibility = View.GONE
                }

                // Remove all observers from aiResponse
                postViewModels[position].aiResponse.removeObservers(viewLifecycleOwner)

                if (position !in observedPosts) {
                    // Create a new observer
                    val observer = Observer<AiResponse> { aiResponse ->
                        // set the aiResponse to the post
                        favourites[position].aiResponse = aiResponse
                        // Update the UI with the AiResponse
                        holder.binding.CostsTexArea.text = favourites[position].aiResponse?.costs
                        holder.binding.CaresTexArea.text = favourites[position].aiResponse?.cares
                        holder.binding.DetailsTexArea.text = favourites[position].aiResponse?.details

                        // hide the 3 progress bars
                        holder.binding.progressBarCares.visibility = View.GONE
                        holder.binding.progressBarCosts.visibility = View.GONE
                        holder.binding.progressBarDetails.visibility = View.GONE

                        notifyItemChanged(position)
                    }

                    // Observe aiResponse in PostItemViewModel
                    postViewModels[position].aiResponse.observe(viewLifecycleOwner, observer)

                    // post al conjunto de posts observados
                    observedPosts.add(position)
                }

            }
            // Establecer la visibilidad del CardView según el valor de isCardVisible
            holder.binding.IaCardView.visibility = if (favourites[position].isCardVisible) View.VISIBLE else View.GONE

            // si airesponse es diferente de null entonces que muestre los datos y quite los progress bars
            if(favourites[position].aiResponse != null){
                holder.binding.CostsTexArea.text = favourites[position].aiResponse?.costs
                holder.binding.CaresTexArea.text = favourites[position].aiResponse?.cares
                holder.binding.DetailsTexArea.text = favourites[position].aiResponse?.details
                holder.binding.progressBarCares.visibility = View.GONE
                holder.binding.progressBarCosts.visibility = View.GONE
                holder.binding.progressBarDetails.visibility = View.GONE
            }else{
                holder.binding.progressBarCares.visibility = View.VISIBLE
                holder.binding.progressBarCosts.visibility = View.VISIBLE
                holder.binding.progressBarDetails.visibility = View.VISIBLE
            }


        } catch (e: Exception) {
            Log.i("FavouriteAdapter", "Error in onBindViewHolder: ${e.message}")
        }
    }

    override fun getItemCount(): Int {
        return favourites.size
    }

    companion object {
        const val POST_ID = "post_id"
        const val USER_EMAIL = "user_email"
    }

    fun calculateAge(birthDate: Date): String {
        val birthDateLocal = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val meses = Period.between(birthDateLocal, LocalDate.now()).toTotalMonths()

        //si meses es igual a 0 entonces que calcule los dias y si es mayor a 12 que calcule los años
        return if (meses == 0L) {
            "${Period.between(birthDateLocal, LocalDate.now()).days} días"
        } else if (meses > 12) {
            "${Period.between(birthDateLocal, LocalDate.now()).years} años"
        } else {
            "$meses meses"
        }
    }
}

class FavouriteViewHolder (
    val binding: FavouriteItemBinding
) : RecyclerView.ViewHolder(binding.root)