package com.Lycle.Server.domain.jpa;

import com.Lycle.Server.domain.User.User;
import com.Lycle.Server.dto.User.SearchProfileWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<SearchProfileWrapper>findUserById(Long id);
    Optional<User> findUserByNickname(String nickname);
    @Override
    Optional<User> findById(Long id);

    @Query(value = "update user u set u.nickname=:nickname where u.id=:id", nativeQuery = true)
    boolean updateNickname(Long id, String nickname);

    @Query(value = "update user u set u.password=:password where u.id=:id", nativeQuery = true)
    boolean updatePassword(Long id, String password);


}
