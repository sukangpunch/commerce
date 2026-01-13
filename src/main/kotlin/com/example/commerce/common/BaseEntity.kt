package com.example.commerce.common

import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.ZoneOffset.UTC
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit.MILLIS

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
@DynamicInsert
@DynamicUpdate
abstract class BaseEntity {

    var createdAt: ZonedDateTime? = null
        protected set

    var updatedAt: ZonedDateTime? = null
        protected set

    @PrePersist
    fun onPrePersist() {
        val now = ZonedDateTime.now(UTC).truncatedTo(MILLIS)
        this.updatedAt = now
        this.createdAt = now
    }

    @PreUpdate
    fun onPreUpdate() {
        this.updatedAt = ZonedDateTime.now(UTC).truncatedTo(MILLIS)
    }
}
