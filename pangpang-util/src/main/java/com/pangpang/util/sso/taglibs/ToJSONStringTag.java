package com.pangpang.util.sso.taglibs;

import com.pangpang.util.mapper.JsonMapper;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jiangjg on 2016/9/30.
 */
public class ToJSONStringTag extends TagSupport {

    private Object obj;
    private static JsonMapper JSON = JsonMapper.nonEmptyMapper();

    @Override
    public int doEndTag() throws JspException {
        try {
            pageContext.getOut().print(JSON.toJson(obj));
        } catch (IOException ex) {
            Logger.getLogger(ToJSONStringTag.class.getName()).log(Level.SEVERE, null, ex);
        }
        return TagSupport.EVAL_PAGE;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public static JsonMapper getJSON() {
        return JSON;
    }

    public static void setJSON(JsonMapper JSON) {
        ToJSONStringTag.JSON = JSON;
    }
}
