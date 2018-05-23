package ru.spbau.mit.alyokhina.LexicalAnalyser;

public class Colon implements Token {
    private int line, beginPos, endPos;
    private char symbol;

    public Colon(int line, int beginPos, int endPos, char x) {
        this.line = line;
        this.beginPos = beginPos;
        this.endPos = endPos;
        symbol = x;
    }

    static boolean isColon(char c) {
        return c == ';' || c == ',';
    }

    static boolean isBlank(char c) {
        return c == ' ' || c == '\r' || c == '\n' || c == '\t' || c == '\f';
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
    public String getName() {
        return symbol == ';' ? ";" : ",";
    }

    @Override
    public int getLine() {
        return line;
    }
}
