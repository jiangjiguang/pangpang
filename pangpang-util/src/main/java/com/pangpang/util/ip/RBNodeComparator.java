package com.pangpang.util.ip;

import java.util.Comparator;

/**
 * Created by jiangjg on 2016/9/23.
 */
public interface RBNodeComparator extends Comparator<RBNode> {
    public int compare(int o1, RBNode o2);
    public int compare(RBNode o1, RBNode o2);
}