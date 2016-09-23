package com.pangpang.util.ip;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by jiangjg on 2016/9/23.
 */
public class IpRBTree implements Serializable {
    private static final long serialVersionUID = 1L;

    private final RBNodeComparator comparator;

    private final RBNodeComparator mergeComparator;

    private transient RBNode root;

    private transient int size = 0;

    public IpRBTree(RBNodeComparator comparator) {
        this.comparator = comparator;
        this.mergeComparator = new MergeComparator3();
    }

    public int size() {
        return size;
    }
    public RBNode getRoot(){
        return root;
    }
    public RBNode getNode(int ip){
        return getEntryUsingComparator(ip, comparator);
    }
    public RBNode getNode(String ip) {
        int intIp= ip2Int(ip);
        RBNode p = getEntryUsingComparator(intIp, comparator);
        return p;
    }

    private int ip2Int(String ip){
        int position1 = ip.indexOf(".");
        int position2 = ip.indexOf(".", position1 + 1);
        int position3 = ip.indexOf(".", position2 + 1);
        int ip0= Integer.parseInt(ip.substring(0, position1));
        int ip1 = Integer.parseInt(ip.substring(position1 + 1, position2));
        int ip2 = Integer.parseInt(ip.substring(position2 + 1, position3));
        int ip3 = Integer.parseInt(ip.substring(position3 + 1));
        return (ip0 << 24) + (ip1 << 16) + (ip2 << 8) + ip3;
    }

    private RBNode minusOne(RBNode oldSeg, RBNode newSeg) {
        return new RBNode(oldSeg.from,newSeg.from-1,oldSeg.getIndex());
    }

    private RBNode plusOne(RBNode oldSeg, RBNode newSeg) {
        return new RBNode(newSeg.end+1,oldSeg.end,oldSeg.getIndex());
    }

    private boolean isConnected(RBNode seg1, RBNode seg2) {
        if((seg2.getFrom() - seg1.getEnd()) ==1) return true;
        if((seg1.getFrom() - seg2.getEnd()) ==1) return true;
        return false;
    }

    private boolean isRightLarger(RBNode oldSeg, RBNode newSeg) {
        return oldSeg.getEnd()>newSeg.getEnd();
    }

    private boolean isRightBetween(RBNode oldSeg, RBNode newSeg) {
        if (!isRightLarger(oldSeg, newSeg) && !isConnected(oldSeg, newSeg)) {
            return true;
        }
        return false;
    }

    private boolean isLeftSmaller(RBNode oldSeg, RBNode newSeg) {
        return oldSeg.getFrom()<newSeg.getFrom();
    }

    private boolean isLeftBetween(RBNode oldSeg, RBNode newSeg) {
        if (!isLeftSmaller(oldSeg, newSeg) && !isConnected(oldSeg, newSeg)) {
            return true;
        }
        return false;
    }

    // segments are connected and have the same index
    private RBNode mergeSegment(RBNode seg1, RBNode seg2) {
        return new RBNode(seg1.getFrom()>seg2.getFrom()?seg2.from:seg1.from, seg1.getEnd()>seg2.getEnd()?seg1.end:seg2.end,seg1.getIndex());
    }

    // segments are connected but have different index
    private List<RBNode> updateSegment(RBNode oldSeg, RBNode newSeg) {
        List<RBNode> result = new ArrayList<>();
        if (isLeftSmaller(oldSeg, newSeg)) {
            if (isRightLarger(oldSeg, newSeg)) {
                RBNode left = minusOne(oldSeg, newSeg);
                RBNode right = plusOne(oldSeg, newSeg);
                if (left != null) {
                    result.add(left);
                }
                if (right != null) {
                    result.add(right);
                }
                return result;
            } else if (isRightBetween(oldSeg, newSeg)) {
                RBNode left = minusOne(oldSeg, newSeg);
                if (left != null) {
                    result.add(left);
                }
                return result;
            } else {
                result.add(oldSeg);
                return result;
            }
        } else if (isLeftBetween(oldSeg, newSeg)) {
            if (isRightLarger(oldSeg, newSeg)) {
                RBNode right = plusOne(oldSeg, newSeg);
                if (right != null) {
                    result.add(right);
                }
                return result;
            } else {
                return Collections.emptyList();
            }
        } else {
            result.add(oldSeg);
            return result;
        }
    }

    public void put(RBNode value) {
        RBNode seg = getEntryUsingComparator(value, mergeComparator);
        List<RBNode> tempSegs = new ArrayList<>();
        while (seg != null) {
            RBNode segCopy = seg.copyof();
            remove(seg);
            if (segCopy.getIndex() == value.getIndex()) {
                value = mergeSegment(segCopy, value);
            } else {
                List<RBNode> result = updateSegment(segCopy, value);
                tempSegs.addAll(result);
            }
            seg = getEntryUsingComparator(value, mergeComparator);
        }
        for (RBNode s : tempSegs) {
            insert(s);
        }
        insert(value);
    }

