package ru.spbau.mit.alyokhina;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {
    private static String readUsingFiles(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }
    public static void main(String[] argv) {
        if (argv.length == 0) {
            System.out.println("Expected path to file");
        }
        else {
            try {
                String data = readUsingFiles(argv[0]);
                LexicalAnalyser lexicalAnalyser = new LexicalAnalyser(data);
                ArrayList<Token> arrayList = lexicalAnalyser.getTokens();
                for (Token token : arrayList) {
                    token.print();
                }
            }
            catch (IOException | LexerException e) {
                System.out.println(e.getMessage());
            }
        }

    }
}
