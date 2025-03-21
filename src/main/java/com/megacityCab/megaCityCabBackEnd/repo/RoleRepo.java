package com.megacityCab.megaCityCabBackEnd.repo;

import com.megacityCab.megaCityCabBackEnd.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Roles,Long> {

    @Query(value = "select * from user_roles where user_role=?1",nativeQuery = true)
    Roles findByRole(String role);
}
