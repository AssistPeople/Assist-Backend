package com.guesthouse.assistpeople.repository;

import com.guesthouse.assistpeople.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findById(String id);

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET " +
            "u.nickName = :nickName, " +
            "u.mbti = :mbti, " +
            "u.Introduce = :introduce, " +
            "u.reason = :reason, " +
            "u.appeal = :appeal, " +
            "u.userPic = :userPic " +
            "WHERE u.id = :id")
    void updateUser(
            @Param("nickName") String nickName,
            @Param("mbti") String mbti,
            @Param("introduce") String introduce,
            @Param("reason") String reason,
            @Param("appeal") String appeal,
            @Param("userPic") String userPic,
            @Param("id") String id
    );

}
