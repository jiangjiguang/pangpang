package com.pangpang.util.sso.taglibs;

import org.apache.commons.lang.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jiangjg on 2016/9/30.
 */
public class JoinTag extends TagSupport {

    private Collection collection;
    private String separator;

    @Override
    public int doEndTag() throws JspException {
        try {
            if (collection != null) {
                pageContext.getOut().print(StringUtils.join(collection, separator));
            }
        } catch (IOException ex) {
            Logger.getLogger(JoinTag.class.getName()).log(Level.SEVERE, null, ex);
        }
        return TagSupport.EVAL_PAGE;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }
}
