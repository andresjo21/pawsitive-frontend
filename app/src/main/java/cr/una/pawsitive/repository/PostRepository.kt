package cr.una.pawsitive.repository

import cr.una.pawsitive.model.AiResponse
import cr.una.pawsitive.model.NewPost
import cr.una.pawsitive.model.PetAiRequest
import cr.una.pawsitive.service.PostService
import retrofit2.Response

class PostRepository constructor(
    private val postService: PostService
){
    suspend fun getAllPosts() = postService.getAllPosts()

    // getAiInformation

    suspend fun getAiInformation(petAiRequest: PetAiRequest):Response<AiResponse> {
        return postService.getAiInformation(petAiRequest)
    }
	
    suspend fun createPost(newPost: NewPost) = postService.createPost(newPost)
}