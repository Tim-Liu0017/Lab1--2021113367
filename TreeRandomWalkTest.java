package test;

import cn.edu.hit.Tree;
import cn.edu.hit.TreeNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TreeRandomWalkTest {

  @Test
  public void testRandomWalkSingleNode() {
    String[] words = {"hello"};
    Tree tree = new Tree(words);
    String result = tree.randomWalk();
    assertEquals("hello", result);
  }

  @Test
  public void testRandomWalkNoChildren() {
    String[] words = {"hello", "world"};
    Tree tree = new Tree(words);
    String result = tree.randomWalk().trim();
    assertTrue(
            result.equals("hello world") || result.equals("world") || result.equals("hello")  // 如果允许单节点输出
    );
  }

  @Test
  public void testRandomWalkNormal() {
    String[] words = {"a", "b", "c", "d", "e"};
    Tree tree = new Tree(words);
    String result = tree.randomWalk();

    assertFalse(result.isEmpty());

    String[] pathNodes = result.split(" ");
    for (String node : pathNodes) {
      assertNotNull(tree.getTreeNodes().nodeCheck(node));
    }
  }

  @Test
  public void testRandomWalkRepeatEdge() {
    String[] words = {"a", "b", "a", "b"};
    Tree tree = new Tree(words);
    String result = tree.randomWalk();

    assertTrue(result.contains("a b a b") || result.contains("b a b"));
  }

  @Test
  public void testRandomWalkDeterministic() {
    // 测试随机性 - 多次运行应得到不同结果
    String[] words = {"a", "b", "c", "d", "e", "f", "g"};
    Tree tree = new Tree(words);

    // 运行多次，检查是否有所不同
    String firstRun = tree.randomWalk();
    boolean differentFound = false;

    for (int i = 0; i < 10; i++) {
      String currentRun = tree.randomWalk();
      if (!currentRun.equals(firstRun)) {
        differentFound = true;
        break;
      }
    }
    assertTrue(differentFound, "Random walk should produce different paths");
  }
}