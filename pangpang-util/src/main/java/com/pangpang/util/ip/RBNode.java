package com.pangpang.util.ip;

/**
 * Created by jiangjg on 2016/9/23.
 */
public class RBNode {
    /**
     * Black and red node
     */
    public static final byte RED = (byte)0;
    public static final byte BLACK = (byte)0x80;
    public static final int COLOR_MARK=0x80;
    RBNode parent;
    RBNode left;
    RBNode right;
    byte color;
    public int from;
    public int end;
    public int index;

    //padding  fields are not used
    byte padding0;
    byte padding1;
    byte padding2;


    public RBNode(){
        this(-1,-1,0);
    }

    public RBNode(int from,int end, int index){
        this.from=from;
        this.end=end;
        this.index=index;
        setColor(BLACK);
    }

    public RBNode(RBNode value, RBNode parent) {
        copyValue(this, value);
        this.parent = parent;
        setColor(BLACK);
    }

    public void setColor(byte color) {
        this.color=color;
    }

    public byte getColor() {
        return this.color;
    }

    public void setIndex(int index) {
        this.index=index;
    }

    public RBNode copyof() {
        return new RBNode(this.from,this.end,this.getIndex());
    }

    public int getIndex() {
        return this.index;
    }

    public RBNode getValue() {
        return this;
    }

    public void setValue(RBNode value) {
        copyValue(this, value);
    }

    private void copyValue(RBNode target, RBNode from) {
        target.from=from.from;
        target.end=from.end;
        target.color=from.color;
        target.index=from.index;
    }

    public boolean equals(Object o) {
        if (!(o instanceof RBNode)) {
            return false;
        }
        RBNode e = (RBNode) o;
        return o == null ? e == null : o.equals(e);
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(color).append(",").append(index).append(",");
        sb.append((from>>24)&0xFF).append(".").append((from>>16)&0xFF).append(".").append((from>>8)&0xFF).append(".").append(from&0xFF);
        sb.append(",");
        sb.append((end>>24)&0xFF).append(".").append((end>>16)&0xFF).append(".").append((end>>8)&0xFF).append(".").append(end&0xFF);
        return sb.toString();
    }


    public long getFrom() {
        return from& 0xFFFFFFFFL;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public long getEnd() {
        return end& 0xFFFFFFFFL;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public RBNode getParent() {
        return parent;
    }
    public void setParent(RBNode parent) {
        this.parent = parent;
    }
    public RBNode getLeft() {
        return left;
    }
    public void setLeft(RBNode left) {
        this.left = left;
    }
    public RBNode getRight() {
        return right;
    }
    public void setRight(RBNode right) {
        this.right = right;
    }

}
