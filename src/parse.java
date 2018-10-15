import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class parse {

    public static void main(String args[]) throws FileNotFoundException
    {

        new Parser().Program();

    } //end main

} //end parse


enum tokenType       // give a name to each terminal
{ mainToken,    valToken,     refToken,   intToken,
    readToken,    writeToken,   callToken,  ifToken,
    elseToken,    endifToken,   whileToken, id,
    number,       leftParen,    rightParen, leftBrace,
    rightBrace,   comma,        semicolon,  assignment,
    equality,     notEqual,     less,       lessEqual,
    greater,      greaterEqual, plus,       minus,
    times,        slash
};

class Globs
{
    static String[] tokenNameStrings=
            { "mainToken    ", "valToken     ", "refToken     ", "intToken     ",
                    "readToken    ", "writeToken   ", "callToken    ", "ifToken      ",
                    "elseToken    ", "endifToken   ", "whileToken   ", "id           ",
                    "number       ", "leftParen    ", "rightParen   ", "leftBrace    ",
                    "rightBrace   ", "comma        ", "semicolon    ", "assignment   ",
                    "equality     ", "notEqual     ", "less         ", "lessEqual    ",
                    "greater      ", "greaterEqual ", "plus         ", "minus        ",
                    "times        ", "slash        "
            };



    static void error(int code)
    { switch ( code )
    { case  0: System.out.println("error type 0 at line "+
            SourceFileReader.linecount+
            ": program incomplete\n");
        System.exit(0);

        case  1: System.out.println("error type 1 at line "+
                SourceFileReader.linecount+
                ": unknown symbol\n");
            System.exit(0);

        case  3: System.out.println("error type 3 at line "+
                SourceFileReader.linecount+
                ": '(' expected\n");
            System.exit(0);
        case  4: System.out.println("error type 4 at line "+
                SourceFileReader.linecount+
                ": ')' expected\n");
            System.exit(0);
        case  5: System.out.println("error type 5 at line "+
                SourceFileReader.linecount+
                ": 'id' expected\n");
            System.exit(0);
        case  8: System.out.println("error type 8 at line "+
                SourceFileReader.linecount+
                ": ',' expected\n");
            System.exit(0);
        case  9: System.out.println("error type 9 at line "+
                SourceFileReader.linecount+
                ": '{' expected\n");
            System.exit(0);
        case  10: System.out.println("error type 10 at line "+
                SourceFileReader.linecount+
                ": '}' expected\n");
            System.exit(0);
        case  11: System.out.println("error type 11 at line "+
                SourceFileReader.linecount+
                ": ';' expected\n");
            System.exit(0);
        case  17: System.out.println("error type 17 at line "+
                SourceFileReader.linecount+
                ": 'int' expected\n");
            System.exit(0);
        case  29: System.out.println("error type 29 at line "+
                SourceFileReader.linecount+
                ": '=' expected\n");
            System.exit(0);
        case  32: System.out.println("error type 32 at line "+
                SourceFileReader.linecount+
                ": 'main' or 'id' expected\n");
            System.exit(0);
        case  33: System.out.println("error type 33 at line "+
                SourceFileReader.linecount+
                ": ')' or 'val' or 'ref' expected\n");
            System.exit(0);
        case  34: System.out.println("error type 34 at line "+
                SourceFileReader.linecount+
                ":'val' or 'ref' expected\n");
            System.exit(0);
        case  35: System.out.println("error type 35 at line "+
                SourceFileReader.linecount+
                ":')' or ',' expected\n");
            System.exit(0);
        case  36: System.out.println("error type 36 at line "+
                SourceFileReader.linecount+
                ":'id' or '{' or '}' or ';' or 'read' or 'write' or 'call' or 'if' or 'while' expected\n");
            System.exit(0);
        case  37: System.out.println("error type 37 at line "+
                SourceFileReader.linecount+
                ":',' or ';' expected\n");
            System.exit(0);
        case  38: System.out.println("error type 38 at line "+
                SourceFileReader.linecount+
                ":'==' or '!=' or '>' or '>=' or '<' or '<=' expected\n");
            System.exit(0);
        case  39: System.out.println("error type 39 at line "+
                SourceFileReader.linecount+
                ":'(' or 'id' or '+' or '-' or 'number' expected\n");
            System.exit(0);
        case  40: System.out.println("error type 40 at line "+
                SourceFileReader.linecount+
                ":')' or ';' or '==' or '!=' or '>' or '>=' or '<' or '<=' or '+' or '-' expected\n");
            System.exit(0);
        case  41: System.out.println("error type 41 at line "+
                SourceFileReader.linecount+
                ":'+' or '-' expected\n");
            System.exit(0);
        case  42: System.out.println("error type 42 at line "+
                SourceFileReader.linecount+
                ":')' or ';' or '==' or '!=' or '>' or '>=' or '<' or '<=' or '+' or '-' or '*' or '/' expected\n");
            System.exit(0);
        case  43: System.out.println("error type 43 at line "+
                SourceFileReader.linecount+
                ":'*' or '/' expected\n");
            System.exit(0);
        case  44: System.out.println("error type 44 at line "+
                SourceFileReader.linecount+
                ":'(' or 'id' or 'number' expected\n");
            System.exit(0);
        case  45: System.out.println("error type 45 at line "+
                SourceFileReader.linecount+
                ":')' or 'id' or 'number' expected\n");
            System.exit(0);
        case  46: System.out.println("error type 46 at line "+
                SourceFileReader.linecount+
                ":'else' or 'endif' or 'number' expected\n");
            System.exit(0);
        case  47: System.out.println("error type 47 at line "+
                SourceFileReader.linecount+
                ":'id' or '{' or ';' or 'read' or 'write' or 'call' or 'if' or 'while' expected\n");
            System.exit(0);


            //***add cases for other syntax errors***

    } //end switch

    } //end function error

}  //end Globs


