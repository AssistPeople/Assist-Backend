package com.guesthouse.assistpeople.repository;

import com.guesthouse.assistpeople.entity.PostEntity;
import com.guesthouse.assistpeople.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<TagEntity, Long> {
    List<TagEntity> findByPostPostId(Long PostId);

}
