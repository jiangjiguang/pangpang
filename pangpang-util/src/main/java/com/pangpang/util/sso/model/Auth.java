package com.pangpang.util.sso.model;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * Created by jiangjg on 2016/9/30.
 */
public class Auth implements Serializable {

    static final long serialVersionUID = 3143123412341234l;
    private String userName;
    private String userId;
    private Set<String> roles;
    private Map<String, Set<String>> bizRoles;
    private String ip;
    private boolean administrator;
    private String lookupPath;

    private String mail;
    private String company;
    private String department;
    private String displayName;
    private String employee;
    private String distinguishedName;
    private String memberOf;
    private String city;

    //cas中组信息
    private Set<String> memberOfs;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Map<String, Set<String>> getBizRoles() {
        return bizRoles;
    }

    public void setBizRoles(Map<String, Set<String>> bizRoles) {
        this.bizRoles = bizRoles;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isAdministrator() {
        return administrator;
    }

    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
    }

    public String getLookupPath() {
        return lookupPath;
    }

    public void setLookupPath(String lookupPath) {
        this.lookupPath = lookupPath;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getDistinguishedName() {
        return distinguishedName;
    }

    public void setDistinguishedName(String distinguishedName) {
        this.distinguishedName = distinguishedName;
    }

    public String getMemberOf() {
        return memberOf;
    }

    public void setMemberOf(String memberOf) {
        this.memberOf = memberOf;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Set<String> getMemberOfs() {
        return memberOfs;
    }

    public void setMemberOfs(Set<String> memberOfs) {
        this.memberOfs = memberOfs;
    }


}
