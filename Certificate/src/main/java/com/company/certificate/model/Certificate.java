package com.company.certificate.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = ("certificates"))
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = ("user_id"), insertable = false, updatable = false)
    private User user;
    @Column(name = ("user_id"))
    private Integer userId;

    @ManyToOne
    @JoinColumn(name = ("direction_id"), insertable = false, updatable = false)
    private Direction direction;
    @Column(name = ("direction_id"))
    private Integer directionId;

    @Column(name = ("finished_at"))
    private LocalDate finishedAt;

    private String token;
    private String url;
    private String urlQR;
    private String path;

    private Boolean status;
    @Column(name = ("created_at"))
    private LocalDateTime createdAt;
    @Column(name = ("deleted_at"))
    private LocalDateTime deletedAt;
}
