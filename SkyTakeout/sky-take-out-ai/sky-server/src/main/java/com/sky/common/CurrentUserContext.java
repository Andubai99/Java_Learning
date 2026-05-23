package com.sky.common;

public final class CurrentUserContext {
    private static final ThreadLocal<CurrentUser> HOLDER = new ThreadLocal<>();

    private CurrentUserContext() {
    }

    public static void set(CurrentUser user) {
        HOLDER.set(user);
    }

    public static CurrentUser get() {
        CurrentUser user = HOLDER.get();
        if (user == null) {
            throw new BusinessException("未登录");
        }
        return user;
    }

    public static Long id() {
        return get().id();
    }

    public static void clear() {
        HOLDER.remove();
    }
}
