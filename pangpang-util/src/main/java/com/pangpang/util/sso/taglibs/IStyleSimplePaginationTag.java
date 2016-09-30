package com.pangpang.util.sso.taglibs;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jiangjg on 2016/9/30.
 */
public class IStyleSimplePaginationTag extends TagSupport {

    private static final long serialVersionUID = 1L;
    private int currentPage;
    private int currentCount;
    private int pageSize;
    private String controller;

    @Override
    public int doEndTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        String queryParams = request.getQueryString();
        queryParams = StringUtils.remove(queryParams, "&page=" + currentPage);
        queryParams = StringUtils.remove(queryParams, "page=" + currentPage);
        String queryString = "";
        String queryStringWithPage = "?page=";
        if (StringUtils.isNotBlank(queryParams)) {
            queryString = "?" + queryParams;
            queryStringWithPage = "?" + queryParams + "&page=";
        }

        StringBuilder pagination = new StringBuilder();
        pagination.append("<div class=\"pagination pagination-simple\">");
        pagination.append("<ul><li><span>第 ").append(currentPage).append(" 页</span></li></ul>");
        pagination.append("<ul>");

        //显示"上一页"
        if (currentPage > 1) {
            pagination.append("<li><a href=\"").append(controller).append(queryString).append("\" title=\"首页\">&lt;&lt;</a></li>");
            pagination.append("<li><a href=\"").append(controller).append(queryStringWithPage).append(currentPage - 1).append("\">上一页</a></li>");
        }

        //显示"下一页"
        if (currentCount >= pageSize) {
            pagination.append("<li><a href=\"").append(controller).append(queryStringWithPage).append(currentPage + 1).append("\">下一页</a></li>");
        }

        pagination.append("</ul>");
        pagination.append("</div>");

        try {
            pageContext.getOut().print(pagination.toString());
        } catch (IOException ex) {
            Logger.getLogger(IStyleSimplePaginationTag.class.getName()).log(Level.SEVERE, null, ex);
        }
        return TagSupport.EVAL_PAGE;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
