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
                        KeyWord keyWord = new KeyWord(tokens.get(j).getLine(), tokens.get(j).getStart(), tokens.get(j).getEnd(),"read");
                        tokens.remove(j);
                        tokens.add(j, colon);
                        tokens.add(j+1, keyWord);
                        flag = true;
                    }
                    j += 1;
                }
                if (j < tokens.size()) {
                    end = j;
                }

                if (flag && end != -1) {
                    KeyWord begKeyWord  = new KeyWord(tokens.get(i).getLine(), tokens.get(i).getStart(), tokens.get(i).getEnd(),"begin");
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


    static Node parseFunction(ArrayList<Token> tokens) throws ParserException {
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

    static Node parseMain(ArrayList<Token> tokens) throws ParserException {
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


    static Node parseBody(ArrayList<Token> tokens, String name) throws ParserException {
        int i = 0;
        Node result = new Node(new ArrayList<Node>(), name);
        while (i < tokens.size()) {
            if (tokens.get(i) instanceof Colon) {
                throw new ParserException("Error in line: " + ((Integer) tokens.get(i).getLine()).toString()
                        + " position: " + ((Integer) tokens.get(i).getEnd()).toString() + " найден разделитель");
            } else if (tokens.get(i) instanceof KeyWord) {
                KeyWord keyWord = (KeyWord) tokens.get(i);
                if (keyWord.getName().equals("read")) {
                    ArrayList<Token> action = new ArrayList<>();
                    int j = i;
                    while (j < tokens.size() && !tokens.get(j).getName().equals(";")) {
                        action.add(tokens.get(j));
                        j++;
                    }
                    i = j + 1;
                    result.getChildren().add(parseRead(action));
                } else if (keyWord.getName().equals("write")) {
                    ArrayList<Token> action = new ArrayList<>();
                    int j = i;
                    while (j < tokens.size() && !tokens.get(j).getName().equals(";")) {
                        action.add(tokens.get(j));
                        j++;
                    }
                    Node write = new Node(new ArrayList<Node>(), "write");
                    action.remove(0);
                    write.getChildren().add(parseExpressions(action));
                    result.getChildren().add(write);
                    i = j + 1;
                } else if (keyWord.getName().equals("if")) {
                    ArrayList<Token> action = new ArrayList<>();
                    action.add(keyWord);
                    int j = i;
                    j++;
                    while (j < tokens.size() && !tokens.get(j).getName().equals("then")) {
                        action.add(tokens.get(j));
                        j++;
                    }
                    if (j >= tokens.size()) {
                        throw new ParserException("Error in line: " + ((Integer) tokens.get(tokens.size() - 1).getLine()).toString()
                                + " position: " + ((Integer) tokens.get(tokens.size() - 1).getEnd()).toString() + " ожидался then");
                    }
                    //Добавили then
                    action.add(tokens.get(j));

                    i = j; // i указывает на then
                    j++;
                    if (j >= tokens.size()) {
                        throw new ParserException("Error in line: " + ((Integer) tokens.get(j).getLine()).toString()
                                + " position: " + ((Integer) tokens.get(j).getEnd()).toString() + " пустое тело if");
                    }
                    if (tokens.get(j).getName().equals("begin")) {
                        j++;
                        int b = 1;
                        while (j < tokens.size() && b != 0) {
                            if (tokens.get(j).getName().equals("begin")) {
                                b++;
                            }
                            if (tokens.get(j).getName().equals("end")) {
                                b--;
                            }
                            if (b < 0) {
                                throw new ParserException("Error in line: " + ((Integer) tokens.get(j).getLine()).toString()
                                        + " position: " + ((Integer) tokens.get(j).getStart()).toString() + " нарушен баланс операторных скобок");
                            }
                            if (b != 0) {
                                action.add(tokens.get(j));
                            }
                            j++;
                        }
                        if (b != 0) {
                            throw new ParserException("Error in line: " + ((Integer) tokens.get(tokens.size() - 1).getLine()).toString()
                                    + " position: " + ((Integer) tokens.get(tokens.size() - 1).getStart()).toString() + " нарушен баланс операторных скобок");
                        }
                        i = j;//указывает на слудующий за end токен

                    } else {
                        int balance1 = 0, balance2 = 0;
                        while (j < tokens.size() && (!tokens.get(j).getName().equals(";")|| balance1 != 0 || balance2!= 0)) {
                            if (tokens.get(j).getName().equals("begin")) {
                                balance1++;
                            }
                            if (tokens.get(j).getName().equals("end")) {
                                balance1--;
                            }
                            if (balance1 < 0) {
                                throw new ParserException("Error in line: " + ((Integer) tokens.get(j).getLine()).toString()
                                        + " position: " + ((Integer) tokens.get(j).getStart()).toString() + " нарушен баланс операторных скобок");
                            }
                            action.add(tokens.get(j));
                            j++;
                        }
                        if (j < tokens.size()) {
                            action.add(tokens.get(j));
                        }
                        i = j + 1; // указывает за следующим за ;
                        j++;
                    }
                    if (j < tokens.size() && tokens.get(j).getName().equals("else")) {
                        action.add(tokens.get(j));
                        i = j + 1; // следующий за else;
                        j += 1;
                        if (tokens.get(j).getName().equals("begin")) {
                            j++;
                            int b = 1;
                            while (j < tokens.size() && b != 0) {
                                if (tokens.get(j).getName().equals("begin")) {
                                    b++;
                                }
                                if (tokens.get(j).getName().equals("end")) {
                                    b--;
                                }
                                if (b < 0) {
                                    throw new ParserException("Error in line: " + ((Integer) tokens.get(j).getLine()).toString()
                                            + " position: " + ((Integer) tokens.get(j).getStart()).toString() + " нарушен баланс операторных скобок");
                                }
                                if (b != 0) {
                                    action.add(tokens.get(j));
                                }
                                j++;
                            }
                            if (b != 0) {
                                throw new ParserException("Error in line: " + ((Integer) tokens.get(tokens.size() - 1).getLine()).toString()
                                        + " position: " + ((Integer) tokens.get(tokens.size() - 1).getStart()).toString() + " нарушен баланс операторных скобок");
                            }
                            i = j; // следующий за end
                        } else {
                            int balance = 0;
                            while (j < tokens.size() && (!tokens.get(j).getName().equals(";") || balance != 0)) {
                                if (tokens.get(j).getName().equals("begin")) {
                                    balance++;
                                }
                                if (tokens.get(j).getName().equals("end")) {
                                    balance--;
                                }
                                if (balance < 0) {
                                    throw new ParserException("Error in line: " + ((Integer) tokens.get(j).getLine()).toString()
                                            + " position: " + ((Integer) tokens.get(j).getStart()).toString() + " нарушен баланс операторных скобок");
                                }
                                action.add(tokens.get(j));
                                j++;
                            }
                            if (j < tokens.size()) {
                                action.add(tokens.get(j));
                            }
                            i = j + 1; //следующий за end;
                        }
                    }

                    result.getChildren().add(parseIf(action));
                } else if (keyWord.getName().equals("then") || keyWord.getName().equals("else") ||
                        keyWord.getName().equals("do") || keyWord.getName().equals("function") ||
                        keyWord.getName().equals("end") || keyWord.getName().equals("main")) {
                    throw new ParserException("Error in line: " + ((Integer) keyWord.getLine()).toString()
                            + " position: " + ((Integer) keyWord.getStart()).toString());
                } else if (keyWord.getName().equals("begin")) {
                    /*int j = i + 1;
                    int b = 1;
                    while (j < tokens.size() && b != 0) {
                        if (tokens.get(j).getName().equals("begin")) {
                            b++;
                        }
                        if (tokens.get(j).getName().equals("end")) {
                            b--;
                        }
                        if (b < 0) {
                            throw new ParserException("Error in line: " + ((Integer) tokens.get(j).getLine()).toString()
                                    + " position: " + ((Integer) tokens.get(j).getStart()).toString() + " нарушен баланс операторных скобок");
                        }

                        j++;
                    }
                    if (b != 0) {
                        throw new ParserException("Error in line: " + ((Integer) tokens.get(tokens.size() - 1).getLine()).toString()
                                + " position: " + ((Integer) tokens.get(tokens.size() - 1).getStart()).toString() + " нарушен баланс операторных скобок");
                    }
                    tokens.remove(j - 1);
                    tokens.remove(i);*/
                    if (!tokens.get(tokens.size() - 1).getName().equals("end")) {
                        throw new ParserException("Error in line: " + ((Integer) tokens.get(tokens.size() - 1).getLine()).toString()
                                + " position: " + ((Integer) tokens.get(tokens.size() - 1).getStart()).toString() + " нарушен баланс операторных скобок");
                    }
                    else {
                        tokens.remove(tokens.size() - 1);
                        tokens.remove(i);
                    }
                } else if (keyWord.getName().equals("while")) {
                    ArrayList<Token> action = new ArrayList<>();
                    action.add(keyWord);
                    int j = i;
                    j++;
                    while (j < tokens.size() && !tokens.get(j).getName().equals("do")) {
                        action.add(tokens.get(j));
                        j++;
                    }
                    if (j >= tokens.size()) {
                        throw new ParserException("Error in line: " + ((Integer) tokens.get(tokens.size() - 1).getLine()).toString()
                                + " position: " + ((Integer) tokens.get(tokens.size() - 1).getEnd()).toString() + " ожидался do");
                    }
                    //Добавили do
                    action.add(tokens.get(j));

                    i = j; // i указывает на do
                    j++;
                    if (j >= tokens.size()) {
                        throw new ParserException("Error in line: " + ((Integer) tokens.get(j).getLine()).toString()
                                + " position: " + ((Integer) tokens.get(j).getEnd()).toString() + " пустое тело while");
                    }
                    if (tokens.get(j).getName().equals("begin")) {
                        j++;
                        int b = 1;
                        while (j < tokens.size() && b != 0) {
                            if (tokens.get(j).getName().equals("begin")) {
                                b++;
                            }
                            if (tokens.get(j).getName().equals("end")) {
                                b--;
                            }
                            if (b < 0) {
                                throw new ParserException("Error in line: " + ((Integer) tokens.get(j).getLine()).toString()
                                        + " position: " + ((Integer) tokens.get(j).getStart()).toString() + " нарушен баланс операторных скобок");
                            }
                            if (b != 0) {
                                action.add(tokens.get(j));
                            }
                            j++;
                        }
                        if (b != 0) {
                            throw new ParserException("Error in line: " + ((Integer) tokens.get(tokens.size() - 1).getLine()).toString()
                                    + " position: " + ((Integer) tokens.get(tokens.size() - 1).getStart()).toString() + " нарушен баланс операторных скобок");
                        }
                        i = j;//указывает на слудующий за end токен

                    } else {
                        while (j < tokens.size() && !tokens.get(j).getName().equals(";")) {
                            action.add(tokens.get(j));
                            j++;
                        }
                        i = j + 1; // указывает за следующим за ;
                    }
                    result.getChildren().add(parseWhile(action));
                }
            } else {
                ArrayList<Token> action = new ArrayList<>();
                int j = i;
                while (j < tokens.size() && !tokens.get(j).getName().equals(";")) {
                    action.add(tokens.get(j));
                    j++;
                }
                i = j + 1;
                result.getChildren().add(parseExpressions(action));
            }
        }

        return result;
    }

    static Node parseRead(ArrayList<Token> tokens) throws ParserException {
        Node result = new Node(new ArrayList<Node>(), "read");
        if (tokens.size() >= 2 && tokens.get(1) instanceof Ident) {
            Ident ident = (Ident) tokens.get(1);
            result.getChildren().add(new Node(new ArrayList<Node>(), ident.getName()));
        } else {
            throw new ParserException("Error in line: " + ((Integer) tokens.get(1).getLine()).toString()
                    + " position: " + ((Integer) tokens.get(1).getStart()).toString() + "\n ожидался идентификатор");
        }
        return result;
    }


    static Node parseExpressions(ArrayList<Token> tokens) throws ParserException {
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


    static Node parseOR(ArrayList<Token> tokens) throws ParserException {
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

    static Node parseAND(ArrayList<Token> tokens) throws ParserException {
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

    static Node parseEquals(ArrayList<Token> tokens) throws ParserException {
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


    static Node parseCompare(ArrayList<Token> tokens) throws ParserException {
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

    static Node parseSum(ArrayList<Token> tokens) throws ParserException {
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

    static Node parseMul(ArrayList<Token> tokens) throws ParserException {
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

    static Node parsePow(ArrayList<Token> tokens) throws ParserException {
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
                Node result = new Node(new ArrayList<Node>(), " " + tokens.get(ind).getName());
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


    static Node parseFunctionCall(ArrayList<Token> tokens) throws ParserException {
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

    static Node parseIf(ArrayList<Token> tokens) throws ParserException {
        ArrayList<Token> cond = new ArrayList<>();
        ArrayList<Token> body = new ArrayList<>();
        ArrayList<Token> bodyElse = new ArrayList<>();
        Node result = new Node(new ArrayList<Node>(), "if");
        int i = 1;
        while (i < tokens.size() && !tokens.get(i).getName().equals("then")) {
            cond.add(tokens.get(i));
            i++;
        }
        if (i == tokens.size()) {
            throw new ParserException("Error in line: " + ((Integer) tokens.get(tokens.size() - 1).getLine()).toString()
                    + " position: " + ((Integer) tokens.get(tokens.size() - 1).getEnd()).toString() + " ожидался then");
        }

        i++;
        int balance = 1;
        while (i < tokens.size() && !(tokens.get(i).getName().equals("else") && balance == 1)) {
            if (tokens.get(i).getName().equals("if")) {
                balance++;
            }
            if (tokens.get(i).getName().equals("else")) {
                balance--;
            }
            if (balance < 0) {
                throw new ParserException("Error in line: " + ((Integer) tokens.get(i).getLine()).toString()
                        + " position: " + ((Integer) tokens.get(i).getStart()).toString() + " нарушен баланс операторных скобок");
            }
            body.add(tokens.get(i));
            i++;
        }

        if (body.isEmpty()) {
            if (i == tokens.size()) {
                throw new ParserException("Error in line: " + ((Integer) tokens.get(i).getLine()).toString()
                        + " position: " + ((Integer) tokens.get(i).getEnd()).toString() + " пустой if");
            }
        }
        Node condNode = new Node(new ArrayList<Node>(), "conditional");
        Node bodyIf = new Node(new ArrayList<Node>(), "If body");
        condNode.getChildren().add(parseExpressions(cond));
        Node tmp = parseBody(body, "");
        bodyIf.getChildren().addAll(tmp.getChildren());
        result.getChildren().add(condNode);
        result.getChildren().add(bodyIf);
        if (i < tokens.size() && tokens.get(i).getName().equals("else")) {
            Node bodyElseNode = new Node(new ArrayList<Node>(), "Else body");
            i++;
            while (i < tokens.size()) {
                bodyElse.add(tokens.get(i));
                i++;
            }
            tmp = parseBody(bodyElse, "");
            bodyElseNode.getChildren().addAll(tmp.getChildren());
            result.getChildren().add(bodyElseNode);
        }
        return result;

    }

    static Node parseWhile(ArrayList<Token> tokens) throws ParserException {
        ArrayList<Token> cond = new ArrayList<>();
        ArrayList<Token> body = new ArrayList<>();
        Node result = new Node(new ArrayList<Node>(), "while");
        int i = 1;
        while (i < tokens.size() && !tokens.get(i).getName().equals("do")) {
            cond.add(tokens.get(i));
            i++;
        }
        if (i == tokens.size()) {
            throw new ParserException("Error in line: " + ((Integer) tokens.get(tokens.size() - 1).getLine()).toString()
                    + " position: " + ((Integer) tokens.get(tokens.size() - 1).getEnd()).toString() + " ожидался do");
        }
        i++;
        while (i < tokens.size()) {
            body.add(tokens.get(i));
            i++;
        }
        if (body.isEmpty()) {
            if (i == tokens.size()) {
                throw new ParserException("Error in line: " + ((Integer) tokens.get(i).getLine()).toString()
                        + " position: " + ((Integer) tokens.get(i).getEnd()).toString() + " пустой while");
            }
        }
        Node condNode = new Node(new ArrayList<Node>(), "conditional");
        Node bodyIf = new Node(new ArrayList<Node>(), "while body");
        condNode.getChildren().add(parseExpressions(cond));
        bodyIf.getChildren().add(parseBody(body, ""));
        result.getChildren().add(condNode);
        result.getChildren().add(bodyIf);
        return result;

    }

}
