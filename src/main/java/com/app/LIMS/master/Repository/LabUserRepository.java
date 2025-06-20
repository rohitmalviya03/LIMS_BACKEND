package com.app.LIMS.master.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.LIMS.master.entity.Lab;
import com.app.LIMS.master.entity.LabUser;

import java.util.List;
import java.util.Optional;

@Repository
public interface LabUserRepository extends JpaRepository<LabUser, Long> {
    Optional<LabUser> findByEmailAndLab(String email, Lab lab);

	List<LabUser> findByLab(Long labId);
}