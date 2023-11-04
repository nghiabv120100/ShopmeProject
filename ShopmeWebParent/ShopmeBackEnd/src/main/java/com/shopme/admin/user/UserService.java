package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private RoleRepository roleRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public static int USERS_PER_PAGE = 4;

    public List<User> listAll() {
        return userRepo.findAll(Sort.by("firstName").ascending());
    }

    public Page<User> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, USERS_PER_PAGE, sort);
        if (keyword == null) {
            return userRepo.findAll(pageable);
        }
        return userRepo.findAll(keyword, pageable);
    }

    public List<Role> listRoles() {
        return (List<Role>) roleRepo.findAll();
    }

    public User save(User user) {
        boolean isUpdating = (user.getId() != null);
        if (isUpdating == true) {
            User oldUser = userRepo.findById(user.getId()).get();
            if (user.getPassword().isBlank()) {
                user.setPassword(oldUser.getPassword());
            } else {
                encodePassword(user);
            }
        } else {
            encodePassword(user);
        }
        return userRepo.save(user);
    }

    private void encodePassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public boolean isEmailUnique(Integer id, String email) {
        User user = userRepo.getUserByEmail(email);
        if (user == null) {
            return true;
        }
        boolean isCreatingNew = (id == null);
        if (isCreatingNew) {
            if (user != null) {
                return false;
            }
        } else {
            if (user.getId() != id) {
                return false;
            }
        }
        return true;
    }

    public User get(Integer id) throws UserNotFoundException {
        try {
            return userRepo.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new UserNotFoundException("Could not find any user with ID " + id);
        }
    }

    public void delete(Integer id) throws UserNotFoundException {
        Long countById = userRepo.countById(id);
        if (countById == null || countById == 0) {
            throw new UserNotFoundException("Could not find any user with Id = " + id);
        }
        userRepo.deleteById(id);
    }

    public void updateEnabledStatus(Integer userId, boolean enabled) throws UserNotFoundException {
        Long countById = userRepo.countById(userId);
        if (countById == null || countById == 0) {
            throw new UserNotFoundException("Could not find any user with Id = " + userId);
        }
        userRepo.updateEnabledStatus(userId, enabled);
    }
}
