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
public class PaginationTag extends TagSupport {

    private static final long serialVersionUID = 1L;
    private int currentPage;
    private int totalPage;
    private int totalCount;
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
        pagination.append("<div class=\"page\">");
        pagination.append("<span>共 ").append(totalCount).append(" 条记录</span>");

        //显示"上一页"
        if (currentPage > 1) {
            pagination.append("<a href=\"").append(controller).append(queryString).append("\" title=\"首页\">&lt;&lt;</a>");
            pagination.append("<a href=\"").append(controller).append(queryStringWithPage).append(currentPage - 1).append("\" class=\"prev\">上一页</a>");
        }

        //如果前面页数过多,显示"..."
        int start = 1;
        if (currentPage > 5) {
            int prevPages = currentPage - 9;
            if (prevPages < 1) {
                prevPages = 1;
            }
            start = currentPage - 4;
            pagination.append("<a href=\"").append(controller).append(queryStringWithPage).append(start).append("\" title=\"向前5页\" >...</a>");
        }

        //显示当前页附近的页
        int end = currentPage + 4;
        if (end > totalPage) {
            end = totalPage;
        }

        for (int i = start; i <= end; i++) {
            if (currentPage == i) {
                pagination.append("<a href=\"#\" class=\"current\">").append(i).append("</a>");
            } else {
                pagination.append("<a href=\"").append(controller).append(queryStringWithPage).append(i).append("\">").append(i).append("</a>");
            }
        }

        //如果后面页数过多,显示"..."
        if (end < totalPage) {
            end = end + 5;
            if (end > totalPage) {
                end = totalPage;
            }
            pagination.append("<a href=\"").append(controller).append(queryStringWithPage).append(end).append("\" title=\"向后5页\" >...</a>");
        }

        //显示"下一页"
        if (currentPage < totalPage) {
            pagination.append("<a href=\"").append(controller).append(queryStringWithPage).append(currentPage + 1).append("\" class=\"next\">下一页</a>");
            pagination.append("<a href=\"").append(controller).append(queryStringWithPage).append(totalPage).append("\" title=\"末页\">&gt;&gt;</a>");
        }

        pagination.append("</div>");

        try {
            pageContext.getOut().print(pagination.toString());
        } catch (IOException ex) {
            Logger.getLogger(PaginationTag.class.getName()).log(Level.SEVERE, null, ex);
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

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
