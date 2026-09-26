package com.NKP.user_service.repo;

import com.NKP.user_service.model.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmailId(String emailId);

//    Optional<User> findById(long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<User> findById(long id);

    Optional<User> findByPhoneNo(String phoneNo);
}
