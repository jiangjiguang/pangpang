package com.pangpang.util.sso.util;

import com.pangpang.util.sso.model.Auth;

/**
 * Created by jiangjg on 2016/9/30.
 */
public class AuthThreadLocal {

    private static ThreadLocal<Auth> local = new ThreadLocal<Auth>();

    public static void setAuth(Auth auth) {
        local.set(auth);
    }

    public static Auth getAuth() {
        return local.get();
    }
}