class SourceFileReader
{
    static int  linecount=1;  // number of lines read
    Scanner source;
    char letter;
    boolean EOF;

    SourceFileReader() throws FileNotFoundException
    {
        source=new Scanner(new File("source.bc"));
        source.useDelimiter("");
        letter=' ';
        EOF=false;
    }

    void getCharacter()
    { if (EOF) Globs.error(0);   //program incomplete, halt compiler
        if (! source.hasNext())
        { EOF=true; //sets end-of-file flag
            letter=' ';
            return;
        }
        letter=source.next().charAt(0);   //reads one character from file

        System.out.print(letter); //for debugging

        if ( letter == '\n') { letter= ' ';   linecount++; }
        else if ( letter == '\t'||letter=='\r') letter= ' ';
    } // end getcharacter
} //end SourceFileReader

class LexAnalyzer
{ //***for debugging only***
    PrintWriter lexout;
    // end ***for debugging only***
    tokenType token=null;    // lookahead token
    String lexeme;        // spellling of identifier token
    int  value;         // value of number token

    SourceFileReader source;

    LexAnalyzer() throws FileNotFoundException
    {
        source=new SourceFileReader();
        //***for debugging only***
        lexout=new PrintWriter("lexout.txt");
        // end ***for debugging only***



    }

    class reservedWord
    { String spelling;
        tokenType kind;

        reservedWord(String s, tokenType t)
        {spelling=s; kind=t;}

    }

    reservedWord[] reservedWords =
            { new reservedWord("call",      tokenType.callToken),
                    new reservedWord("else",      tokenType.elseToken),
                    new reservedWord("endif",     tokenType.endifToken),
                    new reservedWord("if",        tokenType.ifToken),
                    new reservedWord("int",       tokenType.intToken),
                    new reservedWord("main",      tokenType.mainToken),
                    new reservedWord("read",      tokenType.readToken),
                    new reservedWord("ref",       tokenType.refToken),
                    new reservedWord("val",       tokenType.valToken),
                    new reservedWord("while",     tokenType.whileToken),
                    new reservedWord("write",     tokenType.writeToken)
            };

