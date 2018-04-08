import java.io.*;

public class Parse {

    private static String skipWhiteSpace(String expr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < expr.length(); i++) {
            if (expr.charAt(i) != ' ') {
                sb.append(expr.charAt(i));
            }
        }
        return sb.toString();
    }

    static boolean isInteger(String expr) {
        for (int i = 0; i < expr.length(); i++) {
            if (!(expr.charAt(i) >= '0' && expr.charAt(i) <= '9')) {
                return false;
            }
        }
        return true;
    }

    static Node parseExp(String expr) throws CalculateException {
        if (expr.equals("")) {
            return new Node(null, null, '#', 0);
        }
        if (isInteger(expr)) {
            return new Node(null, null, '#', Integer.parseInt(expr));
        }
        return parseSum(expr);
    }

    private static Node parseSum(String expr) throws CalculateException {
        if (isInteger(expr)) {
            return new Node(null, null, '#', Integer.parseInt(expr));
        }
        int b = 0;
        int ind = -1;
        for (int i = 0; i < expr.length(); i++) {
            if (expr.charAt(i) == '(') {
                b++;
            }
            if (expr.charAt(i) == ')') {
                b--;
            }
            if (b < 0) {
                throw new CalculateException(((Integer)i).toString());
            }
            if (b == 0 && (expr.charAt(i) == '+' || expr.charAt(i) == '-')) {
                ind = i;
            }
        }
        if (ind == -1) {
            return parseMul(expr);
        }
        else {
            String expr1;
            String expr2;
            expr1 = expr.substring(0, ind);
            expr2 = expr.substring(ind + 1);
            Node node1 = parseExp(expr1);
            Node node2;
            try {
                node2 = parseExp(expr2);
            }
            catch (CalculateException e) {
                Integer positionError = Integer.parseInt(e.getMessage()) + expr1.length();
                throw new CalculateException(positionError.toString());
            }
            if (expr.charAt(ind) == '+') {
                return new Node(node1, node2, '+', node1.getResult() + node2.getResult());
            }
            else {
                return new Node(node1, node2, '-', node1.getResult() - node2.getResult());
            }
        }
    }





    private static Node parseMul(String expr) throws CalculateException {
        if (isInteger(expr)) {
            return new Node(null, null, '#', Integer.parseInt(expr));
        }
        int b = 0;
        int ind = -1;
        for (int i = 0; i < expr.length(); i++) {
            if (expr.charAt(i) == '(') {
                b++;
            }
            if (expr.charAt(i) == ')') {
                b--;
            }
            if (b < 0) {
                throw new CalculateException(((Integer)i).toString());
            }
            if (b == 0 && (expr.charAt(i) == '*' || expr.charAt(i) == '/')) {
                ind = i;
            }
        }

        if (b != 0) {
            throw new CalculateException(((Integer)expr.length()).toString());
        }

        if (ind == -1) {
            return parsePow(expr);
        }
        else {
            String expr1;
            String expr2;
            expr1 = expr.substring(0, ind);
            expr2 = expr.substring(ind + 1);
            Node node1 = parseExp(expr1);
            Node node2;
            try {
                node2 = parseExp(expr2);
            }
            catch (CalculateException e) {
                Integer positionError = Integer.parseInt(e.getMessage()) + expr1.length();
                throw new CalculateException(positionError.toString());
            }
            if (expr.charAt(ind) == '*') {
                return new Node(node1, node2, '*', node1.getResult() * node2.getResult());
            }
            else {
                int div = node2.getResult();
                if (div == 0) {
                    Node result = new Node(node1, node2, '/', -1);
                    result.divZero();
                    return result;
                } else {
                    return new Node(node1, node2, '/', node1.getResult() / node2.getResult());
                }
            }
        }
    }

    private static Node parsePow(String expr) throws CalculateException{
        if (isInteger(expr)) {
            return new Node(null, null, '#', Integer.parseInt(expr));
        }
        int b = 0;
        for (int i = 0; i < expr.length(); i++) {
            if (expr.charAt(i) == '(') {
                b++;
            }
            if (expr.charAt(i) == ')') {
                b--;
            }
            if (b < 0) {
                throw new CalculateException(((Integer)i).toString());
            }
            if (b == 0 && expr.charAt(i) == '^') {
                String expr1;
                String expr2;
                expr1 = expr.substring(0, i);
                expr2 = expr.substring(i + 1);
                Node node1 = parseExp(expr1);
                Node node2;
                try {
                    node2 = parseExp(expr2);
                }
                catch (CalculateException e) {
                    Integer positionError = Integer.parseInt(e.getMessage()) + expr1.length();
                    throw new CalculateException(positionError.toString());
                }
                if (node2.getResult() < 0) {
                    throw new CalculateException(((Integer)i).toString());
                }
                return new Node(node1, node2, '^', (int) Math.pow(node1.getResult(), node2.getResult()));
            }
        }
        if (b != 0) {
            throw new CalculateException(((Integer)expr.length()).toString());
        }
        if (expr.charAt(0) == '(' && expr.charAt(expr.length() - 1) == ')') {
            Node result;
            try {
                result = parseExp(expr.substring(1, expr.length() - 1));
            }
            catch (CalculateException e) {
                Integer positionError = Integer.parseInt(e.getMessage()) + 1;
                throw new CalculateException(positionError.toString());
            }
            return result;
        }
        throw new CalculateException(((Integer)expr.length()).toString());
    }


    public static void main(String[] args)  {
        FileReader reader;
        BufferedReader in;
        String expr = "";
        try {
            reader = new FileReader(args[0]);
            in = new BufferedReader(reader);
            expr = in.readLine();
        }
        catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }


        String exprWithoutSpaces = skipWhiteSpace(expr);
        try {
            Node result = parseExp(exprWithoutSpaces);
            result.print();
        }
        catch (CalculateException e) {
            System.out.print("ERROR in ");
            int positionError = Integer.parseInt(e.getMessage());
            int count = 0;
            boolean flag = true;
            for (int i = 0; i < expr.length(); i++) {
                if (expr.charAt(i) != ' ') {
                    count++;
                    if (count == positionError + 1) {
                        System.out.print(i+1);
                        flag = false;
                    }
                }
            }
            if (flag) {
                System.out.print(expr.length()+1);
            }
            System.out.println(" position");
        }
    }
}
