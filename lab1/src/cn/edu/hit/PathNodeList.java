package cn.edu.hit;

import java.util.ArrayList;

public class PathNodeList<E> extends ArrayList<E> {
    private static final long serialVersionUID = 1L;

    public boolean push(E e) {
        return this.add(e);
    }

    public PathNode pop() {
        PathNode node = null;
        if (this.size() != 0) {
            node = (PathNode) this.get(0);
            this.remove(0);
        }
        return node;
    }

    public PathNode getShortestPath() {
        PathNode node, retNode = null;
        int shortestLength = Integer.MAX_VALUE;
        for (int i = 0; i < this.size(); i++) {
            node = (PathNode) this.get(i);
            if (node.pathLength < shortestLength) {
                retNode = node;
                shortestLength = node.pathLength;
            }
        }
        return retNode;
    }
}
