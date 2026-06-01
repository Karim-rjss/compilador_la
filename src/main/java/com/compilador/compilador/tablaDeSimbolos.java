package com.compilador.compilador;

import java.io.IOException;
import java.io.RandomAccessFile;


public class tablaDeSimbolos {

    public static final int TAM_REGISTRO = 32;
    public static final int TAM_TABLA = 100;
    public static final int TAM_RANURA = TAM_REGISTRO * 2;

    public int hash(String token){
        long h = 5381;
        for (int i = 0; i < token.length(); i++) {
            h = h * 33 + token.charAt(i);
        }
        return (int)(Math.abs(h)%TAM_TABLA);
    }



    //creacion del archivo y reescribe las palabras reservadas
    public void crearTabla(String[] palabras) throws IOException {
        RandomAccessFile archrand = new RandomAccessFile("TablaDeSimbolos.dat", "rw");
        archrand.setLength(0);

        byte[] ranuraVacia = new byte[TAM_RANURA];
        for (int i = 0; i < TAM_TABLA; i++) {
            archrand.write(ranuraVacia);
        }

        archrand.close();

        for (int i = 0; i < palabras.length; i++) {
            agregarToken(palabras[i], "Palabra reservada");
        }
    }

    //obtener el token y meterlo al raf
    public void agregarToken(String token, String tipo) throws IOException {
        RandomAccessFile archrand = new RandomAccessFile("TablaDeSimbolos.dat", "rw");

        int posicion = hash(token); //posicion del hash
        int intentos = 0;

        while (intentos<TAM_TABLA){
            archrand.seek((long) posicion*TAM_RANURA); //va a la pos en el arch

            byte[] bytes = new byte[TAM_REGISTRO];
            archrand.readFully(bytes);
            String existente = new String(bytes).trim();

            if (existente.isEmpty()){ //si la pos esta empty, almacena ahi mero
                archrand.seek((long) posicion*TAM_RANURA);
                archrand.writeBytes(String.format("%-" + TAM_REGISTRO + "s", token)); //escribe el token con relleno
                archrand.writeBytes(String.format("%-" + TAM_REGISTRO + "s", tipo)); //escribe el tipo con relleno
                break;
            } else if (existente.equals(token)){ //si el token ya existe, ya no
                break;
            } else {
                System.out.println("Colision en la posición " + posicion + " para el token: " + token);
                posicion = (posicion + 1) % TAM_TABLA; //siguiente pos
                intentos++;
            }
        }
        archrand.close();
    }

    public String buscarToken(String token) throws IOException {
        RandomAccessFile archrand = new RandomAccessFile("TablaDeSimbolos.dat", "r");

        int posicion = hash(token); //posicion del hash
        int intentos = 0;

        while (intentos < TAM_TABLA) {
            archrand.seek((long) posicion * TAM_RANURA); //va a la pos en el arch

            byte[] bytesTokem = new byte[TAM_REGISTRO];
            byte[] bytesTipo = new byte[TAM_REGISTRO];
            archrand.readFully(bytesTokem);
            archrand.readFully(bytesTipo);
            String existente = new String(bytesTokem).trim();

            if (existente.isEmpty()) { //si la pos esta empty, no existe
                System.out.println("Token no encontrado: " + token);
                break;
            } else if (existente.equals(token)) { //si el token ya existe, muestra el tipo
                archrand.close();
                return new String(bytesTipo).trim();
            }

            posicion = (posicion + 1) % TAM_TABLA;
            intentos++;


        }
        archrand.close();
        return null;


    }
}