package c_compiler;
import java_cup.runtime.*;
%%
%class Lexer
%int
%unicode
%cup
%line
%column

%{
  private Symbol createToken(int type, Object value){
    return new Symbol(type, yycolumn, yyline, value);
  }

  private Symbol createToken(int type){
    return new Symbol(type, yycolumn, yyline);
  }
%}

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace = [\n\r\t\f ]+
Identifier = [:jletter:][:jletterdigit:]*
DecIntegerLiteral = 0 | [1-9][0-9]*
CharLiteral = \'[([:jletterdigit:]|(\\([:jletterdigit:]|\\\\|\"|\\\')))]\'
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
    "if" {return createToken(sym.IF, yytext());}
    "else" {return createToken(sym.ELSE, yytext());}
    "while" {return createToken(sym.WHILE, yytext());}
    "for" {return createToken(sym.FOR, yytext());}
    "int" {return createToken(sym.INT, yytext());}
    "double" {return createToken(sym.DOUBLE, yytext());}
    "bool" {return createToken(sym.BOOL, yytext());}
    "char" {return createToken(sym.CHAR, yytext());}
    "void" {return createToken(sym.VOID, yytext());}
    "return" {return createToken(sym.RETURN, yytext());}
    "include" {return createToken(sym.INCLUDE, yytext());}
    {Boolean} {return createToken(sym.LITERAL_BOOL, yytext());}
    "(" {return createToken(sym.LPAREN, yytext());}
    ")" {return createToken(sym.RPAREN, yytext());}
    "[" {return createToken(sym.LBRACKET, yytext());}
    "]" {return createToken(sym.RBRACKET, yytext());}
    "{" {return createToken(sym.LKEY, yytext());}
    "}" {return createToken(sym.RKEY, yytext());}
    "=" {return createToken(sym.ASSIGN, yytext());}
    {Compare} {return createToken(sym.OPERATOR_RELATIONAL, yytext());}
    {Sum} {return createToken(sym.OPERATOR_SUM, yytext());}
    {Multiplication} {return createToken(sym.OPERATOR_MUL, yytext());}
    ";" {return createToken(sym.ENDEXP, yytext());}
    "," {return createToken(sym.COMMA, yytext());}
    "printf" {return createToken(sym.PRINTF, yytext());}
    "scanf" {return createToken(sym.SCANF, yytext());}
    \" {yybegin(STRING);}
    {CharLiteral} {return createToken(sym.LITERAL_CHAR, yytext());}
    {DecIntegerLiteral} {return createToken(sym.LITERAL_INT, yytext());}
    {Identifier} {return createToken(sym.IDENTIFIER, yytext());}
    {OnelineComment} {}
    {MultilineComment} {}
    {WhiteSpace} {}
}
<STRING>{
    \" {yybegin(YYINITIAL);return createToken(sym.LITERAL_STRING, "a string");}
    \\[:jletterdigit:] {}
    \\\" {}
    . {}
}
