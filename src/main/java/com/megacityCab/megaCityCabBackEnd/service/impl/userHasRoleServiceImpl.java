package com.megacityCab.megaCityCabBackEnd.service.impl;

import com.megacityCab.megaCityCabBackEnd.entity.userHasRoles;
import com.megacityCab.megaCityCabBackEnd.repo.userHasRoleRepo;
import com.megacityCab.megaCityCabBackEnd.service.userHasRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class userHasRoleServiceImpl implements userHasRoleService {

    @Autowired
    userHasRoleRepo userRoleRepo;

    @Override
    public void saveUserRoleDetails(long userId, long roleId) {
        userHasRoles userRoles = new userHasRoles(
                (long)0,
                roleId,
                userId,
                "1"

        );
        userRoleRepo.save(userRoles);
    }

    @Override
    public void deleteUserHasRole(long userId) {
        List<userHasRoles> allUserRoleByUserId
                = userRoleRepo.getAllUserRoleByUserId(userId);
        for (userHasRoles userRoleDetails : allUserRoleByUserId) {
            userHasRoles userRoleDetailsById = userRoleRepo.getUserRoleDetailsById(userRoleDetails.getUserRoleDetailsId());
            userRoleRepo.delete(userRoleDetailsById);
        }
    }
}
