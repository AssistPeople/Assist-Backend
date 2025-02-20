package com.guesthouse.assistpeople.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(unique = true,nullable = false)
    private String title;

    private String description,location,recruitment,
            latitude,longitude,imageUrl,concept;


    @OneToMany(mappedBy = "tag",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<TagEntity> tagEntities;

    // 채팅방 id
    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity user ;

}
