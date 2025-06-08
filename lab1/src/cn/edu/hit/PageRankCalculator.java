package cn.edu.hit;

import java.util.HashMap;
import java.util.Map;

public class PageRankCalculator {
    private final Map<String, Double> pageRank;
    private final Map<String, Integer> outDegree;
    private final Tree tree;
    private double dampingFactor = 0.85;
    private static final double CONVERGENCE_THRESHOLD = 0.0001;
    private static final int MAX_ITERATIONS = 100;

    public PageRankCalculator(Tree tree) {
        this.tree = tree;
        this.pageRank = new HashMap<>();
        this.outDegree = new HashMap<>();
        initialize();
    }

    private void initialize() {
        // 初始化出度
        for (TreeNode node : tree.getTreeNodes()) {
            outDegree.put(node.getWord(), node.childList.size());
        }

        // 初始化PR值（均匀分布）
        double initialValue = 1.0 / tree.getTreeNodes().size();
        for (TreeNode node : tree.getTreeNodes()) {
            pageRank.put(node.getWord(), initialValue);
        }
    }

    public void calculate() {
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            Map<String, Double> newPR = new HashMap<>();
            double danglingFactor = 0.0;

            // 计算悬挂节点贡献
            for (Map.Entry<String, Double> entry : pageRank.entrySet()) {
                if (outDegree.get(entry.getKey()) == 0) {
                    danglingFactor += entry.getValue();
                }
            }

            // 计算每个节点的新PR值
            for (String u : pageRank.keySet()) {
                double sum = 0.0;

                // 查找所有指向u的节点
                for (TreeNode node : tree.getTreeNodes()) {
                    if (node.childList.nodeCheck(u) != null) {
                        sum += pageRank.get(node.getWord()) / outDegree.get(node.getWord());
                    }
                }

                // 加入悬挂节点贡献
                sum += danglingFactor / tree.getTreeNodes().size();

                // 更新公式
                newPR.put(u, (1 - dampingFactor)/tree.getTreeNodes().size() + dampingFactor * sum);
            }

            // 检查收敛
            if (hasConverged(newPR)) {
                System.out.println("PageRank收敛于第 " + (i+1) + " 次迭代");
                break;
            }

            pageRank.putAll(newPR);
        }
    }

    private boolean hasConverged(Map<String, Double> newPR) {
        for (String node : pageRank.keySet()) {
            if (Math.abs(pageRank.get(node) - newPR.get(node)) > CONVERGENCE_THRESHOLD) {
                return false;
            }
        }
        return true;
    }

    public Map<String, Double> getPageRank() {
        return pageRank;
    }

    public void setDampingFactor(double dampingFactor) {
        this.dampingFactor = dampingFactor;
    }
}