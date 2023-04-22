import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class LS_Complier {

    static String content = "";
    static int count = 0;
    static int holder = 0;
    static int charClass;
    static char[] lexeme = new char[10];
    static char nextChar;
    static int lexLen;
    static int nextToken;
    static Scanner myReader;
    static int lineNumber = 1;
    static HashMap<String, String> symbolTable = new HashMap<>();
    static StringBuilder sb = new StringBuilder();
    static StringBuilder s = new StringBuilder();

    public static final int EOF = -1;
    public static final int LETTER = 0;
    public static final int DIGIT = 1;
    public static final int UNKNOWN = 99;
    public static final int INT_LIT = 10;
    public static final int IDENT = 11;
    public static final int ASSIGN_OP = 20;
    public static final int ADD_OP = 21;
    public static final int SUB_OP = 22;
    public static final int MULT_OP = 23;
    public static final int DIV_OP = 24;
    public static final int LEFT_PAREN = 25;
    public static final int RIGHT_PAREN = 26;
    public static final int COMMA = 27;
    public static final int SEMICOLON = 28;
    public static final int PRINT = 29;
    public static final int LET = 31;
    public static final int IF = 33;
    public static final int THEN = 34;
    public static final int EQ_OP = 35;
    public static final int NE_OP = 36;
    public static final int LT_OP = 37;
    public static final int GT_OP = 38;
    public static final int LE_OP = 39;
    public static final int GE_OP = 40;
    public static final int N_OP = 41;
    public static final int EX_OP = 42;
    public static final int DOUBLE_Q = 45;
    public static final int SINGLE_Q = 46;
    public static final int ENDIF = 47;



    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("filename.txt");
        myReader = new Scanner(file);
        myReader.useDelimiter("");

        String filePath = "filename.txt";
        try {
            content = new String(Files.readAllBytes(Paths.get(filePath)));
        }
        catch (IOException e){
            System.err.println("Error reading file: " + e.getMessage());
        }


        getChar();
        while (nextToken != EOF) {
            lex();
            expr();
        }
        myReader.close();
    }

    public static int lookup(char ch) {
        switch (ch) {
            case '"':
                addChar();
                nextToken = DOUBLE_Q;
                break;

            case '\'':
                addChar();
                nextToken = SINGLE_Q;
                break;

            case '(':
                addChar();
                nextToken = LEFT_PAREN;
                break;

            case ')':
                addChar();
                nextToken = RIGHT_PAREN;
                break;

            case '+':
                addChar();
                nextToken = ADD_OP;
                break;

            case '-':
                addChar();
                nextToken = SUB_OP;
                break;

            case '*':
                addChar();
                nextToken = MULT_OP;
                break;

            case '/':
                addChar();
                nextToken = DIV_OP;
                break;

            case '=':
                addChar();
                try {
                    nextChar = myReader.next().charAt(0);
                    if (nextChar == '=') {
                        addChar();
                        nextToken = EQ_OP;
                    }
                    else {
                        nextToken = ASSIGN_OP;
                    }

                } catch(Exception e) {
                    nextToken = ASSIGN_OP;
                }

                break;

            case '<':
                addChar();
                try{
                    nextChar = myReader.next().charAt(0);
                    if (nextChar == '='){
                        addChar();
                        nextToken = LE_OP;
                    }
                    else {
                        nextToken = LT_OP;
                    }
                } catch (Exception e){
                    nextToken = LT_OP;
                }

                break;

            case '>':
                addChar();
                try{
                    nextChar = myReader.next().charAt(0);
                    if (nextChar == '=') {
                        addChar();
                        nextToken = GE_OP;
                    }
                    else {
                        nextToken = GT_OP;
                    }
                }
                catch (Exception e){
                    nextToken = GT_OP;
                }

                break;


            case '!':
                addChar();
                try{
                    nextChar = myReader.next().charAt(0);
                    if (nextChar == '=') {
                        addChar();
                        nextToken = NE_OP;
                    } else {
                        nextToken = N_OP;
                    }
                }
                catch (Exception e){
                    nextToken = N_OP;
                }
                break;

            case ',':
                addChar();
                nextToken = COMMA;
                break;

            case ';':
                addChar();
                nextToken = SEMICOLON;
                break;

            case '^':
                addChar();
                nextToken = EX_OP;
                break;

            default:
                if (Character.isLetter(ch)) {
                    sb.append(ch);
                    addChar();
                    nextToken = IDENT;
                } else if (Character.isDigit(ch)) {
                    addChar();
                    nextToken = INT_LIT;
                } else {
                    addChar();
                    nextToken = EOF;
                }
                break;
        }
        return nextToken;
    }

    public static void addChar() {
        if (lexLen < lexeme.length) {
            lexeme[lexLen++] = nextChar;
        }
        else {
            System.err.println("Error: lexeme is too long");
        }
    }


    public static void getChar() {
        if (myReader.hasNext()) {
            nextChar = myReader.next().charAt(0);
            if (Character.isLetter(nextChar)) {
                charClass = LETTER;
            } else if (Character.isDigit(nextChar)) {
                charClass = DIGIT;
            } else {
                charClass = UNKNOWN;
            }
        } else {
            charClass = EOF;
        }
    }

    public static void getNonBlank() {
        while (Character.isWhitespace(nextChar)) {
            if (nextChar == '\n') {
                lineNumber++;
            }
            getChar();
        }
    }

    public static int lex() {
        lexeme = new char[10];
        lexLen = 0;
        getNonBlank();

        switch (charClass) {
            case LETTER:
                addChar();
                getChar();
                while (charClass == LETTER || charClass == DIGIT) { // while( 0 == 0 || 1 == 1)
                    addChar();
                    getChar();
                }
                s.setLength(0);
                for(int x = 0; x < lexeme.length; x++){
                    if(lexeme[x] != '\0'){
                        s.append(lexeme[x]);
                    }
                }
                switch (s.toString()) {
                    case "LET" :
                        nextToken = LET;
                        if(!symbolTable.containsKey("LET")){
                            addSymbol("LET", LET);
                        }
                        count++;
                        break;

                    case "PRINT" :
                        nextToken = PRINT;
                        if(!symbolTable.containsKey("PRINT")){
                            addSymbol("PRINT", PRINT);
                        }
                        break;

                    case "IF" :
                        nextToken = IF;
                        if(!symbolTable.containsKey("IF")){
                            addSymbol("IF", IF);
                        }
                        break;

                    case "THEN" :
                        nextToken = THEN;
                        if(!symbolTable.containsKey("THEN")){
                            addSymbol("THEN", THEN);
                        }
                        break;

                    case "ENDIF" :
                        nextToken = ENDIF;
                        if(!symbolTable.containsKey("ENDIF")){
                            addSymbol("ENDIF", ENDIF);
                        }
                        break;

                    default :
                        holder++;
                        nextToken = IDENT;
                        break;
                }
                if (count >= holder && nextToken != LET && holder > 0 && nextToken != PRINT && nextToken != ENDIF
                        && nextToken != THEN && nextToken != IF) {
                    addSymbol(s.toString(), nextToken);
                }
                break;

            case DIGIT:
                addChar();
                getChar();
                while (charClass == DIGIT) {
                    addChar();
                    getChar();
                }
                nextToken = INT_LIT;
                break;

            case UNKNOWN:
                lookup(nextChar);
                getChar();
                break;

            case EOF:
                nextToken = EOF;
                lexeme[0] = 'E';
                lexeme[1] = 'O';
                lexeme[2] = 'F';
                lexeme[3] = '\0';
                break;
        }

        System.out.print("Next token is: " + nextToken + " Next lexeme is: ");
        for (int i = 0; i < lexLen; i++) {
            System.out.print(lexeme[i]);
        }
        if (nextToken == EOF) {
            System.out.print("EOF");
        }
        System.out.print("\n");

        return nextToken;
    }


    public static void addSymbol(String lexeme, int token) {
        if (symbolTable.containsKey(lexeme)) {
            System.out.println("Error: variable " + lexeme + " is already defined.");
        }
        else{
            symbolTable.put(lexeme, String.valueOf(token));
        }
    }

    // <expr> ::= <term> | <expr> <add_op> <term>
    public static void expr() {
        System.out.println("Enter <expr>");
        term();
        while (nextToken == ADD_OP || nextToken == SUB_OP) {
            lex();
            if(nextToken == EOF){
                System.out.print("Missing Var/Int");
                System.exit(1);
            }
            term();
            if(symbolTable.containsKey(lexeme)){
                statement();
            }
        }

        System.out.println("Exit <expr>");
    }

    // <term> ::= <factor> | <term> <mul_op> <factor>
    public static void term() {
        System.out.println("Enter <term>");
        if(nextToken == MULT_OP || nextToken == DIV_OP || nextToken == EX_OP || nextToken == LE_OP || nextToken == LT_OP ||
                nextToken == GT_OP || nextToken == GE_OP || nextToken == EQ_OP || nextToken == COMMA || nextToken == NE_OP){

        }
        else {
            factor();
        }
        while (nextToken == MULT_OP || nextToken == DIV_OP || nextToken == EX_OP || nextToken == LE_OP || nextToken == LT_OP ||
                nextToken == GT_OP || nextToken == GE_OP || nextToken == EQ_OP || nextToken == COMMA || nextToken == NE_OP) {
            lex();
            if (nextToken == -1){
                System.out.print("Missing Int/Var");
                System.exit(1);
            }
            factor();
            if(symbolTable.containsKey(lexeme)){
                statement();
            }
        }
        System.out.println("Exit <term>");
    }

    // <factor> ::= <number> | <variable> | <string> | '(' <expr> ')' | <bool>
    public static void factor() {
        System.out.println("Enter <factor>");
        if(symbolTable.containsKey(s.toString())) {
            if (nextToken == INT_LIT || nextToken == IDENT) {
                lex();
                if(nextToken == ENDIF){
                    definer();
                }
            }
            else if (nextToken == LEFT_PAREN) {
                lex();
                expr();
                if (nextToken == RIGHT_PAREN) {
                    lex();
                }
                else {
                    error();
                }
            }
            else if (s.toString().equals("LET") || (s.toString().equals("PRINT"))){
                statement();
            }
            else if(s.toString().equals("IF") || (s.toString().equals("THEN") || (s.toString().equals("ENDIF")))){
                statement();
            }
            else {
                error();
            }
        }
        else {
            System.out.print(s + " is not defined\n");
            System.exit(1);
        }

        System.out.println("Exit <factor>");
    }

    // <definer> ::= 'LET' | 'PRINT' | 'INPUT' | 'GOTO' | 'IF'
    public static void definer() {
        System.out.println("Enter <definer>");
        if (nextToken == LET || nextToken == PRINT || nextToken == IF || nextToken == ENDIF) { //PRINT
            if(nextToken == PRINT){
                lex();
                if(nextToken != IDENT && nextToken != INT_LIT){
                    System.out.print("Missing Var/ Int to print");
                    System.exit(1);
                }
            }else {
                lex();
            }
        }else {
            error();
        }
        System.out.println("Exit <definer>");
    }

    // <assignment> ::= <variable> '=' <expr>
    public static void assignment() {
        System.out.println("Enter <assignment>");
        lex();
        if (nextToken == IDENT || nextToken == INT_LIT) {
            digit();
            lex();
        } else {
            System.out.print("Missing Int");
            System.exit(1);
        }
        System.out.println("Exit <digit>");
        System.out.println("Exit <assignment>");
    }

    // <statement> ::= <assignment> | <definer> <if_statement> | <definer> <print_statement>
    public static void statement() {
        System.out.println("Enter <statement>");
        if (nextToken == IDENT) {
            assignment();
        } else if (nextToken == LET || nextToken == PRINT) {
            definer();
            if (nextToken == IDENT) {
                lex();
                if (nextToken == ASSIGN_OP) {
                    assignment();
                } else if (nextToken == ENDIF){
                    definer();
                } else if( nextToken == COMMA || nextToken == SEMICOLON){

                } else {
                    error();
                }
            } else {
                error();
            }
        } else if (nextToken == IF) {
            definer();
            if (nextToken == LEFT_PAREN) {
                expr();
                if (nextToken == EQ_OP || nextToken == NE_OP || nextToken == GT_OP || nextToken == GE_OP || nextToken == LE_OP || nextToken == LT_OP) {
                    lex();
                    expr();
                    if (nextToken == THEN) {
                        lex();
                        statement();
                    } else {
                        error();
                    }
                } else {
                    error();
                }
            } else {
                error();
            }
        } else {
            error();
        }
        System.out.println("Exit <statement>");
    }

    public static void digit(){
        System.out.println("Enter <digit>");
    }


    public static void error(){
        System.out.print("ERROR");
    }
}
