package com.pangpang.util.sso.taglibs;

import com.google.common.collect.Lists;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.List;

/**
 * Created by jiangjg on 2016/9/30.
 */
public class ArrayContainsTag extends TagSupport {

    private Object[] array;
    private Object item;

    @Override
    public int doStartTag() throws JspException {

        if (array == null) {
            return TagSupport.SKIP_BODY;
        } else {
            List temp = Lists.newArrayList(array);
            if (temp.contains(item)) {
                return TagSupport.EVAL_BODY_INCLUDE;
            } else {
                return TagSupport.SKIP_BODY;
            }
        }
    }

    public Object[] getArray() {
        return array;
    }

    public void setArray(Object[] array) {
        this.array = array;
    }

    public Object getItem() {
        return item;
    }

    public void setItem(Object item) {
        this.item = item;
    }
}

