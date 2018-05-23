package ru.spbau.mit.alyokhina.LexicalAnalyser;

public interface Token {
    void print();
    boolean equals(Object o);
    int getStart();
    int getLine();
    int getEnd();
    String getName();
}
