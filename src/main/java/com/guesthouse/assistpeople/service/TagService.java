package com.guesthouse.assistpeople.service;

import com.guesthouse.assistpeople.entity.PostEntity;
import com.guesthouse.assistpeople.entity.TagEntity;
import com.guesthouse.assistpeople.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class TagService {
    private final TagRepository tagRepository;
    public List<TagEntity> getTag(Long postId) {
        return tagRepository.findByPostPostId(postId);

    }
}
