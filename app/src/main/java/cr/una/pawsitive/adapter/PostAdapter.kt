package cr.una.pawsitive.adapter

import android.graphics.drawable.Drawable
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
import cr.una.pawsitive.databinding.PostItemBinding
import cr.una.pawsitive.model.AiResponse
import cr.una.pawsitive.model.AnimalTypeAiRequest
import cr.una.pawsitive.model.BreedAiRequest
import cr.una.pawsitive.model.PetAiRequest
import cr.una.pawsitive.model.PetPost
import cr.una.pawsitive.model.PostModel
import cr.una.pawsitive.model.PostModelOutputFavourite
import cr.una.pawsitive.repository.FavouriteRepository
import cr.una.pawsitive.utils.calculateAge
import cr.una.pawsitive.viewmodel.PostItemViewModel
import cr.una.pawsitive.viewmodel.PostViewModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.ZoneId
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale
import java.util.TimeZone

class PostAdapter(
    private val postViewModel: PostViewModel,
    private val favouriteRepository: FavouriteRepository,
    private val viewLifecycleOwner: LifecycleOwner
): RecyclerView.Adapter<PostViewHolder>(){

    // registro de los posts para los que ya se ha establecido un observer
    private val observedPosts = mutableSetOf<Int>()
    var posts = mutableListOf<PostModel>()
    var postViewModels = mutableListOf<PostItemViewModel>()

    fun setPostList(postResponseList: List<PostModel>){
        Log.i("PostAdapter", "setPostList")
        this.posts.clear()
        this.posts.addAll(postResponseList.toMutableList())
        // Create a PostItemViewModel for each post
        postViewModels.clear()
        postResponseList.forEach { post ->
            postViewModels.add(PostItemViewModel(postViewModel.postRepository, favouriteRepository))
        }
        this.notifyDataSetChanged()
        //imprimir la lista de post con un forEach
        //posts.forEach { post -> Log.i("PostAdapter", post.toString()) }
        //Log.i("PostAdapter", posts.size.toString())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostItemBinding.inflate(inflater, parent, false)

        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        try {

        Log.i("PostAdapter", "onBindViewHolder")
        val post = posts[position]
        holder.binding.petNameLabel.text = post.pet.name

        holder.binding.aboutLabel.text = post.pet.about

        holder.binding.animalLabel.text = post.pet.breed.animalType.name

        holder.binding.addresLabel.text = post.pet.address

        holder.binding.breedLabel.text = post.pet.breed.name

        holder.binding.ageLabel.text = calculateAge(post.pet.age)

        holder.binding.forLabel.text = post.postType.name

        holder.binding.CostsTexArea.text = post.aiResponse?.costs

        holder.binding.CaresTexArea.text = post.aiResponse?.cares

        holder.binding.DetailsTexArea.text = post.aiResponse?.details


        val textView = holder.binding.contactButton
        textView.text = post.user.email

        // set the heart icon if the post is liked
        if(post.isLiked!!){
            holder.binding.favoriteButton.setImageResource(R.mipmap.ic_heart_favourite_foreground)
        } else {
            holder.binding.favoriteButton.setImageResource(R.mipmap.ic_favorite_foreground)
        }

        //listener for the button
        textView.setOnClickListener(){
            Log.i("PostAdapter", "Button clicked")

            val bundle = bundleOf(USER_EMAIL to post.user.email)

            holder.itemView.findNavController().navigate(
                R.id.action_homeScreen_to_other_user_profile, bundle
            )
        }

        } catch (e: Exception) {
            Log.i("PostAdapter", "Error en onBindViewHolder: ${e.message}")
        }

        // Ai button Listenner
        val aiButton = holder.binding.AiButton
        aiButton.setOnClickListener(){
            Log.i("PostAdapter", "Ai Button clicked")
            //get pet from the post
            val pet = posts[position].pet
            Log.i("PostAdapter", "Pet: $pet")
            posts[position].isCardVisible = !posts[position].isCardVisible
            notifyItemChanged(position)

            // Create PetAiRequest object
            val petAiRequest = PetAiRequest(pet.name, pet.address, pet.about, pet.age, AnimalTypeAiRequest(pet.breed.animalType.name,
                BreedAiRequest(pet.breed.name)))

//            val petAiRequest = PetAiRequest("Buddy", "123 Main St", "Friendly and playful",
//                "2022-02-15T00:00:00.000Z",
//                AnimalTypeAiRequest("Dog", BreedAiRequest("Golden Retriever")))


            // Call getAiInformation in PostViewModel
            // get ai information only if the card is visible and not already loaded
            var post = posts[position]
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
                    posts[position].aiResponse = aiResponse
                    // Update the UI with the AiResponse
                    holder.binding.CostsTexArea.text = posts[position].aiResponse?.costs
                    holder.binding.CaresTexArea.text = posts[position].aiResponse?.cares
                    holder.binding.DetailsTexArea.text = posts[position].aiResponse?.details

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
        holder.binding.IaCardView.visibility = if (posts[position].isCardVisible) View.VISIBLE else View.GONE

        // si airesponse es diferente de null entonces que muestre los datos y quite los progress bars
        if(posts[position].aiResponse != null){
            holder.binding.CostsTexArea.text = posts[position].aiResponse?.costs
            holder.binding.CaresTexArea.text = posts[position].aiResponse?.cares
            holder.binding.DetailsTexArea.text = posts[position].aiResponse?.details
            holder.binding.progressBarCares.visibility = View.GONE
            holder.binding.progressBarCosts.visibility = View.GONE
            holder.binding.progressBarDetails.visibility = View.GONE
        }else{
            holder.binding.progressBarCares.visibility = View.VISIBLE
            holder.binding.progressBarCosts.visibility = View.VISIBLE
            holder.binding.progressBarDetails.visibility = View.VISIBLE
        }

        // listenner para el favoriteButton
        val favoriteButton = holder.binding.favoriteButton
        favoriteButton.setOnClickListener(){
            Log.i("PostAdapter", "Favorite Button clicked")
            //instance of the actual post
            var post = posts[position]
            posts[position].isLiked = !posts[position].isLiked!!
            if(posts[position].isLiked!!){
                favoriteButton.setImageResource(R.mipmap.ic_heart_favourite_foreground)
                // Create PostModelOutputFavourite from PostModel
                // Call addFavouritePost in PostItemViewModel
                postViewModels[position].addFavouritePost(post.id)
            } else {
                // Call deleteFavouritePost in PostItemViewModel
                postViewModels[position].deleteFavouritePost(post.id)
                favoriteButton.setImageResource(R.mipmap.ic_favorite_foreground)
            }
            //force the button to redraw
            favoriteButton.invalidate()
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    companion object {
        const val POST_ID = "post_id"
        const val USER_EMAIL = "user_email"
    }

    fun convertStringToDate(inputString: String): Date {
        // Crear un SimpleDateFormat para parsear la cadena de texto a un objeto Date
        val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US)
        val date = inputFormat.parse(inputString)

        // Crear un SimpleDateFormat para formatear el objeto Date a una cadena de texto en formato ISO 8601
        val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US)
        outputFormat.timeZone = TimeZone.getTimeZone("UTC")

        val iso8601String = outputFormat.format(date)

        // Convertir la cadena de texto en formato ISO 8601 a un objeto Date
        return outputFormat.parse(iso8601String)
    }

    fun convertStringToGregorianDate(inputString: String): Date {
        // Crear un SimpleDateFormat para parsear la cadena de texto a un objeto Date
        val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US)
        val date = inputFormat.parse(inputString)

        // Crear un Calendar para convertir el objeto Date a GregorianCalendar
        val calendar = GregorianCalendar()
        calendar.time = date

        return calendar.time
    }
}

class PostViewHolder (
    val binding: PostItemBinding
) : RecyclerView.ViewHolder(binding.root)
