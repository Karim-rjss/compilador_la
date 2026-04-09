package com.compilador.compilador;

public class automatas {
    //AFD del identificador
    public boolean identificador(String segmento) {
        if (segmento == null || segmento.isEmpty())
            return false;

        // estados, inicial q0
        int estado = 0;
        for (int i = 0; i < segmento.length(); i++) {
            char ch = segmento.charAt(i);

            switch (estado) {
                case 0: // q0
                    if (Character.isLetter(ch) || ch == '_')
                        estado = 1;
                    else if (Character.isDigit(ch))
                        estado = 2;
                    break;
                case 1: // aceptado
                    if (Character.isLetter(ch) || Character.isDigit(ch) || ch == '_')
                        estado = 1;
                    else
                        estado = 2;
                    break;
                case 2: // rechazado
                    estado = 2;
                    break;
            }
        }

        return estado == 1;
    }

    //AFD de los numeros
    public String numeros(String segmento) {
        if (segmento == null || segmento.isEmpty())
            return null;

        int estado = 0; //q0
        for (int i = 0; i < segmento.length(); i++) {
            char ch = segmento.charAt(i);
            switch (estado) {
                case 0:
                    if (Character.isDigit(ch))
                        estado = 1;
                    else
                        estado = 4;
                    break;
                case 1:
                    if (Character.isDigit(ch))
                        estado = 1;
                    else if (ch == '.')
                        estado = 2;
                    else
                        estado = 4;
                    break;
                case 2:
                case 3:
                    if (Character.isDigit(ch))
                        estado = 3;
                    else
                        estado = 4;
                    break;
                case 4:
                    estado = 4;
                    break;
            }
        }

        if (estado == 1) return "ENTERO";
        if (estado == 3) return "DECIMAL";
        return null;
    }
}