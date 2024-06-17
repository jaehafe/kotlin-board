package com.kt.kotlinboard.service

import com.kt.kotlinboard.domain.Comment
import com.kt.kotlinboard.domain.Post
import com.kt.kotlinboard.domain.Tag
import com.kt.kotlinboard.exception.PostNotDeletableException
import com.kt.kotlinboard.exception.PostNotFoundException
import com.kt.kotlinboard.exception.PostNotUpdatableException
import com.kt.kotlinboard.repository.CommentRepository
import com.kt.kotlinboard.repository.PostRepository
import com.kt.kotlinboard.repository.TagRepository
import com.kt.kotlinboard.service.dto.post.request.PostCreateRequestDto
import com.kt.kotlinboard.service.dto.post.request.PostSearchRequestDto
import com.kt.kotlinboard.service.dto.post.request.PostUpdateRequestDto
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
class PostServiceTest(
    private val postService: PostService,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val tagRepository: TagRepository,
    private val likeService: LikeService
) : BehaviorSpec({
    beforeSpec {
        postRepository.saveAll(
            listOf(
                Post(title = "title1", content = "content1", createdBy = "harris1", tags = listOf("tag1", "tag2")),
                Post(title = "title2", content = "content2", createdBy = "harris2", tags = listOf("tag1", "tag2")),
                Post(title = "title3", content = "content3", createdBy = "harris3", tags = listOf("tag1", "tag2")),
                Post(title = "title4", content = "content4", createdBy = "harris4", tags = listOf("tag1", "tag2")),
                Post(title = "title5", content = "content5", createdBy = "harris5", tags = listOf("tag1", "tag2")),
                Post(title = "title6", content = "content6", createdBy = "harris6", tags = listOf("tag1", "tag5")),
                Post(title = "title7", content = "content7", createdBy = "harris7", tags = listOf("tag1", "tag5")),
                Post(title = "title8", content = "content8", createdBy = "harris8", tags = listOf("tag1", "tag5")),
                Post(title = "title9", content = "content9", createdBy = "harris9", tags = listOf("tag1", "tag5")),
                Post(title = "title10", content = "content10", createdBy = "harris0", tags = listOf("tag1", "tag5")),
            )
        )
    }
    given("게시글 생성시") {
        When("게시글 생성 인풋이 정상적으로 들어오면") {
            val postId = postService.createPost(
                PostCreateRequestDto(
                    title = "제목",
                    content = "내용",
                    createdBy = "harris"
                )
            )
            then("게시글이 정상적으로 생성됨을 확인한다.") {
                postId shouldBeGreaterThan 0L
                val post = postRepository.findByIdOrNull(postId)
                post shouldNotBe null
                post?.title shouldBe "제목"
                post?.content shouldBe "내용"
                post?.createdBy shouldBe "harris"
            }
            When("태그가 추가되면") {
                val postId2 = postService.createPost(
                    PostCreateRequestDto(
                        title = "제목",
                        content = "내용",
                        createdBy = "harris",
                        tags = listOf("tag1", "tag2")
                    )
                )
                then("태그가 정상적으로 추가됨을 확인한다.") {
                    val tags = tagRepository.findByPostId(postId2)
                    tags.size shouldBe 2
                    tags[0].name shouldBe "tag1"
                    tags[1].name shouldBe "tag2"
                }
            }
        }
    }

    given("게시글 수정시") {
        val saved = postRepository.save(Post(title = "title", content = "content", createdBy = "harris", tags = listOf("tags1", "tags2")))
        When("정상 수정시") {
            val updatedId = postService.updatePost(
                saved.id,
                PostUpdateRequestDto(
                    title = "update title",
                    content = "update content",
                    updatedBy = "harris"
                )
            )
            then("게시글이 정상적으로 수정됨을 확인하다.") {
                saved.id shouldBe updatedId
                val updated = postRepository.findByIdOrNull(updatedId)
                updated shouldNotBe null
                updated?.title shouldBe "update title"
                updated?.content shouldBe "update content"
            }
        }
        When("게시글이 없을 때") {
            then("게시글을 찾을 수 없다라는 예외가 발생한다.") {
                shouldThrow<PostNotFoundException> {
                    postService.updatePost(
                        9999L,
                        PostUpdateRequestDto(
                            title = "update title",
                            content = "update content",
                            updatedBy = "update harris"
                        )
                    )
                }
            }
        }
        When("작성자가 동일하지 않으면") {
            then("수정할 수 없는 게시물 입니다 예외가 발생한다") {
                shouldThrow<PostNotUpdatableException> {
                    postService.updatePost(
                        1L,
                        PostUpdateRequestDto(
                            title = "update title",
                            content = "update content",
                            updatedBy = "update harris"
                        )
                    )
                }
            }
        }
        When("태그가 수정되었을 떄") {
            val updatedId = postService.updatePost(
                saved.id,
                PostUpdateRequestDto(
                    title = "update title",
                    content = "update content",
                    updatedBy = "harris",
                    tags = listOf("tags1", "tags2", "tags3")
                )
            )
            then("정상적으로 수정됨을 확인한다.") {
                val tags = tagRepository.findByPostId(updatedId)
                tags.size shouldBe 3
                tags[2].name shouldBe "tags3"
            }
            then("태그 순서가 변경됐을 떄 정상적으로 변경됨을 확인한다.") {
                postService.updatePost(
                    saved.id,
                    PostUpdateRequestDto(
                        title = "update title",
                        content = "update content",
                        updatedBy = "harris",
                        tags = listOf("tags3", "tags2", "tags1")
                    )
                )

                val tags = tagRepository.findByPostId(updatedId)
                tags.size shouldBe 3
                tags[2].name shouldBe "tags1"
            }
        }
    }

    given("게시글 삭제시") {
        val saved = postRepository.save(Post(title = "title", content = "content", createdBy = "harris"))
        When("정상 삭제시") {
            val postId = postService.deletePost(saved.id, "harris")
            then("게시글이 정상저긍로 삭제됨을 확인한다.") {
                postId shouldBe saved.id
                postRepository.findByIdOrNull(postId) shouldBe null
            }
        }
        When("작성자가 동일하지 않으면") {
            val saved2 = postRepository.save(Post(title = "title", content = "content", createdBy = "harris"))
            then("삭제할 수 없는 게시물 입니다 예외가 발생한다") {
                shouldThrow<PostNotDeletableException> {
                    postService.deletePost(saved2.id, "harris2")
                }
            }
        }
    }

    given("게시글 상세조회시") {
        val saved = postRepository.save(Post(title = "title", content = "content", createdBy = "harris"))
        tagRepository.saveAll(
            listOf(
                Tag(name = "tags1", post = saved, createdBy = "adam"),
                Tag(name = "tags2", post = saved, createdBy = "adam"),
                Tag(name = "tags3", post = saved, createdBy = "adam"),
            )
        )
        likeService.createLike(saved.id, "harris")
        likeService.createLike(saved.id, "harris1")
        likeService.createLike(saved.id, "harris2")
        When("정상 조회시") {
            val post = postService.getPost(saved.id)
            then("게시글의 내용이 정상적으로 반환됨을 확인한다") {
                post.id shouldBe saved.id
                post.title shouldBe saved.title
                post.content shouldBe saved.content
                post.createdBy shouldBe saved.createdBy
            }
        }
        When("게시글이 없을 때") {
            then("게시글을 찾을 수 없다라는 예외가 발생한다") {
                shouldThrow<PostNotFoundException> {
                    postService.getPost(9999L)
                }
            }
        }
        When("댓글 추가시") {
            commentRepository.save(Comment(content = "comment1", post = saved, createdBy = "댓글 작성자"))
            commentRepository.save(Comment(content = "comment2", post = saved, createdBy = "댓글 작성자"))
            commentRepository.save(Comment(content = "comment3", post = saved, createdBy = "댓글 작성자"))
            val post = postService.getPost(saved.id)
            then("댓글이 함께 조회됨을 확인한다") {
                post.comments.size shouldBe 3
                post.comments[0].content shouldBe "comment1"
                post.comments[1].content shouldBe "comment2"
                post.comments[2].content shouldBe "comment3"
            }
            then("태그가 정상적으로 조회됨을 확인한다.") {
                post.tags.size shouldBe 3
                post.tags[0] shouldBe "tags1"
                post.tags[1] shouldBe "tags2"
                post.tags[2] shouldBe "tags3"
            }
            then("좋아요 개수가 조회됨을 확인한다") {
                post.likeCount shouldBe 3
            }
        }
    }

    given("게시글 목록 조회시") {
        When("정상 조회시") {
            val postPage = postService.findPageBy(PageRequest.of(0, 5), PostSearchRequestDto(title = "title"))
            then("게시글 페이지가 반환된다.") {
                postPage.number shouldBe 0
                postPage.size shouldBe 5
                postPage.content.size shouldBe 5
                postPage.content[0].title shouldContain "title"
                postPage.content[0].createdBy shouldContain "harris"
            }
        }
        When("타이틀로 검색") {
            val postPage = postService.findPageBy(PageRequest.of(0, 5), PostSearchRequestDto(title = "title1"))
            then("타이틀에 해당하는 게시글이 반환된다.") {
                postPage.number shouldBe 0
                postPage.size shouldBe 5
                postPage.content.size shouldBe 5
                postPage.content[0].title shouldContain "title1"
                postPage.content[0].createdBy shouldContain "harris1"
            }
        }
        When("작성자로 검색") {
            val postPage = postService.findPageBy(PageRequest.of(0, 5), PostSearchRequestDto(title = "title1"))
            then("작성자에 해당하는 게시글이 반환된다.") {
                postPage.number shouldBe 0
                postPage.size shouldBe 5
                postPage.content.size shouldBe 5
                postPage.content[0].title shouldContain "title1"
                postPage.content[0].createdBy shouldBe "harris1"
            }
            then("첫번째 태그가 함께 조회됨을 확인한다.") {
                postPage.content.forEach {
                    it.firstTag shouldBe "tag1"
                }
            }
        }
        When("태그로 검색") {
            val postPage = postService.findPageBy(PageRequest.of(0, 5), PostSearchRequestDto(tag = "tag5"))
            then("태그에 해당하는 게시글이 반환된다.") {
                postPage.number shouldBe 0
                postPage.size shouldBe 5
                postPage.content.size shouldBe 5
                postPage.content[0].title shouldBe "title10"
                postPage.content[1].title shouldBe "title9"
                postPage.content[2].title shouldBe "title8"
                postPage.content[3].title shouldBe "title7"
                postPage.content[4].title shouldBe "title6"
            }
        }
        When("좋아요가 2개 추가됐을 떄") {
            val postPage = postService.findPageBy(PageRequest.of(0, 5), PostSearchRequestDto(tag = "tag5"))
            postPage.content.forEach {
                likeService.createLike(it.id, "harris1")
                likeService.createLike(it.id, "harris2")
            }
            val likedPostPage = postService.findPageBy(PageRequest.of(0, 5), PostSearchRequestDto(tag = "tag5"))
            then("좋아요 개수가 정상적으로 조회됨을 확인한다.") {
                likedPostPage.content.forEach {
                    it.likeCount shouldBe 2
                }
            }
        }
    }
})
