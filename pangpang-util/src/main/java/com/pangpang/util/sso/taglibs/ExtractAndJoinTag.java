package com.pangpang.util.sso.taglibs;

import com.pangpang.util.Collections3;
import org.apache.commons.lang.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jiangjg on 2016/9/30.
 */
public class ExtractAndJoinTag extends TagSupport {

    private Collection collection;
    private String propertyName;
    private String separator;

    @Override
    public int doEndTag() throws JspException {
        try {
            if (collection != null) {
                List list = Collections3.extractToList(collection, propertyName);
                if (list != null) {
                    pageContext.getOut().print(StringUtils.join(list, separator));
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ExtractAndJoinTag.class.getName()).log(Level.SEVERE, null, ex);
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

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

}
