package c_compiler;
import java_cup.runtime.*;
%%
%class Lexer
%int
%unicode
%cup

%{
  private Symbol createToken(int type, Object value){
    return new Symbol(type, yycolumn, yyline, value);
  }

  private Symbol createToken(int type){
    return new Symbol(type, yycolumn,  yyline);
  }
%}

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
    "if" {return createToken(sym.IF);}
    "else" {return createToken(sym.ELSE);}
    "while" {return createToken(sym.WHILE);}
    "for" {return createToken(sym.FOR);}
    "int" {return createToken(sym.INT);}
    "double" {return createToken(sym.DOUBLE);}
    "bool" {return createToken(sym.BOOL);}
    "char" {return createToken(sym.CHAR);}
    "void" {return createToken(sym.VOID);}
    "return" {return createToken(sym.RETURN);}
    "include" {return createToken(sym.INCLUDE);}
    {Boolean} {return createToken(sym.BOOLEAN);}
    "(" {return createToken(sym.OPENPAREN);}
    ")" {return createToken(sym.CLOSEPAREN);}
    "[" {return createToken(sym.OPENBRCKT);}
    "]" {return createToken(sym.CLOSEBRCKT);}
    "{" {return createToken(sym.OPENKEY);}
    "}" {return createToken(sym.CLOSEKEY);}
    "=" {return createToken(sym.ASSIGN);}
    {Compare} {return createToken(sym.OPERATOR_RELATIONAL);}
    {Sum} {return createToken(sym.OPERATOR_SUM);}
    {Multiplication} {return createToken(sym.OPERATOR_MUL);}
    ";" {return createToken(sym.ENDEXP);}
    "," {return createToken(sym.COMMA);}
    "#" {return createToken(sym.PREPROCESSOR);}
    "printf" {return createToken(sym.PRINTF);}
    "scanf" {return createToken(sym.SCANF);}
    \" {yybegin(STRING);}
    {CharLiteral} {return createToken(sym.LITERAL_CHAR);}
    {DecIntegerLiteral} {return createToken(sym.LITERAL_INT);}
    {Identifier} {return createToken(sym.IDENTIFIER);}
    {OnelineComment} {System.out.println(yytext());}
    {MultilineComment} {System.out.println(yytext());}
    {WhiteSpace} {}
}
<STRING>{
    \" {yybegin(YYINITIAL);return createToken(sym.LITERAL_CHARARRAY);}
    \\[:jletterdigit:] {}
    \\\" {}
    . {}
}
