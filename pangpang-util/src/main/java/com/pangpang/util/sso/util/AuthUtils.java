package com.pangpang.util.sso.util;

import com.pangpang.util.sso.model.Auth;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jiangjg on 2016/9/30.
 */
public class AuthUtils {

    /**
     * 当前用户是否是管理员
     */
    public static boolean isAdministrator() {
        Auth auth = AuthThreadLocal.getAuth();
        if (auth != null) {
            return auth.isAdministrator();
        }
        return false;
    }

    /**
     * 取得当前用户的姓名, 如果当前用户未登录则返回空字符串.
     */
    public static String getCurrentUserName() {
        Auth auth = AuthThreadLocal.getAuth();
        if (auth != null) {
            return auth.getUserName();
        }
        return "";
    }

    /**
     * 取得当前用户的登录名, 如果当前用户未登录则返回空字符串.
     */
    public static String getCurrentUserId() {
        Auth auth = AuthThreadLocal.getAuth();
        if (auth != null) {
            return auth.getUserId();
        }
        return "";
    }

    /**
     * 取得当前用户的登录IP, 如果当前用户未登录则返回空字符串.
     */
    public static String getIP() {
        Auth auth = AuthThreadLocal.getAuth();
        if (auth != null) {
            return auth.getIp();
        }
        return "";
    }

    /**
     * 判断用户是否拥有角色, 如果用户拥有参数中的任意一个角色则返回true.
     */
    public static boolean hasAnyRole(String... roles) {
        Auth auth = AuthThreadLocal.getAuth();
        if (auth != null) {
            for (String role : roles) {
                for (String myRole : auth.getRoles()) {
                    if (role.trim().equals(myRole)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean hasAnyBizRole(String bizRoleGroup, String... roles) {
        Set<String> bizRoles = AuthUtils.getBizRoles(bizRoleGroup);
        if (bizRoles != null) {
            for (String role : roles) {
                for (String bizRole : bizRoles) {
                    if (role.trim().equals(bizRole)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取业务权限列表
     *
     * @param bizRoleGroup 业务权限分组名称
     * @return
     */
    public static Set<String> getBizRoles(String bizRoleGroup) {
        Auth auth = AuthThreadLocal.getAuth();
        Set<String> list = auth.getBizRoles().get(bizRoleGroup);
        if (list == null) {
            return new HashSet<String>();
        } else {
            return list;
        }
    }

    /**
     * 返回整个Auth对象
     */
    public static Auth getAuthEntity() {
        return AuthThreadLocal.getAuth();
    }

    /**
     * 是否在邮件组内<br/>
     * 如果参数为多个，任何一个在邮件组内就正确
     */
    public static boolean memberOfGroups(String... groups) {

        Auth auth = AuthThreadLocal.getAuth();
        Set<String> memberOfs = auth.getMemberOfs();
        if (null != memberOfs && null != groups) {
            for (String group : groups) {
                for (String memberOf : memberOfs) {
                    if (group.trim().equals(memberOf)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
