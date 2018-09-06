import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class lex {

    public static void main(String args[]) throws FileNotFoundException
    {

  /* the following code to test the lexical analyzer
     will be discarded in the next stage
  */


        LexAnalyzer lexan=new LexAnalyzer();
        lexan.getToken();
        PrintWriter lexout=new PrintWriter("lexout.txt");

        while (! (lexan.token == tokenType.id
                && lexan.lexeme.equals("done")))
        { lexout.print(Globs.tokenNameStrings[lexan.token.ordinal()]);
            if (lexan.token == tokenType.id) lexout.print("     "+lexan.lexeme);
            if (lexan.token == tokenType.number) lexout.print("     "+lexan.value);
            lexout.println();
            lexout.flush();
            lexan.getToken();
        } //end while

    } //end main

} //end lex


enum tokenType       // give a name to each terminal
{ mainToken,    valToken,     refToken,   intToken,
    readToken,    writeToken,   callToken,  ifToken,
    elseToken,    endifToken,   whileToken, id,
    number,       leftParen,    rightParen, leftBrace,
    rightBrace,   comma,        semicolon,  assignment,
    equality,     notEqual,     lessToken,  lessEqual,
    greater,      greaterEqual, plusToken,  minusToken,
    times,        slash
};

class Globs
{
    //table of token names as strings. for use in debugging.

    static String[] tokenNameStrings=
            {"mainToken    ", "valToken     ", "refToken     ", "intToken     ",
                    "readToken    ", "writeToken   ", "callToken    ", "ifToken      ",
                    "elseToken    ", "endifToken   ", "whileToken   ", "id           ",
                    "number       ", "leftParen    ", "rightParen   ", "leftBrace    ",
                    "rightBrace   ", "comma        ", "semicolon    ", "assignment   ",
                    "equality     ", "notEqual     ", "lessToken    ", "lessEqual    ",
                    "greater      ", "greaterEqual ", "plusToken    ", "minusToken   ",
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
{
    tokenType token;    // lookahead token
    String lexeme;        // spelling of identifier token
    int  value;         // value of number token

    SourceFileReader source;

    LexAnalyzer() throws FileNotFoundException
    {
        source=new SourceFileReader();
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
    /*****THIS IS THE FUNCTION YOU SHALL WRITE*****)
     *the main tasks of the function are
     1. Skip all blank characters preceding a token,
     2. If the first character of a token is a letter,
     then find the character string over a..z and 0..9.
     Search table of resevered words to determine whether
     the string is a reserved word and set the global variable token.
     3. If the first character of a token is in 0..9,
     then the token must be a number. Find the numeral string
     and convert it into an integer for the global variable value.
     4. Other cases are quite easy */

  /* the main trick is probably that getoken always
     keeps a single lookahead character. That is, after
     finding each token, we should always make sure that the global
     variable letter contains the character that immediately follows the
     token, no matter what that character is. This technique
     will simplify the code */
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
                        token = tokenType.lessToken;
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
                    token = tokenType.plusToken;
                    source.getCharacter();
                    break;
                case '-':
                    token = tokenType.minusToken;
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


    } //end function getToken

} //end class LexAnalyzer

