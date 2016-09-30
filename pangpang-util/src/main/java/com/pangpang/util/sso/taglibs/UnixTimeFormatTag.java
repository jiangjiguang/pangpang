package com.pangpang.util.sso.taglibs;

import org.apache.commons.lang3.time.FastDateFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jiangjg on 2016/9/30.
 */
public class UnixTimeFormatTag extends TagSupport {

    private long unixTime;
    private String pattern;

    @Override
    public int doEndTag() throws JspException {
        try {
            pageContext.getOut().print(FastDateFormat.getInstance(pattern).format(new Date(unixTime * 1000L)));
        } catch (IOException ex) {
            Logger.getLogger(UnixTimeFormatTag.class.getName()).log(Level.SEVERE, null, ex);
        }
        return TagSupport.EVAL_PAGE;
    }

    public long getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(long unixTime) {
        this.unixTime = unixTime;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
