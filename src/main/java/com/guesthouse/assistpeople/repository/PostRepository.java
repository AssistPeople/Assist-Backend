package com.guesthouse.assistpeople.repository;

import com.guesthouse.assistpeople.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity,Long> {
    List<PostEntity> findByConcept(String concept);

    PostEntity findByPostId(Long postId);

}
