package com.pangpang.util.sso.taglibs;

import com.pangpang.util.sso.util.AuthUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Created by jiangjg on 2016/9/30.
 */
public class HasRolesTag extends TagSupport {

    private String roles;

    @Override
    public int doStartTag() throws JspException {
        if (AuthUtils.hasAnyRole(roles.split(","))) {
            return TagSupport.EVAL_BODY_INCLUDE;
        } else {
            return TagSupport.SKIP_BODY;
        }
    }

    /**
     * @return the roles
     */
    public String getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(String roles) {
        this.roles = roles;
    }
}
