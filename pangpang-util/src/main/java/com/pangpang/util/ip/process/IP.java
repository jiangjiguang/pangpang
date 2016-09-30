package com.pangpang.util.ip.process;

import com.pangpang.util.ip.IpComparator;
import com.pangpang.util.ip.IpRBTree;
import com.pangpang.util.ip.IpSegmentRegion;
import com.pangpang.util.ip.RBNode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jiangjg on 2016/9/23.
 */
public class IP {
    //public static IpTree2 segmentTree = new IpTree2(new DefaultComparator2());
    public static IpRBTree tree = new IpRBTree(new IpComparator());
    public static IpRBTree tree0 = new IpRBTree(new IpComparator());
    public static IpRBTree tree1 = new IpRBTree(new IpComparator());
    public static IpRBTree tree2 = new IpRBTree(new IpComparator());
    public static IpSegmentRegion[] segmentRegions=null;
    public static Area[] regions=null;
    public static String base="D:\\ip\\";
    public static String compact_ex_ip_segment="compact-ex-ip-segment.txt";
    public static String ip_segment_regions="ip-segment-regions.txt";
    public static String raw_meta="raw-meta.txt";
    static final Runtime runTime = Runtime.getRuntime();
    static List<RBNode> compactSegments=new ArrayList<RBNode>();
    public static void loadCompactExIp(String file, List<RBNode> compactSegments){
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
                //String[] sIPs = parts[1].split("\\.");
                //String[] sIPe = parts[2].split("\\.");
                RBNode seg = new RBNode(IPUtils.ipToInt(parts[1]),IPUtils.ipToInt(parts[2]),
                        Integer.parseInt(parts[0]));
                compactSegments.add(seg);
                line = br.readLine();
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
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
                //String[] sIPs = parts[1].split("\\.");
                CompactIpSegment seg = new CompactIpSegment(IPUtils.ipToInt(parts[1]),IPUtils.ipToInt(parts[2]),
                        Integer.parseInt(parts[0]));
                // segments.add(seg);
                where.add(seg);
                line = br.readLine();
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //	public static void loadIp(String file) {
//		try {
//            String pathname = file;
//            File filename = new File(pathname);
//            InputStreamReader reader = new InputStreamReader(
//                    new FileInputStream(filename),"UTF-8");
//            BufferedReader br = new BufferedReader(reader);
//            String line = "";
//            line = br.readLine();
//            while (line != null) {
//				String[] parts = line.split(",");
//				assert parts.length == 3;
//				//String[] sIPs = parts[1].split("\\.");
//				//String[] sIPe = parts[2].split("\\.");
//				RBNode seg = new RBNode(IPUtils.ipToInt(parts[1]),IPUtils.ipToInt(parts[2]),
//						Integer.parseInt(parts[0]));
//				segmentTree.put(seg);
//				line = br.readLine();
//			}
//            br.close();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
    public static void loadIpSegmentRegions(String file){
        int index=0;
        try {
            String pathname = file;
            File filename = new File(pathname);
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename),"UTF-8");
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();
            int count=Integer.parseInt(line);
            segmentRegions=new IpSegmentRegion[count];
            while ((line =br.readLine())!=null) {
                String[] parts=line.split(",");
                IpSegmentRegion a=new IpSegmentRegion(Integer.parseInt(parts[0]),
                        Integer.parseInt(parts[1]),Integer.parseInt(parts[2]),Integer.parseInt(parts[3]));
                int i=Integer.parseInt(parts[4]);
                assert i==index;
                segmentRegions[index++]=a;

            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static Area[] loadRawMeta(String file){
        short index=0;
        Area[] metaIndex=null;
        try {
            String pathname = file;
            File filename = new File(pathname);
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename),"UTF-8");
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();
            metaIndex=new Area[Integer.parseInt(line)];
            while ((line = br.readLine()) != null) {
                String[] parts=line.split("\\|");
                Area a=new Area(index,Short.parseShort(parts[0]),parts[1]);
                metaIndex[index++]=a;
                //line=br.readLine();
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return metaIndex;
    }

    public static int maxHeight(IpRBTree tree){
        RBNode root=tree.getRoot();
        return maxheight(root);
    }
    public static int minHeight(IpRBTree tree){
        RBNode root=tree.getRoot();
        return minheight(root);
    }
    public static void blackHeight(IpRBTree tree){
        RBNode root=tree.getRoot();
        if(root.getColor()!=RBNode.BLACK) System.out.println("Root ERROR.");
        blackcolor(root);
    }
    public static int maxheight(RBNode node){
        if(node.getLeft()==null && node.getRight()==null) return 1;
        else if(node.getLeft()!=null && node.getRight()==null){
            return maxheight(node.getLeft())+1;
        }else if(node.getLeft()==null && node.getRight()!=null){
            return maxheight(node.getRight())+1;
        }else{
            return Math.max(maxheight(node.getLeft()), maxheight(node.getRight()))+1;
        }
    }
    public static int minheight(RBNode node){
        if(node.getLeft()==null && node.getRight()==null ) return 1;
        else if(node.getLeft()!=null && node.getRight()==null){
            return minheight(node.getLeft())+1;
        }else if(node.getLeft()==null && node.getRight()!=null){
            return minheight(node.getRight())+1;
        }else{
            return Math.min(minheight(node.getLeft()), minheight(node.getRight()))+1;
        }
    }


    public static void blackcolor(RBNode node){
        if( node.getColor()==RBNode.BLACK){
            if(node.getLeft()!=null) blackcolor(node.getLeft());
            if(node.getRight()!=null) blackcolor(node.getRight());
        }
        if( node.getColor()==RBNode.RED){
            if(node.getLeft()!=null && node.getLeft().getColor()!=RBNode.BLACK) System.out.println("Node ERROR:"+node.toString());
            if(node.getRight()!=null && node.getRight().getColor()!=RBNode.BLACK) System.out.println("Node ERROR:"+node.toString());
            if(node.getLeft()!=null) blackcolor(node.getLeft());
            if(node.getRight()!=null) blackcolor(node.getRight());
        }
    }
    public static int blackMinMaxHeight(IpRBTree tree){
        RBNode root=tree.getRoot();
        return blackheight(root);
    }
    public static int blackheight(RBNode node){
        int incr=node.getColor()==RBNode.BLACK? 1:0;

        if(node.getLeft()!=null && node.getRight()!=null ){
            return Math.max(blackheight(node.getLeft()), blackheight(node.getRight()))+incr;
        }else
        if(node.getLeft()==null && node.getRight()!=null){
            return Math.max(1, blackheight(node.getRight()))+incr;
        }else
        if(node.getRight()==null && node.getLeft()!=null){
            return Math.max(1, blackheight(node.getLeft()))+incr;
        }else {
            return 1+incr;
        }

    }

    static void uniqueRegions(RBNode node, Set<Integer> r){
        if(node!=null){
            r.add(node.getIndex());
            uniqueRegions(node.getLeft(),r);
            uniqueRegions(node.getRight(),r);
        }
    }

    public static void main(String[] args){
        long before=getUsedMemory();
        GEO3.buildRBTree(base+"compact-ex-ip-segment-0.txt", tree0);
        long after0=getUsedMemory();
        GEO3.buildRBTree(base+"compact-ex-ip-segment-1.txt", tree1);
        long after1=getUsedMemory();
        GEO3.buildRBTree(base+"compact-ex-ip-segment-2.txt", tree2);
        long after2=getUsedMemory();
        loadIpSegmentRegions(base+ip_segment_regions);
        regions=loadRawMeta(base+raw_meta);
        long after=getUsedMemory();
        System.out.println("tree0.size="+tree0.size()+",tree1.size="+tree1.size()+",tree2.size="+tree2.size());
        System.out.println("IP Mem-Used="+(after2-before)+", ip0="+(after0-before)+", ip1="+(after1-after0)+",ip2="+(after2-after1));
        System.out.println("IP & Meta Mem-Used="+(after-before)+",meta="+(after-after2));

        before=getUsedMemory();
        GEO3.buildRBTree(base+"compact-ex-ip-segment-2.txt", tree);
        GEO3.buildRBTree(base+"compact-ex-ip-segment-1.txt", tree);
        GEO3.buildRBTree(base+"compact-ex-ip-segment-0.txt", tree);
        after2=getUsedMemory();
        System.out.println("tree0+tree1+tree2="+(tree1.size()+tree2.size()+tree0.size())+",tree.size="+tree.size()+",tree.mem="+(after2-before));


        System.out.println("Load formatted raw ip.");
        List<IpSegment> segments2=new ArrayList<IpSegment>();
        List<IpSegment> segments1=new ArrayList<IpSegment>();
        List<IpSegment> segments0=new ArrayList<IpSegment>();
        GEO3.loadIp(base+"fomatted-raw-ip-2.txt",segments2);
        GEO3.loadIp(base+"fomatted-raw-ip-1.txt",segments1);
        GEO3.loadIp(base+"fomatted-raw-ip-0.txt",segments0);
        loadCompactExIp(base+compact_ex_ip_segment,compactSegments);

        System.out.println("Start run test for mem-use.");

        before=getUsedMemory();
        runTest(segments0,tree0);
        runTest(segments1,tree1);
        runTest(segments2,tree2);
        after= runTime.totalMemory() - runTime.freeMemory();
        System.out.println("Query mem used="+(after-before));

        System.out.println("Start run test for validate.");
        runTest(segments0,tree);
        runTest(segments1,tree);
        runTest(segments2,tree);

        System.out.println("Start run test for query time.");
        runTestForTime(segments0,segments1,segments2);
//		int max=maxHeight(tree);
//		int min=minHeight(tree);
//		blackHeight(tree);
//		int min2=blackMinMaxHeight(tree);
        blackHeight(tree0);
        blackHeight(tree1);
        blackHeight(tree2);
        System.out.println("max="+maxHeight(tree0)+",min="+minHeight(tree0)+",min2="+blackMinMaxHeight(tree0));
        System.out.println("max="+maxHeight(tree1)+",min="+minHeight(tree1)+",min2="+blackMinMaxHeight(tree1));
        System.out.println("max="+maxHeight(tree2)+",min="+minHeight(tree2)+",min2="+blackMinMaxHeight(tree2));

        System.out.println("Caculate unique regions.");
        Set<Integer> ur=new HashSet<Integer>();
        uniqueRegions(tree.getRoot(),ur);
        System.out.println("Unique regions:"+ur.size());
    }

    static void runNothing(){

    }
    static void runTest(List<IpSegment> segments, IpRBTree tree){
        for(int i=0;i<segments.size();i++){
            testOne(segments.get(i), tree);
            //runNothing();
        }
    }
    static void runTestForTime(List<IpSegment> segments0, List<IpSegment> segments1,List<IpSegment> segments2){
        List<Integer> ips0=new ArrayList<Integer>();
        samples(segments0,ips0);
        List<Integer> ips1=new ArrayList<Integer>();
        samples(segments1,ips1);
        List<Integer> ips2=new ArrayList<Integer>();
        samples(segments2,ips2);
        long before=System.nanoTime();
        runTestForTime(ips0,tree0);
        runTestForTime(ips1,tree1);
        runTestForTime(ips2,tree2);
        long after=System.nanoTime();
        System.out.println((ips0.size()+ips1.size()+ips2.size())+" times queries used:"+(after-before)+"ms");
    }
    static void runTestForTime(List<Integer> ips, IpRBTree tree){

        for(Integer ip: ips){
            RBNode node = tree.getNode(ip);
            System.out.println(node);
        }
        //long after=System.currentTimeMillis();
        //System.out.println(ips.size()+" times queries used:"+(after-before)+"ms");

    }

    static void samples(List<IpSegment> segments,List<Integer> ips){
        for(int i=0;i<segments.size();i++){
            IpSegment ex=segments.get(i);
            int ip=ex.from;
            ips.add(ip);
            ip=ex.end;
            ips.add(ip);
            if(ex.getEnd()-ex.getFrom()>1){
                ip=(int)((ex.getEnd()+ex.getFrom())/2);
                ips.add(ip);
            }
        }

    }



    static void testOne(IpSegment ex, IpRBTree tree){
        int ip=ex.from;
        RBNode one=tree.getNode(ip);
        if(one==null) {
            System.out.println("Error: "+ex.toString());
            return;
        }
        boolean err=false;
        if(ex.getCountry()!=-1 && segmentRegions[one.getIndex()].getCountry()!=-1 && ex.getCountry()!=segmentRegions[one.getIndex()].getCountry()){
            err=true;
        }
        if(ex.getRegion()!=-1&& segmentRegions[one.getIndex()].getRegion()!=-1 && ex.getRegion()!=segmentRegions[one.getIndex()].getRegion()){
            err=true;
        }
        if(ex.getCity()!=-1&& segmentRegions[one.getIndex()].getCity()!=-1 && ex.getCity()!=segmentRegions[one.getIndex()].getCity()){
            err=true;
        }
        if(err){
            RBNode o0=tree0.getNode(ip);
            RBNode o1=tree1.getNode(ip);
            RBNode o2=tree2.getNode(ip);
            if(o0!=null && o0.getIndex()!=one.getIndex()){
                System.out.println("Error: "+ex.toString()+"--->"+one.toString()+"--->"+segmentRegions[one.getIndex()].toString());
            }
            if(o0==null && o1 !=null && o1.getIndex()!=one.getIndex()){
                System.out.println("Error: "+ex.toString()+"--->"+one.toString()+"--->"+segmentRegions[one.getIndex()].toString());
            }
            if(o0==null && o1==null && o2!=null && o2.getIndex()!=one.getIndex()){
                System.out.println("Error: "+ex.toString()+"--->"+one.toString()+"--->"+segmentRegions[one.getIndex()].toString());
            }
        }

        ip=ex.end;

        one=tree.getNode(ip);
        if(one==null) {
            System.out.println("Error: "+ex.toString());
            return;
        }
        err=false;
        if(ex.getCountry()!=-1 && segmentRegions[one.getIndex()].getCountry()!=-1 && ex.getCountry()!=segmentRegions[one.getIndex()].getCountry()){
            //System.out.println("Error: "+ex.toString()+"--->"+one.toString()+"--->"+segmentRegions[one.getIndex()].toString());
            err=true;
            //return;
        }
        if(ex.getRegion()!=-1&& segmentRegions[one.getIndex()].getRegion()!=-1 && ex.getRegion()!=segmentRegions[one.getIndex()].getRegion()){
            //System.out.println("Error: "+ex.toString()+"--->"+one.toString()+"--->"+segmentRegions[one.getIndex()].toString());
            err=true;
            //return;
        }
        if(ex.getCity()!=-1&& segmentRegions[one.getIndex()].getCity()!=-1 && ex.getCity()!=segmentRegions[one.getIndex()].getCity()){
            //System.out.println("Error: "+ex.toString()+"--->"+one.toString()+"--->"+segmentRegions[one.getIndex()].toString());
            err=true;
            //return;
        }
        if(err){
            RBNode o0=tree0.getNode(ip);
            RBNode o1=tree1.getNode(ip);
            RBNode o2=tree2.getNode(ip);
            if(o0!=null && o0.getIndex()!=one.getIndex()){
                System.out.println("Error: "+ex.toString()+"--->"+one.toString()+"--->"+segmentRegions[one.getIndex()].toString());
            }
            if(o0==null && o1 !=null && o1.getIndex()!=one.getIndex()){
                System.out.println("Error: "+ex.toString()+"--->"+one.toString()+"--->"+segmentRegions[one.getIndex()].toString());
            }
            if(o0==null && o1==null && o2!=null&& o2.getIndex()!=one.getIndex()){
                System.out.println("Error: "+ex.toString()+"--->"+one.toString()+"--->"+segmentRegions[one.getIndex()].toString());
            }
        }

        if(ex.getEnd()-ex.getFrom()>2){
            ip=(int)((ex.getFrom()+ex.getEnd())/2);
            one=tree.getNode(ip);
            if(one==null) {
                System.out.println("Error: "+ex.toString());
                return;
            }
            err=false;
            if(ex.getCountry()!=-1 && segmentRegions[one.getIndex()].getCountry()!=-1 && ex.getCountry()!=segmentRegions[one.getIndex()].getCountry()){
                //System.out.println("Error: "+ex.toString()+"--->"+one.toString()+"--->"+segmentRegions[one.getIndex()].toString());
                err=true;
                //return;
            }
            if(ex.getRegion()!=-1&& segmentRegions[one.getIndex()].getRegion()!=-1 && ex.getRegion()!=segmentRegions[one.getIndex()].getRegion()){
                //System.out.println("Error: "+ex.toString()+"--->"+one.toString()+"--->"+segmentRegions[one.getIndex()].toString());
                err=true;
                //return;
            }
            if(ex.getCity()!=-1&& segmentRegions[one.getIndex()].getCity()!=-1 && ex.getCity()!=segmentRegions[one.getIndex()].getCity()){
                //System.out.println("Error: "+ex.toString()+"--->"+one.toString()+"--->"+segmentRegions[one.getIndex()].toString());
                err=true;
                //return;
            }
            if(err){
                RBNode o0=tree0.getNode(ip);
                RBNode o1=tree1.getNode(ip);
                RBNode o2=tree2.getNode(ip);
                if(o0!=null && o0.getIndex()!=one.getIndex()){
                    System.out.println("Error: "+ex.toString()+"--->"+one.toString()+"--->"+segmentRegions[one.getIndex()].toString());
                }
                if(o0==null && o1 !=null && o1.getIndex()!=one.getIndex()){
                    System.out.println("Error: "+ex.toString()+"--->"+one.toString()+"--->"+segmentRegions[one.getIndex()].toString());
                }
                if(o0==null && o1==null && o2!=null&& o2.getIndex()!=one.getIndex()){
                    System.out.println("Error: "+ex.toString()+"--->"+one.toString()+"--->"+segmentRegions[one.getIndex()].toString());
                }
            }
        }


    }

    static boolean same(IpSegment ex, RBNode n){
        boolean r1=ex.getCountry()==segmentRegions[n.getIndex()].getCountry();
        boolean r2=ex.getRegion()==segmentRegions[n.getIndex()].getRegion();
        boolean r3=ex.getCity()==segmentRegions[n.getIndex()].getCity();
        return r1&& r2 && r3;
    }
    static long getUsedMemory() {
        runTime.gc();
        return runTime.totalMemory() - runTime.freeMemory();
    }
}
