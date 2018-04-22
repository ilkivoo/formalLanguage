package ru.spbau.mit.alyokhina;

import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class LexicalAnalizatorTest {
    @Test
    public void test1() throws Exception {
        LexicalAnalizator la = new LexicalAnalizator("read x; if y + 1 == x then write y else write x");
        ArrayList<Token> tokens = new ArrayList<Token>();
        tokens.add(new KeyWord(0, 0, 3, "read"));
        tokens.add(new Ident(0, 5, 5, "x"));
        tokens.add(new Colon(0, 6, 6, ';'));
        tokens.add(new KeyWord(0, 8, 9, "if"));
        tokens.add(new Ident(0, 11, 11, "y"));
        tokens.add(new Operator(0, 13, 13, "+"));
        tokens.add(new Num(0, 15, 15, "1"));
        tokens.add(new Operator(0, 17, 18, "=="));
        tokens.add(new Ident(0, 20, 20, "x"));
        tokens.add(new KeyWord(0, 22, 25, "then"));
        tokens.add(new KeyWord(0, 27, 31, "write"));
        tokens.add(new Ident(0, 33, 33, "y"));
        tokens.add(new KeyWord(0, 35, 38, "else"));
        tokens.add(new KeyWord(0, 40, 44, "write"));
        tokens.add(new Ident(0, 46, 46, "x"));
        assertEquals(tokens, la.getTokens());
    }

    @Test
    public void test2() throws Exception {
        LexicalAnalizator la = new LexicalAnalizator("\nread x; if y + 1 == x then write y else write x");
        ArrayList<Token> tokens = new ArrayList<Token>();
        tokens.add(new KeyWord(1, 0, 3, "read"));
        tokens.add(new Ident(1, 5, 5, "x"));
        tokens.add(new Colon(1, 6, 6, ';'));
        tokens.add(new KeyWord(1, 8, 9, "if"));
        tokens.add(new Ident(1, 11, 11, "y"));
        tokens.add(new Operator(1, 13, 13, "+"));
        tokens.add(new Num(1, 15, 15, "1"));
        tokens.add(new Operator(1, 17, 18, "=="));
        tokens.add(new Ident(1, 20, 20, "x"));
        tokens.add(new KeyWord(1, 22, 25, "then"));
        tokens.add(new KeyWord(1, 27, 31, "write"));
        tokens.add(new Ident(1, 33, 33, "y"));
        tokens.add(new KeyWord(1, 35, 38, "else"));
        tokens.add(new KeyWord(1, 40, 44, "write"));
        tokens.add(new Ident(1, 46, 46, "x"));
        assertEquals(tokens, la.getTokens());
    }



    @Test
    public void test3() throws Exception {
        LexicalAnalizator la = new LexicalAnalizator("\nread x;\n if y + 1 == x \nthen write y else write x");
        ArrayList<Token> tokens = new ArrayList<Token>();
        tokens.add(new KeyWord(1, 0, 3, "read"));
        tokens.add(new Ident(1, 5, 5, "x"));
        tokens.add(new Colon(1, 6, 6, ';'));

        tokens.add(new KeyWord(2, 1, 2, "if"));
        tokens.add(new Ident(2, 4, 4, "y"));
        tokens.add(new Operator(2, 6, 6, "+"));
        tokens.add(new Num(2, 8, 8, "1"));
        tokens.add(new Operator(2, 10, 11, "=="));
        tokens.add(new Ident(2, 13, 13, "x"));



        tokens.add(new KeyWord(3, 0, 3, "then"));
        tokens.add(new KeyWord(3, 5, 9, "write"));
        tokens.add(new Ident(3, 11, 11, "y"));
        tokens.add(new KeyWord(3, 13, 16, "else"));
        tokens.add(new KeyWord(3, 18, 22, "write"));
        tokens.add(new Ident(3, 24, 24, "x"));
        assertEquals(tokens, la.getTokens());
    }



    @Test
    public void test4() throws Exception {
        LexicalAnalizator la = new LexicalAnalizator("if(x==1)then\r\narar");
        ArrayList<Token> tokens = new ArrayList<Token>();
        tokens.add(new KeyWord(0, 0, 1, "if"));
        tokens.add(new Colon(0, 2, 2, '('));
        tokens.add(new Ident(0, 3, 3, "x"));
        tokens.add(new Operator(0, 4, 5, "=="));
        tokens.add(new Num(0, 6, 6, "1"));
        tokens.add(new Colon(0, 7, 7, ')'));
        tokens.add(new KeyWord(0, 8, 11, "then"));
        tokens.add(new Ident(1, 0, 3, "arar"));
        assertEquals(tokens, la.getTokens());
    }

    @Test
    public void test5() throws Exception {
        LexicalAnalizator la = new LexicalAnalizator("read x; if y + 1 == x\fthen write\fy else write x");
        ArrayList<Token> tokens = new ArrayList<Token>();
        tokens.add(new KeyWord(0, 0, 3, "read"));
        tokens.add(new Ident(0, 5, 5, "x"));
        tokens.add(new Colon(0, 6, 6, ';'));
        tokens.add(new KeyWord(0, 8, 9, "if"));
        tokens.add(new Ident(0, 11, 11, "y"));
        tokens.add(new Operator(0, 13, 13, "+"));
        tokens.add(new Num(0, 15, 15, "1"));
        tokens.add(new Operator(0, 17, 18, "=="));
        tokens.add(new Ident(0, 20, 20, "x"));
        tokens.add(new KeyWord(0, 22, 25, "then"));
        tokens.add(new KeyWord(0, 27, 31, "write"));
        tokens.add(new Ident(0, 33, 33, "y"));
        tokens.add(new KeyWord(0, 35, 38, "else"));
        tokens.add(new KeyWord(0, 40, 44, "write"));
        tokens.add(new Ident(0, 46, 46, "x"));
        assertEquals(tokens, la.getTokens());
    }


    @Test
    public void test6() throws Exception {
        LexicalAnalizator la = new LexicalAnalizator("read x; if y + 1 == x\fthen write\fy else write +1.e-10");
        ArrayList<Token> tokens = new ArrayList<Token>();
        tokens.add(new KeyWord(0, 0, 3, "read"));
        tokens.add(new Ident(0, 5, 5, "x"));
        tokens.add(new Colon(0, 6, 6, ';'));
        tokens.add(new KeyWord(0, 8, 9, "if"));
        tokens.add(new Ident(0, 11, 11, "y"));
        tokens.add(new Operator(0, 13, 13, "+"));
        tokens.add(new Num(0, 15, 15, "1"));
        tokens.add(new Operator(0, 17, 18, "=="));
        tokens.add(new Ident(0, 20, 20, "x"));
        tokens.add(new KeyWord(0, 22, 25, "then"));
        tokens.add(new KeyWord(0, 27, 31, "write"));
        tokens.add(new Ident(0, 33, 33, "y"));
        tokens.add(new KeyWord(0, 35, 38, "else"));
        tokens.add(new KeyWord(0, 40, 44, "write"));
        tokens.add(new Operator(0, 46, 46, "+"));
        tokens.add(new Num(0, 47, 52, "1.e-10"));
        assertEquals(tokens, la.getTokens());
    }

    @Test
    public void test7() throws Exception {
        LexicalAnalizator la = new LexicalAnalizator("read x; if y + 1 == x\fthen write\fy else write +1.E-10");
        ArrayList<Token> tokens = new ArrayList<Token>();
        tokens.add(new KeyWord(0, 0, 3, "read"));
        tokens.add(new Ident(0, 5, 5, "x"));
        tokens.add(new Colon(0, 6, 6, ';'));
        tokens.add(new KeyWord(0, 8, 9, "if"));
        tokens.add(new Ident(0, 11, 11, "y"));
        tokens.add(new Operator(0, 13, 13, "+"));
        tokens.add(new Num(0, 15, 15, "1"));
        tokens.add(new Operator(0, 17, 18, "=="));
        tokens.add(new Ident(0, 20, 20, "x"));
        tokens.add(new KeyWord(0, 22, 25, "then"));
        tokens.add(new KeyWord(0, 27, 31, "write"));
        tokens.add(new Ident(0, 33, 33, "y"));
        tokens.add(new KeyWord(0, 35, 38, "else"));
        tokens.add(new KeyWord(0, 40, 44, "write"));
        tokens.add(new Operator(0, 46, 46, "+"));
        tokens.add(new Num(0, 47, 52, "1.e-10"));
        assertEquals(tokens, la.getTokens());
    }

    @Test
    public void test8() throws Exception {
        String str = Num.isNum("-12.13e-13asdadadad", 0);
        Double d = -12.13e-13;
        Double d1 = Double.parseDouble(str);
        assertEquals(d, d1);
    }


    @Test
    public void test9() throws Exception {
        String str = Ident.isIdent("a_12351235", 0);
        assertEquals("a_12351235", str);
    }


    @Test
    public void test10() throws Exception {
        String str = KeyWord.isKeyWord("a_12351235", 0);
        assertEquals("", str);
    }

    @Test
    public void test11() throws Exception {
        String str = KeyWord.isKeyWord("if", 0);
        assertEquals("if", str);
    }

    @Test
    public void test12() throws Exception {
        String str = KeyWord.isKeyWord("then123", 0);
        assertEquals("", str);
    }

    @Test
    public void test13() throws Exception {
        String str = Literal.isLiteral("true e41213", 0);
        assertEquals("true", str);
    }

}