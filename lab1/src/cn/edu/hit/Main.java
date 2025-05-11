package cn.edu.hit;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main extends JComponent {
    private static final long serialVersionUID = 1L;
    public static MyFrame f;
    public static String fileUrl;
    public static String[] words;
    public static Tree t;
    public static int imgState;

    public static void readInFile() {
        File file = new File(fileUrl);
        String wordsStr = "";
        try {
            Scanner in = new Scanner(file);
            while (in.hasNextLine()) {
                String str = in.nextLine();
                wordsStr = wordsStr.concat(replaceStr(str) + " ");
            }
            System.out.println(wordsStr);
            words = wordsStr.split("\\s+");
            t = new Tree(words);
            DirectedGraph.createDirectedGraph(t, fileUrl, "Verdana", "12");
            in.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public Main() {
        setBackground(Color.WHITE);
    }

    public static String[] wordSplit(String str) {
        return str.split("\\s+");
    }

    public static String replaceStr(String str) {
        return str.replaceAll("[^a-zA-Z]", " ").toLowerCase();
    }

    public static void main(String[] args) {
        f = new MyFrame();
    }
}

