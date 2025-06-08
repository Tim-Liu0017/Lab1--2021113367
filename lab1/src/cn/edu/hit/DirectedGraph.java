package cn.edu.hit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class DirectedGraph {
    static String filetxt = "txt";
    static String filedot = "dot";

    public static boolean createDirectedGraph(Tree t, final String fileUrl, String fontname, String fontsize) {
        final File dotFile = new File(fileUrl.replace(".txt", ".dot"));
        try {
            dotFile.createNewFile();
            BufferedWriter outBuffer = new BufferedWriter(new FileWriter(dotFile));

            outBuffer.write(String.format("digraph %s {\n\tfontname = \"%s\";\n\tfontsize = %s;\n\n", "test", fontname, fontsize));
            outBuffer.write(String.format("\tnode [ fontname = \"%s\", fontsize = %s ]\n", fontname, fontsize));
            outBuffer.write(String.format("\tedge [ fontname = \"%s\", fontsize = %s ]\n\n", fontname, fontsize));

            for (int i = 0; i < Main.words.length; i++) {
                outBuffer.write(String.format("\t%s;\n", Main.words[i]));
            }
            for (int i = 0; i < t.getTreeNodes().size(); i++) {
                for (int j = 0; j < t.treeNodes.size(); j++) {
                    TreeNode node1 = t.treeNodes.get(i);
                    TreeNode node2 = t.treeNodes.get(j);
                    if (node1.childList.indexOf(node2) != -1 ) {
                        outBuffer.write(String.format("\t%s -> %s [label=\"%d\"];\n", node1.getWord(), node2.getWord(), node1.getWeightOfNode(node2)));
                    }
                }
            }
            outBuffer.write("}");
            outBuffer.flush();
            outBuffer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
            //e.printStackTrace();
        }

        final Runnable createGraphR = new CreateGraphRunnable(Main.fileUrl.replace("txt", "dot"),
                Main.fileUrl.replace("txt", "png"));
        Runnable showWaitingRunnable = new ShowWaitingRunnable(Main.fileUrl.replace("txt", "png"));

        final Thread createGraphThread = new Thread(createGraphR);
        Thread showWaitingThread = new Thread(showWaitingRunnable);

        Main.imgState = 0;
        createGraphThread.start();
        showWaitingThread.start();

        return true;
    }

    public static boolean createShortestDirectedGraph(Tree t, String fileUrl, String fontname, int fontsize, String shortest, PathGraphAssist pga) {
        TreeNode node1;
        TreeNode node2;
        String[] shroads = shortest.split("\n");
        for (int i = 0; i < shroads.length; i++) {
            shroads[i] = shroads[i].replaceAll("Path [0-9]+ :", "");
            shroads[i] = shroads[i].replace(".", "");
        }
        final String[] colors = { "#1abc9c", "#3498db", "#f1c40f", "#8e44ad", "#c0392b" };
        File dotFile = new File(fileUrl.replace(filetxt, filedot));
        File sdotFile = new File(fileUrl.replace(".txt", "s.dot"));
        try {
            sdotFile.createNewFile();
            Scanner in = new Scanner(dotFile);
            BufferedWriter outBuffer = new BufferedWriter(new FileWriter(sdotFile));
            while (in.hasNextLine()) {
                String str = in.nextLine();
                for (int i = 0; i < pga.allNodes.size(); i++) {
                    for (int j = 0; j < pga.allNodes.size(); j++) {
                        node1 = pga.allNodes.get(i);
                        node2 = pga.allNodes.get(j);
                        str = str.replace(String.format("%s -> %s [color = \"#3498db\"]", node1.getWord(), node2.getWord()),
                                String.format("%s -> %s [color = \"%s\"]", node1.getWord(), node2.getWord(), "#778899"));
                    }
                }
                for (int i = 0; i < shroads.length; i++) {
                    str = replaceResult(str, shroads[i], colors[i], pga);
                }
                outBuffer.write(str);
            }
            outBuffer.flush();
            outBuffer.close();
            in.close();
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }

        final Runnable createGraphR = new CreateGraphRunnable(Main.fileUrl.replace(".txt", "s.dot"),
                Main.fileUrl.replace(".txt", "s.png"));
        Runnable showWaitingRunnable = new ShowWaitingRunnable(Main.fileUrl.replace(".txt", "s.png"));

        final Thread createGraphThread = new Thread(createGraphR);
        Thread showWaitingThread = new Thread(showWaitingRunnable);

        // 初始化图片生成状态
        Main.imgState = 0;

        // 开始进程
        createGraphThread.start();
        showWaitingThread.start();

        return true;
    }

    public static String replaceResult(String ain, String shortroad, String color, PathGraphAssist pga) {
        String[] srNodes;
        srNodes = shortroad.split("->");
        for (int i = 0; i < srNodes.length - 1; i++) {
            TreeNode node1 = pga.allNodes.nodeCheck(srNodes[i]), node2 = pga.allNodes.nodeCheck(srNodes[i + 1]);
            int state = pga.queryNodeToNode(node1, node2);
            if (state == Integer.MAX_VALUE) {
                color = "#B71C1C";
            }
            ain = ain.replace(String.format("%s -> %s", srNodes[i], srNodes[i + 1]),
                    String.format("%s -> %s [color = \"%s\"]", srNodes[i], srNodes[i + 1], color));
        }
        return ain + "\n";
    }

    public static boolean createPRDirectedGraph(Tree t, String fileUrl, String fontname, int fontsize, Map<String, Double> prValues) {
        // 找出最大最小PR值用于归一化
        double maxPR = prValues.values().stream().max(Double::compare).orElse(1.0);
        double minPR = prValues.values().stream().min(Double::compare).orElse(0.0);

        File dotFile = new File(fileUrl.replace(".txt", "_pr.dot"));
        try (BufferedWriter out = new BufferedWriter(new FileWriter(dotFile))) {
            out.write("digraph G {\n");
            out.write("\tfontname = \"" + fontname + "\";\n");
            out.write("\tfontsize = " + fontsize + ";\n\n");
            out.write("\tnode [fontname=\"" + fontname + "\", fontsize=" + fontsize + "];\n");
            out.write("\tedge [fontname=\"" + fontname + "\", fontsize=" + fontsize + "];\n\n");

            // 添加节点（根据PR值设置颜色和大小）
            for (TreeNode node : t.getTreeNodes()) {
                double pr = prValues.get(node.getWord());
                double normalized = (pr - minPR) / (maxPR - minPR);

                // 颜色从蓝色(低PR)到红色(高PR)
                int red = (int)(normalized * 255);
                int blue = 255 - red;
                String color = String.format("#%02x00%02x", red, blue);

                // 大小根据PR值缩放
                int size = 12 + (int)(normalized * 20);

                out.write(String.format("\t%s [color=\"%s\", fontsize=%d, width=%.2f, height=%.2f];\n",
                        node.getWord(), color, size, normalized+0.5, normalized+0.5));
            }

            // 添加边
            for (TreeNode node1 : t.getTreeNodes()) {
                for (TreeNode node2 : t.getTreeNodes()) {
                    if (node1.childList.indexOf(node2) != -1) {
                        out.write(String.format("\t%s -> %s [label=\"%d\"];\n",
                                node1.getWord(), node2.getWord(), node1.getWeightOfNode(node2)));
                    }
                }
            }

            out.write("}");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean createRandomDirectedGraph(Tree t, String fileUrl, String fontname, int fontsize, String random) {
        String[] randomWords = random.split(" ");
        final String[] colors = { "#1abc9c", "#3498db", "#f1c40f", "#8e44ad", "#c0392b" };
        File dotFile = new File(fileUrl.replace(filetxt, filedot));
        File sdotFile = null;

        try {
            for (int i = 0; i < randomWords.length; i++) {
                sdotFile = new File(fileUrl.replace(".txt", String.format("%d.dot", i)));
                sdotFile.createNewFile();
                Scanner scannerin = new Scanner(dotFile);
                BufferedWriter outBuffer = new BufferedWriter(new FileWriter(sdotFile));
                while (scannerin.hasNextLine()) {
                    String str = scannerin.nextLine();
                    str = replaceRandomResult(str, randomWords, colors[2], i);
                    outBuffer.write(str);
                }
                outBuffer.flush();
                outBuffer.close();
                scannerin.close();
            }
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }

        final Runnable createRandGR = new CreateRandomGraphRunnable(Main.fileUrl.replace(filetxt, filedot),
                Main.fileUrl.replace(filetxt, "png"), randomWords.length);
        Runnable showRandWR = new ShowRandomWaitingRunnable(Main.fileUrl.replace(filetxt, "png"), randomWords.length);

        final Thread createRandomGraphThread = new Thread(createRandGR);
        Thread showWaitingThread = new Thread(showRandWR);

        // 初始化图片生成状态
        Main.imgState = 0;

        // 开始进程
        createRandomGraphThread.start();
        showWaitingThread.start();

        return true;
    }

    public static String replaceRandomResult(String ain, String[] random, String color, int num) {
        String intemp;
        if (num > 1) {
            intemp = ain.replace(String.format("%s -> %s", random[num - 1], random[num]),
                    String.format("%s -> %s [color = \"%s\"]", random[num - 1], random[num], color));
        } else {
            intemp = ain;
        }
        return intemp + "\n";
    }
}

class CreateGraphRunnable implements Runnable {

    String txtPath;
    public String graphPath;

    public final void setPath(String txtPath, String graphPath) {
        this.txtPath = txtPath;
        this.graphPath = graphPath;
    }

    public CreateGraphRunnable(String txtPath, String graphPath) {
        setPath(txtPath, graphPath);
    }

    @Override
    public void run() {
        Runtime run = Runtime.getRuntime();
        try {
            // 运行dot(已配置好环境变量的情况下)
            Main.imgState = 0;
            //Process process = run.exec(String.format("dot -Tpng %s -o %s", txtPath, graphPath));
            //Process process = run.exec(String.format("dot -Tpng %s -o %s", graphPath, txtPath));
            String dotPath = "D:\\Graphviz\\bin\\dot.exe"; // 调整为您系统中的实际路径
            Process process = run.exec(String.format("\"%s\" -Tpng %s -o %s", dotPath, txtPath, graphPath));

            System.out.println("DOT文件路径: " + txtPath);
            System.out.println("PNG输出路径: " + graphPath);

            process.waitFor();
            // 生成完图片标记imgState通知Thread2结束
            Main.imgState = 1;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

class ShowWaitingRunnable implements Runnable {

    String graphPath;

    public final void setPath(String graphPath) {
        this.graphPath = graphPath;
    }

    public ShowWaitingRunnable(String graphPath) {
        this.graphPath = graphPath;
    }

    @Override
    public void run() {
        while (Main.imgState == 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Create Img Success!");
        PicDisplayPanel.setPic(graphPath);
    }
}

class CreateRandomGraphRunnable implements Runnable {
    String txtPath;
    String graphPath;
    int picNum;

    public void setPath(String txtPath, String graphPath, int picNum) {
        this.txtPath = txtPath;
        this.graphPath = graphPath;
        this.picNum = picNum;
    }

    public CreateRandomGraphRunnable(String txtPath, String graphPath, int picNum) {
        setPath(txtPath, graphPath, picNum);
    }

    @Override
    public void run() {
        // 运行dot(已配置好环境变量的情况下)
        Runtime run = Runtime.getRuntime();
        try {
            Main.imgState = 0;
            Process process = null;
            for (int i = 0; i < picNum; i++) {
                process = run.exec(String.format("dot -Tpng %s -o %s", txtPath.replace(".dot", String.format("%d.dot", i)),
                        graphPath.replace(".png", String.format("%d.png", i))));
            }
            process.waitFor();
            // 生成完图片标记imgState通知Thread2结束
            Main.imgState = 1;
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
    }
}

class ShowRandomWaitingRunnable implements Runnable {

    String graphPath;
    int picNum;

    public ShowRandomWaitingRunnable(String graphPath, int picNum) {
        setPath(graphPath, picNum);
    }

    public void setPath(String graphPath, int picNum) {
        this.graphPath = graphPath;
        this.picNum = picNum;
    }

    @Override
    public void run() {
        while (Main.imgState == 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        SetPicRunnable setPicRunnable = new SetPicRunnable(graphPath, picNum);
        Thread setPicThread = new Thread(setPicRunnable);
        setPicThread.start();
    }
}

class SetPicRunnable implements Runnable {

    String graphPath;
    int picNum;

    public void setgraphPath(String graph) {
        graphPath = graph;
        return;
    }

    public String getgraphPath() {
        return graphPath;
    }

    public SetPicRunnable(String graphPath, int picNum) {
        setPath(graphPath, picNum);
    }

    public void setPath(String graphPath, int picNum) {
        this.graphPath = graphPath;
        this.picNum = picNum;
    }

    @Override
    public void run() {
        for (int i = 0; i < picNum; i++) {
            try {
                PicDisplayPanel.setPic(graphPath.replace(".png", String.format("%d.png", i)));
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
            }
        }
    }
}