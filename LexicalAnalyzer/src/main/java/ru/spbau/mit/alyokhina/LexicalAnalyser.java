package ru.spbau.mit.alyokhina;

import java.util.ArrayList;

public class LexicalAnalyser {
    private String data;
    private int curPos, curLine, size;
    private ArrayList<Token> result;

    public LexicalAnalyser(String data) {
        this.data = data;
        curPos = 0;
        curLine = 0;
        size = 0;
    }

    private void skipComments() {
        if (size + 1 < data.length()) {
            if (data.charAt(size) == '/' && data.charAt(size + 1) == '/') {
                size += 2;
                curPos += 2;
                while (size < data.length() && (data.charAt(size) != '\n' && data.charAt(size) != '\r')) {
                    size++;
                    curPos++;
                }
                skipWhiteSpace();
            }
        }
    }

    public ArrayList<Token> getTokens() throws LexerException {
        if (result != null) {
            return result;
        }
        result = new ArrayList<>();
        skipWhiteSpace();
        skipComments();
        while (size < data.length()) {
            if (Colon.isColon(data.charAt(size))) {
                Colon colon = new Colon(curLine, curPos, curPos);
                add(colon, 1);
            } else {
                if (Bracket.isBracket(data.charAt(size))) {
                    Bracket bracket = new Bracket(curLine, curPos, curPos, data.charAt(size));
                    add(bracket, 1);
                } else {
                    String str = KeyWord.isKeyWord(data, size);
                    if (!str.equals("")) {
                        KeyWord keyWord = new KeyWord(curLine, curPos, curPos + str.length() - 1, str);
                        add(keyWord, str.length());
                    } else {
                        str = Literal.isLiteral(data, size);
                        if (!str.equals("")) {
                            Literal literal = new Literal(curLine, curPos, curPos + str.length() - 1, str);
                            add(literal, str.length());
                        } else {
                            str = Operator.isOperator(data, size);
                            if (!str.equals("")) {
                                Operator operator = new Operator(curLine, curPos, curPos + str.length() - 1, str);
                                add(operator, str.length());
                            } else {
                                str = Num.isNum(data, size);
                                if (!str.equals("")) {
                                    Num num = new Num(curLine, curPos, curPos + str.length() - 1, str);
                                    add(num, str.length());
                                } else {
                                    str = Ident.isIdent(data, size);
                                    if (!str.equals("")) {
                                        Ident ident = new Ident(curLine, curPos, curPos + str.length() - 1, str);
                                        add(ident, str.length());
                                    } else {
                                        throw new LexerException("Error in line: " + ((Integer) curLine).toString()
                                                + " position: " + ((Integer) curPos).toString());
                                    }
                                }

                            }

                        }
                    }
                }
            }
            skipWhiteSpace();
            skipComments();
        }
        return result;
    }


    private void add(Token token, int len) {
        result.add(token);
        curPos += len;
        size += len;
    }


    private void skipWhiteSpace() {
        boolean flag = false;
        while (size < data.length() && Colon.isBlank(data.charAt(size))) {
            if (data.charAt(size) == '\r') {
                curLine++;
                curPos = 0;
                size++;
                flag = true;
            } else {
                if (data.charAt(size) == '\n') {
                    if (!flag) {
                        curLine++;
                    }
                    curPos = 0;
                    size++;
                } else {
                    curPos++;
                    size++;
                    flag = false;
                }

            }
        }
    }


}
