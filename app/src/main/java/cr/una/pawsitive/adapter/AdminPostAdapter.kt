package cr.una.pawsitive.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cr.una.pawsitive.databinding.PostAdminItemBinding
import cr.una.pawsitive.model.Post
import cr.una.pawsitive.model.PostModel

class AdminPostAdapter: RecyclerView.Adapter<PostAdminViewHolder>(){

    var posts = mutableListOf<PostModel>()

    fun setPostList(postResponseList: List<PostModel>){
        Log.i("PostAdminAdapter", "setPostList")
        this.posts.clear()
        this.posts.addAll(postResponseList.toMutableList())
        this.notifyDataSetChanged()
        //imprimir la lista de post con un forEach
        //posts.forEach { post -> Log.i("PostAdapter", post.toString()) }
        //Log.i("PostAdapter", posts.size.toString())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdminViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostAdminItemBinding.inflate(inflater, parent, false)

        return PostAdminViewHolder(binding)
    }


    override fun onBindViewHolder(holder: PostAdminViewHolder, position: Int) {
        val post = posts[position]
        holder.binding.petNameLabel.text = post.pet?.name
        holder.binding.aboutLabel.text = post.pet?.about
        holder.binding.animalLabel.text = post.pet?.breed?.animalType?.name
        holder.binding.addresLabel.text = post.pet?.address
        holder.binding.breedLabel.text = post.pet?.breed?.name
        holder.binding.ageLabel.text = post.pet?.age.toString()
//        val textView = holder.binding.contactButton
//        //listener for the button
//        textView.setOnClickListener(){
//            Log.i("PostAdapter", "Button clicked")
//
//            try {
//                onContactButtonClick()
//            } catch (e: Exception) {
//                Log.e("PostAdapter", "Error: $e")
//            }
//
//        }
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    companion object {
        const val POST_ID = "post_id"
    }

}

class PostAdminViewHolder (
    val binding: PostAdminItemBinding
) : RecyclerView.ViewHolder(binding.root)
