package cn.edu.hit;

public class PathNode implements Cloneable{
    int pathLength;
    TreeNode presentNode;
    TreeNodeList<TreeNode> path;

    public PathNode(TreeNode startNode) {
        this.pathLength = 0;
        this.presentNode = startNode;
        this.path = new TreeNodeList<TreeNode>();
        this.path.push(startNode);
    }

    //重写实现深度克隆操作
    @SuppressWarnings("unchecked")
    @Override
    protected Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        PathNode newPathNode = (PathNode) super.clone();
        newPathNode.path = (TreeNodeList<TreeNode>) this.path.clone();
        return newPathNode;
    }
}