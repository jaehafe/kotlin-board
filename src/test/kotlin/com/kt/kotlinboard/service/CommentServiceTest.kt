package com.kt.kotlinboard.service

import com.kt.kotlinboard.domain.Comment
import com.kt.kotlinboard.domain.Post
import com.kt.kotlinboard.exception.CommentNotDeletableException
import com.kt.kotlinboard.exception.CommentNotUpdatableException
import com.kt.kotlinboard.exception.PostNotFoundException
import com.kt.kotlinboard.repository.CommentRepository
import com.kt.kotlinboard.repository.PostRepository
import com.kt.kotlinboard.service.dto.comment.request.CommentCreateRequestDto
import com.kt.kotlinboard.service.dto.comment.request.CommentUpdateRequestDto
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
class CommentServiceTest(
    private val commentService: CommentService,
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
) : BehaviorSpec({
    given("댓글 생성시") {
        val post = postRepository.save(
            Post(
                title = "게시글 제목",
                content = "게시글 내용",
                createdBy = "게시글 작성자",
            )
        )
        When("인풋이 정상으로 들어오면") {
            val commentId = commentService.createComment(
                post.id, CommentCreateRequestDto(
                    content = "댓글 내용",
                    createdBy = "댓글 작성자",
                )
            )
            then("정상 생성됨을 확인한다.") {
                commentId shouldBeGreaterThan 0L
                val comment = commentRepository.findByIdOrNull(commentId)
                comment shouldNotBe null
                comment?.content shouldBe "댓글 내용"
                comment?.createdBy shouldBe "댓글 작성자"
            }
        }
        When("게시글이 존재하지 않으면") {
            then("게시글 존재하지 않음 예외가 발생한다") {
                shouldThrow<PostNotFoundException> {
                    commentService.createComment(
                        9999L, CommentCreateRequestDto(
                            content = "댓글 내용",
                            createdBy = "댓글 작성자",
                        )
                    )
                }
            }
        }
    }

    given("댓글 수정시") {
        val post = postRepository.save(
            Post(title = "게시글 제목", content = "게시글 내용", createdBy = "게시글 작성자")
        )
        val saved = commentRepository.save(
            Comment("댓글 내용", post, "댓글 생성자")
        )
        When("인풋이 정상적으로 들어오면") {
            val updatedId = commentService.updateComment(saved.id, CommentUpdateRequestDto(
                    content = "수정된 댓글 내용",
                    updatedBy = "댓글 생성자",
                )
            )
            then("정상 수정됨을 확인한다.") {
                updatedId shouldBe saved.id
                val updated = commentRepository.findByIdOrNull(updatedId)
                updated shouldNotBe null
                updated?.content shouldBe "수정된 댓글 내용"
                updated?.updatedBy shouldBe "댓글 생성자"
            }
        }
        When("작성자와 수정자가 다르면") {
            then("수정할 수 없는 게시 예외가 발생한다.") {
                shouldThrow<CommentNotUpdatableException> {
                    commentService.updateComment(saved.id, CommentUpdateRequestDto(
                            content = "수정된 댓글 내용",
                            updatedBy = "수정된 댓글 작성자",
                        )
                    )
                }
            }
        }
    }

    given("댓글 삭제시") {
        val post = postRepository.save(
            Post(title = "게시글 제목", content = "게시글 내용", createdBy = "게시글 작성자")
        )
        val saved = commentRepository.save(
            Comment("댓글 내용", post, "댓글 생성자")
        )
        val saved2 = commentRepository.save(
            Comment("댓글 내용2", post, "댓글 생성자2")
        )
        When("id, 작성자를 입력하면") {
            then("댓글 id가 반환된다.") {
                val deletedId = commentService.deleteComment(saved.id, "댓글 생성자")
                deletedId shouldBe saved.id
                commentRepository.findByIdOrNull(deletedId) shouldBe null
            }
        }
        When("작성자가 동일하지 않으면") {
            then("댓글 삭제 불가 예외가 발생한다.") {
                shouldThrow<CommentNotDeletableException> {
                    commentService.deleteComment(saved2.id, "다른 작성자")
                }
            }
        }
    }
})