    void getToken()
    {


        int i=0, k;
        while (source.letter == ' ') source.getCharacter();  //skip blanks

        if(Character.isDigit(source.letter)){
            String tempDigit = "";
            while(Character.isDigit(source.letter)){
                tempDigit+=source.letter;
                source.getCharacter();
            }
            token=tokenType.number;
            value=Integer.parseInt(tempDigit);

        }
        else if(Character.isLetter(source.letter)){

            String tempString = "";
            while (Character.isLetter(source.letter) || Character.isDigit(source.letter)){
                tempString+=String.valueOf(source.letter);
                source.getCharacter();
            }
            if (!tempString.equals(tempString.toLowerCase())){
                Globs.error(1);
            }
            token=tokenType.id;
            lexeme = tempString;

            for (reservedWord word:
                    reservedWords) {
                if (tempString.equals(word.spelling)){
                    token = word.kind;
                    break;
                }
            }


        }
        else{
            switch (source.letter){
                case '(':
                    token = tokenType.leftParen;
                    source.getCharacter();
                    break;
                case ')':
                    token = tokenType.rightParen;
                    source.getCharacter();
                    break;
                case '{':
                    token = tokenType.leftBrace;
                    source.getCharacter();
                    break;
                case '}':
                    token = tokenType.rightBrace;
                    source.getCharacter();
                    break;
                case ',':
                    token = tokenType.comma;
                    source.getCharacter();
                    break;
                case ';':
                    token = tokenType.semicolon;
                    source.getCharacter();
                    break;

                case '!':
                    String tempNeString = "";
                    while(source.letter == '=' || source.letter == '!'){
                        tempNeString+=source.letter;
                        source.getCharacter();
                    }
                    if(tempNeString.equals("!=")){
                        token = tokenType.notEqual;

                    }
                    else {
                        Globs.error(1);
                    }
                    break;
                case '<':
                    String tempLeString = "";
                    while(source.letter == '=' || source.letter=='<'){
                        tempLeString+=source.letter;
                        source.getCharacter();
                    }
                    if(tempLeString.equals("<=")){
                        token = tokenType.lessEqual;
                    }
                    else if (tempLeString.equals("<")){
                        token = tokenType.less;
                    }
                    else {
                        Globs.error(1);
                    }
                    break;
                case '>':
                    String tempGeString = "";
                    while(source.letter == '=' || source.letter=='>'){
                        tempGeString+=source.letter;
                        source.getCharacter();
                    }
                    if(tempGeString.equals(">=")){
                        token = tokenType.greaterEqual;
                    }
                    else if(tempGeString.equals(">")) {
                        token = tokenType.greater;
                    }
                    else {
                        Globs.error(1);
                    }
                    break;
                case '=':
                    String tempEqualsString = "";
                    while(source.letter == '='){
                        tempEqualsString+=source.letter;
                        source.getCharacter();
                    }

                    if(tempEqualsString.equals("==")){
                        token = tokenType.equality;

                    }
                    else if(tempEqualsString.equals("=")) {
                        token = tokenType.assignment;
                    }
                    else {
                        Globs.error(1);
                    }
                    break;
                case '+':
                    token = tokenType.plus;
                    source.getCharacter();
                    break;
                case '-':
                    token = tokenType.minus;
                    source.getCharacter();
                    break;
                case '*':
                    token = tokenType.times;
                    source.getCharacter();
                    break;
                case '/':
                    token = tokenType.slash;
                    source.getCharacter();
                    break;
                default:
                    Globs.error(1);
                    break;
            }

        }


    }

} //end class LexAnalyzer

class Parser {

    LexAnalyzer lexan;
    boolean noLookAhead = false;
    PrintWriter lexout;
    //Constructor
    Parser() throws FileNotFoundException {

        lexan = new LexAnalyzer();
        lexan.getToken();
        lexout=new PrintWriter("lexout.txt");

    }

//this variable will be set to true only when parsing the main function
//if it is true, the paser will not read a lookahead token after the main
//function is parsed compleletely.


//***define parser functions***
    void printToken(){
        lexout.print(Globs.tokenNameStrings[lexan.token.ordinal()]);
        if (lexan.token == tokenType.id) lexout.print("     "+lexan.lexeme);
        if (lexan.token == tokenType.number) lexout.print("     "+lexan.value);
        lexout.println();
        lexout.flush();

    }

    void MainFunction() {
        lexan.getToken();   //get past mainToken
        printToken();
        if (lexan.token != tokenType.leftParen) Globs.error(3);
        lexan.getToken();
        printToken();
        if (lexan.token != tokenType.rightParen) Globs.error(4);
        lexan.getToken();
        printToken();
        noLookAhead = true;
        FunctionBody();
    }

    void Program() {
        if (lexan.token == tokenType.id) {
            Function();
            Program();
        } else if (lexan.token == tokenType.mainToken) {
            MainFunction();
        } else {
            Globs.error(32);
        }
    }

