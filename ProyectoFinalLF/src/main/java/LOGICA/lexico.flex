package LOGICA;

//Seccion de imports
import java.util.ArrayList;
import java.util.List;
import LOGICA.Token;
import java.awt.Color;


%%
%{

// Codigo Java

    private List<Token> lista = new ArrayList<>();
    private List<Token> listaErrores = new ArrayList<>();
    private List<Token> listaColores = new ArrayList<>();
    private Color MORADO = new Color(192, 0, 255);

    public List<Token> getLista(){
        return lista;
    }
    
    public List<Token> getListaErrores(){
        return listaErrores;
    }

    public List<Token> getListaColores() {
        return listaColores;
    }

%}

// Configuracion
%public
%class AnalizadorLexico
%unicode
%line
%column
%standalone

// Expresiones Regulares
//NUMEROS
ENTERO1 = [0-9]
ENTERO2 = [1-9]
ENTERO3 = [1-2]
CERO = [0]
UNO = [1]
DOS = [2]
TRES= [3]

//LETRAS
LETRA = [a-zA-záéíóú]
MINUSCULA = [a-z]
//ESPACIOS = [" "\r\t\b\n]
ESPACIOS = [\r\t\b]
NEWLINE = [\n]
SPACE = [" "]
GUIONBAJO = [_]
TODO = ("_"|"-"|"/"|"."|","|"~"|"!"|"@"|"#"|"$"|"%"|"^"|"&"|"*"|"|"|"("|")"|"="|"+"|"|"|"\"|":"|";"|"""|"<"|">"|"?"|"`"|"{"|"}"|"["|"]")

CREATE = ("CREATE" | "DATABASE" | "TABLE" | "KEY" | "NULL" | "PRIMARY" | "UNIQUE" | "FOREIGN" | 
          "REFERENCES" | "ALTER" | "ADD" | "COLUMN" | "TYPE" | "DROP" | "CONSTRAINT" | "IF" | 
          "EXIST" | "EXISTS" | "CASCADE" | "ON" | "DELETE" | "SET" | "UPDATE" | "INSERT" | "INTO" | 
          "VALUES" | "SELECT" | "FROM" | "WHERE" | "AS" | "GROUP" | "ORDER" | "BY" | 
          "ASC" | "DESC" | "LIMIT" | "JOIN")

TIPODEDATO = ("INTEGER" | "NUMERIC" | "BIGINT" | "VARCHAR" | "DECIMAL" | "DATE" | "TEXT" | "BOOLEAN" | "SERIAL")

//FECHAS
ANO = ({ENTERO1}{ENTERO1}{ENTERO1}{ENTERO1})
//Abajo define meses primero la secuancia 09 y luego la secuencia 10
MES = ({CERO}{ENTERO2}) | ({UNO}({CERO}|{UNO}|{DOS}))
//Abajo define días, el primer elemento es cuando inicia con 0 y el segundo no empieza con 0
DIAS = (({CERO}{ENTERO2}) | ({ENTERO3}{ENTERO1}) | {TRES}({CERO}|{UNO}))



BOOLEANO = ("TRUE" | "FALSE")
FUNCIONAGREGACION = ("SUM" | "AVG" | "COUNT" | "MAX" | "MIN")
SIGNOS = ("(" | ")" | "," | ";" | "." | "=")
ARITMETICOS = ("+" | "-" | "*" | "/")
RELACIONALES = ("<" | ">" | "<=" | ">=")
LOGICOS = ("AND" | "OR" | "NOT")





%%
// Reglas de Escaneo de Expresiones


{CREATE}                                                                     { Token token = new Token(yytext(), "CREATE", yyline + 1, yycolumn + 1, Color.ORANGE); lista.add(token); listaColores.add(token); }
{TIPODEDATO}                                                                 { Token token = new Token(yytext(), "TIPO DE DATO", yyline + 1, yycolumn + 1, MORADO); lista.add(token); listaColores.add(token);}
//Entero
{ENTERO1}+                                                                   { Token token = new Token(yytext(), "ENTERO", yyline + 1, yycolumn + 1, Color.BLUE); lista.add(token); listaColores.add(token);}
//Decimal
{ENTERO1}{ENTERO1}*"."{ENTERO1}{ENTERO1}*                                    { Token token = new Token(yytext(), "DECIMAL", yyline + 1, yycolumn + 1, Color.BLUE); lista.add(token); listaColores.add(token); }

//Fecha
("'" | "‘"){ANO}"-"{MES}"-"{DIAS}("'" | "’")                                                 {Token token = new Token(yytext(), "FECHA", yyline + 1, yycolumn + 1, Color.YELLOW); lista.add(token); listaColores.add(token); }

//CADENA
("'" | "‘")({LETRA} | {ENTERO1} | {TODO})({LETRA} | {SPACE} | {ENTERO1} | {TODO})*("'" | "’")        { Token token = new Token(yytext(), "CADENA", yyline + 1, yycolumn + 1, Color.GREEN); lista.add(token); listaColores.add(token); }
//IDENTIFICADOR
({MINUSCULA} | {GUIONBAJO} | {ENTERO1})+                                     { Token token = new Token(yytext(), "IDENTIFICADOR", yyline + 1, yycolumn + 1, Color.MAGENTA); lista.add(token); listaColores.add(token); }
{BOOLEANO}                                                                   { Token token = new Token(yytext(), "BOOLEANO", yyline + 1, yycolumn + 1, Color.BLUE); lista.add(token); listaColores.add(token); }
{FUNCIONAGREGACION}                                                          { Token token = new Token(yytext(), "FUNCION AGREGACION", yyline + 1, yycolumn + 1, Color.BLUE); lista.add(token); listaColores.add(token); }
{SIGNOS}                                                                     { Token token = new Token(yytext(), "SIGNOS", yyline + 1, yycolumn + 1, Color.BLACK); lista.add(token); listaColores.add(token); }
{ARITMETICOS}                                                                { Token token = new Token(yytext(), "ARITMETICOS", yyline + 1, yycolumn + 1, Color.BLACK); lista.add(token); listaColores.add(token); }
{RELACIONALES}                                                               { Token token = new Token(yytext(), "RELACIONALES", yyline + 1, yycolumn + 1, Color.BLACK); lista.add(token); listaColores.add(token); }
{LOGICOS}                                                                    { Token token = new Token(yytext(), "LOGICOS", yyline + 1, yycolumn + 1, Color.ORANGE); lista.add(token); listaColores.add(token); }


//COMENTARIO
"- -" ({LETRA}|{ENTERO1}|{SPACE})*                                           { Token token = new Token(yytext(), "COMENTARIO", yyline + 1, yycolumn + 1, Color.GRAY); lista.add(token); listaColores.add(token); }

{ESPACIOS}                                                                   { /*Ignore*/ }
{NEWLINE} | {SPACE}                                                                  { Token token = new Token(yytext(), "ESPACIO", yyline + 1, yycolumn + 1, Color.BLACK); listaColores.add(token); }
//{SPACE}                                                                  { Token token = new Token(yytext(), "NEWLINE", yyline + 1, yycolumn + 1, Color.BLACK); listaColores.add(token); }

.                                                                            { Token token = new Token(yytext(), "ERROR", yyline + 1, yycolumn + 1, Color.BLACK); 
                                                                                listaErrores.add(token); lista.add(token); listaColores.add(token); }



