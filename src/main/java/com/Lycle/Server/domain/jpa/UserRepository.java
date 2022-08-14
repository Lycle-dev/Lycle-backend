package com.Lycle.Server.domain.jpa;

import com.Lycle.Server.domain.User.User;
import com.Lycle.Server.dto.User.SearchProfileWrapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<SearchProfileWrapper>findUserById(Long id);
    Optional<User> findUserByNickname(String nickname);
    @Override
    Optional<User> findById(Long id);

}
