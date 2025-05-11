package cn.edu.hit;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class MyFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final int WIDTH = 520;
    private static final int HEIGHT = 550;
    PicDisplayPanel picPanel;

    public MyFrame() {
        try {
            String feel = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(feel);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        setBackground(Color.WHITE);
        setTitle("Flow Chart");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Container c = getContentPane();

        //菜单栏定义
        JMenuBar mb = new JMenuBar();
        JMenu mFile = new JMenu("文件(F)");
        //File菜单
        mFile.setMnemonic('F');
        JMenuItem miOpen = new JMenuItem("打开(O)");
        miOpen.setMnemonic('O');
        JMenuItem miReset = new JMenuItem("恢复默认图(R)");
        miReset.setMnemonic('R');
        mFile.add(miOpen);
        mFile.add(miReset);
        miReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                Main.readInFile();
            }
        });
        miOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser fc = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Text File", "txt");
                fc.setFileFilter(filter);
                fc.setDialogTitle("Choose file");
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fc.showOpenDialog(Main.f);
                File filename = fc.getSelectedFile();
                if (filename != null) {
                    Main.fileUrl = fc.getSelectedFile().getAbsolutePath();
                    Main.readInFile();
                }
            }
        });
        //将主菜单添加到菜单栏
        mb.add(mFile);
        //将菜单栏添加到主程序
        setJMenuBar(mb);

        JPanel mainPanel = new JPanel(new GridLayout());
        //this.picPanel = new PicDisplayPanel();
        picPanel = new PicDisplayPanel();
        //PicDisplayPanel picPanel = new PicDisplayPanel();
        JScrollPane sp = new JScrollPane(picPanel);
        sp.validate();
        mainPanel.add(sp);

        FunctionPanel funcPanel = new FunctionPanel();
        mainPanel.add(funcPanel);

        c.add(mainPanel);
        setVisible(true);

        System.out.println("窗口是否可见: " + isVisible());  // 应该输出 true
        System.out.println("窗口尺寸: " + getWidth() + "x" + getHeight()); // 检查窗口大小


    }
}

class FunctionPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTabbedPane tp = new JTabbedPane(JTabbedPane.TOP);
    private String[] tabNames = {"查询桥接词", "生成新文本", "查找最短路", "随机游走", "PageRank分析"};

    public FunctionPanel() {
        setBackground(Color.WHITE);
        JPanel tab1 = new QueryBridgePanel();
        tp.addTab(tabNames[0], null, tab1);
        JPanel tab2 = new NewTextPanel();
        tp.addTab(tabNames[1], null, tab2);
        JPanel tab3 = new ShortestPanel();
        tp.addTab(tabNames[2], null, tab3);
        JPanel tab4 = new RandomPanel();
        tp.addTab(tabNames[3], null, tab4);
        JPanel tab5 = new PageRankPanel();  // PageRank功能面板
        tp.addTab(tabNames[4], null, tab5);
        add(tp);
    }
}

class QueryBridgePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    JTextField tfWord1 = new JTextField(112);
    JLabel lbWord1 = new JLabel("单词1: ");
    JTextField tfWord2 = new JTextField(112);
    JLabel lbWord2 = new JLabel("单词2: ");
    JLabel lbRst = new JLabel();
    JButton btnQB = new JButton("开始查询");

    public QueryBridgePanel() {
        GridBagConstraints gbc = new GridBagConstraints();
        btnQB.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    lbRst.setText(Main.t.queryBridgeWords(tfWord1.getText(), tfWord2.getText()));
                } catch (Exception err) {
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        setLayout(new GridBagLayout());
        add(lbWord1, gbc);
        gbc.gridy = 1;
        add(tfWord1, gbc);
        gbc.gridy = 2;
        add(lbWord2, gbc);
        gbc.gridy = 3;
        add(tfWord2, gbc);
        gbc.gridy = 4;
        add(btnQB, gbc);
        gbc.gridy = 5;
        add(lbRst, gbc);
    }
}

class NewTextPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    JTextArea taText = new JTextArea(3, 40);
    JLabel lbText = new JLabel("新文本: ");
    JLabel lbRst = new JLabel();
    JButton btnG = new JButton("开始生成");

    public NewTextPanel() {
        taText.setBorder(new LineBorder(new Color(127, 157, 185), 1, false));
        taText.setLineWrap(true);
        btnG.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    lbRst.setText(Main.t.generateNewText(taText.getText()));
                } catch (Exception err) {

                }
            }
        });
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        setLayout(new GridBagLayout());
        add(lbText, gbc);
        gbc.gridy = 1;
        add(taText, gbc);
        gbc.gridy = 4;
        add(btnG, gbc);
        gbc.gridy = 5;
        add(lbRst, gbc);
    }
}

class ShortestPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    JTextField tfWord1 = new JTextField(112);
    JLabel lbWord1 = new JLabel("单词1: ");
    JTextField tfWord2 = new JTextField(12);
    JLabel lbWord2 = new JLabel("单词2: ");
    JTextArea txRst = new JTextArea(3, 30);
    JButton btnQB = new JButton("开始计算");
    PathGraphAssist pga;

    public ShortestPanel() {
        GridBagConstraints gbc = new GridBagConstraints();
        btnQB.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    pga = new PathGraphAssist(Main.t.treeNodes);
                    String shortest = Main.t.calcShortestPath(tfWord1.getText(), tfWord2.getText(), pga);
                    txRst.setText(shortest);
                    DirectedGraph.createShortestDirectedGraph(Main.t, Main.fileUrl, "Verdana", 12, shortest, pga);
                } catch (CloneNotSupportedException e1) {
                    // TODO 自动生成的 catch 块
                    e1.printStackTrace();
                } catch (NullPointerException e2) {

                }
            }

        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        setLayout(new GridBagLayout());
        add(lbWord1, gbc);
        gbc.gridy = 1;
        add(tfWord1, gbc);
        gbc.gridy = 2;
        add(lbWord2, gbc);
        gbc.gridy = 3;
        add(tfWord2, gbc);
        gbc.gridy = 4;
        add(btnQB, gbc);
        gbc.gridy = 5;
        add(txRst, gbc);
    }
}

class PageRankPanel extends JPanel {
    private final JButton calculateBtn = new JButton("计算PageRank");
    private final JTextArea resultArea = new JTextArea(10, 30);
    private final JSlider dampingSlider = new JSlider(1, 99, 85);
    private final JLabel statusLabel = new JLabel("就绪");

    public PageRankPanel() {
        setLayout(new BorderLayout());

        // 控制面板
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(new JLabel("阻尼系数:"));
        dampingSlider.setMajorTickSpacing(20);
        dampingSlider.setMinorTickSpacing(5);
        dampingSlider.setPaintTicks(true);
        dampingSlider.setPaintLabels(true);
        controlPanel.add(dampingSlider);
        controlPanel.add(calculateBtn);
        controlPanel.add(statusLabel);

        // 结果区域
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        add(controlPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // 事件处理
        calculateBtn.addActionListener(e -> {
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    calculateBtn.setEnabled(false);
                    statusLabel.setText("计算中...");

                    double damping = dampingSlider.getValue() / 100.0;
                    PageRankCalculator prc = new PageRankCalculator(Main.t);
                    prc.setDampingFactor(damping);
                    prc.calculate();

                    Map<String, Double> results = prc.getPageRank();
                    displayResults(results);
                    return null;
                }

                @Override
                protected void done() {
                    calculateBtn.setEnabled(true);
                    statusLabel.setText("计算完成");
                }
            }.execute();
        });
    }

    private void displayResults(Map<String, Double> results) {
        StringBuilder sb = new StringBuilder();
        sb.append("排名\t单词\tPR值\n");

        results.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEachOrdered(entry -> {
                    sb.append(String.format("%s\t%.6f\n",
                            entry.getKey(), entry.getValue()));
                });

        resultArea.setText(sb.toString());

        // 添加可视化按钮
        JButton visualizeBtn = new JButton("可视化结果");
        visualizeBtn.addActionListener(e -> {
            String prGraphPath = Main.fileUrl.replace(".txt", "_pr.png");
            if (DirectedGraph.createPRDirectedGraph(Main.t, Main.fileUrl, "Verdana", 12, results)) {
                new CreateGraphRunnable(
                        Main.fileUrl.replace(".txt", "_pr.dot"),
                        prGraphPath
                ).run();

                // 延迟加载确保图片生成完成
                Timer timer = new Timer(1000, evt -> {
                    PicDisplayPanel.setPic(prGraphPath);
                });
                timer.setRepeats(false);
                timer.start();
            }
        });

        add(visualizeBtn, BorderLayout.SOUTH);
    }
}

class RandomPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    JLabel lbRst = new JLabel();
    JButton btnG = new JButton("开始生成");

    public RandomPanel() {
        btnG.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                String random = Main.t.randomWalk();
                lbRst.setText(random);
                DirectedGraph.createRandomDirectedGraph(Main.t, Main.fileUrl, "Verdana", 12, random);
            }

        });
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        setLayout(new GridBagLayout());
        gbc.gridy = 1;
        add(btnG, gbc);
        gbc.gridy = 2;
        add(lbRst, gbc);
    }
}

class PicDisplayPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public static JLabel picLabel = new JLabel();
    public static int WIDTH;
    public static int HEIGHT;
    public static ImageIcon pic;
    boolean isAltDown = false;
    int percent = 100;

    public PicDisplayPanel() {
        picLabel = new JLabel();
        setBackground(Color.WHITE);
        add(picLabel);
    }

    public static void setPic(String path) {
        try {
            pic = new ImageIcon(ImageIO.read(new File(path)));
            WIDTH = pic.getIconWidth();
            HEIGHT = pic.getIconHeight();
            picLabel.setIcon(pic);
            picLabel.repaint();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void changeSize(int percent) {
        pic.setImage(pic.getImage().getScaledInstance(percent * WIDTH, percent * HEIGHT, Image.SCALE_DEFAULT));
        picLabel.setIcon(pic);
    }
}

