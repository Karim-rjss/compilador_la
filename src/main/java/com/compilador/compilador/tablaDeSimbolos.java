package com.compilador.compilador;

import java.io.IOException;
import java.io.RandomAccessFile;


public class tablaDeSimbolos {

    public int TAM_REGISTRO = 32;

    //creacion del archivo y reescribe las palabras reservadas
    public void crearTabla(String[] palabras) throws IOException {
        RandomAccessFile archrand = new RandomAccessFile("TablaDeSimbolos.dat", "rw");
        archrand.setLength(0);

        for (int i = 0; i < palabras.length; i++) {
            String registro = String.format("%-" + TAM_REGISTRO + "s", palabras[i]);
            archrand.writeBytes(registro);
        }
        archrand.close();
    }

    //lectura de las palabras (entradas), despues reescribe las pal resrvadas
    public String[] leerTabla() throws IOException {
        RandomAccessFile archrand = new RandomAccessFile("TablaDeSimbolos.dat", "r");
        int totalRegistros = (int) (archrand.length() / TAM_REGISTRO);
        String[] entradas = new String[totalRegistros];

        for (int i = 0; i < totalRegistros; i++) {
            byte[] bytes = new byte[TAM_REGISTRO];
            archrand.readFully(bytes);
            entradas[i] = new String(bytes).trim(); //trim quita los espacios de relleno
        }
        archrand.close();
        return entradas;
    }

    //obtener el token y meterlo al raf
    public void agregarToken(String segmento, String token) throws IOException {
        RandomAccessFile archrand = new RandomAccessFile("TablaDeSimbolos.dat", "rw");
        archrand.seek(archrand.length()); //mover el puntero al final del archivo
        String addSegmento = String.format("%-" + TAM_REGISTRO + "s", segmento);
        String addToken = String.format("%-" + TAM_REGISTRO + "s", token);
        archrand.writeBytes(addSegmento);
        archrand.writeBytes(addToken);
        archrand.close();
    }

}