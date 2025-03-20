package com.megacityCab.megaCityCabBackEnd.repo;


import com.megacityCab.megaCityCabBackEnd.entity.userHasRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface userHasRoleRepo extends JpaRepository<userHasRoles,Long> {

    @Query(value ="SELECT ur.user_role FROM user_roles ur JOIN user_role_details urd ON ur.role_id = urd.user_role  JOIN user u ON urd.user_id= u.user_id WHERE u.email = :email", nativeQuery = true)
    List<String> getUserRoleByUserEmail(@Param("email") String email);

    @Query(value ="SELECT * FROM user_role_details WHERE user_id = :userId", nativeQuery = true)
    List<userHasRoles> getAllUserRoleByUserId(@Param("userId") long userId);

    @Query(value = "select * from user_role_details where user_has_role_id = :urlID", nativeQuery = true)
    userHasRoles getUserRoleDetailsById(@Param("urlID") long urlID);


}
