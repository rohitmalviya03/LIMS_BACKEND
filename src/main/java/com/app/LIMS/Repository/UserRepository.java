package com.app.LIMS.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.LIMS.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    User findById(Long id);
    Optional<User> findById(Integer id);
	List<User> findByLabcode(String labCode);
	User findByIdAndLabcode(Long id, String labcode);
	boolean existsByUsernameAndLabcode(String username, String labcode);
}

