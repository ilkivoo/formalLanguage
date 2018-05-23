package ru.spbau.mit.alyokhina.LexicalAnalyser;

public class KeyWord implements Token {
    private int line, beginPos, endPos;
    private String name;
    private static int maxLength = 0;
    private static String[] keyWords = {"if", "then", "else", "while", "do", "read", "write", "function", "begin", "end", "main", "elif"};

    static  {
        for (String keyWord : keyWords) {
            maxLength = (maxLength < keyWord.length()) ? keyWord.length() : maxLength;
        }
    }
    public KeyWord(int line, int beginPos, int endPos, String name) {
        this.line = line;
        this.beginPos = beginPos;
        this.endPos = endPos;
        this.name = name;

    }

    private static boolean isSeparator(char c) {
        return Colon.isColon(c) || Colon.isBlank(c) || Bracket.isBracket(c);
    }

    static String isKeyWord(String data, int pos) {
        int i = pos;
        StringBuilder sb = new StringBuilder();
        while (i < data.length() && !isSeparator(data.charAt(i))) {
            sb.append(data.charAt(i));
            if (sb.length() > maxLength) {
                return "";
            }
            i++;
        }
        String str = sb.toString();
        for (String keyWord : keyWords) {
            if (str.equals(keyWord)) {
                return keyWord;
            }
        }
        return "";
    }


    public void print() {
        System.out.print("KW_" + name + "(");
        System.out.print(line);
        System.out.print(", ");
        System.out.print(beginPos);
        System.out.print(", ");
        System.out.print(endPos);
        System.out.println(")");
    }

    public boolean equals(Object o) {
        if (o instanceof KeyWord) {
            KeyWord keyWord = (KeyWord) o;
            return line == keyWord.line && beginPos == keyWord.beginPos && endPos == keyWord.endPos && name.equals(keyWord.name);
        } else {
            return false;
        }
    }

    @Override
    public int getStart() {
        return beginPos;
    }

    @Override
    public int getLine() {
        return line;
    }

    @Override
    public int getEnd() {
        return endPos;
    }

    @Override
    public String getName() {
        return name;
    }

}
