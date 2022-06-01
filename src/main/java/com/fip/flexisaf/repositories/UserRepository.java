package com.fip.flexisaf.repositories;

import com.fip.flexisaf.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
   
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    Optional<User> findUserByEmail(String email);
    
//    @Query("SELECT u FROM User u WHERE u.role = (SELECT r FROM Role r WHERE R.name = ?1)")
//    List<User> findUserByRole(String roleName);
    
    //void delete(User user);
}
