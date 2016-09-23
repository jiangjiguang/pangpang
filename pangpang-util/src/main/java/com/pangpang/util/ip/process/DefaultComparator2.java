package com.pangpang.util.ip.process;

import com.pangpang.util.ip.RBNode;

import java.util.Comparator;

/**
 * Created by jiangjg on 2016/9/23.
 */
public class DefaultComparator2 implements Comparator<RBNode> {
    @Override
    public int compare(RBNode o1, RBNode o2) {

        if(o1.getFrom()==o2.getFrom() && o1.getEnd()==o2.getEnd()) return 0;
        else if(o1.getFrom()!=o2.getFrom()) return (int)(o1.getFrom()-o2.getFrom());
        else return (int)(o1.getEnd()-o2.getEnd());

//		if (o1.getIp0() != o2.getIp0()) {
//			return (o1.getIp0() & 0xFF) - (o2.getIp0() & 0xFF);
//		}
//		if (o1.getIp1() != o2.getIp1()) {
//			return (o1.getIp1() & 0xFF) - (o2.getIp1() & 0xFF);
//		}
//		if (o1.getIp2() != o2.getIp2()) {
//			return (o1.getIp2() & 0xFF) - (o2.getIp2() & 0xFF);
//		}
//		if (o1.getIp3() != o2.getIp3()) {
//			return (o1.getIp3() & 0xFF) - (o2.getIp3() & 0xFF);
//		}
//		if (o1.getIp4() != o2.getIp4()) {
//			return (o1.getIp4() & 0xFF) - (o2.getIp4() & 0xFF);
//		}
//		if(o1.getIp5() != o2.getIp5()){
//			return (o1.getIp5() & 0xFF) - (o2.getIp5() & 0xFF);
//		}
//		return 0;
    }
}
