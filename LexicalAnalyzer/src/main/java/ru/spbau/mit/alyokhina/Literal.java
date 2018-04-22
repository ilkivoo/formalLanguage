package ru.spbau.mit.alyokhina;

public class Literal implements Token {
    private int line, beginPos, endPos;
    private String name;
    private static String[] literals = {"true", "false"};

    public Literal(int line, int beginPos, int endPos, String name) {
        this.line = line;
        this.beginPos = beginPos;
        this.endPos = endPos;
        this.name = name;
    }


    private static boolean isSeparator(char c) {
       return Colon.isColon(c) || Colon.isBlank(c);
    }

    public static String isLiteral(String data, int pos) {
        int i = pos;
        StringBuilder sb = new StringBuilder();
        while(i < data.length() && !isSeparator(data.charAt(i))) {
            sb.append(data.charAt(i));
            if (sb.length() > 5) {
                return "";
            }
            i++;
        }
        String str = sb.toString();
        for (String literal : literals) {
            if (str.equals(literal)) {
                return literal;
            }
        }
        return "";
    }

    public void print() {
        System.out.print("Literal(");
        System.out.print(name);
        System.out.print(", ");
        System.out.print(line);
        System.out.print(", ");
        System.out.print(beginPos);
        System.out.print(", ");
        System.out.print(endPos);
        System.out.println(")");
    }

    public boolean equals(Object o) {
        if (o instanceof Literal) {
            Literal literal = (Literal) o;
            return line == literal.line && beginPos == literal.beginPos && endPos == literal.endPos && name.equals(literal.name);
        }
        else {
            return false;
        }
    }
}
