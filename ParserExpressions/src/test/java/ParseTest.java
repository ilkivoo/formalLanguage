import org.junit.Test;

import static org.junit.Assert.*;

public class ParseTest {
    @Test
    public void isInteger() throws Exception {
        assertEquals(true, Parse.isInteger("56"));
        assertEquals(false, Parse.isInteger("5zxc"));
        assertEquals(false, Parse.isInteger("()svasdv"));
        assertEquals(false, Parse.isInteger("(56)"));
        assertEquals(false, Parse.isInteger("()()()"));
        assertEquals(true, Parse.isInteger("234234234"));
    }

    @Test
    public void parseExp() throws Exception {
        Node rightAnswer = new Node(null, null, '#', 0);
        assertEquals(true, Node.eq(rightAnswer, Parse.parseExp("0")));
        rightAnswer = new Node(null, null, '#', 5);
        assertEquals(true, Node.eq(rightAnswer, Parse.parseExp("5")));
    }

    @Test
    public void parseSum() throws Exception {
        Node rightAnswer = new Node(new Node(null, null, '#', 2), new Node(null, null, '#', 3), '+', 5);
        assertEquals(true, Node.eq(rightAnswer, Parse.parseExp("2+3")));
        rightAnswer = new Node(new Node(null, null, '#', 2), new Node(null, null, '#', 3), '+', 5);
        assertEquals(true, Node.eq(rightAnswer, Parse.parseExp("(2+3)")));
    }

    @Test
    public void parseMul() throws Exception {
        Node rightAnswer = new Node(new Node(null, null, '#', 2), new Node(null, null, '#', 3), '*', 6);
        assertEquals(true, Node.eq(rightAnswer, Parse.parseExp("2*3")));
        rightAnswer = new Node(new Node(null, null, '#', 2), new Node(null, null, '#', 3), '*', 6);
        assertEquals(true, Node.eq(rightAnswer, Parse.parseExp("(2*3)")));
    }

    @Test
    public void parsePow() throws Exception {
        Node rightAnswer = new Node(new Node(null, null, '#', 2), new Node(null, null, '#', 3), '^', 8);
        assertEquals(true, Node.eq(rightAnswer, Parse.parseExp("2^3")));
        rightAnswer = new Node(new Node(null, null, '#', 2), new Node(null, null, '#', 3), '^', 8);
        assertEquals(true, Node.eq(rightAnswer, Parse.parseExp("(2^3)")));
    }

    @Test
    public void test1() throws Exception {
        String expr = "(((2+3)*2)/10)+(2^3)*2/8";
        //2+3
        Node node1 = new Node(new Node(null, null, '#', 2), new Node(null, null, '#', 3), '+', 5);
        //(2+3)*2
        Node node2 = new Node(node1, new Node(null, null, '#', 2), '*', 10);
        //((2+3)*2)/10
        Node node3 = new Node(node2,  new Node(null, null, '#', 10), '/', 1);

        //2^3
        Node node4 = new Node(new Node(null, null, '#', 2), new Node(null, null, '#', 3), '^', 8);
        //(2^3)*2
        Node node5 = new Node(node4, new Node(null, null, '#', 2), '*', 16);
        //(2^3)*2/8
        Node node6 = new Node(node5, new Node(null, null, '#', 8), '/', 2);
        Node rightAnswer = new Node(node3, node6, '+', 3);
        assertEquals(true, Node.eq(rightAnswer, Parse.parseExp(expr) ));
    }

    @Test
    public void test2() throws Exception {
        String expr = "(0+13)*((42-7)/0)";
        Node node1 = new Node(new Node(null, null, '#', 0), new Node(null, null, '#', 13), '+', 13);
        Node node2 = new Node(new Node(null, null, '#', 42), new Node(null, null, '#', 7), '-', 35);
        Node node3 = new Node(node2, new Node(null, null, '#', 0), '/', -1);
        Node rAns = new Node(node1, node3, '*', -13);
        assertEquals(true, Node.eq(rAns, Parse.parseExp(expr)));
    }


    @Test
    public void test3() throws Exception {
        String expr = "1-2-3-4-(5-6)";
        Node node1 = new Node(new Node(null, null, '#', 1), new Node(null, null, '#', 2), '-', -1);
        Node node2 = new Node(node1, new Node(null, null, '#', 3), '-', -4);
        Node node3 = new Node(node2, new Node(null, null, '#', 4), '-', -8);
        Node node4 = new Node(new Node(null, null, '#', 5), new Node(null, null, '#', 6), '-', -1);
        Node rAns = new Node(node3, node4, '-', -7);
        assertEquals(true, Node.eq(rAns, Parse.parseExp(expr)));
    }

    @Test
    public void test4() throws Exception {
        String expr = "2^3^2";
        Node node1 = new Node(new Node(null, null, '#', 3), new Node(null, null, '#', 2), '^', 9);
        Node rAns = new Node(new Node(null, null, '#', 2), node1, '^', 512);
        assertEquals(true, Node.eq(rAns, Parse.parseExp(expr)));
    }

    @Test
    public void test5() throws Exception {
        String expr = "(2^(3-1)*3)";
        Node node1 = new Node(new Node(null, null, '#', 3), new Node(null, null, '#', 1), '-', 2);
        Node node2 = new Node(new Node(null, null, '#', 2), node1, '^', 4);
        Node rAns = new Node(node2, new Node(null, null, '#', 3), '*', 12);
        assertEquals(true, Node.eq(rAns, Parse.parseExp(expr)));
    }

    @Test
    public void test6() throws Exception {
        String expr = "10/2/5";
        Node node1 = new Node(new Node(null, null, '#', 10), new Node(null, null, '#', 2), '/', 5);
        Node rAns = new Node( node1, new Node(null, null, '#', 5),'/', 1);
        assertEquals(true, Node.eq(rAns, Parse.parseExp(expr)));
    }

    @Test
    public void test7() throws Exception {
        String expr = "(((((13)))))";
        Node rAns = new Node(null, null, '#', 13);
        assertEquals(true, Node.eq(rAns, Parse.parseExp(expr)));
    }

    @Test
    public void test8() {
        String str = Parse.help("");
        assertEquals(str, "");

    }

}