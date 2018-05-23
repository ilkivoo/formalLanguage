package ru.spbau.mit.alyokhina.Parser;


import java.io.*;
import java.util.ArrayList;

public class Node {
    private ArrayList<Node> children;
    private String name;

    Node(ArrayList<Node> children, String name) {
        this.children = children;
        this.name = name;
    }


    public void print(FileWriter fw) throws IOException {
        print("", true, fw);
    }

    private void print(String prefix, boolean isTail, FileWriter fw) throws IOException {
        fw.write(prefix + (isTail ? "└── " : "├── "));
        fw.write(name);
        fw.write("\n");
        fw.flush();
        //System.out.print(prefix + (isTail ? "└── " : "├── "));
        //System.out.println(name);
        for (Node child : children) {
            child.print(prefix + (isTail ? "    " : "│   "), false, fw);
        }
    }

    ArrayList<Node> getChildren() {
        return children;
    }
}
