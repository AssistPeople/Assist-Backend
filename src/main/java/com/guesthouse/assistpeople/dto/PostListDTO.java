package com.guesthouse.assistpeople.dto;

import com.guesthouse.assistpeople.entity.TagEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Primary;

import java.util.List;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Setter
public class PostListDTO {
    private Long postId;
    private String title;
    private String description;
    private String  location;
    private String recruitment;
    private String latitude;
    private String longitude;
    private String imageUrl;
    private String concept;
    private List<TagDTO> tagEntities;
}
