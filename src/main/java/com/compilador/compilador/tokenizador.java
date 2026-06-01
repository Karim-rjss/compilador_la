package com.compilador.compilador;

import java.util.ArrayList;

public class tokenizador {
    public String[] palabrasReservadas; //manda llamar reservadas de controller
    public tokenizador(String[] palabrasReservadas) {
        this.palabrasReservadas = palabrasReservadas;
    }

    public ArrayList<String[]> token(String palabra){
        ArrayList<String[]> token = new ArrayList<>();
        int i=0;
        while (i<palabra.length()){ //avance de caracter por caracter
            char ch = palabra.charAt(i);

            if (ch==' '||ch=='\n'||ch=='\r'||ch=='\t'){
                i++;
                continue;
            }

            // ' ' - espacio
            // \n - salto de linea
            // \r - enter
            // \t - tab

            //palabra
            if (Character.isLetter(ch) || ch=='_'){
                int comienzo = i; // caracter inicial de la palabra
                //mientras no se sale del texto y el caracter sea letra, numero o _, sigue avanzando
                while (i<palabra.length() && (Character.isLetter(palabra.charAt(i)) || Character.isDigit(palabra.charAt(i)) || palabra.charAt(i)=='_')){
                    i++;
                }
                String segmento = palabra.substring(comienzo, i);

                boolean palabraReservada = false;
                for (int j=0; j< palabrasReservadas.length; j++){
                    if (palabrasReservadas[j].equals(segmento)){
                        palabraReservada = true;
                        break;
                    }
                }

                if (palabraReservada == true)
                    token.add(new String[]{segmento,"Palabra reservada"});
                else {
                    automatas afd = new automatas();
                            if (afd.identificador(segmento))
                                token.add(new String[]{segmento, "Identificador"});
                            else
                                token.add(new String[]{segmento, "Desconocido"});
                }
            }

            //numero
            else if (Character.isDigit(ch)){
                int comienzo = i; //posicion actual
                //mientras no se acabe el texto y el caracter es un numero o un punto, sigue avanzando
                while (i<palabra.length() && (Character.isDigit(palabra.charAt(i)) || palabra.charAt(i)=='.'))
                    i++;
                if (i < palabra.length() && Character.isLetter(palabra.charAt(i))){ //si despues del numero hay una letra, no es un numero valido
                    while (i < palabra.length() && (Character.isLetter(palabra.charAt(i)) || Character.isDigit(palabra.charAt(i)))){
                        i++;
                    }
                    String segmento = palabra.substring(comienzo, i);
                    token.add(new String[]{segmento, "Desconocido"});
                } else {
                    String segmento = palabra.substring(comienzo, i);
                    automatas afd = new automatas();
                    String tipoNumero = afd.numeros(segmento);
                    if (tipoNumero != null)
                        token.add(new String[]{segmento, tipoNumero});
                    else
                        token.add(new String[]{segmento, "Desconocido"});
                }
            }

            //nuevos operadoress y sigue aanzando
            else{
                String operador = String.valueOf(ch);

                if (i+1<palabra.length()){
                    String doble = String.valueOf(ch) + palabra.charAt(i+1);
                    if (doble.equals("==") || doble.equals("!=") || doble.equals(">=") || doble.equals("<=")) {
                        token.add(new String[]{doble, "Operador"});
                        i += 2;
                        continue;
                    }
                }

                if (ch == '=' || ch == '+' || ch == '-' || ch == '*' || ch == '/') {
                    token.add(new String[]{operador, "Operador"});
                } else if (ch == '>' || ch == '<') {
                    token.add(new String[]{operador, "Operador"});
                } else if (ch == '(' || ch == ')') {
                    token.add(new String[]{operador, "Parentesis"});
                } else if (ch == '{' || ch == '}') {
                    token.add(new String[]{operador, "LlaveS"});
                } else {
                    token.add(new String[]{operador, "Desconocido"});
                }
                i++;

            }

        }
        return token;
    }
}