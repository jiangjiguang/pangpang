package com.pangpang.util.ip.process;

/**
 * Created by jiangjg on 2016/9/23.
 */
/*
用来表示IP地址每行信息中在Area中的索引的位置
 */
public class IpSegment {
    //byte[] segment;
    int from;
    int end;

    int country;
    int region;
    int city;
    int county;

    public IpSegment(int from, int end,int country,int region,int city,int county){
        //this.segment=segment;
//		if(segment.length!=6 && segment.length!=5){
//			System.out.println("Invalid Segment");
//		}
//		this.ip0=segment[0];
//		this.ip1=segment[1];
//		this.ip2=segment[2];
//		this.ip3=segment[3];
//		if(segment.length==6){
//			this.ip4=segment[4];
//			this.ip5=segment[5];
//		}else if(segment.length==5){
//			this.ip4=segment[2];
//			this.ip5=segment[4];
//		}
        this.from=from;
        this.end=end;
        this.country=country;
        this.region=region;
        this.city=city;
        this.county=county;

    }
//	public IpSegment(byte ip0, byte ip1, byte ip2, byte ip3, byte ip4, byte ip5,byte country, byte region, short city,
//			short county) {
//		this.ip0 = ip0;
//		this.ip1 = ip1;
//		this.ip2 = ip2;
//		this.ip3 = ip3;
//		this.ip4 = ip4;
//		this.ip5 = ip5;
//		this.country = country;
//		this.region = region;
//		this.city = city;
//		this.county = county;
//	}

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(country).append(",").append(region).append(",").append(city).append(",").append(county).append(",");
        sb.append((from>>24) & 0xFF).append(".").append((from>>16) & 0xFF).append(".").append((from>>8) & 0xFF)
                .append(".").append(from & 0xFF);
        sb.append(",");
        sb.append((end>>24) & 0xFF).append(".").append((end>>16) & 0xFF).append(".").append((end>>8) & 0xFF)
                .append(".").append(end & 0xFF);
        return sb.toString();
    }
    public int getCountry() {
        return country;
    }
    public void setCountry(int country) {
        this.country = country;
    }
    public int getRegion() {
        return region;
    }
    public void setRegion(int region) {
        this.region = region;
    }
    public int getCity() {
        return city;
    }
    public void setCity(int city) {
        this.city = city;
    }
    public int getCounty() {
        return county;
    }
    public void setCounty(int county) {
        this.county = county;
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

}
