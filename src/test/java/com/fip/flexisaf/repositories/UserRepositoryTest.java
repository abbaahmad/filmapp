package com.fip.flexisaf.repositories;

import com.fip.flexisaf.models.Role;
import com.fip.flexisaf.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;
    
    @BeforeEach
    void setUp(){
        User alice = new User().setName("Alice Alex")
                               .setEmail("aalex@film.com")
                               .setPassword("aliceAlex123")
                               .setRole(Role.REGISTERED);
        User bob = new User().setName("Robert Reed")
                             .setEmail("bobreed@film.com")
                             .setPassword("bobbyreeder1")
                             .setRole(Role.REGISTERED);
        User charlie = new User().setName("Charles Cousy")
                                 .setEmail("ccousy@film.com")
                                 .setPassword("charliecousy")
                                 .setRole(Role.ADMINISTRATOR);
        userRepository.saveAll(List.of(alice, bob, charlie));
    }
    
    @AfterEach
    void tearDown(){
        userRepository.deleteAll();
    }
    
    @Test
    public void createAndDeleteUser(){
        User newUser = new User()
                .setEmail("newuser@film.com")
                .setPassword("newpassword")
                .setName("New User")
                .setRole(Role.REGISTERED);
        userRepository.save(newUser);
        
        //when
        List<User> users = userRepository.findAll();
        
        //then
        assertThat(users.size()).isEqualTo(4);
        
        //when
        userRepository.deleteAll();
        List<User> usersAfterDeletion = userRepository.findAll();
        
        //then
        assertThat(usersAfterDeletion.size()).isEqualTo(0);
    }
    
    @Test
    public void findUserByEmailTest(){
        Optional<User> findUser = userRepository.findUserByEmail("aalex@film.com");
        assertThat(findUser).isPresent();
        assertThat(findUser.get().getName()).isEqualTo("Alice Alex");
        
        Optional<User> findNonExistentUser = userRepository.findUserByEmail("emptyuser@film.com");
        assertThat(findNonExistentUser).isEmpty();
    }
    
//    @Test
//    public void findUserByRoleTest(){
//        List<User> findCandidates = userRepository.findUserByRole(Role.REGISTERED);
//        assertThat(findCandidates.size()).isEqualTo(2);
//
//        List<User> findAdmin = userRepository.findUserByRole(Role.ADMINISTRATOR);
//        assertThat(findAdmin.size()).isEqualTo(1);
//    }
}
