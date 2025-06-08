package cn.edu.hit;

import java.util.ArrayList;

public class TreeNode {
    String word;
    int level;
    TreeNodeList<TreeNode> parentList;
    TreeNodeList<TreeNode> childList;
    ArrayList<Integer> childPathWeightList;

    public TreeNode(String word, TreeNode parent) {
        this.word = word;
        this.parentList = new TreeNodeList<TreeNode>();
        if (parent != null) {
            this.parentList.add(parent);
        }
        this.childList = new TreeNodeList<TreeNode>();
        this.childPathWeightList = new ArrayList<>();
    }

    public String getWord() {
        return word;
    }

    public void addParent(TreeNode anotherParent) {
        if (this.parentList.nodeCheck(anotherParent.getWord()) == null) {
            this.parentList.add(anotherParent);
        }
    }

    public void addChild(TreeNode anotherChild) {
        TreeNode checkNode = this.childList.nodeCheck(anotherChild.getWord());
        int nodeIndex, childPathWeight;
        if (checkNode != null) {
            nodeIndex = this.childList.indexOf(checkNode);
            childPathWeight = this.childPathWeightList.get(nodeIndex) + 1;
            //childPathWeight = this.childPathWeightList.get(nodeIndex).intValue() + 1;
            this.childPathWeightList.set(nodeIndex, new Integer(childPathWeight));
        } else {
            this.childList.add(anotherChild);
            this.childPathWeightList.add(new Integer(1));
        }
    }

    public int getWeightOfNode(TreeNode childNode) {
        int weight, childIndex;
        childIndex = this.childList.indexOf(childNode);
        if (childIndex == -1) {
            weight = 0;
        } else {
            weight = this.childPathWeightList.get(childIndex).intValue();
        }
        return weight;
    }

    public void setNodeLevel(int level) {
        this.level = level;
    }

    public int getNodeLevel() {
        return this.level;
    }
}
