package ru.spbau.mit.alyokhina;

public class Operator implements Token {
    private int line, beginPos, endPos;
    private String name;

    public Operator(int line, int beginPos, int endPos, String name) {
        this.line = line;
        this.beginPos = beginPos;
        this.endPos = endPos;
        this.name = name;
    }




    public static String isOperator(String data, int pos) {
        if (pos +1 >= data.length()) {
            return "";
        }
        if (pos < data.length()) {
            char c1 = data.charAt(pos);
            if (c1 == '+' || c1 == '-' || c1 == '*' || c1 == '/' || c1 == '%' || c1 == '>' || c1 == '<') {
                boolean flag = !Ident.isIdent(data, pos+1).equals("");
                flag = flag || Colon.isColon(data.charAt(pos+1));
                flag = flag || Colon.isBlank(data.charAt(pos+1));
                flag = flag || !Num.isNum(data, pos+1).equals("");
                if (flag) {
                    return "" + c1;
                }
            }

            if (pos + 2 >= data.length()) {
                return "";
            }
            char c2 = data.charAt(pos + 1);
            String str = "" + c1 + c2;
            if ( str.equals("==") ||  str.equals("!=") || str.equals(">=") ||
                    str.equals("<=") || str.equals("&&") || str.equals("||") ) {
                boolean flag = !Ident.isIdent(data, pos+2).equals("");
                flag = flag || Colon.isColon(data.charAt(pos+2));
                flag = flag || Colon.isBlank(data.charAt(pos+2));
                flag = flag || !Num.isNum(data, pos+2).equals("");
                if (flag) {
                    return str;
                }
                else {
                    return "";
                }
            }
            else {
                return "";
            }
        }
        return "";
    }


    public void print() {
        System.out.print("Op(");
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
        if (o instanceof Operator) {
            Operator operator = (Operator) o;
            return line == operator.line && beginPos == operator.beginPos && endPos == operator.endPos && name.equals(operator.name);
        }
        else {
            return false;
        }
    }
}