    public void insert(RBNode value) {
        RBNode t = root;
        if (t == null) {
            root = new RBNode(value, null);
            size = 1;
            return;
        }
        int cmp;
        RBNode parent;
        Comparator<RBNode> cpr = comparator;
        do {
            parent = t;
            cmp = cpr.compare(value, t);
            if (cmp < 0) {
                t = t.left;
            } else if (cmp > 0) {

                t = t.right;
            } else {
                t.setValue(value);
                return;
            }
        } while (t != null);
        RBNode e = new RBNode(value, parent);
        if (cmp < 0) {
            parent.left = e;
        } else {
            parent.right = e;
        }
        fixAfterInsertion(e);
        size++;
    }

    public void remove(RBNode value) {
        RBNode p = getEntryUsingComparator(value, comparator);
        if (p == null) {
            return;
        }
        deleteEntry(p);
    }
    final RBNode getEntryUsingComparator(RBNode value, RBNodeComparator comparator) {
        if (comparator != null) {
            RBNode p = root;
            while (p != null) {
                int cmp = comparator.compare(value, p);
                if (cmp < 0)
                    p = p.left;
                else if (cmp > 0)
                    p = p.right;
                else
                    return p;
            }
        }
        return null;
    }
    final RBNode getEntryUsingComparator(int value, RBNodeComparator comparator) {
        if (comparator != null) {
            RBNode p = root;
            while (p != null) {
                int cmp = comparator.compare(value, p);
                if (cmp < 0)
                    p = p.left;
                else if (cmp > 0)
                    p = p.right;
                else
                    return p;
            }
        }
        return null;
    }

    private static byte colorOf(RBNode p) {
        if (p == null) {
            return RBNode.BLACK;
        }
        return p.getColor();
    }

    private RBNode parentOf(RBNode p) {
        return (p == null ? null : p.parent);
    }

    private void setColor(RBNode p, byte c) {
        if (p == null) {
            return;
        }
        p.setColor(c);
    }

    private static RBNode leftOf(RBNode p) {
        return (p == null) ? null : p.left;
    }

    private static RBNode rightOf(RBNode p) {
        return (p == null) ? null : p.right;
    }

    /** From CLR */
    private void rotateLeft(RBNode p) {
        if (p != null) {
            RBNode r = p.right;
            p.right = r.left;
            if (r.left != null)
                r.left.parent = p;
            r.parent = p.parent;
            if (p.parent == null)
                root = r;
            else if (p.parent.left == p)
                p.parent.left = r;
            else
                p.parent.right = r;
            r.left = p;
            p.parent = r;
        }
    }

    /** From CLR */
    private void rotateRight(RBNode p) {
        if (p != null) {
            RBNode l = p.left;
            p.left = l.right;
            if (l.right != null)
                l.right.parent = p;
            l.parent = p.parent;
            if (p.parent == null)
                root = l;
            else if (p.parent.right == p)
                p.parent.right = l;
            else
                p.parent.left = l;
            l.right = p;
            p.parent = l;
        }
    }