    void Function() {
        FunctionHeading();
        FunctionBody();
    }

    void FunctionHeading() {
        if (lexan.token != tokenType.id) {
            Globs.error(5);
        }
        lexan.getToken();
        if (lexan.token != tokenType.leftParen) {
            Globs.error(3);
        }
        lexan.getToken();
        if (lexan.token == tokenType.valToken || lexan.token == tokenType.refToken) {
            lexan.getToken();
            printToken();
            if (lexan.token != tokenType.id) {
                Globs.error(5);
            }
            lexan.getToken();
            printToken();
            FunctionHeadingRecursive();
            lexan.getToken();
            printToken();
        } else if (lexan.token == tokenType.rightParen) {
            lexan.getToken();
            printToken();
        } else {
            Globs.error(33);
        }
    }

    void FunctionHeadingRecursive() {
        if (lexan.token == tokenType.comma) {
            lexan.getToken();
            printToken();
            if (lexan.token == tokenType.valToken || lexan.token == tokenType.refToken) {
                lexan.getToken();
                printToken();
            } else {
                Globs.error(34);
            }
            if (lexan.token == tokenType.id) {
                lexan.getToken();
                printToken();
            } else {
                Globs.error(5);
            }
            if (lexan.token == tokenType.comma) {
                FunctionHeadingRecursive();
            } else if (lexan.token == tokenType.rightParen) {

            } else {
                Globs.error(35);
            }
        }
    }

    void FunctionBody() {
        if (lexan.token == tokenType.leftBrace) {
            lexan.getToken();
            printToken();
        } else {
            Globs.error(9);
        }
        if (lexan.token == tokenType.intToken) {
            FunctionBodyDeclarationRecursive();
        }
        else{

        }
        if (lexan.token == tokenType.leftBrace || lexan.token == tokenType.rightBrace ||
                lexan.token == tokenType.semicolon || lexan.token == tokenType.readToken ||
                lexan.token == tokenType.writeToken || lexan.token == tokenType.writeToken || lexan.token == tokenType.callToken ||
                lexan.token == tokenType.ifToken || lexan.token == tokenType.whileToken || lexan.token == tokenType.id) {
                if (lexan.token == tokenType.leftBrace ||
                        lexan.token == tokenType.semicolon || lexan.token == tokenType.readToken ||
                        lexan.token == tokenType.writeToken || lexan.token == tokenType.writeToken || lexan.token == tokenType.callToken ||
                        lexan.token == tokenType.ifToken || lexan.token == tokenType.whileToken || lexan.token == tokenType.id) {
                    FunctionBodyStatementRecursive();
            }
        } else {
            Globs.error(36);
        }
        if (lexan.token == tokenType.rightBrace) {
            if(noLookAhead==false){
                lexan.getToken();
                printToken();
            }
            else if(noLookAhead){

            }
        }
        else {
            Globs.error(10);
        }
    }

    void FunctionBodyDeclarationRecursive() {
        if (lexan.token == tokenType.intToken) {
            Declaration();
            if (lexan.token == tokenType.intToken) {
                FunctionBodyDeclarationRecursive();
            }
        } else {
            Globs.error(36);
        }
    }

    void FunctionBodyStatementRecursive() {
        if (lexan.token == tokenType.leftBrace ||
                lexan.token == tokenType.semicolon || lexan.token == tokenType.readToken ||
                lexan.token == tokenType.writeToken || lexan.token == tokenType.writeToken || lexan.token == tokenType.callToken ||
                lexan.token == tokenType.ifToken || lexan.token == tokenType.whileToken || lexan.token == tokenType.id) {
            Statement();
            if (lexan.token == tokenType.leftBrace ||
                    lexan.token == tokenType.semicolon || lexan.token == tokenType.readToken ||
                    lexan.token == tokenType.writeToken || lexan.token == tokenType.writeToken || lexan.token == tokenType.callToken ||
                    lexan.token == tokenType.ifToken || lexan.token == tokenType.whileToken || lexan.token == tokenType.id) {
                FunctionBodyStatementRecursive();
            }
        } else {
            Globs.error(36);
        }
    }

