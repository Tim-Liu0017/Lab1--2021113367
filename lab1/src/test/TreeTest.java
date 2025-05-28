package test;

import cn.edu.hit.Tree;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TreeTest {

    private Tree tree;

    @BeforeEach
    void setUp() {
        // 初始化测试数据 - 构建一个简单的图
        String[] words = {"test", "bridge", "graph", "word", "another", "graph"};
        tree = new Tree(words);
    }

    @Test
    public void testQueryBridgeWords_SingleBridge() {
        // 测试有一个桥接词的情况
        String result = tree.queryBridgeWords("test","graph");
        assertEquals("The bridge word from test to graph is: bridge.", result);
    }

    @Test
    public void testQueryBridgeWords_MultipleBridges() {
        // 添加更多数据创建多个桥接词的情况
        String[] moreWords = {"test", "bridge1", "graph", "test", "bridge2", "graph", "test", "bridge3", "graph"};
        tree = new Tree(moreWords);

        String result = tree.queryBridgeWords("test", "graph");
        assertTrue(result.startsWith("The bridge words from test to graph are:"));
        assertTrue(result.contains("bridge1"));
        assertTrue(result.contains("bridge2"));
        assertTrue(result.contains("and bridge3."));
    }

    @Test
    public void testQueryBridgeWords_NoBridge() {
        // 测试没有桥接词的情况
        String[] words = {"apple", "orange", "banana"};
        tree = new Tree(words);

        String result = tree.queryBridgeWords("apple", "orange");
        assertEquals("No bridge words from apple to orange!", result);
    }

    @Test
    public void testQueryBridgeWords_FirstWordNotExist() {
        String result = tree.queryBridgeWords("nonexist", "graph");
        assertEquals("No nonexist or graph in the graph!", result);
    }

    @Test
    public void testQueryBridgeWords_SecondWordNotExist() {
        String result = tree.queryBridgeWords("test", "nonexist");
        assertEquals("No test or nonexist in the graph!", result);
    }

    @Test
    public void testQueryBridgeWords_BothWordsNotExist() {
        String result = tree.queryBridgeWords("foo", "bar");
        assertEquals("No foo or bar in the graph!", result);
    }

    @Test
    public void testQueryBridgeWords_SingleWordGraph() {
        String[] singleWord = {"lonely"};
        tree = new Tree(singleWord);

        String result = tree.queryBridgeWords("lonely", "lonely");
        // 根据实际需求可能是两种可能的输出
        assertTrue(result.equals("No lonely or lonely in the graph!") ||
                result.equals("No bridge words from lonely to lonely!"));
    }

    @Test
    public void testQueryBridgeWords_NullInput() {
        assertThrows(NullPointerException.class, () -> {
            tree.queryBridgeWords(null, null);
        });
    }

    @Test
    public void testQueryBridgeWords_EmptyInput() {
        String result = tree.queryBridgeWords("", "");
        assertEquals("No  or  in the graph!", result);
    }

    @AfterEach
    void tearDown() {
    }
}

