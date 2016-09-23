package com.pangpang.util.ip.process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.pangpang.util.ip.IpComparator;
import com.pangpang.util.ip.IpRBTree;
import com.pangpang.util.ip.IpSegmentRegion;
import com.pangpang.util.ip.RBNode;
import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
/**
 * Created by jiangjg on 2016/9/23.
 */
public class GEO3 {

    public static final String base="D:\\ip\\";
    //public static final String raw_ip_file="ipip.txt";
    public static final String raw_ip_0="0.txt";
    public static final String raw_ip_1="1.txt";
    public static final String raw_ip_2="2.txt";
    //public static final String rawlinedatafile="";
    public static final String raw_meta_file="raw-meta.txt";
    public static final String raw_latest_geo_meta="raw-latest-geo-meta.txt";
    public static final String formatted_latest_geo_meta="formatted-latest-geo-meta.txt";
    public static final String formatted_raw_ip="fomatted-raw-ip.txt";
    public static final String formatted_new_ip="fomatted-new-ip.txt";
    public static final String checked_error="error.txt";

    public static final String COLUMN_SEPERATOR="|";
    public static final String COLUMN_SEPERATOR_REGEX="\\|";


    public static List<Area> generateRawMeta(String rawIpFile, Map<String,Area> areas){
        try {
            String pathname =rawIpFile;
            File filename = new File(pathname);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename),"UTF-8");
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            line = br.readLine();
            List<Area> meta=new ArrayList<Area>();
            TypeReference<Map<String, String>> typeMap = new TypeReference<Map<String, String>>() {
            };
            ObjectMapper mapper = new ObjectMapper();
            while (line != null) {
                if(line.startsWith("[")) line=line.substring(1);
                if(line.endsWith("]")) line=line.substring(0, line.length()-1);
                Map<String,String> one=null;
                try{
                    one = mapper.reader(typeMap).readValue(line);
                }catch(Exception e){
                    e.printStackTrace();
                    continue;
                }
                //{"start":"1.8.101.0","end":"1.8.101.255","country":"中国","region":"北京","city":"北京","county":"海淀区"}

                String country=(String)one.get("country");
                String region=(String)one.get("region");
                String city=(String)one.get("city");
                //String county=(String)one.get("county");
                Area a=areas.get(country);
                if(a==null){
                    a=new Area(meta.size(),Area.COUNTRY,country);
                    areas.put(country, a);
                    meta.add(a);
                }else{
                    a.setType(Area.COUNTRY);
                }

                a=areas.get(region);
                if(a==null){
                    a=new Area(meta.size(),Area.REGION,region);
                    areas.put(region, a);
                    meta.add(a);
                }else{
                    a.setType(Area.REGION);
                }

                a=areas.get(city);
                if(a==null){
                    a=new Area(meta.size(),Area.CITY,city);
                    areas.put(city, a);
                    meta.add(a);
                }else{
                    a.setType(Area.CITY);
                }
                line = br.readLine(); // 一次读入一行数据
            }
            br.close();
            return meta;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void outputMeta(String metafile, List<Area> meta){
        try {
            File writename = new File(metafile);
            writename.createNewFile();
            BufferedWriter out=new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(writename), "UTF-8"));
            out.write(meta.size()+"");
            for(int i=0;i<meta.size();i++){
                out.newLine();
                out.write(meta.get(i).toString());
                out.write("|");
                out.write(i+"");
            }
            out.flush();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void generateRawMeta(String ipfile,String metafile){
        try {

            String pathname =ipfile;
            File filename = new File(pathname);
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename),"UTF-8");
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            line = br.readLine();
            Map<String,Area> areas=new HashMap<String,Area>();
            List<Area> meta=new ArrayList<Area>();
            TypeReference<Map<String, String>> typeMap = new TypeReference<Map<String, String>>() {
            };
            ObjectMapper mapper = new ObjectMapper();
            while (line != null) {
                if(line.startsWith("[")) line=line.substring(1);
                if(line.endsWith("]")) line=line.substring(0, line.length()-1);
                Map<String,String> one=null;
                try{
                    one = mapper.reader(typeMap).readValue(line);
                }catch(Exception e){
                    e.printStackTrace();
                    continue;
                }
                //{"start":"1.8.101.0","end":"1.8.101.255","country":"中国","region":"北京","city":"北京","county":"海淀区"}

                String country=(String)one.get("country");
                String region=(String)one.get("region");
                String city=(String)one.get("city");
                //String county=(String)one.get("county");
                Area a=areas.get(country);
                if(a==null){
                    a=new Area((short)meta.size(),Area.COUNTRY,country);
                    areas.put(country, a);
                    meta.add(a);
                }else{
                    a.setType(Area.COUNTRY);
                }

                a=areas.get(region);
                if(a==null){
                    a=new Area((short)meta.size(),Area.REGION,region);
                    areas.put(region, a);
                    meta.add(a);
                }else{
                    a.setType(Area.REGION);
                }

                a=areas.get(city);
                if(a==null){
                    a=new Area((short)meta.size(),Area.CITY,city);
                    areas.put(city, a);
                    meta.add(a);
                }else{
                    a.setType(Area.CITY);
                }
                line = br.readLine(); // 一次读入一行数据
            }
            br.close();

