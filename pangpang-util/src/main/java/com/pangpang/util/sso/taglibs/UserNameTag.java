package com.pangpang.util.sso.taglibs;

import com.pangpang.util.sso.util.AuthUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jiangjg on 2016/9/30.
 */
public class UserNameTag extends TagSupport {

    @Override
    public int doEndTag() throws JspException {
        String userName = AuthUtils.getCurrentUserName();
        try {
            pageContext.getOut().print(userName);
        } catch (IOException ex) {
            Logger.getLogger(UserNameTag.class.getName()).log(Level.SEVERE, null, ex);
        }
        return TagSupport.EVAL_PAGE;
    }
}
