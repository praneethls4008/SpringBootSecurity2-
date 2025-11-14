package com.learning.security.repository;

import com.learning.security.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query("""
            select t1 from Token t1 inner join User u1 on t1.user.id = u1.id
            where u1.id = :userId and (t1.expired = false and t1.revoked=false)
            """)
    List<Token> findAllValidTokensByUser(Integer userId);

    Optional<Token> findByToken(String token);
}
