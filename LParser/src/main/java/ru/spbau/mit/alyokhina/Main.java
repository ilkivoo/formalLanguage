package ru.spbau.mit.alyokhina;


import ru.spbau.mit.alyokhina.LexicalAnalyser.LexerException;
import ru.spbau.mit.alyokhina.LexicalAnalyser.LexicalAnalyser;
import ru.spbau.mit.alyokhina.LexicalAnalyser.Token;
import ru.spbau.mit.alyokhina.Parser.Node;
import ru.spbau.mit.alyokhina.Parser.Parse;
import ru.spbau.mit.alyokhina.Parser.ParserException;

import java.io.File;
import java.io.FileWriter;
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
                ArrayList<Node> al = Parse.parse(arrayList);
                FileWriter fileWriter = new FileWriter("1.txt", false);
                for (Node node : al) {
                    node.print(fileWriter);
                    fileWriter.write("\n\n\n\n");
                    fileWriter.flush();
                    System.out.println("\n\n\n\n");
                }
            }
            catch (IOException | LexerException  | ParserException e) {
                System.out.println(e.getMessage());
            }
        }

    }
}
