package com.pangpang.util.ip.process;

/**
 * Created by jiangjg on 2016/9/23.
 */
public class CompactIpSegment {

    int from;
    int end;
    int index;


//	byte ip0;
//	byte ip1;
//	byte ip2;
//	byte ip3;
//	byte ip4;
//
//	// First bit represents color
//	byte indexHigh;
//	short indexLow;

//	public CompactIpSegment(byte ip0, byte ip1, byte ip2, byte ip3, byte ip4, int index) {
//		this.ip0 = ip0;
//		this.ip1 = ip1;
//		this.ip2 = ip2;
//		this.ip3 = ip3;
//		this.ip4 = ip4;
//		indexHigh = (byte) (index >> Short.SIZE);
//		indexLow = (short) index;
//	}
//	public void setIndex(int index) {
//		indexHigh = (byte) (index >> Short.SIZE);
//		indexLow = (short) index;
//	}

    public CompactIpSegment(int from, int end, int index){
        this.from=from;
        this.end=end;
        this.index=index;
    }
    public void setIndex(int index){
        this.index=index;
    }
    public int getIndex(){
        return this.index;
    }
    public long getFrom() {
        return from&0xFFFFFFFFL;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public long getEnd() {
        return end&0xFFFFFFFFL;
    }

    public void setEnd(int end) {
        this.end = end;
    }
    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(getIndex()).append(",");
        sb.append((from>>24) & 0xFF).append(".").append((from>>16) & 0xFF).append(".").append((from>>8) & 0xFF)
                .append(".").append(from & 0xFF);
        sb.append(",");
        sb.append((end>>24) & 0xFF).append(".").append((end>>16) & 0xFF).append(".").append((end>>8) & 0xFF)
                .append(".").append(end & 0xFF);
        return sb.toString();
    }

}