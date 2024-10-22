package LOGICA;

public class Token {
    private String nombre;
    private String expresionRegular;
    private String lenguaje;
    private String tipo;
    private int fila;
    private int columna;
    private String idiomaSugerido = "";

    public Token(String nombre, String expresionRegular, String lenguaje, String tipo, int fila, int columna) {
        this.nombre = nombre;
        this.expresionRegular = expresionRegular;
        this.lenguaje = lenguaje;
        this.tipo = tipo;
        this.fila = fila;
        this.columna = columna;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getExpresionRegular() {
        return expresionRegular;
    }

    public void setExpresionRegular(String expresionRegular) {
        this.expresionRegular = expresionRegular;
    }

    public String getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(String lenguaje) {
        this.lenguaje = lenguaje;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public String getIdiomaSugerido() {
        return idiomaSugerido;
    }

    public void setIdiomaSugerido(String idiomaSugerido) {
        this.idiomaSugerido = idiomaSugerido;
    }
    
    

    @Override
    public String toString() {
        return "Token{" + "nombre= " + nombre + " , expresionRegular= " + expresionRegular + ", lenguaje= " + lenguaje + ", tipo= " + tipo + ", fila= " + fila + ", columna= " + columna + '}';
    }
}
