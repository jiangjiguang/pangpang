package com.pangpang.util.sso.taglibs;

import com.pangpang.util.Reflections;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jiangjg on 2016/9/30.
 */
public class InvokeMethodTag extends TagSupport {

    private Object obj;
    private String methodName;
    private Object[] args;

    @Override
    public int doEndTag() throws JspException {
        try {
            Object result = Reflections.invokeMethodByName(obj, methodName, args);
            pageContext.getOut().print(result.toString());
        } catch (IOException ex) {
            Logger.getLogger(InvokeMethodTag.class.getName()).log(Level.SEVERE, null, ex);
        }
        return TagSupport.EVAL_PAGE;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
