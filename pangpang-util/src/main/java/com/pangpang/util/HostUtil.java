/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pangpang.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 *
 * @author zhengbaiyun
 */
public final class HostUtil {
    private static final Logger logger = LoggerFactory.getLogger(HostUtil.class);

    private static String hostName = "Unknown";
    private static String hostIp = "0.0.0.0";

    static {
        try {
            boolean assigned = false;
            Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                Enumeration addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ip = (InetAddress) addresses.nextElement();
                    logger.info("interface = {}, ip = {}, is loopback = {}", netInterface, ip, ip.isLoopbackAddress());
                    if (ip != null && ip instanceof Inet4Address && !ip.isLoopbackAddress()) {
                        hostName = ip.getHostName();
                        hostIp = ip.getHostAddress();
                        assigned = true;
                        break;
                    }
                }
                if (assigned) {
                    break;
                }
            }
        } catch (SocketException e) {
            // ignored
        }
    }

    public static String getLocalHostName() {
        return hostName;
    }

    public static String getLocalHostAddress() {
        return hostIp;
    }

}
