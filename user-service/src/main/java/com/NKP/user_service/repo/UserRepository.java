package com.NKP.user_service.repo;

import com.NKP.user_service.model.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmailId(String emailId);

    Optional<User> findById(long id);

    Optional<User> findByPhoneNo(String phoneNo);
}
