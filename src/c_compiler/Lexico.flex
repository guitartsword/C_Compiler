package c_compiler;
//import java_cup.runtime.*;
%%
%class Lexer
%int
%unicode
%standalone

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]

WhiteSpace = [\n\r\t\f ]+
Identifier = [:jletter:][:jletterdigit:]*
DecIntegerLiteral = 0 | [1-9][0-9]*
CharLiteral = \'[([:jletterdigit:]|(\\([:jletterdigit:]|\\\\|\"|\\\')))]\'
StringLiteral = \"[^\"]*\"
OnelineComment = "//"{InputCharacter}*{LineTerminator}
MultilineComment = "/*" ~"*/"
Boolean = "true"|"false"
Compare = "<="|"=>"|"=="|"!="|">"|"<"
Sum = "+"|"-"
Multiplication = "*"|"/"
%state STRING
%state COMMENTS
%%
<YYINITIAL>{
    "if" {System.out.println("IF");}
    "else" {System.out.println("ELSE");}
    "while" {System.out.println("WHILE");}
    "for" {System.out.println("FOR");}
    "int" {System.out.println("INT");}
    "double" {System.out.println("DOUBLE");}
    "bool" {System.out.println("BOOL");}
    "char" {System.out.println("CHAR");}
    "void" {System.out.println("VOID");}
    "return" {System.out.println("RETURN");}
    "include" {System.out.println("INCLUDE");}
    {Boolean} {System.out.println("BOOLEAN");}
    "(" {System.out.println("OPENPAREN");}
    ")" {System.out.println("CLOSEPAREN");}
    "[" {System.out.println("OPENBRCKT");}
    "]" {System.out.println("CLOSEBRCKT");}
    "{" {System.out.println("OPENKEY");}
    "}" {System.out.println("CLOSEKEY");}
    "=" {System.out.println("ASSIGN");}
    {Compare} {System.out.println("OPERATOR_RELATIONAL");}
    {Sum} {System.out.println("OPERATOR_SUM");}
    {Multiplication} {System.out.println("OPERATOR_MUL");}
    ";" {System.out.println("ENDEXP");}
    "," {System.out.println("COMMA");}
    "#" {System.out.println("PREPROCESSOR");}
    "printf" {System.out.println("PRINTF");}
    "scanf" {System.out.println("SCANF");}
    \" {yybegin(STRING);}
    {CharLiteral} {System.out.println("LITERAL_CHAR");}
    {DecIntegerLiteral} {System.out.println("LITERAL_INT");}
    {Identifier} {System.out.println("IDENTIFIER");}
    {OnelineComment} {System.out.println(yytext());}
    {MultilineComment} {System.out.println(yytext());}
    {WhiteSpace} {}
}
<STRING>{
    \" {yybegin(YYINITIAL);System.out.println("LITERAL_CHARARRAY");}
    \\[:jletterdigit:] {}
    \\\" {}
    . {}
}