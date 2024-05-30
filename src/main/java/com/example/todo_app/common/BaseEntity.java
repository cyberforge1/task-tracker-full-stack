package com.example.todo_app.common;

import jakarta.persistence.*;
import java.util.Date;

@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date updatedAt;

    public BaseEntity() {
    }

    public Long getId() {
        return id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    @PrePersist
    public void onCreate() {
        Date timestamp = new Date();
        createdAt = timestamp;
        updatedAt = timestamp;
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = new Date();
    }
}
