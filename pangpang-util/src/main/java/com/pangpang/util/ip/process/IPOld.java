package com.pangpang.util.ip.process;

import java.util.Map;

/**
 * Created by jiangjg on 2016/9/23.
 */
public class IPOld {
    static final Runtime runTime = Runtime.getRuntime();

    public static void main(String[] args) {
        try {

            long before=getUsedMemory();
//			RiskProfileClientConfig.init("http://ws.userprofile.infosec.fat70.qa.nt.ctripcorp.com:8080/userprofilews", "100000024", "http://ws.config.framework.fws.qa.nt.ctripcorp.com/configws/",
//					"collector.logging.uat.qa.nt.ctripcorp.com", "63100");
            //RiskProfileClientConfig.init("http://ws.userprofile.infosec.ctripcorp.com/userprofilews", "100000024", "http://ws.config.framework.sh.ctripcorp.com/configws/",
                    //"collector.logging.sh.ctriptravel.com", "63100");
            Thread.sleep(5*60*1000);
            @SuppressWarnings("unchecked")
            //Map<String,?> m=(Map<String,?>)IPAddressService.getIpArea("61.152.150.145");
            //System.out.println("JSON:"+JSON.toJSONString(m));

            long after=getUsedMemory();
            System.out.println("Finished.mem-used="+(after-before));
            //output();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static long getUsedMemory() {
        runTime.gc();
        return runTime.totalMemory() - runTime.freeMemory();
    }
}

