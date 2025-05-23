package com.michael.tinyurl.security.context;

import com.michael.tinyurl.security.database.entity.UserEntity;
import jakarta.persistence.EntityManager;

public class UserEntityContextHolder {
    private static final ThreadLocal<UserEntity> userEntityThreadLocal = new ThreadLocal<>();

    private UserEntityContextHolder() {}

    public static void set(UserEntity userEntity) {
        userEntityThreadLocal.set(userEntity);
    }

    public static UserEntity get(EntityManager entityManager) {
        if (userEntityThreadLocal.get() != null) {
            return entityManager.merge(userEntityThreadLocal.get());
        }
        return userEntityThreadLocal.get();
    }

    /**
     * You should only call this method if you are sure the UserEntity is in the current transaction.
     * Failure to do so will result in JPA blowing up.
     * @return - UserEntity
     */
    public static UserEntity get() {
        return userEntityThreadLocal.get();
    }

    public static void remove() {
        userEntityThreadLocal.remove();
    }
}
