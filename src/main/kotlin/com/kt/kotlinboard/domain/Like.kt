package com.kt.kotlinboard.domain

import jakarta.persistence.*

@Entity
@Table(name = "likes")
class Like(
    post: Post,
    createdBy: String,
): BaseEntity(createdBy) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var post: Post = post
        protected set
}
