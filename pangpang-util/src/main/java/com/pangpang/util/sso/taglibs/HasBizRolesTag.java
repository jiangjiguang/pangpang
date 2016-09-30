package com.pangpang.util.sso.taglibs;

import com.pangpang.util.sso.util.AuthUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Created by jiangjg on 2016/9/30.
 */
public class HasBizRolesTag extends TagSupport {

    private String bizRoleGroup;
    private String bizRoles;

    @Override
    public int doStartTag() throws JspException {
        if (AuthUtils.hasAnyBizRole(bizRoleGroup, bizRoles.split(","))) {
            return TagSupport.EVAL_BODY_INCLUDE;
        } else {
            return TagSupport.SKIP_BODY;
        }
    }

    public String getBizRoleGroup() {
        return bizRoleGroup;
    }

    public void setBizRoleGroup(String bizRoleGroup) {
        this.bizRoleGroup = bizRoleGroup;
    }

    public String getBizRoles() {
        return bizRoles;
    }

    public void setBizRoles(String bizRoles) {
        this.bizRoles = bizRoles;
    }
}
