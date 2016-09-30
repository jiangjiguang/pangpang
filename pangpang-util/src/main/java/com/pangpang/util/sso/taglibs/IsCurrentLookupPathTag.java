package com.pangpang.util.sso.taglibs;

import com.pangpang.util.sso.model.Auth;
import com.pangpang.util.sso.util.AuthThreadLocal;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Created by jiangjg on 2016/9/30.
 */
public class IsCurrentLookupPathTag extends TagSupport {

    private String lookupPath;

    @Override
    public int doStartTag() throws JspException {
        Auth auth = AuthThreadLocal.getAuth();
        String[] prefixs = lookupPath.split(",");
        if (auth != null) {
            String currentLookupPath = auth.getLookupPath();
            for (String prefix : prefixs) {
                if (currentLookupPath.startsWith(prefix)) {
                    return TagSupport.EVAL_BODY_INCLUDE;
                }
            }
        }
        return TagSupport.SKIP_BODY;
    }

    public String getLookupPath() {
        return lookupPath;
    }

    public void setLookupPath(String lookupPath) {
        this.lookupPath = lookupPath;
    }

}