    void Declaration() {
        if (lexan.token == tokenType.intToken) {
            lexan.getToken();
            printToken();
        } else {
            Globs.error(17);
        }
        if (lexan.token == tokenType.id) {
            lexan.getToken();
            printToken();
        } else {
            Globs.error(5);
        }
        if (lexan.token == tokenType.comma) {
            DeclarationRecursive();
        }
        else{

        }
        if (lexan.token == tokenType.semicolon) {
            lexan.getToken();
            printToken();
        } else {
            Globs.error(37);
        }
    }

    void DeclarationRecursive() {
        if (lexan.token == tokenType.comma) {
            lexan.getToken();
            printToken();
        } else {
            Globs.error(8);
        }
        if (lexan.token == tokenType.id) {
            lexan.getToken();
            printToken();
        } else {
            Globs.error(5);
        }
        if (lexan.token == tokenType.comma) {
            DeclarationRecursive();
        }
    }

    void Condition() {
        Expression();
        if (lexan.token == tokenType.equality || lexan.token == tokenType.notEqual || lexan.token == tokenType.greater ||
                lexan.token == tokenType.greaterEqual || lexan.token == tokenType.less || lexan.token == tokenType.lessEqual) {
            lexan.getToken();
            printToken();
        } else {
            Globs.error(38);
        }
        Expression();
    }

    void Expression() {
        if (lexan.token == tokenType.plus || lexan.token == tokenType.minus) {
            lexan.getToken();
            printToken();
        } else if (lexan.token == tokenType.id || lexan.token == tokenType.leftParen || lexan.token == tokenType.number) {

        } else {
            Globs.error(39);
        }
        Term();
        if (lexan.token == tokenType.plus || lexan.token == tokenType.minus) {
            ExpressionRecursive();
        } else if (lexan.token == tokenType.semicolon || lexan.token == tokenType.rightParen ||
                lexan.token == tokenType.equality || lexan.token == tokenType.notEqual ||
                lexan.token == tokenType.greater || lexan.token == tokenType.greaterEqual ||
                lexan.token == tokenType.less || lexan.token == tokenType.lessEqual) {

        } else {
            Globs.error(40);
        }
    }

    void ExpressionRecursive() {
        if (lexan.token == tokenType.plus || lexan.token == tokenType.minus) {
            lexan.getToken();
            printToken();
            Term();
        } else {
            Globs.error(41);
        }
    }

    void Term() {
        Factor();
        if (lexan.token == tokenType.times || lexan.token == tokenType.slash) {
            TermRecursive();
        } else if (lexan.token == tokenType.semicolon || lexan.token == tokenType.rightParen ||
                lexan.token == tokenType.equality || lexan.token == tokenType.notEqual ||
                lexan.token == tokenType.greater || lexan.token == tokenType.greaterEqual ||
                lexan.token == tokenType.less || lexan.token == tokenType.lessEqual ||
                lexan.token == tokenType.plus || lexan.token == tokenType.minus) {

        } else {

            Globs.error(42);
        }
    }

    void TermRecursive() {
        if (lexan.token == tokenType.times || lexan.token == tokenType.slash) {
            lexan.getToken();
            printToken();
        } else {
            Globs.error(43);
        }
        Factor();
        if (lexan.token == tokenType.times || lexan.token == tokenType.slash) {
            TermRecursive();
        }
    }

    void Factor() {
        if (lexan.token == tokenType.number || lexan.token == tokenType.id) {
            lexan.getToken();
            printToken();
        } else if (lexan.token == tokenType.leftParen) {
            lexan.getToken();
            printToken();
            Expression();
            if (lexan.token == tokenType.rightParen) {
                lexan.getToken();
                printToken();
            } else {
                Globs.error(4);
            }
        } else {
            Globs.error(44);
        }
    }

