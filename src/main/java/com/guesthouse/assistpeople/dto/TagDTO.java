package com.guesthouse.assistpeople.dto;

import lombok.*;

import java.io.PipedReader;
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Setter
public class TagDTO {
    private Long tagId;
    private String tag;
}
