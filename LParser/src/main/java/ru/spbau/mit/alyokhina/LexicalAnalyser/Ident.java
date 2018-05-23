package ru.spbau.mit.alyokhina.LexicalAnalyser;

public class Ident implements Token {
    private int line, beginPos, endPos;
    private String name;

    public Ident(int line, int beginPos, int endPos, String name) {
        this.line = line;
        this.beginPos = beginPos;
        this.endPos = endPos;
        this.name = name;
    }

    private static boolean check(char c) {
        return (c == '_' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9'));
    }


    static String isIdent(String data, int pos) {
        if (pos >= data.length()) {
            return "";
        } else {
            int i = pos;
            StringBuilder sb = new StringBuilder();
            if (data.charAt(i) == '_' || (data.charAt(i) >= 'a' && data.charAt(i) <= 'z') || (data.charAt(i) >= 'A' && data.charAt(i) <= 'Z')) {
                sb.append(data.charAt(i));
            } else {
                return "";
            }
            i++;
            while (i < data.length() && check(data.charAt(i))) {
                sb.append(data.charAt(i));
                i++;
            }
            if (i == data.length()) {
                return sb.toString();
            } else {
                if (Colon.isBlank(data.charAt(i)) || Colon.isColon(data.charAt(i)) || Bracket.isBracket(data.charAt(i)) || !Operator.isOperator(data, i).equals("")) {
                    return sb.toString();
                } else return "";
            }
        }
    }

    public void print() {
        System.out.print("Ident(");
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
        if (o instanceof Ident) {
            Ident ident = (Ident) o;
            return line == ident.line && beginPos == ident.beginPos && endPos == ident.endPos && name.equals(ident.name);
        } else {
            return false;
        }
    }

    @Override
    public int getStart() {
        return beginPos;
    }

    @Override
    public int getEnd() {
        return endPos;
    }

    @Override
    public int getLine() {
        return line;
    }

    @Override
    public String getName() {
        return name;
    }
}
