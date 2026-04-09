package com.compilador.compilador;

public class filaTabla {

    public String segmento;
    public String tipo;

    public filaTabla(String segmento, String tipo) {
        this.segmento = segmento;
        this.tipo = tipo;
    }

    public String getSegmento() {
        return segmento; }
    public String getTipo()   {
        return tipo;   }
}