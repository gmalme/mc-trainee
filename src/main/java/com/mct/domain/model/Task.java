package com.mct.domain.model;

import com.mct.domain.enums.TaskStatus;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.UUID;

/**
 * Entidade que representa uma tarefa associada a um usuário.
 */
@Entity
@Table(name = "tb_task")
public class Task extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @Column(nullable = false, length = 120)
    public String title;

    @Column(columnDefinition = "TEXT")
    public String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public TaskStatus status = TaskStatus.OPEN;

    @Column(name = "due_date")
    public Instant dueDate;

    @Column(name = "created_at", nullable = false)
    public Instant createdAt = Instant.now();

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    public User user;
}
