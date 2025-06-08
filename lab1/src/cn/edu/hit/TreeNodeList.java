package cn.edu.hit;

import java.util.ArrayList;

public class TreeNodeList<E> extends ArrayList<E> implements Cloneable{
    private static final long serialVersionUID = 1L;
    int longestWordLength = 0;

    public TreeNode nodeCheck(String word) {
        TreeNode existedNode = null, getNode;
        for (int i = 0; i < this.size(); i++) {
            getNode = (TreeNode) this.get(i);
            if (word.equals(getNode.word)) {
                existedNode = getNode;
                break;
            }
        }
        return existedNode;
    }

    @Override
    public boolean add(E e) {
        // TODO Auto-generated method stub
        if (e != null) {
            TreeNode addNode = (TreeNode) e;
            String word = addNode.word;
            this.longestWordLength = word.length() > this.longestWordLength ? word.length() : this.longestWordLength;
        }
        return super.add(e);
    }

    public int getLongestWordLength() {
        return this.longestWordLength;
    }

    public boolean push(E pushNode) {
        boolean flag;
        flag = this.add(pushNode);
        return flag;
    }

    public TreeNode pop() {
        TreeNode popNode;
        if (this.size() != 0 ) {
            popNode = (TreeNode) this.get(0);
            this.remove(0);
        } else {
            popNode = null;
        }
        return popNode;
    }

    public ArrayList<Integer> multiIndexOf(E e) {
        ArrayList<Integer> multiIndex = new ArrayList<Integer>();
        TreeNode queryNode = (TreeNode) e;
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).equals(queryNode)) {
                multiIndex.add(new Integer(i));
            }
        }
        return multiIndex;
    }
}
