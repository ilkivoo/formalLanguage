package ru.spbau.mit.alyokhina.Parser;

import org.junit.Test;
import ru.spbau.mit.alyokhina.LexicalAnalyser.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ParseTest {
    private static String readUsingFiles(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    @Test
    public void test1() throws Exception {
        String data = readUsingFiles("src/main/resources/test1/test1.L");
        LexicalAnalyser lexicalAnalyser = new LexicalAnalyser(data);
        ArrayList<Token> tokens= lexicalAnalyser.getTokens();
        ArrayList<Node> trees = Parse.parse(tokens);
        FileWriter fileWriter = new FileWriter("src/main/resources/test1/test1.out", false);
        for (Node tree:trees) {
            tree.print(fileWriter);
        }

        assertEquals(readUsingFiles("src/main/resources/test1/rightAnswerForTest1"), readUsingFiles("src/main/resources/test1/test1.out"));
    }

    @Test
    public void test2() throws Exception {
        String data = readUsingFiles("src/main/resources/test2/test2.L");
        LexicalAnalyser lexicalAnalyser = new LexicalAnalyser(data);
        ArrayList<Token> tokens= lexicalAnalyser.getTokens();
        ArrayList<Node> trees = Parse.parse(tokens);
        FileWriter fileWriter = new FileWriter("src/main/resources/test2/test2.out", false);
        for (Node tree:trees) {
            tree.print(fileWriter);
        }

        assertEquals(readUsingFiles("src/main/resources/test2/rightAnswerForTest2"), readUsingFiles("src/main/resources/test2/test2.out"));
    }

    @Test
    public void test3() throws Exception {
        String data = readUsingFiles("src/main/resources/test3/test3.L");
        LexicalAnalyser lexicalAnalyser = new LexicalAnalyser(data);
        ArrayList<Token> tokens= lexicalAnalyser.getTokens();
        ArrayList<Node> trees = Parse.parse(tokens);
        FileWriter fileWriter = new FileWriter("src/main/resources/test3/test3.out", false);
        for (Node tree:trees) {
            tree.print(fileWriter);
        }

        assertEquals(readUsingFiles("src/main/resources/test3/rightAnswerForTest3"), readUsingFiles("src/main/resources/test3/test3.out"));
    }


    @Test
    public void test4() throws Exception {
        String data = readUsingFiles("src/main/resources/test4/test4.L");
        LexicalAnalyser lexicalAnalyser = new LexicalAnalyser(data);
        ArrayList<Token> tokens= lexicalAnalyser.getTokens();
        ArrayList<Node> trees = Parse.parse(tokens);
        FileWriter fileWriter = new FileWriter("src/main/resources/test4/test4.out", false);
        for (Node tree:trees) {
            tree.print(fileWriter);
        }

        assertEquals(readUsingFiles("src/main/resources/test4/rightAnswerForTest4"), readUsingFiles("src/main/resources/test4/test4.out"));
    }

    @Test
    public void test5() throws Exception {
        String data = readUsingFiles("src/main/resources/test5/test5.L");
        LexicalAnalyser lexicalAnalyser = new LexicalAnalyser(data);
        ArrayList<Token> tokens= lexicalAnalyser.getTokens();
        ArrayList<Node> trees = Parse.parse(tokens);
        FileWriter fileWriter = new FileWriter("src/main/resources/test5/test5.out", false);
        for (Node tree:trees) {
            tree.print(fileWriter);
        }

        assertEquals(readUsingFiles("src/main/resources/test5/rightAnswerForTest5"), readUsingFiles("src/main/resources/test5/test5.out"));
    }

    @Test
    public void test6() throws Exception {
        String data = readUsingFiles("src/main/resources/test6/test6.L");
        LexicalAnalyser lexicalAnalyser = new LexicalAnalyser(data);
        ArrayList<Token> tokens= lexicalAnalyser.getTokens();
        ArrayList<Node> trees = Parse.parse(tokens);
        FileWriter fileWriter = new FileWriter("src/main/resources/test6/test6.out", false);
        for (Node tree:trees) {
           tree.print(fileWriter);
        }

        assertEquals(readUsingFiles("src/main/resources/test6/rightAnswerForTest6"), readUsingFiles("src/main/resources/test6/test6.out"));
    }

    @Test
    public void test7() throws Exception {
        String data = readUsingFiles("src/main/resources/test7/test7.L");
        LexicalAnalyser lexicalAnalyser = new LexicalAnalyser(data);
        ArrayList<Token> tokens= lexicalAnalyser.getTokens();
        ArrayList<Node> trees = Parse.parse(tokens);
        FileWriter fileWriter = new FileWriter("src/main/resources/test7/test7.out", false);
        for (Node tree:trees) {
            tree.print(fileWriter);
        }

        assertEquals(readUsingFiles("src/main/resources/test7/rightAnswerForTest7"), readUsingFiles("src/main/resources/test7/test7.out"));
    }

    /* With sugar */
    @Test
    public void test8() throws Exception {
        String data = readUsingFiles("src/main/resources/test8/test8.L");
        LexicalAnalyser lexicalAnalyser = new LexicalAnalyser(data);
        ArrayList<Token> tokens= lexicalAnalyser.getTokens();
        ArrayList<Node> trees = Parse.parse(tokens);
        FileWriter fileWriter = new FileWriter("src/main/resources/test8/test8.out", false);
        for (Node tree:trees) {
            tree.print(fileWriter);
        }

        assertEquals(readUsingFiles("src/main/resources/test8/rightAnswerForTest8"), readUsingFiles("src/main/resources/test8/test8.out"));
    }

    /* With sugar */
    @Test
    public void test9() throws Exception {
        String data = readUsingFiles("src/main/resources/test9/test9.L");
        LexicalAnalyser lexicalAnalyser = new LexicalAnalyser(data);
        ArrayList<Token> tokens= lexicalAnalyser.getTokens();
        ArrayList<Node> trees = Parse.parse(tokens);
        FileWriter fileWriter = new FileWriter("src/main/resources/test9/test9.out", false);
        for (Node tree:trees) {
            tree.print(fileWriter);
        }

        assertEquals(readUsingFiles("src/main/resources/test9/rightAnswerForTest9"), readUsingFiles("src/main/resources/test9/test9.out"));
    }

    /* With sugar */
    @Test
    public void test10() throws Exception {
        String data = readUsingFiles("src/main/resources/test10/test10.L");
        LexicalAnalyser lexicalAnalyser = new LexicalAnalyser(data);
        ArrayList<Token> tokens= lexicalAnalyser.getTokens();
        ArrayList<Node> trees = Parse.parse(tokens);
        FileWriter fileWriter = new FileWriter("src/main/resources/test10/test10.out", false);
        for (Node tree:trees) {
            tree.print(fileWriter);
        }

        assertEquals(readUsingFiles("src/main/resources/test10/rightAnswerForTest10"), readUsingFiles("src/main/resources/test10/test10.out"));
    }
    @Test
    public void test11() throws Exception {
        String data = readUsingFiles("src/main/resources/example.in");
        LexicalAnalyser lexicalAnalyser = new LexicalAnalyser(data);
        ArrayList<Token> tokens= lexicalAnalyser.getTokens();
        ArrayList<Node> trees = Parse.parse(tokens);
        FileWriter fileWriter = new FileWriter("src/main/resources/example.out", false);
        for (Node tree:trees) {
            tree.print(fileWriter);
        }

    }
}