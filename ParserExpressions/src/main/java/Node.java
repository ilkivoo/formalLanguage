public class Node {
    private Node left, right;
    private char operand;
    private boolean isDivZero = false;
    private int res;
    Node(Node left, Node right, char operand, int res) {
        this.left = left;
        this.right = right;
        this.operand = operand;
        this.res = res;
    }
    int getResult() {
        return res;
    }

    void divZero() {
        isDivZero = true;
    }

    /*static void print(Node node) {
        System.out.println("in");
        if (node.operand == '#') {
            System.out.println(node.res);
        }
        else {
            System.out.println(node.operand);
            print(node.left);
            print(node.right);
        }
        System.out.println("out");
    }*/

    static boolean eq(Node node1, Node node2) {
        if (node1.operand != node2.operand || node2.res != node1.res) {
           return false;
        }
        if (node1.left == null && node2.left != null) {
            return false;
        }
        if (node2.left == null && node1.left != null) {
            return false;
        }
        if (node1.right == null && node2.right != null) {
            return false;
        }
        if (node2.right == null && node1.right != null) {
            return false;
        }
        boolean result = true;
        if (node1.left != null) {
            result = eq(node1.left, node2.left);
        }

        if (node1.right != null) {
            result = result && eq(node1.right, node2.right);
        }
        return result;
    }

    public void print() {
        print("", true);
    }

    private void print(String prefix, boolean isTail) {
        System.out.print(prefix + (isTail ? "└── " : "├── "));
        if (operand == '#') {
            System.out.println(res);
        }
        else {
            System.out.println(operand);
        }
        if (left != null)
            left.print(prefix + (isTail ? "    " : "│   "), false);

        if (right!= null) {
            right.print(prefix + (isTail ?"    " : "│   "), true);
        }
    }

}
