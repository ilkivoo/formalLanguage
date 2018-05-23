package ru.spbau.mit.alyokhina.LexicalAnalyser;

public class Bracket implements Token {
    private int line, beginPos, endPos;
    private String type;
    private char symbol;
    Bracket(int line, int beginPos, int endPos, char c) {
        this.line = line;
        this.beginPos = beginPos;
        this.endPos = endPos;
        this.symbol = c;
        type = (c == '(')? "LeftParenthesis":"RightParenthesis";
    }

    static boolean isBracket(char c) {
        return c == '(' || c == ')';
    }

    public void print() {
        System.out.print(type + "(");
        System.out.print(line);
        System.out.print(", ");
        System.out.print(beginPos);
        System.out.print(", ");
        System.out.print(endPos);
        System.out.println(")");
    }

    public boolean equals(Object o) {
        if (o instanceof Bracket) {
            Bracket bracket = (Bracket) o;
            return line == bracket.line && beginPos == bracket.beginPos && endPos == bracket.endPos && type.equals(bracket.type);
        }
        else {
            return false;
        }
    }

    public String getName() {
        return symbol == '(' ? "(" : ")";
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
}