            File writename = new File(metafile);
            writename.createNewFile();
            //BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            BufferedWriter out=new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(writename), "UTF-8"));
            out.write(meta.size()+"");
            out.newLine();
            for(int i=0;i<meta.size();i++){
                out.write(meta.get(i).toString());
                out.write(COLUMN_SEPERATOR);
                out.write(i+"");
                out.newLine();
            }
            out.flush();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static Map<String,Area> loadRawMetaToMap(String file){
        int index=0;
        Map<String,Area> meta=null;
        try {
            String pathname = file;
            File filename = new File(pathname);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename),"UTF-8");
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();//count
            meta=new HashMap<String,Area>(Integer.parseInt(line));
            while ((line=br.readLine()) != null) {
                String[] parts=line.split("\\|");
                Area a=new Area(index++,Short.parseShort(parts[0]),parts[1]);
                meta.put(a.getName(), a);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return meta;
    }
    public static Area[] loadRawMeta(String file){
        int index=0;
        Area[] metaIndex=null;
        try {
            String pathname = file;
            File filename = new File(pathname);
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename),"UTF-8");
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();
            metaIndex=new Area[Integer.parseInt(line)];
            while ((line =br.readLine())!= null) {
                String[] parts=line.split(COLUMN_SEPERATOR_REGEX);
                Area a=new Area(index,Short.parseShort(parts[0]),parts[1]);
                metaIndex[index++]=a;
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return metaIndex;
    }


    public static void processRawIp(String rawIpFile,Map<String,Area> meta,String formattedRawIpFile,boolean append){
        try {
            String pathname = rawIpFile;
            File filename = new File(pathname);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename),"UTF-8");
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();
            TypeReference<Map<String, String>> typeMap = new TypeReference<Map<String, String>>() {
            };
            ObjectMapper mapper = new ObjectMapper();

