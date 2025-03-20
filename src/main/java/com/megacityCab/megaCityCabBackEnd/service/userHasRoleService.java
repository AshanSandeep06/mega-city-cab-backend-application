package com.megacityCab.megaCityCabBackEnd.service;

public interface userHasRoleService {

    void saveUserRoleDetails(long userId, long roleId);
    void deleteUserHasRole(long userId);
}
