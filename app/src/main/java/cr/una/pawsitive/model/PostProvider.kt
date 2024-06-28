package cr.una.pawsitive.model

import java.util.*

class PostProvider {
    companion object {
        fun findPostById(id: Long): Post {
            return postList.find { it.postId == id } ?: throw IllegalArgumentException("Post not found")
        }

        fun findAllPosts(): List<Post> {
            return postList
        }

        private val postList = listOf(
            Post(
                postId = 1,
                show = true,
                postType = 1,
                imageId = "image_1",
                user = User(
                    userId = 1,
                    createDate = Date(),
                    email = "example1@example.com",
                    enabled = true,
                    firstName = "John",
                    lastName = "Doe",
                    password = "password123"
                ),
                pet = Pet(
                    petId = 1,
                    name = "Buddy",
                    address = "123 Main St",
                    about = "Friendly dog",
                    age = 3,
                    animalType = AnimalType(
                        animalTypeId = 1,
                        name = "Dog",
                        breed = Breed(
                            breedId = 1,
                            name = "Golden Retriever"
                        )
                    )
                )
            ),
            Post(
                postId = 2,
                show = true,
                postType = 2,
                imageId = "image_2",
                user = User(
                    userId = 2,
                    createDate = Date(),
                    email = "example2@example.com",
                    enabled = true,
                    firstName = "Jane",
                    lastName = "Smith",
                    password = "password456"
                ),
                pet = Pet(
                    petId = 2,
                    name = "Whiskers",
                    address = "456 Oak St",
                    about = "Playful cat",
                    age = 2,
                    animalType = AnimalType(
                        animalTypeId = 2,
                        name = "Cat",
                        breed = Breed(
                            breedId = 2,
                            name = "Siamese"
                        )
                    )
                )
            ),
            Post(
                postId = 5,
                show = true,
                postType = 1,
                imageId = "image_5",
                user = User(
                    userId = 5,
                    createDate = Date(),
                    email = "example5@example.com",
                    enabled = true,
                    firstName = "Emily",
                    lastName = "Brown",
                    password = "passworddef"
                ),
                pet = Pet(
                    petId = 5,
                    name = "Rocky",
                    address = "789 Pine St",
                    about = "Playful dog",
                    age = 2,
                    animalType = AnimalType(
                        animalTypeId = 1,
                        name = "Dog",
                        breed = Breed(
                            breedId = 5,
                            name = "German Shepherd"
                        )
                    )
                )
            )
        )
    }
}