    /** From CLR */
    private void fixAfterInsertion(RBNode x) {
        x.setColor(RBNode.RED);

        while (x != null && x != root && x.parent.getColor() == RBNode.RED) {
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
                RBNode y = rightOf(parentOf(parentOf(x)));
                if (colorOf(y) == RBNode.RED) {
                    setColor(parentOf(x), RBNode.BLACK);
                    setColor(y, RBNode.BLACK);
                    setColor(parentOf(parentOf(x)), RBNode.RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == rightOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateLeft(x);
                    }
                    setColor(parentOf(x), RBNode.BLACK);
                    setColor(parentOf(parentOf(x)), RBNode.RED);
                    rotateRight(parentOf(parentOf(x)));
                }
            } else {
                RBNode y = leftOf(parentOf(parentOf(x)));
                if (colorOf(y) == RBNode.RED) {
                    setColor(parentOf(x), RBNode.BLACK);
                    setColor(y, RBNode.BLACK);
                    setColor(parentOf(parentOf(x)), RBNode.RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == leftOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateRight(x);
                    }
                    setColor(parentOf(x), RBNode.BLACK);
                    setColor(parentOf(parentOf(x)), RBNode.RED);
                    rotateLeft(parentOf(parentOf(x)));
                }
            }
        }
        root.setColor(RBNode.BLACK);
        ;
    }

    /** From CLR */
    private void fixAfterDeletion(RBNode x) {
        while (x != root && colorOf(x) == RBNode.BLACK) {
            if (x == leftOf(parentOf(x))) {
                RBNode sib = rightOf(parentOf(x));

                if (colorOf(sib) == RBNode.RED) {
                    setColor(sib, RBNode.BLACK);
                    setColor(parentOf(x), RBNode.RED);
                    rotateLeft(parentOf(x));
                    sib = rightOf(parentOf(x));
                }

                if (colorOf(leftOf(sib)) == RBNode.BLACK && colorOf(rightOf(sib)) == RBNode.BLACK) {
                    setColor(sib, RBNode.RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(rightOf(sib)) == RBNode.BLACK) {
                        setColor(leftOf(sib), RBNode.BLACK);
                        setColor(sib, RBNode.RED);
                        rotateRight(sib);
                        sib = rightOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), RBNode.BLACK);
                    setColor(rightOf(sib), RBNode.BLACK);
                    rotateLeft(parentOf(x));
                    x = root;
                }
            } else {
                RBNode sib = leftOf(parentOf(x));

                if (colorOf(sib) == RBNode.RED) {
                    setColor(sib, RBNode.BLACK);
                    setColor(parentOf(x), RBNode.RED);
                    rotateRight(parentOf(x));
                    sib = leftOf(parentOf(x));
                }

                if (colorOf(rightOf(sib)) == RBNode.BLACK && colorOf(leftOf(sib)) == RBNode.BLACK) {
                    setColor(sib, RBNode.RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(leftOf(sib)) == RBNode.BLACK) {
                        setColor(rightOf(sib), RBNode.BLACK);
                        setColor(sib, RBNode.RED);
                        rotateLeft(sib);
                        sib = leftOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), RBNode.BLACK);
                    setColor(leftOf(sib), RBNode.BLACK);
                    rotateRight(parentOf(x));
                    x = root;
                }
            }
        }

        setColor(x, RBNode.BLACK);
    }

    final RBNode getFirstEntry() {
        RBNode p = root;
        if (p != null) {
            while (p.left != null) {
                p = p.left;
            }
        }
        return p;
    }

    final RBNode getLastEntry() {
        RBNode p = root;
        if (p != null)
            while (p.right != null)
                p = p.right;
        return p;
    }

    static RBNode successor(RBNode t) {
        if (t == null)
            return null;
        else if (t.right != null) {
            RBNode p = t.right;
            while (p.left != null)
                p = p.left;
            return p;
        } else {
            RBNode p = t.parent;
            RBNode ch = t;
            while (p != null && ch == p.right) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }

    static RBNode predecessor(RBNode t) {
        if (t == null)
            return null;
        else if (t.left != null) {
            RBNode p = t.left;
            while (p.right != null)
                p = p.right;
            return p;
        } else {
            RBNode p = t.parent;
            RBNode ch = t;
            while (p != null && ch == p.left) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }

    private void deleteEntry(RBNode p) {
        size--;

        // If strictly internal, copy successor's element to p and then make p
        // point to successor.
        if (p.left != null && p.right != null) {
            RBNode s = successor(p);
            p.from=s.from;
            p.end=s.end;
            p.index=s.index;
            p = s;
        } // p has 2 children

        // Start fixup at replacement node, if it exists.
        RBNode replacement = (p.left != null ? p.left : p.right);

        if (replacement != null) {
            // Link replacement to parent
            replacement.parent = p.parent;
            if (p.parent == null)
                root = replacement;
            else if (p == p.parent.left)
                p.parent.left = replacement;
            else
                p.parent.right = replacement;

            // Null out links so they are OK to use by fixAfterDeletion.
            p.left = p.right = p.parent = null;

            // Fix replacement
            if (p.getColor() == RBNode.BLACK)
                fixAfterDeletion(replacement);
        } else if (p.parent == null) { // return if we are the only node.
            root = null;
        } else { // No children. Use self as phantom replacement and unlink.
            if (p.getColor() == RBNode.BLACK)
                fixAfterDeletion(p);

            if (p.parent != null) {
                if (p == p.parent.left)
                    p.parent.left = null;
                else if (p == p.parent.right)
                    p.parent.right = null;
                p.parent = null;
            }
        }
    }

    public String toString() {
        return root.toString();
    }

}


class MergeComparator3 implements RBNodeComparator{

    @Override
    public int compare(RBNode o1, RBNode o2) {
        if((o1.getFrom()-o2.getEnd())>1) return 1;
        if((o1.getEnd()-o2.getFrom())<-1) return -1;
        return 0;
    }

    @Override
    public int compare(int o1, RBNode o2) {
        if((o1&0xFFFFFFFFL)<o2.getFrom()) return -1;
        if((o1&0xFFFFFFFFL)>o2.getFrom()) return 1;
        return 0;
    }

}