            File writename = new File(formattedRawIpFile);
            BufferedWriter out=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(writename,append), "UTF-8"));
            while (line != null) {
                if(line.startsWith("[")) line=line.substring(1);
                if(line.endsWith("]")) line=line.substring(0, line.length()-1);
                Map<String,String> one=null;
                try{
                    one = mapper.reader(typeMap).readValue(line);
                }catch(Exception e){
                    e.printStackTrace();
                    continue;
                }
                //{"start":"1.8.101.0","end":"1.8.101.255","country":"中国","region":"北京","city":"北京","county":"海淀区"}
                String start=(String)one.get("start");
                String end=(String)one.get("end");
                String country=(String)one.get("country");
                String region=(String)one.get("region");
                String city=(String)one.get("city");
                //String county=(String)one.get("county");
                // byte[] segment=IPUtils.ipSegment(start, end);
                int icountry=-1;
                if(!StringUtils.isEmpty(country))
                    icountry=meta.get(country).getIndex();
                int iregion=-1;
                if(!StringUtils.isEmpty(region))
                    iregion=meta.get(region).getIndex();
                int icity=-1;
                if(!StringUtils.isEmpty(city)){
                    icity=meta.get(city).getIndex();
                }

                //short icounty=meta.get(county).getIndex();
                IpSegment seg=new IpSegment(IPUtils.ipToInt(start),IPUtils.ipToInt(end),icountry,iregion,icity,-1);
                out.write(seg.toString());
                out.newLine();

                line = br.readLine(); // 一次读入一行数据
            }
            br.close();

            out.flush();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public static void processRawIp(String rawIpFile,Map<String,Area> meta,String formattedRawIpFile){
        try {
            String pathname = rawIpFile;
            File filename = new File(pathname);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename),"UTF-8");
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();
            TypeReference<Map<String, String>> typeMap = new TypeReference<Map<String, String>>() {
            };
            ObjectMapper mapper = new ObjectMapper();

            File writename = new File(formattedRawIpFile);
            writename.createNewFile();
            BufferedWriter out=new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(writename), "UTF-8"));
            while (line != null) {
                if(line.startsWith("[")) line=line.substring(1);
                if(line.endsWith("]")) line=line.substring(0, line.length()-1);
                Map<String,String> one=null;
                try{
                    one = mapper.reader(typeMap).readValue(line);
                }catch(Exception e){
                    e.printStackTrace();
                    continue;
                }
                //{"start":"1.8.101.0","end":"1.8.101.255","country":"中国","region":"北京","city":"北京","county":"海淀区"}
                String start=(String)one.get("start");
                String end=(String)one.get("end");
                String country=(String)one.get("country");
                String region=(String)one.get("region");
                String city=(String)one.get("city");
                //String county=(String)one.get("county");
                // byte[] segment=IPUtils.ipSegment(start, end);
                int icountry=-1;
                if(!StringUtils.isEmpty(country))
                    icountry=meta.get(country).getIndex();
                int iregion=-1;
                if(!StringUtils.isEmpty(region))
                    iregion=meta.get(region).getIndex();
                int icity=-1;
                if(!StringUtils.isEmpty(city)){
                    icity=meta.get(city).getIndex();
                }

                //short icounty=meta.get(county).getIndex();
                IpSegment seg=new IpSegment(IPUtils.ipToInt(start),IPUtils.ipToInt(end),icountry,iregion,icity,-1);
                out.write(seg.toString());
                out.newLine();
                line = br.readLine(); // 一次读入一行数据
            }
            br.close();

            out.flush();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }





    public static void loadIp(String file,List<IpSegment> where){
        try {
            String pathname = file;
            File filename = new File(pathname);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename),"UTF-8");
            BufferedReader br = new BufferedReader(reader);
            String line = null;
            while ((line=br.readLine()) != null) {
                String[] parts=line.split(",");
                if(parts.length!=6)
                    System.out.println("Invalidate parts.");
                //byte[] segment,short country,short region,short city,short county
                IpSegment seg=new IpSegment(IPUtils.ipToInt(parts[4]),IPUtils.ipToInt(parts[5]),Integer.parseInt(parts[0])
                        ,Integer.parseInt(parts[1]),Integer.parseInt(parts[2]),Integer.parseInt(parts[3]));
                where.add(seg);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }



    public static void extractIpSegmentRegions(List<IpSegment> segment, Map<IpSegmentRegion,Integer> regions){
        try {
            int i=0;
            for(IpSegment seg: segment){
                IpSegmentRegion is=new IpSegmentRegion(seg.getCountry(),seg.getRegion(),seg.getCity(),seg.getCounty());
                if(!regions.containsKey(is)){
                    is.setIndex(i);
                    if(i>Integer.MAX_VALUE){
                        System.out.println("Index Overflow int.. MAX="+Short.MAX_VALUE+" i="+i);
                    }
                    regions.put(is, i++);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void compactIpSegment(List<IpSegment> segment, Map<IpSegmentRegion,Integer> regions,List<CompactIpSegment> compact){
        try {

            for(IpSegment seg: segment){
                IpSegmentRegion is=new IpSegmentRegion(seg.getCountry(),seg.getRegion(),seg.getCity(),seg.getCounty());
                CompactIpSegment c=new CompactIpSegment(seg.from,seg.end,regions.get(is));
                compact.add(c);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void outputIpSegRegions(Map<IpSegmentRegion,Integer> regions, String file){
        IpSegmentRegion[] tmp=new IpSegmentRegion[regions.size()];
        for(IpSegmentRegion sr: regions.keySet()){
            tmp[sr.getIndex()]=sr;
        }
        try {
            File writename = new File(file);
            writename.createNewFile();
            BufferedWriter out=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(writename), "UTF-8"));
            out.write(tmp.length+"");
            for(int i=0;i<tmp.length;i++){
                out.newLine();
                out.write(tmp[i].toString());
            }
            out.flush();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void outputCompactIpSegments(List<CompactIpSegment> compact, String file){
        try {
            File writename = new File(file);
            writename.createNewFile();
            BufferedWriter out=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(writename), "UTF-8"));
            for(CompactIpSegment c: compact){
                out.write(c.toString());
                out.newLine();
            }
            out.flush();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static IpSegmentRegion[]  loadIpSegmentRegions(String file){
        int index=0;
        IpSegmentRegion[] regions=null;
        try {
            String pathname = file;
            File filename = new File(pathname);
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename),"UTF-8");
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            line = br.readLine();
            int count=Integer.parseInt(line);
            regions=new IpSegmentRegion[count];
            while ((line =br.readLine())!=null) {
                String[] parts=line.split(",");
                IpSegmentRegion a=new IpSegmentRegion(Short.parseShort(parts[0]),
                        Short.parseShort(parts[1]),Short.parseShort(parts[2]),Short.parseShort(parts[3]));
                int i=Integer.parseInt(parts[4]);
                assert i==index;
                regions[index++]=a;
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return regions;
    }

    public static void loadCompactIpSegments(String file, List<CompactIpSegment> where){
        try {
            String pathname = file;
            File filename = new File(pathname);
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename),"UTF-8");
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            line = br.readLine();
            while (line != null) {
                String[] parts = line.split(",");
                assert parts.length == 3;
                String[] sIPs = parts[1].split("\\.");
//				CompactIpSegment seg = new CompactIpSegment((byte) Short.parseShort(sIPs[0]),
//						(byte) Short.parseShort(sIPs[1]), (byte) Short.parseShort(sIPs[2]),
//						(byte) Short.parseShort(sIPs[3]),
//						(byte) Short.parseShort(parts[2].substring(parts[2].lastIndexOf(".") + 1)),
//						(int) Integer.parseInt(parts[0]));
                CompactIpSegment seg=new CompactIpSegment(IPUtils.ipToInt(parts[1]),IPUtils.ipToInt(parts[2]),Integer.parseInt(parts[0]));
                // segments.add(seg);
                where.add(seg);
                line = br.readLine();
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void buildRBTree(String file, IpRBTree tree){
        try {
            String pathname = file;
            File filename = new File(pathname);
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            line = br.readLine();
            while (line != null) {
                String[] parts = line.split(",");
                assert parts.length == 3;
                RBNode seg=new RBNode(IPUtils.ipToInt(parts[1]),IPUtils.ipToInt(parts[2]),Integer.parseInt(parts[0]));
                tree.put(seg);
                line = br.readLine();
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static List<CompactIpSegmentEx> compactEx(List<CompactIpSegment> compact, int ipSegRegions){
        List<CompactIpSegment>[] temp=new ArrayList[ipSegRegions];
        for(CompactIpSegment c: compact){
            int index=c.getIndex();
            if(temp[index]==null) temp[index]=new ArrayList<CompactIpSegment>();
            temp[index].add(c);
        }
        List<CompactIpSegmentEx>[] exRet=new ArrayList[ipSegRegions];
        for(int i=0;i<temp.length;i++){
            if(temp[i]==null) continue;
            exRet[i]=new ArrayList<CompactIpSegmentEx>();
            CompactIpSegmentEx e=new CompactIpSegmentEx();
            exRet[i].add(e);
            Collections.sort(temp[i], new Comparator<CompactIpSegment>(){
                @Override
                public int compare(CompactIpSegment arg0, CompactIpSegment arg1) {
                    int f0=((arg0.from>>24)&0xFF)-((arg1.from>>24)&0xFF);
                    int f1=((arg0.from>>16)&0xFF)-((arg1.from>>16)&0xFF);
                    int f2=((arg0.from>>8)&0xFF)-((arg1.from>>8)&0xFF);
                    int f3=((arg0.from)&0xFF)-(arg1.from&0xFF);
                    return f0==0?(f1==0?(f2==0?f3:f2):f1):f0;
                }
            });

            for(CompactIpSegment c: temp[i]){
                if(e.from==-1){
                    e.from=c.from;
                    e.end=c.end;
                    e.setIndex(c.getIndex());
                }else{
                    boolean ct=false;
                    if(c.getFrom()<=e.getEnd()+1) ct=true;
                    if(ct){
                        if(e.getEnd()<c.getEnd())
                            e.end=c.end;
                    }else{
                        e=new CompactIpSegmentEx();
                        exRet[i].add(e);
                        e.from=c.from;
                        e.end=c.end;
                        e.setIndex(c.getIndex());
                    }
                }
            }
        }
        int count=0;
        for(int i=0;i<exRet.length;i++){
            if(exRet[i]!=null)
                count+=exRet[i].size();
        }
        List<CompactIpSegmentEx> list=new ArrayList<CompactIpSegmentEx>(count);
        for(int i=0;i<exRet.length;i++){
            if(exRet[i]!=null)
                list.addAll(exRet[i]);
        }
        return list;

    }

    public static void outputCompactEx(List<CompactIpSegmentEx> list,String file){
        try {
            File writename = new File(file);
            writename.createNewFile();
            BufferedWriter out=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(writename), "UTF-8"));
            for(CompactIpSegmentEx c: list){
                out.write(c.toString());
                out.newLine();
            }
            out.flush();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    static final Runtime runTime = Runtime.getRuntime();
    //public static Region earth=new Region(null,"地球","0000000000");
    //public static Region[] regions=null;//new Region[3929];
    public static List<IpSegment> segments1=new ArrayList<IpSegment>();
    public static List<IpSegment> segments2=new ArrayList<IpSegment>();
    public static List<CompactIpSegment> segments3=new ArrayList<CompactIpSegment>();
    public static Map<String,Area> meta=new HashMap<String,Area>();
    public static Area[] metaIndex;

    public static IpSegmentRegion[] segRegions=null;
    public static Set<IpSegmentRegion> uniqueSegRegions=new HashSet<IpSegmentRegion>();

    public static IpRBTree segmentTree = new IpRBTree(new IpComparator());
    public static void main(String[] args) {
        System.out.println("Start....");
        System.out.println("GenerateRawMeta.");

        List<Area> list0=GEO3.generateRawMeta(base+raw_ip_0,meta);
        List<Area> list1=GEO3.generateRawMeta(base+raw_ip_1,meta);
        List<Area> list2=GEO3.generateRawMeta(base+raw_ip_2,meta);
        List<Area> all=new ArrayList<Area>(list0.size()+list1.size()+list2.size());
        all.addAll(list0);
        all.addAll(list1);
        all.addAll(list2);
        System.out.println("Output raw meta. count="+all.size());
        GEO3.outputMeta(base+raw_meta_file, all);

        System.out.println("Load Raw Meta.");
        metaIndex=GEO3.loadRawMeta(base+raw_meta_file);
        System.out.println("Process Raw Ip & Generate Formatted Raw IP");
        GEO3.processRawIp(base+raw_ip_2,meta,base+formatted_raw_ip,false);
        GEO3.processRawIp(base+raw_ip_1,meta,base+formatted_raw_ip,true);
        GEO3.processRawIp(base+raw_ip_0,meta,base+formatted_raw_ip,true);

        GEO3.processRawIp(base+raw_ip_2,meta,base+"fomatted-raw-ip-2.txt",false);
        GEO3.processRawIp(base+raw_ip_1,meta,base+"fomatted-raw-ip-1.txt",false);
        GEO3.processRawIp(base+raw_ip_0,meta,base+"fomatted-raw-ip-0.txt",false);

        System.out.println("Load Formattted Raw Ip.");
        GEO3.loadIp(base+formatted_raw_ip,segments1);

        System.out.println("Generate IpSegmentRegions.");
        Map<IpSegmentRegion,Integer> ipSegRegions=new HashMap<IpSegmentRegion, Integer>();
        GEO3.extractIpSegmentRegions(segments1,ipSegRegions);
        ipSegRegions=ImmutableMap.copyOf(ipSegRegions);
        System.out.println("Output IpSegRegions.");
        GEO3.outputIpSegRegions(ipSegRegions, base+"ip-segment-regions.txt");


        System.out.println("Compact IpSegment 0.");
        segments1.clear();
        segments3.clear();
        GEO3.loadIp(base+"fomatted-raw-ip-0.txt",segments1);
        GEO3.compactIpSegment(segments1,ipSegRegions,segments3);
        System.out.println("Output CompactIp Segment size="+segments3.size());
        GEO3.outputCompactIpSegments(segments3, base+"compact-ip-segment-0.txt");
        System.out.println("Compact IpSegment 0 again.");
        long before=getUsedMemory();
        List<CompactIpSegmentEx> compactEx=compactEx(segments3,ipSegRegions.size());
        long after=getUsedMemory();
        System.out.println("After Compact Ex: count="+compactEx.size()+",Mem-Used="+(after-before));
        System.out.println("Output CompactExIpSegment 0.");
        GEO3.outputCompactEx(compactEx, base+"compact-ex-ip-segment-0.txt");


        System.out.println("Compact IpSegment 1.");
        segments1.clear();
        segments3.clear();
        GEO3.loadIp(base+"fomatted-raw-ip-1.txt",segments1);
        GEO3.compactIpSegment(segments1,ipSegRegions,segments3);
        System.out.println("Output Compact Ip Segment.");
        GEO3.outputCompactIpSegments(segments3, base+"compact-ip-segment-1.txt");
        System.out.println("Compact IpSegment 1 again.");
        before=getUsedMemory();
        compactEx=compactEx(segments3,ipSegRegions.size());
        after=getUsedMemory();
        System.out.println("After Compact Ex: count="+compactEx.size()+",Mem-Used="+(after-before));
        System.out.println("Output CompactExIpSegment 1.");
        GEO3.outputCompactEx(compactEx, base+"compact-ex-ip-segment-1.txt");


        System.out.println("Compact IpSegment 2.");
        segments1.clear();
        segments3.clear();
        GEO3.loadIp(base+"fomatted-raw-ip-2.txt",segments1);
        GEO3.compactIpSegment(segments1,ipSegRegions,segments3);
        System.out.println("Output Compact Ip Segment.");
        GEO3.outputCompactIpSegments(segments3, base+"compact-ip-segment-2.txt");
        System.out.println("Compact IpSegment 2 again.");
        before=getUsedMemory();
        compactEx=compactEx(segments3,ipSegRegions.size());
        after=getUsedMemory();
        System.out.println("After Compact Ex: count="+compactEx.size()+",Mem-Used="+(after-before));
        System.out.println("Output CompactExIpSegment 2.");
        GEO3.outputCompactEx(compactEx, base+"compact-ex-ip-segment-2.txt");



        System.out.println("Load Compact IpSegment & Build Red-Black Tree.");
        before=getUsedMemory();
        GEO3.buildRBTree(base+"compact-ex-ip-segment-2.txt",segmentTree );
        GEO3.buildRBTree(base+"compact-ex-ip-segment-1.txt",segmentTree );
        GEO3.buildRBTree(base+"compact-ex-ip-segment-0.txt",segmentTree );
        after=getUsedMemory();
        System.out.println("RBTree count="+segmentTree.size()+" size="+(after-before));
        System.out.println("Finished.");

    }

    static long getUsedMemory() {
        runTime.gc();
        return runTime.totalMemory() - runTime.freeMemory();
    }
}