    void Statement() {
        if (lexan.token == tokenType.callToken) {
            lexan.getToken();
            printToken();
            if (lexan.token == tokenType.id) {
                lexan.getToken();
                printToken();
            } else {
                Globs.error(5);
            }
            if (lexan.token == tokenType.leftParen) {
                lexan.getToken();
                printToken();
            } else {
                Globs.error(3);
            }
            if (lexan.token == tokenType.id) {
                lexan.getToken();
                printToken();
                if (lexan.token == tokenType.comma) {
                    StatementRecursiveComma();
                } else if (lexan.token == tokenType.rightParen) {

                } else {
                    Globs.error(35);
                }
            }
            else{

            }
            if (lexan.token == tokenType.rightParen) {
                lexan.getToken();
                printToken();
            } else {
                Globs.error(45);
            }
            if (lexan.token == tokenType.semicolon) {
                lexan.getToken();
                printToken();
            } else {
                Globs.error(11);
            }
        } else if (lexan.token == tokenType.semicolon) {
            lexan.getToken();
            printToken();
        } else if (lexan.token == tokenType.id) {
            lexan.getToken();
            printToken();
            if (lexan.token == tokenType.assignment) {
                lexan.getToken();
                printToken();
            } else {
                Globs.error(29);
            }
            Expression();
            if (lexan.token == tokenType.semicolon) {
                lexan.getToken();
                printToken();
            } else {
                Globs.error(11);
            }
        } else if (lexan.token == tokenType.readToken) {
            lexan.getToken();
            printToken();
            if (lexan.token == tokenType.id) {
                lexan.getToken();
                printToken();
            } else {
                Globs.error(5);
            }
            if (lexan.token == tokenType.semicolon) {
                lexan.getToken();
                printToken();
            } else {
                Globs.error(11);
            }
        } else if (lexan.token == tokenType.writeToken) {
            lexan.getToken();
            printToken();
            if (lexan.token == tokenType.id) {
                lexan.getToken();
                printToken();
            } else {
                Globs.error(5);
            }
            if (lexan.token == tokenType.semicolon) {
                lexan.getToken();
                printToken();
            } else {
                Globs.error(11);
            }
        } else if (lexan.token == tokenType.leftBrace) {
            lexan.getToken();
            printToken();
            if (lexan.token == tokenType.leftBrace || lexan.token == tokenType.semicolon ||
                    lexan.token == tokenType.readToken || lexan.token == tokenType.writeToken ||
                    lexan.token == tokenType.callToken || lexan.token == tokenType.ifToken ||
                    lexan.token == tokenType.whileToken || lexan.token == tokenType.id) {
                StatementRecursiveStatement();
            }
            else{

            }
            if (lexan.token == tokenType.rightBrace){
                lexan.getToken();
                printToken();
            }
            else{
                Globs.error(36);
            }
        }else if(lexan.token == tokenType.whileToken){
            lexan.getToken();
            printToken();
            if(lexan.token == tokenType.leftParen){
                lexan.getToken();
                printToken();
            }
            else{
                Globs.error(3);
            }
            Condition();
            if(lexan.token == tokenType.rightParen){
                lexan.getToken();
                printToken();
            }
            else{
                Globs.error(4);
            }
            Statement();
        }else if (lexan.token == tokenType.ifToken){
            lexan.getToken();
            printToken();
            if(lexan.token == tokenType.leftParen){
                lexan.getToken();
                printToken();
            }
            else{
                Globs.error(3);
            }
            Condition();
            if(lexan.token == tokenType.rightParen){
                lexan.getToken();
                printToken();
            }
            else{
                Globs.error(4);
            }
            Statement();
            if (lexan.token == tokenType.elseToken){
                lexan.getToken();
                printToken();
                Statement();
            }
            else{

            }
            if(lexan.token == tokenType.endifToken){
                lexan.getToken();
                printToken();
            }
            else {
                Globs.error(46);
            }
        }
        else{
            Globs.error(47);
        }

    }

    void StatementRecursiveComma() {
        if (lexan.token == tokenType.comma) {
            lexan.getToken();
            printToken();
        } else {
            Globs.error(8);
        }
        if (lexan.token == tokenType.id) {
            lexan.getToken();
            printToken();
        } else {
            Globs.error(5);
        }
        if (lexan.token == tokenType.comma) {
            StatementRecursiveComma();
        }
    }

    void StatementRecursiveStatement(){
        if (lexan.token == tokenType.leftBrace || lexan.token == tokenType.semicolon ||
                lexan.token == tokenType.readToken || lexan.token == tokenType.writeToken ||
                lexan.token == tokenType.callToken || lexan.token == tokenType.ifToken ||
                lexan.token == tokenType.whileToken || lexan.token == tokenType.id) {
            Statement();
            StatementRecursiveStatement();
        }
    }


    //***define other parser functions***
}   // end class Parser