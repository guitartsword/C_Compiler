package proyecto_compiladores;
import java_cup.runtime.*;
%%
%class Lexer
%cup
%int
%standalone
%line
%column

%{
  private Symbol createToken(int type, Object value){
    System.out.println("Token: "+value+" linea: "+yyline+ " columna: "+yycolumn);
    return new Symbol(type, yycolumn, yyline, value);
  }

  private Symbol createToken(int type){
    return new Symbol(type, yycolumn,  yyline);
  }
%}

letter = [a-zA-Z]
digit = [0-9]
alphanumeric = {letter}|{digit}|\_
character = \'([^'])?\'
string = \'([^'])*\'
id = {letter}({alphanumeric})*
integer = {digit}+
leftBracket = \{
rightBracket = \}
notBracket = [^}]|{space}
commentBody= {notBracket}*
comment = {leftBracket}{commentBody}{rightBracket}
space = [ \n\t\r]

%%
<YYINITIAL>{
  //TERMINALS
    "*" { return createToken(sym.TIMES, "*");}
    "+" { return createToken(sym.PLUS, "+"); }
    "-" { return createToken(sym.MINUS, "-"); }
    "/" { return createToken(sym.DIVIDE, "/"); }
    ";" { return createToken(sym.SEMI, ";"); }
    "," { return createToken(sym.COMMA, ","); }
    "(" { return createToken(sym.LEFT_PAREN, yytext());}
    ")" { return createToken(sym.RT_PAREN, yytext()); }
    "[" { return createToken(sym.LEFT_BRKT, yytext()); }
    "]" { return createToken(sym.RT_BRKT, yytext()); }
    //"{" { return createToken(sym.LEFT_CB, yytext()); }
    // "}" { return createToken(sym.RT_CB, yytext()); }
    "and" { return createToken(sym.AND, "and"); }
    "or" { return createToken(sym.OR, "or"); }
    "not" { return createToken(sym.NOT, "not"); }
    "NOT" { return createToken(sym.NOT, "not"); }
    "Not" { return createToken(sym.NOT, "not"); }
    "=" { return createToken(sym.EQ, "="); }
    "<" { return createToken(sym.LESS, "<"); }
    ">" { return createToken(sym.GTR, ">"); }
    "<=" { return createToken(sym.LESS_EQ, "<="); }
    ">=" { return createToken(sym.GTR_EQ, ">="); }
    "<>" { return createToken(sym.NOT_EQ, "<>"); }
    ":" { return createToken(sym.COLON, ":"); }
    ":=" { return createToken(sym.ASSMNT, yytext()); }
    "." { return createToken(sym.DOT, "."); }
    "Program" {return createToken(sym.PROGRAM, "program"); }
    "program" {return createToken(sym.PROGRAM, "program"); }
    "PROGRAM" {return createToken(sym.PROGRAM, "program"); }
    "array" {return createToken(sym.ARRAY, "array" ); }
    "Array" {return createToken(sym.ARRAY, "array" ); }
    "ARRAY" {return createToken(sym.ARRAY, "array" ); }
    "Begin" {return createToken(sym.BEGIN, "begin" ); }
    "begin" {return createToken(sym.BEGIN, "begin" ); }
    "BEGIN" {return createToken(sym.BEGIN, "begin" ); }
    "else" {return createToken(sym.ELSE, "else" ); }
    "ELSE" {return createToken(sym.ELSE, "else" ); }
    "Else" {return createToken(sym.ELSE, "else" ); }
    "End"  {return createToken(sym.END, "end" ); }
    "end"  {return createToken(sym.END, "end" ); }
    "END"  {return createToken(sym.END, "end" ); }
    "Procedure" {return createToken(sym.PROCEDURE, "procedure"); }
    "procedure" {return createToken(sym.PROCEDURE, "procedure"); }
    "PROCEDURE" {return createToken(sym.PROCEDURE, "procedure"); }
    "If"    {return createToken(sym.IF, "if"); }
    "if"    {return createToken(sym.IF, "if"); }
    "IF"    {return createToken(sym.IF, "if"); }
    "Of"    {return createToken(sym.OF, "of"); }
    "of"    {return createToken(sym.OF, "of"); }
    "OF"    {return createToken(sym.OF, "of"); }
    "Then"  {return createToken(sym.THEN, "then"); }
    "then"  {return createToken(sym.THEN, "then"); }
    "THEN"  {return createToken(sym.THEN, "then"); }
    "Var"   {return createToken(sym.VAR, "var");  }
    "VAR"   {return createToken(sym.VAR, "var");  }
    "var"   {return createToken(sym.VAR, "var");  }
    "type" {return createToken(sym.TYPE, "type"); }
    "TYPE" {return createToken(sym.TYPE, "type"); }
    "Type" {return createToken(sym.TYPE, "type"); }
    "WHILE" {return createToken(sym.TYPE, "WHILE"); }
    "While" {return createToken(sym.TYPE, "WHILE"); }
    "while" {return createToken(sym.TYPE, "WHILE"); }
    {id} { return createToken(sym.IDENT, yytext()); }
    {integer} { return createToken(sym.INT, new Integer(yytext())); }
    {character} { return createToken(sym.CHAR, new Character(yytext().charAt(1)));}
    {comment} { return createToken(sym.COMMENT, yytext());}
    //"forward" {return createToken(sym.FORWARD); }
    "do" {return createToken(sym.DO); }
    "DO" {return createToken(sym.DO); }
    "Do" {return createToken(sym.DO); }
    //"while" {return createToken(sym.WHILE); }
    //"case"  {return createToken(sym.CASE); }
    //"to"    {return createToken(sym.TO); }
    //"xor" { return createToken(sym.XOR); }
    //"shl" { return createToken(sym.DESP_IZQ); }
    //"shr" { return createToken(sym.DER); }
    //"break" {return createToken(sym.BREAK); }
    //"^" { return createToken(sym.PUNTERO, yytext()); }
    //"#" { return createToken(sym.ASCII, yytext()); }
    //"const" {return createToken(sym.CONST);}
    //"true"  {return createToken(sym.TRUE); }
    //"until" {return createToken(sym.UNTIL); }
    //"repeat" {return createToken(sym.REPEAT); }
    //"goto" {return createToken(sym.GOTO); }
    //"with" {return createToken(sym.WITH); }
    //"false" {return createToken(sym.FALSE); }
    //"for"   {return createToken(sym.FOR); }
    //"function" {return createToken(sym.FUNCTION); }
    //"record" {return createToken(sym.RECORD); }
    //"set" {return createToken(sym.SET); }
    //"file" {return createToken(sym.FILE); }
    //"packed" {return createToken(sym.PACKED); }
    //{string} { return createToken(sym.STRING, new Character(yytext().charAt(1)));}
}
