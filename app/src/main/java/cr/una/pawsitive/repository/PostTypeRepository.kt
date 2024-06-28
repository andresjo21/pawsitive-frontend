package cr.una.pawsitive.repository

import cr.una.pawsitive.service.PostTypeService

class PostTypeRepository constructor(
    private val postTypeService: PostTypeService
) {
    suspend fun getAllPostTypes() = postTypeService.getAllPostTypes()
}