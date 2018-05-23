package ru.spbau.mit.alyokhina.Parser;

import ru.spbau.mit.alyokhina.LexicalAnalyser.*;

import java.util.ArrayList;

public class Parse {

    public static ArrayList<Node> parse(ArrayList<Token> tokens) throws ParserException {

        /* Add sugar */
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i) instanceof KeyWord && tokens.get(i).getName().equals("elif")) {
                Token token = tokens.get(i);
                KeyWord iff = new KeyWord(token.getLine(), token.getStart(), token.getEnd(), "if");
                KeyWord elsee = new KeyWord(token.getLine(), token.getStart(), token.getEnd(), "else");
                tokens.remove(i);
                tokens.add(i, elsee);
                tokens.add(i + 1, iff);
                System.out.println();
            }


            if ((i < tokens.size() - 1) && (tokens.get(i) instanceof Operator && tokens.get(i).getName().equals("+"))
                    && (tokens.get(i + 1) instanceof Operator && tokens.get(i + 1).getName().equals("+"))) {
                if (i > 0 && tokens.get(i - 1) instanceof Ident) {
                    Token token = tokens.get(i);
                    Operator assignment = new Operator(token.getLine(), token.getStart(), token.getEnd(), ":=");
                    Ident ident = new Ident(token.getLine(), token.getStart(), token.getEnd(), tokens.get(i - 1).getName());
                    Operator plus = new Operator(token.getLine(), token.getStart(), token.getEnd(), "+");
                    Num one = new Num(token.getLine(), token.getStart(), token.getEnd(), "1");
                    tokens.remove(i);
                    tokens.remove(i);
                    tokens.add(i, assignment);
                    tokens.add(i + 1, ident);
                    tokens.add(i + 2, plus);
                    tokens.add(i + 3, one);
                }
            }

            if (tokens.get(i) instanceof KeyWord && tokens.get(i).getName().equals("read")) {
                boolean flag = false;
                int j = i + 1;
                int end = -1;
                while (j < tokens.size() && !tokens.get(j).getName().equals(";")) {
                    if (tokens.get(j) instanceof Colon && tokens.get(j).getName().equals(",")) {
                        Colon colon = new Colon(tokens.get(j).getLine(), tokens.get(j).getStart(), tokens.get(j).getEnd(), ';');
                        KeyWord keyWord = new KeyWord(tokens.get(j).getLine(), tokens.get(j).getStart(), tokens.get(j).getEnd(), "read");
                        tokens.remove(j);
                        tokens.add(j, colon);
                        tokens.add(j + 1, keyWord);
                        flag = true;
                    }
                    j += 1;
                }
                if (j < tokens.size()) {
                    end = j;
                }

                if (flag && end != -1) {
                    KeyWord begKeyWord = new KeyWord(tokens.get(i).getLine(), tokens.get(i).getStart(), tokens.get(i).getEnd(), "begin");
                    KeyWord endKeyWord = new KeyWord(tokens.get(end).getLine(), tokens.get(end).getStart(), tokens.get(end).getEnd(), "end");

                    tokens.add(end + 1, endKeyWord);
                    tokens.add(i, begKeyWord);

                }

            }


        }
        if (tokens.size() == 0) {
            throw new ArithmeticException("");
        }
        ArrayList<Node> result = new ArrayList<>();
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i) instanceof KeyWord) {
                KeyWord keyWord = (KeyWord) tokens.get(i);
                if (keyWord.getName().equals("function") || keyWord.getName().equals("main")) {
                    ArrayList<Token> arrayList = new ArrayList<>();
                    int balance = 0;
                    boolean flag = true, wasBegin = false;
                    for (int j = i + 1; j < tokens.size() && flag; j++) {
                        arrayList.add(tokens.get(j));
                        if (tokens.get(j) instanceof KeyWord) {
                            KeyWord thisKeyWord = (KeyWord) tokens.get(j);
                            if (thisKeyWord.getName().equals("end")) {
                                balance--;
                            }
                            if (thisKeyWord.getName().equals("begin")) {
                                balance++;
                                wasBegin = true;
                            }
                        }
                        if (balance == 0 && wasBegin) {
                            flag = false;
                            i = j;
                        }
                    }
                    if (balance != 0 || !wasBegin) {
                        throw new ParserException("Error in line: " + ((Integer) tokens.get(i).getLine()).toString()
                                + " position: " + ((Integer) tokens.get(i).getStart()).toString());
                    }
                    if (keyWord.getName().equals("function")) {
                        result.add(parseFunction(arrayList));
                    } else {
                        result.add(parseMain(arrayList));
                    }
                }
            } else {
                throw new ParserException("Error in line: " + ((Integer) tokens.get(i).getLine()).toString()
                        + " position: " + ((Integer) tokens.get(i).getStart()).toString());
            }
        }
        return result;
    }


    private static Node parseFunction(ArrayList<Token> tokens) throws ParserException {
        if (tokens.size() < 5) {
            throw new ParserException("Error in line: " + ((Integer) tokens.get(0).getLine()).toString()
                    + " position: " + ((Integer) tokens.get(0).getStart()).toString());
        }
        if (!(tokens.get(0) instanceof Ident)) {
            throw new ParserException("Error in line: " + ((Integer) tokens.get(0).getLine()).toString()
                    + " position: " + ((Integer) tokens.get(0).getStart()).toString());
        }
        Ident functionName = (Ident) tokens.get(0);
        Node result = new Node(new ArrayList<Node>(), functionName.getName());

        if (!(tokens.get(1) instanceof Bracket) || (!tokens.get(1).getName().equals("("))) {
            throw new ParserException("Error in line: " + ((Integer) tokens.get(1).getLine()).toString()
                    + " position: " + ((Integer) tokens.get(1).getStart()).toString());
        }


        int i = 2;
        StringBuilder stringBuilder = new StringBuilder();
        while (i < tokens.size() && !(tokens.get(i) instanceof Bracket)) {
            if (tokens.get(i) instanceof Ident) {
                stringBuilder.append(tokens.get(i).getName()).append(",");
                if (i + 1 < tokens.size() && (tokens.get(i + 1) instanceof Colon)) {
                    i += 2;
                } else if (i + 1 < tokens.size() && (tokens.get(i + 1) instanceof Bracket)) {
                    i += 1;
                } else {
                    throw new ParserException("Error in line: " + ((Integer) tokens.get(i).getLine()).toString()
                            + " position: " + ((Integer) tokens.get(i).getStart()).toString());
                }
            }
        }

        if (i + 1 >= tokens.size()) {
            throw new ParserException("Error in line: " + ((Integer) tokens.get(tokens.size() - 1).getLine()).toString()
                    + " position: " + ((Integer) tokens.get(tokens.size() - 1).getEnd()).toString());
        }

        if (!tokens.get(i).getName().equals(")")) {
            throw new ParserException("Error in line: " + ((Integer) tokens.get(i).getLine()).toString()
                    + " position: " + ((Integer) tokens.get(i).getStart()).toString());
        }

        if (!(tokens.get(i + 1) instanceof KeyWord) ||
                !(tokens.get(i + 1).getName().equals("begin"))) {
            throw new ParserException("Error in line: " + ((Integer) tokens.get(i + 1).getLine()).toString()
                    + " position: " + ((Integer) tokens.get(i + 1).getStart()).toString());
        }

        for (int j = 0; j <= i + 1; j++) {
            tokens.remove(0);
        }
        tokens.remove(tokens.size() - 1);
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        result.getChildren().add(new Node(new ArrayList<Node>(), stringBuilder.toString()));
        result.getChildren().add(parseBody(tokens, "body function"));
        return result;
    }

    private static Node parseMain(ArrayList<Token> tokens) throws ParserException {
        if (!(tokens.get(0) instanceof KeyWord)) {
            throw new ParserException("Error in line: " + ((Integer) tokens.get(0).getLine()).toString()
                    + " position: " + ((Integer) tokens.get(0).getStart()).toString());
        }
        if (!(tokens.get(tokens.size() - 1) instanceof KeyWord)) {
            throw new ParserException("Error in line: " + ((Integer) tokens.get(tokens.size() - 1).getLine()).toString()
                    + " position: " + ((Integer) tokens.get(tokens.size() - 1).getStart()).toString());
        }
        KeyWord begin = (KeyWord) tokens.get(0);
        KeyWord end = (KeyWord) tokens.get(tokens.size() - 1);
        if (!begin.getName().equals("begin")) {
            throw new ParserException("Error in line: " + ((Integer) tokens.get(0).getLine()).toString()
                    + " position: " + ((Integer) tokens.get(0).getStart()).toString());
        }

        if (!end.getName().equals("end")) {
            throw new ParserException("Error in line: " + ((Integer) tokens.get(tokens.size() - 1).getLine()).toString()
                    + " position: " + ((Integer) tokens.get(tokens.size() - 1).getStart()).toString());
        }
        tokens.remove(0);
        tokens.remove(tokens.size() - 1);
        return parseBody(tokens, "main");
    }

    private static ArrayList<Token> findBody(ArrayList<Token> tokens, int begin, String str) throws ParserException {
        int i = begin;
        ArrayList<Token> result = new ArrayList<>();
        String endd;
        if (i < tokens.size() && tokens.get(i).getName().equals("begin")) {
            endd = "end";
        } else {
            endd = ";";
        }
        endd = str.equals("") ? endd : str;

        int balance = 0;
        result.add(tokens.get(i));
        i++;
        while (i < tokens.size() && !(balance == 0 && tokens.get(i).getName().equals(endd))) {
            if (tokens.get(i).getName().equals("begin")) {
                balance++;
            }
            if (tokens.get(i).getName().equals("end")) {
                balance--;
            }
            if (balance < 0) {
                throw new ParserException("Error in line: " + ((Integer) tokens.get(tokens.size() - 1).getLine()).toString()
                        + " position: " + ((Integer) tokens.get(tokens.size() - 1).getStart()).toString() + " нарушен баланс операторных скобок");
            }
            result.add(tokens.get(i));
            i++;
        }
        if (balance != 0 || i == tokens.size()) {
            throw new ParserException("Error in line: " + ((Integer) tokens.get(tokens.size() - 1).getLine()).toString()
                    + " position: " + ((Integer) tokens.get(tokens.size() - 1).getStart()).toString() + " нарушен баланс операторных скобок");
        }
        result.add(tokens.get(i));
        return result;
    }

    private static Node parseBody(ArrayList<Token> tokens, String name) throws ParserException {
        int i = 0;
        Node result = new Node(new ArrayList<Node>(), name);
        while (i < tokens.size()) {
            if (tokens.get(i) instanceof Colon) {
                throw new ParserException("Error in line: " + ((Integer) tokens.get(i).getLine()).toString()
                        + " position: " + ((Integer) tokens.get(i).getEnd()).toString() + " найден разделитель");
            } else if (tokens.get(i) instanceof KeyWord) {
                KeyWord keyWord = (KeyWord) tokens.get(i);
                switch (keyWord.getName()) {
                    case "read": {
                        ArrayList<Token> action;
                        action = findBody(tokens, i + 1, ";");
                        i += action.size() + 1;
                        result.getChildren().add(parseRead(action));
                        break;
                    }
                    case "write": {
                        ArrayList<Token> action;
                        action = findBody(tokens, i + 1, ";");
                        i += action.size() + 1;
                        result.getChildren().add(parseWrite(action));
                        break;
                    }
                    case "if": {
                        ArrayList<Token> action = new ArrayList<>();
                        action.add(keyWord);
                        action.addAll(findBody(tokens, i + 1, "then"));
                        action.addAll(findBody(tokens, i + action.size(), ""));
                        int j = i + action.size();
                        if (j < tokens.size() && tokens.get(j).getName().equals("else")) {
                            action.add(tokens.get(j));
                            action.addAll(findBody(tokens, j + 1, ""));
                        }
                        i += action.size();
                        result.getChildren().add(parseIf(action));
                        break;
                    }
                    case "then":
                    case "else":
                    case "do":
                    case "function":
                    case "end":
                    case "main":
                        throw new ParserException("Error in line: " + ((Integer) keyWord.getLine()).toString()
                                + " position: " + ((Integer) keyWord.getStart()).toString());
                    case "begin": {
                        ArrayList<Token> action = findBody(tokens, i, "end");
                        int j = i + action.size() - 1;
                        tokens.remove(j);
                        tokens.remove(i);
                        break;
                    }
                    case "while": {
                        ArrayList<Token> action = new ArrayList<>();
                        action.add(keyWord);
                        action.addAll(findBody(tokens, i + 1, "do"));
                        action.addAll(findBody(tokens, i + action.size(), ""));
                        result.getChildren().add(parseWhile(action));
                        i += action.size();
                        break;
                    }
                    default:
                        throw new ParserException("Error in line: " + ((Integer) keyWord.getLine()).toString()
                                + " position: " + ((Integer) keyWord.getStart()).toString());
                }
            } else {
                ArrayList<Token> action = findBody(tokens, i, ";");
                //удалить ;
                i += action.size();
                action.remove(action.size() - 1);
                result.getChildren().add(parseExpressions(action));
            }
        }

        return result;
    }

    private static Node parseRead(ArrayList<Token> tokens) throws ParserException {
        Node result = new Node(new ArrayList<Node>(), "read");

        if (tokens.size() >= 2 && tokens.get(0) instanceof Ident && tokens.get(1).getName().equals(";")) {
            Ident ident = (Ident) tokens.get(0);
            result.getChildren().add(new Node(new ArrayList<Node>(), ident.getName()));
        } else {
            throw new ParserException("Error in line: " + ((Integer) tokens.get(1).getLine()).toString()
                    + " position: " + ((Integer) tokens.get(1).getStart()).toString() + "\n ожидался идентификатор");
        }
        return result;
    }

    private static Node parseWrite(ArrayList<Token> tokens) throws ParserException {
        Node write = new Node(new ArrayList<Node>(), "write");
        if (tokens.get(tokens.size() - 1).getName().equals(";")) {
            tokens.remove(tokens.size() - 1);
        } else {
            throw new ParserException("Error in line: " + ((Integer) tokens.get(tokens.size() - 1).getLine()).toString()
                    + " position: " + ((Integer) tokens.get(tokens.size() - 1).getStart()).toString());
        }
        write.getChildren().add(parseExpressions(tokens));
        return write;
    }

    private static Node parseExpressions(ArrayList<Token> tokens) throws ParserException {
        if (tokens.size() == 0) {
            throw new ParserException("Expected expression");
        }
        if (tokens.size() == 2 && tokens.get(0) instanceof Operator && tokens.get(0).getName().equals("-")
                && (tokens.get(1) instanceof Num || tokens.get(1) instanceof Ident)) {
            Node result = new Node(new ArrayList<Node>(), tokens.get(0).getName());
            result.getChildren().add(new Node(new ArrayList<Node>(), tokens.get(1).getName()));
            return result;
        }
        if (tokens.size() == 1) {
            if (tokens.get(0) instanceof Ident || tokens.get(0) instanceof Num) {
                return new Node(new ArrayList<Node>(), tokens.get(0).getName());
            } else {
                throw new ParserException("Error in line: " + ((Integer) tokens.get(0).getLine()).toString()
                        + " position: " + ((Integer) tokens.get(0).getStart()).toString());
            }
        }
        if (tokens.get(0) instanceof Ident && tokens.get(1) instanceof Operator && tokens.get(1).getName().equals(":=")) {
            Node result = new Node(new ArrayList<Node>(), ":=");
            result.getChildren().add(new Node(new ArrayList<Node>(), tokens.get(0).getName()));
            tokens.remove(0);
            tokens.remove(0);
            result.getChildren().add(parseExpressions(tokens));
            return result;
        }


        return parseOR(tokens);


    }


    private static Node parseOR(ArrayList<Token> tokens) throws ParserException {
        if (tokens.size() == 0) {
            return null;
        }
        int balance = 0;
        int ind = -1;
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).getName().equals("(")) {
                balance++;
            }
            if (tokens.get(i).getName().equals(")")) {
                balance--;
            }
            if (balance < 0) {
                throw new ParserException("Error in line: " + ((Integer) tokens.get(i).getLine()).toString()
                        + " position: " + ((Integer) tokens.get(i).getEnd()).toString() + " нарушен баланс скобок");
            }
            if (balance == 0 && (tokens.get(i).getName().equals("||"))) {
                ind = i;
            }

        }

        if (balance != 0) {
            throw new ParserException("Error in line: " + ((Integer) tokens.get(tokens.size() - 1).getLine()).toString()
                    + " position: " + ((Integer) tokens.get(tokens.size() - 1).getEnd()).toString() + " нарушен баланс скобок");
        }
        if (ind == -1) {
            return parseAND(tokens);
        } else {
            Node result = new Node(new ArrayList<Node>(), " " + tokens.get(ind).getName());
            ArrayList<Token> left = new ArrayList<>();
            ArrayList<Token> right = new ArrayList<>();
            for (int i = 0; i < ind; i++) {
                left.add(tokens.get(i));
            }
            for (int i = ind + 1; i < tokens.size(); i++) {
                right.add(tokens.get(i));
            }
            result.getChildren().add(parseExpressions(left));
            result.getChildren().add(parseExpressions(right));
            return result;
        }
    }

    private static Node parseAND(ArrayList<Token> tokens) throws ParserException {
        if (tokens.size() == 0) {
            return null;
        }
        int balance = 0;
        int ind = -1;
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).getName().equals("(")) {
                balance++;
            }
            if (tokens.get(i).getName().equals(")")) {
                balance--;
            }
            if (balance < 0) {
                throw new ParserException("Error in line: " + ((Integer) tokens.get(i).getLine()).toString()
                        + " position: " + ((Integer) tokens.get(i).getEnd()).toString() + " нарушен баланс скобок");
            }
            if (balance == 0 && (tokens.get(i).getName().equals("&&"))) {
                ind = i;
            }

        }
        if (balance != 0) {
            throw new ParserException("Error in line: " + ((Integer) tokens.get(tokens.size() - 1).getLine()).toString()
                    + " position: " + ((Integer) tokens.get(tokens.size() - 1).getEnd()).toString() + " нарушен баланс скобок");
        }
        if (ind == -1) {
            return parseEquals(tokens);
        } else {
            Node result = new Node(new ArrayList<Node>(), " " + tokens.get(ind).getName());
            ArrayList<Token> left = new ArrayList<>();
            ArrayList<Token> right = new ArrayList<>();
            for (int i = 0; i < ind; i++) {
                left.add(tokens.get(i));
            }
            for (int i = ind + 1; i < tokens.size(); i++) {
                right.add(tokens.get(i));
            }
            result.getChildren().add(parseExpressions(left));
            result.getChildren().add(parseExpressions(right));
            return result;
        }
    }

    private static Node parseEquals(ArrayList<Token> tokens) throws ParserException {
        if (tokens.size() == 0) {
            return null;
        }
        int balance = 0;
        int ind = -1;
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).getName().equals("(")) {
                balance++;
            }
            if (tokens.get(i).getName().equals(")")) {
                balance--;
            }
            if (balance < 0) {
                throw new ParserException("Error in line: " + ((Integer) tokens.get(i).getLine()).toString()
                        + " position: " + ((Integer) tokens.get(i).getEnd()).toString() + " нарушен баланс скобок");
            }
            if (balance == 0 && (tokens.get(i).getName().equals("==") || tokens.get(i).getName().equals("!="))) {
                ind = i;
            }

        }
        if (balance != 0) {
            throw new ParserException("Error in line: " + ((Integer) tokens.get(tokens.size() - 1).getLine()).toString()
                    + " position: " + ((Integer) tokens.get(tokens.size() - 1).getEnd()).toString() + " нарушен баланс скобок");
        }
        if (ind == -1) {
            return parseCompare(tokens);
        } else {
            Node result = new Node(new ArrayList<Node>(), " " + tokens.get(ind).getName());
            ArrayList<Token> left = new ArrayList<>();
            ArrayList<Token> right = new ArrayList<>();
            for (int i = 0; i < ind; i++) {
                left.add(tokens.get(i));
            }
            for (int i = ind + 1; i < tokens.size(); i++) {
                right.add(tokens.get(i));
            }
            result.getChildren().add(parseExpressions(left));
            result.getChildren().add(parseExpressions(right));
            return result;
        }
    }


    private static Node parseCompare(ArrayList<Token> tokens) throws ParserException {
        if (tokens.size() == 0) {
            return null;
        }
        int balance = 0;
        int ind = -1;
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).getName().equals("(")) {
                balance++;
            }
            if (tokens.get(i).getName().equals(")")) {
                balance--;
            }
            if (balance < 0) {
                throw new ParserException("Error in line: " + ((Integer) tokens.get(i).getLine()).toString()
                        + " position: " + ((Integer) tokens.get(i).getEnd()).toString() + " нарушен баланс скобок");
            }
            if (balance == 0 && (tokens.get(i).getName().equals("<") || tokens.get(i).getName().equals(">")
                    || tokens.get(i).getName().equals(">=") || tokens.get(i).getName().equals("<="))) {
                ind = i;
            }

        }
        if (balance != 0) {
            throw new ParserException("Error in line: " + ((Integer) tokens.get(tokens.size() - 1).getLine()).toString()
                    + " position: " + ((Integer) tokens.get(tokens.size() - 1).getEnd()).toString() + " нарушен баланс скобок");
        }
        if (ind == -1) {
            return parseSum(tokens);
        } else {
            Node result = new Node(new ArrayList<Node>(), " " + tokens.get(ind).getName());
            ArrayList<Token> left = new ArrayList<>();
            ArrayList<Token> right = new ArrayList<>();
            for (int i = 0; i < ind; i++) {
                left.add(tokens.get(i));
            }
            for (int i = ind + 1; i < tokens.size(); i++) {
                right.add(tokens.get(i));
            }
            result.getChildren().add(parseExpressions(left));
            result.getChildren().add(parseExpressions(right));
            return result;
        }
    }

    private static Node parseSum(ArrayList<Token> tokens) throws ParserException {
        if (tokens.size() == 0) {
            return null;
        }
        int balance = 0;
        int ind = -1;
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).getName().equals("(")) {
                balance++;
            }
            if (tokens.get(i).getName().equals(")")) {
                balance--;
            }
            if (balance < 0) {
                throw new ParserException("Error in line: " + ((Integer) tokens.get(i).getLine()).toString()
                        + " position: " + ((Integer) tokens.get(i).getEnd()).toString() + " нарушен баланс скобок");
            }
            if (balance == 0 && (tokens.get(i).getName().equals("+") || tokens.get(i).getName().equals("-"))) {
                ind = i;
            }

        }
        if (balance != 0) {
            throw new ParserException("Error in line: " + ((Integer) tokens.get(tokens.size() - 1).getLine()).toString()
                    + " position: " + ((Integer) tokens.get(tokens.size() - 1).getEnd()).toString() + " нарушен баланс скобок");
        }
        if (ind == -1) {
            return parseMul(tokens);
        } else {
            Node result = new Node(new ArrayList<Node>(), " " + tokens.get(ind).getName());
            ArrayList<Token> left = new ArrayList<>();
            ArrayList<Token> right = new ArrayList<>();
            for (int i = 0; i < ind; i++) {
                left.add(tokens.get(i));
            }
            for (int i = ind + 1; i < tokens.size(); i++) {
                right.add(tokens.get(i));
            }
            result.getChildren().add(parseExpressions(left));
            result.getChildren().add(parseExpressions(right));
            return result;
        }
    }

    private static Node parseMul(ArrayList<Token> tokens) throws ParserException {
        if (tokens.size() == 0) {
            return null;
        }
        int balance = 0;
        int ind = -1;
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).getName().equals("(")) {
                balance++;
            }
            if (tokens.get(i).getName().equals(")")) {
                balance--;
            }
            if (balance < 0) {
                throw new ParserException("Error in line: " + ((Integer) tokens.get(i).getLine()).toString()
                        + " position: " + ((Integer) tokens.get(i).getEnd()).toString() + " нарушен баланс скобок");
            }
            if (balance == 0 && (tokens.get(i).getName().equals("*") || tokens.get(i).getName().equals("/")
                    || tokens.get(i).getName().equals("%"))) {
                ind = i;
            }

        }
        if (balance != 0) {
            throw new ParserException("Error in line: " + ((Integer) tokens.get(tokens.size() - 1).getLine()).toString()
                    + " position: " + ((Integer) tokens.get(tokens.size() - 1).getEnd()).toString() + " нарушен баланс скобок");
        }
        if (ind == -1) {
            return parsePow(tokens);
        } else {
            Node result = new Node(new ArrayList<Node>(), " " + tokens.get(ind).getName());
            ArrayList<Token> left = new ArrayList<>();
            ArrayList<Token> right = new ArrayList<>();
            for (int i = 0; i < ind; i++) {
                left.add(tokens.get(i));
            }
            for (int i = ind + 1; i < tokens.size(); i++) {
                right.add(tokens.get(i));
            }
            result.getChildren().add(parseExpressions(left));
            result.getChildren().add(parseExpressions(right));
            return result;
        }
    }

    private static Node parsePow(ArrayList<Token> tokens) throws ParserException {
        if (tokens.size() == 0) {
            return null;
        }
        int balance = 0;
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).getName().equals("(")) {
                balance++;
            }
            if (tokens.get(i).getName().equals(")")) {
                balance--;
            }
            if (balance < 0) {
                throw new ParserException("Error in line: " + ((Integer) tokens.get(i).getLine()).toString()
                        + " position: " + ((Integer) tokens.get(i).getEnd()).toString() + " нарушен баланс скобок");
            }
            if (balance == 0 && (tokens.get(i).getName().equals("^"))) {
                Node result = new Node(new ArrayList<Node>(), " ^");
                ArrayList<Token> left = new ArrayList<>();
                ArrayList<Token> right = new ArrayList<>();
                for (int j = 0; j < i; j++) {
                    left.add(tokens.get(j));
                }
                for (int j = i + 1; j < tokens.size(); j++) {
                    right.add(tokens.get(j));
                }
                result.getChildren().add(parseExpressions(left));
                result.getChildren().add(parseExpressions(right));
                return result;
            }
        }
        if (balance != 0) {
            throw new ParserException("Error in line: " + ((Integer) tokens.get(tokens.size() - 1).getLine()).toString()
                    + " position: " + ((Integer) tokens.get(tokens.size() - 1).getEnd()).toString() + " нарушен баланс скобок");
        }

        if (tokens.size() >= 2 && tokens.get(0).getName().equals("(") &&
                tokens.get(tokens.size() - 1).getName().equals(")")) {
            tokens.remove(0);
            tokens.remove(tokens.size() - 1);
            return parseExpressions(tokens);
        } else if (tokens.size() >= 3 && tokens.get(1).getName().equals("(") &&
                tokens.get(tokens.size() - 1).getName().equals(")") && tokens.get(0) instanceof Ident) {
            return parseFunctionCall(tokens);
        } else {
            throw new ParserException("Error in line: " + ((Integer) tokens.get(tokens.size() - 1).getLine()).toString()
                    + " position: " + ((Integer) tokens.get(tokens.size() - 1).getEnd()).toString());
        }
    }


    private static Node parseFunctionCall(ArrayList<Token> tokens) throws ParserException {
        if (!(tokens.size() >= 3 && tokens.get(1).getName().equals("(") &&
                tokens.get(tokens.size() - 1).getName().equals(")") && tokens.get(0) instanceof Ident)) {
            throw new ParserException("Error in line: " + ((Integer) tokens.get(0).getLine()).toString()
                    + " position: " + ((Integer) tokens.get(0).getEnd()).toString());
        }

        Node result = new Node(new ArrayList<Node>(), tokens.get(0).getName());
        int last = 2;
        for (int i = 2; i < tokens.size() - 1; i++) {
            if (tokens.get(i) instanceof Colon &&
                    tokens.get(i).getName().equals(",")) {
                ArrayList<Token> arrayList = new ArrayList<>();
                for (int j = last; j < i; j++) {
                    arrayList.add(tokens.get(j));
                }
                result.getChildren().add(parseExpressions(arrayList));
                last = i + 1;
            }
        }

        ArrayList<Token> arrayList = new ArrayList<>();
        for (int j = last; j < tokens.size() - 1; j++) {
            arrayList.add(tokens.get(j));
        }
        result.getChildren().add(parseExpressions(arrayList));
        return result;
    }

    private static Node parseIf(ArrayList<Token> tokens) throws ParserException {
        if (!tokens.get(0).getName().equals("if")) {
            throw new ParserException("Error in line: " + ((Integer) tokens.get(0).getLine()).toString()
                    + " position: " + ((Integer) tokens.get(0).getEnd()).toString() + " ожидался if");
        }

        ArrayList<Token> cond = findBody(tokens, 1, "then");
        int cur = cond.size() + 1;

        ArrayList<Token> body = findBody(tokens, cur, "");
        cur = cond.size() + 1 + body.size();
        Node result = new Node (new ArrayList<Node>(), "if");

        Node nodeCond = new Node(new ArrayList<Node>(), "conditional");
        //Удалить then
        cond.remove(cond.size() - 1);
        nodeCond.getChildren().add(parseExpressions(cond));

        Node nodeBody = new Node(new ArrayList<Node>(), "body");
        nodeBody.getChildren().addAll(parseBody(body, "").getChildren());

        result.getChildren().add(nodeCond);
        result.getChildren().add(nodeBody);

        if (cur < tokens.size() && tokens.get(cur).getName().equals("else")) {
            ArrayList<Token> bodyElse = findBody(tokens, cur + 1, "");
            Node nodeElse = new Node(new ArrayList<Node>(), "else");
            nodeElse .getChildren().addAll(parseBody(bodyElse, "").getChildren());
            result.getChildren().add(nodeElse);
        }
        return result;
    }

    private static Node parseWhile(ArrayList<Token> tokens) throws ParserException {
        if (!tokens.get(0).getName().equals("while")) {
            throw new ParserException("Error in line: " + ((Integer) tokens.get(0).getLine()).toString()
                    + " position: " + ((Integer) tokens.get(0).getEnd()).toString() + " ожидался while");
        }
        ArrayList<Token> cond = findBody(tokens, 1, "do");
        ArrayList<Token> body = findBody(tokens, cond.size() + 1, "");

        Node result = new Node(new ArrayList<Node>(), "while");

        Node condNode = new Node(new ArrayList<Node>(), "conditional");
        Node bodyWhile = new Node(new ArrayList<Node>(), "while body");
        //удалить do
        cond.remove(cond.size() - 1);

        condNode.getChildren().add(parseExpressions(cond));
        bodyWhile.getChildren().addAll(parseBody(body, "").getChildren());

        result.getChildren().add(condNode);
        result.getChildren().add(bodyWhile);

        return result;

    }

}
