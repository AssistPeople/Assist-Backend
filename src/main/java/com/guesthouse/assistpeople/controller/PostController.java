package com.guesthouse.assistpeople.controller;

import com.guesthouse.assistpeople.dto.PostListDTO;
import com.guesthouse.assistpeople.dto.TagDTO;
import com.guesthouse.assistpeople.dto.UserDTO;
import com.guesthouse.assistpeople.entity.PostEntity;
import com.guesthouse.assistpeople.entity.TagEntity;
import com.guesthouse.assistpeople.exeption.ApiResponse;
import com.guesthouse.assistpeople.repository.PostRepository;
import com.guesthouse.assistpeople.service.PostService;
import com.guesthouse.assistpeople.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
@Slf4j
public class PostController {
    private final PostService postService;
    private final TagService tagService;

    @GetMapping("/getPost")
    public ResponseEntity<?> signUp(String concept) {
        try {
           List<PostListDTO> postListDTOS = new ArrayList<>();
            List<PostEntity> posts =  postService.getPost(concept);

           for(int i=0; i<posts.size(); i++){
               List<TagEntity> tag = tagService.getTag(posts.get(i).getPostId());
               List<TagDTO> tagDTOS = new ArrayList<>();
               for(int j=0; j<tag.size(); j++){
                    TagDTO tagDTO =TagDTO.builder()
                            .tagId(tag.get(j).getTagId())
                            .tag(tag.get(j).getTag())
                                    .build();

                    tagDTOS.add(tagDTO);
               }

               PostListDTO postListDTO = PostListDTO.builder()
                       .postId(posts.get(i).getPostId())
                       .concept(posts.get(i).getConcept())
                       .title(posts.get(i).getTitle())
                       .concept(posts.get(i).getConcept())
                       .description(posts.get(i).getDescription())
                       .imageUrl(posts.get(i).getImageUrl())
                       .latitude(posts.get(i).getLatitude())
                       .longitude(posts.get(i).getLongitude())
                       .location(posts.get(i).getLocation())
                       .recruitment(posts.get(i).getRecruitment())
                       .tagEntities(tagDTOS)
                       .build();

               postListDTOS.add(postListDTO);
           }

            return ResponseEntity.ok(new ApiResponse(true, "가져오기 성공", postListDTOS));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, ex.getMessage(), null));
        }
    }
    @GetMapping("/getOnePost")
    public ResponseEntity<?> getOnePost(Long postId) {
        try {
            PostListDTO postListDTOS = new PostListDTO();
            PostEntity posts =  postService.getOnePost(postId);

            List<TagEntity> tag = tagService.getTag(posts.getPostId());
            List<TagDTO> tagDTOS = new ArrayList<>();
            for(int j=0; j<tag.size(); j++){
                TagDTO tagDTO =TagDTO.builder()
                        .tagId(tag.get(j).getTagId())
                        .tag(tag.get(j).getTag())
                        .build();

                tagDTOS.add(tagDTO);
            }

                PostListDTO postListDTO = PostListDTO.builder()
                        .postId(posts.getPostId())
                        .concept(posts.getConcept())
                        .title(posts.getTitle())
                        .concept(posts.getConcept())
                        .description(posts.getDescription())
                        .imageUrl(posts.getImageUrl())
                        .latitude(posts.getLatitude())
                        .longitude(posts.getLongitude())
                        .location(posts.getLocation())
                        .recruitment(posts.getRecruitment())
                        .tagEntities(tagDTOS)
                        .build();



            return ResponseEntity.ok(new ApiResponse(true, "가져오기 성공", postListDTO));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, ex.getMessage(), null));
        }
    }
}
