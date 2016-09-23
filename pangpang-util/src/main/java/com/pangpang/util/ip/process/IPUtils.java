package com.pangpang.util.ip.process;

/**
 * Created by jiangjg on 2016/9/23.
 */
public class IPUtils {
    public static long AStart = ipToLong("10.0.0.0");
    public static long AEnd = ipToLong("10.255.255.255");
    public static long BStart = ipToLong("172.16.0.0");
    public static long BEnd = ipToLong("172.31.255.255");
    public static long CStart = ipToLong("192.168.0.0");
    public static long CEnd = ipToLong("192.168.255.255");

    public static byte[] ipSegment(String startIp,String endIp){

        String[] sIps=startIp.split("\\.");
        String[] eIps=endIp.split("\\.");
        int same=0;
        if(sIps[0].equals(eIps[0])){
            same++;
            if(sIps[1].equals(eIps[1])){
                same++;
                if(sIps[2].equals(eIps[2])){
                    same++;
                    if(sIps[3].equals(eIps[3])){
                        same++;
                    }
                }
            }
        }
        byte[] ip = new byte[4+4-same];
        int n=0;
        ip[n++] = (byte)Integer.parseInt(sIps[0]);
        ip[n++] = (byte)Integer.parseInt(sIps[1]);
        ip[n++] = (byte)Integer.parseInt(sIps[2]);
        ip[n++] =(byte)Integer.parseInt(sIps[3]);

        for(int i=same;i<eIps.length;i++){
            ip[n++]=(byte)Integer.parseInt(eIps[same]);
        }


//        int p1 = startIp.indexOf(".");
//        int p2 = startIp.indexOf(".", p1 + 1);
//        int p3 = startIp.indexOf(".", p2 + 1);
//
//        int e1 = endIp.indexOf(".");
//        int e2 = endIp.indexOf(".", e1 + 1);
//        int e3 = endIp.indexOf(".", e2 + 1);
//        int same=0;
//        if(startIp.substring(0, p1).equals(endIp.substring(0, e1))){
//        	same++;
//        	if(startIp.substring(p1 + 1, p2).equals(endIp.substring(e1 + 1, e2))){
//        		same++;
//        		if(startIp.substring(p2 + 1, p3).equals(endIp.substring(e2 + 1, e3))){
//        			same++;
//        			if(startIp.substring(p3 + 1).equals(endIp.substring(e3 + 1))){
//        				same++;
//        			}
//        		}
//        	}
//        }
//
//        byte[] ip = new byte[4+4-same];
//        ip[0] = (byte)Integer.parseInt(startIp.substring(0, p1));
//        ip[1] = (byte)Integer.parseInt(startIp.substring(p1 + 1, p2));
//        ip[2] = (byte)Integer.parseInt(startIp.substring(p2 + 1, p3));
//        ip[3] =(byte)Integer.parseInt(startIp.substring(p3 + 1));


        return ip;
    }

    public static byte[] ipTo4Bytes(String strIp){
        byte[] ip = new byte[4];
        int position1 = strIp.indexOf(".");
        int position2 = strIp.indexOf(".", position1 + 1);
        int position3 = strIp.indexOf(".", position2 + 1);

        ip[0] = (byte)Integer.parseInt(strIp.substring(0, position1));
        ip[1] = (byte)Integer.parseInt(strIp.substring(position1 + 1, position2));
        ip[2] = (byte)Integer.parseInt(strIp.substring(position2 + 1, position3));
        ip[3] =(byte)Integer.parseInt(strIp.substring(position3 + 1));
        return ip;
    }
    public static String bytes2String(byte[] ip){
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<ip.length;i++){
            sb.append(ip[i]&0xFF).append(".");
        }
        return sb.substring(0,sb.length()-1);
    }

    public static int ipToInt(String strIp) {
        int[] ip = new int[4];
        int position1 = strIp.indexOf(".");
        int position2 = strIp.indexOf(".", position1 + 1);
        int position3 = strIp.indexOf(".", position2 + 1);
        ip[0] = Integer.parseInt(strIp.substring(0, position1));
        ip[1] = Integer.parseInt(strIp.substring(position1 + 1, position2));
        ip[2] = Integer.parseInt(strIp.substring(position2 + 1, position3));
        ip[3] = Integer.parseInt(strIp.substring(position3 + 1));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    public static long ipToLong(String strIp) {
        long[] ip = new long[4];
        int position1 = strIp.indexOf(".");
        int position2 = strIp.indexOf(".", position1 + 1);
        int position3 = strIp.indexOf(".", position2 + 1);
        ip[0] = Long.parseLong(strIp.substring(0, position1));
        ip[1] = Long.parseLong(strIp.substring(position1 + 1, position2));
        ip[2] = Long.parseLong(strIp.substring(position2 + 1, position3));
        ip[3] = Long.parseLong(strIp.substring(position3 + 1));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }
    public static String longToIp(long ip){
        StringBuilder sb=new StringBuilder();
        sb.append((ip>>24)&0xFF).append(".").append((ip>>16) & 0xFF).append(".").append((ip>>8)&0xFF).append(".").append(ip&0xFF);
        return sb.toString();
    }

    public static void main(String[] args) {

        int i=-1;
        long b=i & 0x00000000ffffffffL;
        System.out.println("long="+b);

        System.out.println("AStart=" + ipToLong("10.0.0.0"));
        System.out.println("AEnd=" + ipToLong("10.255.255.255"));
        System.out.println("BStart=" + ipToLong("172.16.0.0"));
        System.out.println("BEnd=" + ipToLong("172.31.255.255"));
        System.out.println("CStart=" + ipToLong("192.168.0.0"));
        System.out.println("CEnd=" + ipToLong("192.168.255.255"));

        System.out.println("-------");
        System.out.println("AStart=" + ipToInt("10.0.0.0"));
        System.out.println("AEnd=" + ipToInt("10.255.255.255"));
        System.out.println("BStart=" + ipToInt("172.16.0.0"));
        System.out.println("BEnd=" + ipToInt("172.31.255.255"));
        System.out.println("CStart=" + ipToInt("192.168.0.0"));
        System.out.println("CEnd=" + ipToInt("192.168.255.255"));

        System.out.println("-------");
        System.out.println("AStart=" +bytes2String( ipTo4Bytes("10.0.0.0")));
        System.out.println("AEnd=" + bytes2String(ipTo4Bytes("10.255.255.255")));
        System.out.println("BStart=" + bytes2String(ipTo4Bytes("172.16.0.0")));
        System.out.println("BEnd=" + bytes2String(ipTo4Bytes("172.31.255.255")));
        System.out.println("CStart=" + bytes2String(ipTo4Bytes("192.168.0.0")));
        System.out.println("CEnd=" + bytes2String(ipTo4Bytes("192.168.255.255")));


        System.out.println("-------");
        String start="192.167.253.0";String end="192.168.254.255";
        System.out.println("CEnd=" + bytes2String(ipSegment(start,end)));
    }
}
