package com.guesthouse.assistpeople.service;

import com.guesthouse.assistpeople.dto.LoginDTO;
import com.guesthouse.assistpeople.dto.UserDTO;
import com.guesthouse.assistpeople.entity.PostEntity;
import com.guesthouse.assistpeople.entity.UserEntity;
import com.guesthouse.assistpeople.repository.PostRepository;
import com.guesthouse.assistpeople.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class PostService {

    private final PostRepository postRepository;

    public  List<PostEntity> getPost(String contect) {
        return postRepository.findByConcept(contect);

    }

    public  PostEntity getOnePost(Long postId) {
        return postRepository.findByPostId(postId);

    }
}
