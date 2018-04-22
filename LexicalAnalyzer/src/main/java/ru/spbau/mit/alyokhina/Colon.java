package ru.spbau.mit.alyokhina;

public class Colon implements Token {
    private int line, beginPos, endPos;
    public Colon(int line, int beginPos, int endPos) {
        this.line = line;
        this.beginPos = beginPos;
        this.endPos = endPos;
    }

    public static boolean isColon(char c) {
        return c == '(' || c == ')' || c == ';' || c == ',';
    }
    public static boolean isBlank (char c) {
        return c == ' ' || c == '\r' || c == '\n' ||  c == '\t' || c == '\f';
    }
    public void print() {
        System.out.print("Colon(");
        System.out.print(line);
        System.out.print(", ");
        System.out.print(beginPos);
        System.out.print(", ");
        System.out.print(endPos);
        System.out.println(")");
    }

    public boolean equals(Object o) {
        if (o instanceof Colon) {
            Colon colon = (Colon) o;
            return line == colon.line && beginPos == colon.beginPos && endPos == colon.endPos;
        }
        else {
            return false;
        }
    }
}
