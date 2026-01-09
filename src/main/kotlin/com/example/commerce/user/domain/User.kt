package com.example.commerce.user.domain

import com.example.commerce.common.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "users")
class User ( // internal = 같은 모듈 내에서 접근 가능
    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "email", nullable = false)
    val email: String,

    @Column(name = "password", nullable = false)
    val password: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    val role: Role,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long? = null,
): BaseEntity()