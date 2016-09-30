package com.pangpang.util.ip.process;

/**
 * Created by jiangjg on 2016/9/23.
 */
public class Area {
    public final static short COUNTRY=1;
    public final static short REGION=2;
    public final static short CITY=4;
    public final static short COUNTY=8;


    int index;
    short type;//1 county1, 2 region, 4 city, 8 county2;
    String name;

    public Area(int index,short type,String name){
        this.index=index;
        this.type=type;
        this.name=name;
    }

    public void setType(int t){
        type|=t;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getName(){
        return name;
    }
    public int getIndex(){
        return index;
    }
    public String toString(){
        return type+"|"+name;
    }
}
