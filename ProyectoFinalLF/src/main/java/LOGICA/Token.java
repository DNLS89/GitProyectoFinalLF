package LOGICA;

import java.awt.Color;

public class Token {
    private String nombre;
    private String tipo;
    private int fila;
    private int columna;
    private Color colorToken;

    public Token(String nombre, String tipo, int fila, int columna, Color colorToken) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.fila = fila;
        this.columna = columna;
        this.colorToken = colorToken;
    }

    public Color getColorToken() {
        return colorToken;
    }

    public void setColorToken(Color colorToken) {
        this.colorToken = colorToken;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    @Override
    public String toString() {
        return "Token{" + "nombre=" + nombre + ", tipo=" + tipo + ", fila=" + fila + ", columna=" + columna + ", colorToken=" + colorToken + '}';
    }
}
