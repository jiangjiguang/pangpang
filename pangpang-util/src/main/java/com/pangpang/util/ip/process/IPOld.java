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
            Thread.sleep(5*60*1000);
            @SuppressWarnings("unchecked")
            long after=getUsedMemory();
            System.out.println("Finished.mem-used="+(after-before));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static long getUsedMemory() {
        runTime.gc();
        return runTime.totalMemory() - runTime.freeMemory();
    }
}

