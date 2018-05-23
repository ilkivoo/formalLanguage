package ru.spbau.mit.alyokhina.LexicalAnalyser;

public class Num implements Token {
    private int line, beginPos, endPos;
    private double num;
    private String stringNum;

    public Num(int line, int beginPos, int endPos, String num) {
        this.line = line;
        this.beginPos = beginPos;
        this.endPos = endPos;
        this.num = Double.parseDouble(num);
        stringNum = num;
    }

    @Override
    public String getName() {
        return stringNum;
    }

    private static boolean check(String data, int pos) {
        return !Operator.isOperator(data, pos).equals("") || Colon.isColon(data.charAt(pos)) || Bracket.isBracket(data.charAt(pos)) || Colon.isBlank(data.charAt(pos)) || !Ident.isIdent(data, pos).equals("");
    }

    private static boolean canBeInNum(char c, boolean flag0, boolean flag1, boolean flag2) {
        return c >= '0' && c <= '9' || (c == 'e' || c == 'E') && flag0 || (c == '-' || c == '+') && flag1 || c == '.' && flag2;
    }

    static String isNum(String data, int pos) {
        int i = pos;
        StringBuilder sb = new StringBuilder();

        if (i < data.length() && (data.charAt(i) == '-' || data.charAt(i) == '+')) {
            if (i + 1 < data.length() && ((data.charAt(i + 1) >= '0' && data.charAt(i + 1) <= '9') || data.charAt(i + 1) == '.')) {
                sb.append(data.charAt(i));
            } else
                return "";
            i++;
        }

        boolean flag0 = false, flag1 = false, flag2 = true, e = false;
        while (i < data.length() && canBeInNum(data.charAt(i), flag0, flag1, flag2)) {
            if (data.charAt(i) >= '0' && data.charAt(i) <= '9') {
                sb.append(data.charAt(i));
            }
            if (data.charAt(i) == '.') {
                flag2 = false;
                sb.append(data.charAt(i));
            }
            if (data.charAt(i) == 'e' || data.charAt(i) == 'E') {
                e = true;
                flag0 = false;
                flag1 = true;
                if (i + 1 < data.length() && ((data.charAt(i + 1) >= '0' && data.charAt(i + 1) <= '9') || data.charAt(i + 1) == '-' || data.charAt(i + 1) == '+')) {
                    sb.append(data.charAt(i));
                }
            }
            if (data.charAt(i) == '-' || data.charAt(i) == '+') {
                flag1 = false;
                if (i + 1 < data.length() && data.charAt(i + 1) >= '0' && data.charAt(i + 1) <= '9') {
                    sb.append(data.charAt(i));
                }
            }

            if (!e) {
                flag0 = true;
            }
            i++;
        }

        if (i < data.length() && check(data, i) || i == data.length()) {
            return sb.toString();
        } else {
            return "";
        }
    }

    public void print() {
        System.out.print("Num(");
        System.out.print(num);
        System.out.print(", ");
        System.out.print(line);
        System.out.print(", ");
        System.out.print(beginPos);
        System.out.print(", ");
        System.out.print(endPos);
        System.out.println(")");
    }

    public boolean equals(Object o) {
        if (o instanceof Num) {
            Num num1 = (Num) o;
            return line == num1.line && beginPos == num1.beginPos && endPos == num1.endPos && num == num1.num;
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

}
