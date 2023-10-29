package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTests {
    @Autowired
    private UserRepository repo;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void testCreateNewUserWithOneRole() {
        Role roleAdmin = testEntityManager.find(Role.class, 1);
        User user = new User("nghia@gmail.com", "123456", "Bui", "Nghia");
        user.addRole(roleAdmin);
        User userSaved = repo.save(user);
        assertThat(userSaved.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateNewUserWithTwoRole() {
        Role roleEditor = new Role(3);
        Role roleAssistant = new Role(5);
        User user = new User("nghiaeditor@gmail.com", "123456", "Editor", "Nghia");
        user.addRole(roleEditor);
        user.addRole(roleAssistant);
        User userSaved = repo.save(user);
        assertThat(userSaved.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllUser() {
        List<User> users = repo.findAll();
        users.forEach(user -> System.out.println(user));
    }

    @Test
    public void testGetUserByEmail() {
        String email = "nghia22@gmail.com";
        User user = repo.getUserByEmail(email);
        System.out.println(user);
    }

    @Test
    public void testUpdateEnabledStatus() {
        Integer id = 1;
        repo.updateEnabledStatus(id, true);
    }

    @Test
    public void testListFirstPage() {
        int pageNumber = 0;
        int pageSize = 4;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = repo.findAll(pageable);
        List<User> users = page.getContent();
        users.forEach(u -> System.out.println(u.toString()));
        assertThat(users.size()).isEqualTo(pageSize);
    }

    @Test
    public void testSearchUsers() {
        String keyword = "bruce";
        int pageNumber = 0;
        int pageSize = 4;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = repo.findAll(keyword, pageable);
        List<User> users = page.getContent();
        users.forEach(u -> System.out.println(u.toString()));
        assertThat(users.size()).isGreaterThan(0);
    }

}
