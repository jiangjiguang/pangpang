package com.pangpang.util.sso.taglibs;

import com.pangpang.util.sso.util.AuthThreadLocal;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Created by jiangjg on 2016/9/30.
 */
public class IsLoginedTag extends TagSupport {

    @Override
    public int doStartTag() throws JspException {
        if (AuthThreadLocal.getAuth() != null) {
            return TagSupport.EVAL_BODY_INCLUDE;
        } else {
            return TagSupport.SKIP_BODY;
        }
    }
}
