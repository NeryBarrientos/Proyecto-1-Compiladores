package Analizadores;

import java.io.*;
import java_cup.runtime.*;
import java_cup.runtime.Symbol;
import java.util.ArrayList;

%%
%public
%class lex

%column                // Para registrar las columnas
%line                  // Para registrar las líneas
%char                  // Llevar un conteo de caracteres
%cup                   // Habilita la integración con Cup
%unicode               // Reconocimiento de caracteres unicode
%function next_token

%init{                 // Constructor del analizador
    yyline   = 1; 
    yycolumn = 1;
%init}


//Componentes lexicos (extension de las expresiones regulares)

Whitespace = [ \t\f] | {LineTerminator}
LineTerminator = \r|\n|\r\n

[ \t\f | (\r|\n|\r\n)]

digito = [0-9]
minusculas = [a-z]
mayusculas = [A-Z]

COMENTARIO_MULTILINEA = "<!"[^!]*"!>"
COMENTARIO_LINEAL = "#".*

// Palabras Reservadas

conjunto = "CONJ"
opera = "OPERA"
evaluar = "EVALUAR"

//ER
ID = ({minusculas}|{mayusculas})({minusculas}|{mayusculas}|_|{digito})*
caracter = [\u0021-\u007E]

// Signos
LLAVEA = "{"
LLAVEC = "}"
IG = "="
PUNTOYCOMA = ";"
COMA = ","
DOS_PUNTOS = ":"
ASIGNACION = "->"
PARA = "("
PARC = ")"
virguilla = "~"
UNION = "U"
INTERSECCION = "&"
COMPLEMENTO = "^"
DIFERENCIA = "-"
%{
    StringBuffer string = new StringBuffer();

    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}

%eofval{
    return symbol(ParserSym.EOF);
%eofval}

%%
// Reglas Lexicas (comentarios)

<YYINITIAL> {COMENTARIO_MULTILINEA}   {}
<YYINITIAL> {COMENTARIO_LINEAL}       {}
<YYINITIAL>{Whitespace} {}
// Ejemplo
<YYINITIAL>{conjunto} {Funciones.funcionalidades.agregarToken(yytext(),"CONJUNTO",yyline,yycolumn); return symbol(ParserSym.CONJ, yytext());}
<YYINITIAL>{opera} {Funciones.funcionalidades.agregarToken(yytext(),"OPERA",yyline,yycolumn); return symbol(ParserSym.OPERA, yytext());}
<YYINITIAL>{evaluar} {Funciones.funcionalidades.agregarToken(yytext(),"EVALUAR",yyline,yycolumn); return symbol(ParserSym.EVALUAR, yytext());}
<YYINITIAL>{UNION} {Funciones.funcionalidades.agregarToken(yytext(),"UNION",yyline,yycolumn); return symbol(ParserSym.UNION, yytext());}
<YYINITIAL>{INTERSECCION} {Funciones.funcionalidades.agregarToken(yytext(),"INTERSECCION",yyline,yycolumn); return symbol(ParserSym.INTERSECCION, yytext());}
<YYINITIAL>{COMPLEMENTO} {Funciones.funcionalidades.agregarToken(yytext(),"COMPLEMENTO",yyline,yycolumn); return symbol(ParserSym.COMPLEMENTO, yytext());}
<YYINITIAL>{DIFERENCIA} {Funciones.funcionalidades.agregarToken(yytext(),"DIFERENCIA",yyline,yycolumn); return symbol(ParserSym.DIFERENCIA, yytext());}
<YYINITIAL>{mayusculas} {Funciones.funcionalidades.agregarToken(yytext(),"MAYUSCULA",yyline,yycolumn); return symbol(ParserSym.MAYUSCULA, yytext());}
<YYINITIAL>{minusculas} {Funciones.funcionalidades.agregarToken(yytext(),"MINUSCULA",yyline,yycolumn); return symbol(ParserSym.MINUSCULA, yytext());}
<YYINITIAL>{ID} {Funciones.funcionalidades.agregarToken(yytext(),"ID",yyline,yycolumn); return symbol(ParserSym.ID, yytext());}
<YYINITIAL>{virguilla} {Funciones.funcionalidades.agregarToken(yytext(),"VIRGUILLA",yyline,yycolumn); return symbol(ParserSym.VIRGUILLA, yytext());}
<YYINITIAL>{PUNTOYCOMA} {Funciones.funcionalidades.agregarToken(yytext(),"PUNTOYCOMMA",yyline,yycolumn); return symbol(ParserSym.PUNTOYCOMMA, yytext());}
<YYINITIAL>{COMA} {Funciones.funcionalidades.agregarToken(yytext(),"COMMA",yyline,yycolumn); return symbol(ParserSym.COMMA, yytext());}
<YYINITIAL>{DOS_PUNTOS} {Funciones.funcionalidades.agregarToken(yytext(),"DOS_PUNTOS",yyline,yycolumn); return symbol(ParserSym.DOS_PUNTOS, yytext());}
<YYINITIAL>{ASIGNACION} {Funciones.funcionalidades.agregarToken(yytext(),"ASIGNACION",yyline,yycolumn); return symbol(ParserSym.ASIGNACION, yytext());}
<YYINITIAL>{PARA} {Funciones.funcionalidades.agregarToken(yytext(),"PARA",yyline,yycolumn); return symbol(ParserSym.PARA, yytext());}
<YYINITIAL>{PARC} {Funciones.funcionalidades.agregarToken(yytext(),"PARC",yyline,yycolumn); return symbol(ParserSym.PARC, yytext());}
<YYINITIAL>{LLAVEA} {Funciones.funcionalidades.agregarToken(yytext(),"LLAVEA",yyline,yycolumn); return symbol(ParserSym.LLAVEA, yytext());}
<YYINITIAL>{LLAVEC} {yycolumn += yytext().length(); Funciones.funcionalidades.agregarToken(yytext(),"LLAVEC",yyline,yycolumn); return symbol(ParserSym.LLAVEC, yytext());}
<YYINITIAL>{caracter} {Funciones.funcionalidades.agregarToken(yytext(),"CARACTER",yyline,yycolumn); return symbol(ParserSym.CARACTER, yytext());}

. {Funciones.funcionalidades.agregarError("Lexico","El caracter: '" + yytext() + "' No pertenece al lenguaje",yyline,yycolumn);
     System.err.println("warning: Unrecognized character '" + yytext()+"' -- ignored" + " at : "+ (yyline+1) + " " + yycolumn);}
