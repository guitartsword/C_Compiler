package c_compiler;
import java_cup.runtime.*;

%%
%class Lexer
%cup
%int
%line
%column

%{
	private Symbol symbol(int sym) {
		Symbol data = new Symbol(sym, yycolumn, yyline);
		return data;
	}

	private Symbol symbol(int sym, Object val) {
		Symbol data = new Symbol(sym, yycolumn, yyline);
		data.value = new Symbol(sym, yycolumn, yyline, val);
		return data;
	}

	private Symbol symbol(int sym, Object val,int buflength) {
    return new Symbol(sym, yycolumn, yyline, val);
	}
%}

Digit = [0-9]
Letter = [a-zA-Z_]
CharLiteral = \'([:jletterdigit:]| (\\ ([:jletterdigit:]|\\|"'") ) )\'
H = [a-fA-F0-9]
E = [Ee][+-]?{Digit}+
FS = (f|F|l|L)
IS = (u|U|l|L)*
TC = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EC = "//" [^\r\n]* {new_line}
new_line = \r|\n|\r\n
white_space = {new_line} | [ \t\f]
%%

{EC}                    { }
{TC}                    { }
{white_space}           { }
"auto"			{ return symbol(sym.AUTO,yytext()); }
"break"			{ return symbol(sym.BREAK,yytext()); }
"case"			{ return symbol(sym.CASE,yytext()); }
"char"			{ return symbol(sym.CHAR,yytext()); }
"const"			{ return symbol(sym.CONST,yytext()); }
"continue"		{ return symbol(sym.CONTINUE,yytext()); }
"default"		{ return symbol(sym.DEFAULT,yytext()); }
"printf"		{ return symbol(sym.PRINTF,yytext()); }
"scanf"			{ return symbol(sym.SCANF,yytext()); }
"do"			{ return symbol(sym.DO,yytext()); }
"double"		{ return symbol(sym.DOUBLE,yytext()); }
"else"			{ return symbol(sym.ELSE,yytext()); }
"enum"			{ return symbol(sym.ENUM,yytext()); }
"extern"		{ return symbol(sym.EXTERN,yytext()); }
"float"			{ return symbol(sym.FLOAT,yytext()); }
"for"			{ return symbol(sym.FOR,yytext()); }
"goto"			{ return symbol(sym.GOTO,yytext()); }
"if"			{ return symbol(sym.IF,yytext()); }
"int"			{ return symbol(sym.INT,yytext()); }
"long"			{ return symbol(sym.LONG,yytext()); }
"register"		{ return symbol(sym.REGISTER,yytext()); }
"return"		{ return symbol(sym.RETURN,yytext()); }
"short"			{ return symbol(sym.SHORT,yytext()); }
"signed"		{ return symbol(sym.SIGNED,yytext()); }
"sizeof"		{ return symbol(sym.SIZEOF,yytext()); }
"static"		{ return symbol(sym.STATIC,yytext()); }
"struct"		{ return symbol(sym.STRUCT,yytext()); }
"switch"		{ return symbol(sym.SWITCH,yytext()); }
"typedef"		{ return symbol(sym.TYPEDEF,yytext()); }
"union"			{ return symbol(sym.UNION,yytext()); }
"unsigned"		{ return symbol(sym.UNSIGNED,yytext()); }
"void"			{ return symbol(sym.VOID,yytext()); }
"volatile"		{ return symbol(sym.VOLATILE,yytext()); }
"while"			{ return symbol(sym.WHILE,yytext()); }
"..."			{ return symbol(sym.ELLIPSIS, yytext()); }
">>="			{ return symbol(sym.RIGHT_ASSIGN, yytext()); }
"<<="			{ return symbol(sym.LEFT_ASSIGN, yytext()); }
"+="			{ return symbol(sym.ADD_ASSIGN, yytext()); }
"-="			{ return symbol(sym.SUB_ASSIGN, yytext()); }
"*="			{ return symbol(sym.MUL_ASSIGN, yytext()); }
"/="			{ return symbol(sym.DIV_ASSIGN, yytext()); }
"%="			{ return symbol(sym.MOD_ASSIGN, yytext()); }
"&="			{ return symbol(sym.AND_ASSIGN, yytext()); }
"^="			{ return symbol(sym.XOR_ASSIGN, yytext()); }
"|="			{ return symbol(sym.OR_ASSIGN, yytext()); }
">>"			{ return symbol(sym.RIGHT_OP, yytext()); }
"<<"			{ return symbol(sym.LEFT_OP, yytext()); }
"++"			{ return symbol(sym.INC_OP,"++"); }
"--"			{ return symbol(sym.DEC_OP,"--"); }
"->"			{ return symbol(sym.PTR_OP, yytext()); }
"&&"			{ return symbol(sym.AND_OP,"&&"); }
"||"			{ return symbol(sym.OR_OP,"||"); }
"<="			{ return symbol(sym.LE_OP,"<="); }
">="			{ return symbol(sym.GE_OP,">="); }
"=="			{ return symbol(sym.EQ_OP,"=="); }
"!="			{ return symbol(sym.NE_OP,"!="); }
";"			{ return symbol(sym.SEMI, yytext()); }
("{"|"<%")		{ return symbol(sym.CURLYL, yytext()); }
("}"|"%>")		{ return symbol(sym.CURLYR, yytext()); }
","			{ return symbol(sym.COMMA, yytext()); }
":"			{ return symbol(sym.COLON, yytext()); }
"="			{ return symbol(sym.ASSIGN, yytext()); }
"("			{ return symbol(sym.PARAL,"("); }
")"			{ return symbol(sym.PARAR, ")"); }
("["|"<:")		{ return symbol(sym.SQUAREDL, yytext()); }
("]"|":>")		{ return symbol(sym.SQUAREDR, yytext()); }
"."			{ return symbol(sym.POINT, yytext()); }
"&"			{ return symbol(sym.ADRESS, yytext()); }
"!"			{ return symbol(sym.NOT,"!"); }
"~"			{ return symbol(sym.TILDE, yytext()); }
"-"			{ return symbol(sym.MINUS, yytext()); }
"+"			{ return symbol(sym.PLUS,"+"); }
"*"			{ return symbol(sym.MUL,"*"); }
"/"			{ return symbol(sym.DIVIDE,"/"); }
"%"			{ return symbol(sym.MODULUS,"%"); }
"<"			{ return symbol(sym.LESS,"<"); }
">"			{ return symbol(sym.GREATER,">"); }
"^"			{ return symbol(sym.XOR, yytext()); }
"|"			{ return symbol(sym.OR, yytext()); }
"?"			{ return symbol(sym.COND, yytext()); }

{Letter}({Letter}|{Digit})*		{ return symbol(sym.IDENTIFIER, yytext()); }
0[xX]{H}+{IS}?		{ return symbol(sym.CONSTANT,yytext()); }
0{Digit}+{IS}?		{ return symbol(sym.CONSTANT,yytext()); }
{Digit}+{IS}?		{ return symbol(sym.CONSTANT,yytext()); }
{CharLiteral}		{ return symbol(sym.CONSTANT,yytext()); }
{Digit}+{E}{FS}?		{ return symbol(sym.CONSTANT,yytext()); }
{Digit}*"."{Digit}+({E})?{FS}?	{ return symbol(sym.CONSTANT,yytext()); }
{Digit}+"."{Digit}*({E})?{FS}?	{ return symbol(sym.CONSTANT,yytext()); }
L?\"(\\.|[^\\\"])*\"	{ return symbol(sym.STRING_LITERAL,yytext()); }
L?'(\\.|[^\\'])+'	{ System.err.println("Error Lexico, comillas dobles para strings: "+yytext()+" "+(yyline+1)+" : "+(yycolumn+1));}
[^]			{ System.err.println("Error Lexico, token no identificado: "+yytext()+" "+(yyline+1)+" : "+(yycolumn+1));}
