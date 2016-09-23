package com.pangpang.util.ip;

/**
 * Created by jiangjg on 2016/9/23.
 */
public class IpComparator implements RBNodeComparator {
    @Override
    public int compare(int o1, RBNode o2) {
        if((o1&0x00000000ffffffffL)<o2.getFrom()) return -1;
        if((o1&0x00000000ffffffffL)>o2.getEnd()) return 1;
        return 0;
    }

    @Override
    public int compare(RBNode o1, RBNode o2) {
        if((o1.getFrom()>o2.getEnd())) return 1;
        if((o1.getEnd()<o2.getFrom())) return -1;
        return 0;
    }
}
