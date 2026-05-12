package com.example.oodlegame.util;

import java.util.Arrays;

public class JuegoValidator {

    public static boolean esNumeroValido(String texto) {
        return texto != null && texto.trim().matches("[1-9]");
    }

    public static String[] evaluarIntento(int[] numerosIntento, int[] solucionNums) {
        if (numerosIntento == null || solucionNums == null || numerosIntento.length != 4 || solucionNums.length != 4) {
            throw new IllegalArgumentException("El intento y la solucion deben tener exactamente 4 numeros.");
        }

        String[] estados = new String[4];
        boolean[] solucionUsada = new boolean[4];
        boolean[] intentoUsado = new boolean[4];

        for (int i = 0; i < 4; i++) {
            if (numerosIntento[i] == solucionNums[i]) {
                estados[i] = "CORRECT";
                solucionUsada[i] = true;
                intentoUsado[i] = true;
            }
        }

        for (int i = 0; i < 4; i++) {
            if (intentoUsado[i]) {
                continue;
            }

            for (int j = 0; j < 4; j++) {
                if (!solucionUsada[j] && numerosIntento[i] == solucionNums[j]) {
                    estados[i] = "PARTIAL";
                    solucionUsada[j] = true;
                    intentoUsado[i] = true;
                    break;
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            if (estados[i] == null) {
                estados[i] = "WRONG";
            }
        }

        return estados;
    }

    public static boolean esVictoria(String[] estados) {
        if (estados == null || estados.length != 4) {
            return false;
        }

        for (String estado : estados) {
            if (!"CORRECT".equals(estado)) {
                return false;
            }
        }

        return true;
    }

    public static boolean esDivisionValida(int a, String op1, int b, String op2, int c, String op3, int d) {
        int[] nums = { a, b, c, d };
        String[] opArr = { op1, op2, op3 };

        for (int i = 0; i < opArr.length; i++) {
            if (opArr[i].equals("/")) {
                if (nums[i + 1] == 0 || nums[i] % nums[i + 1] != 0) {
                    return false;
                }
            }
        }

        return true;
    }

    public static int calcularResultado(int a, String op1, int b, String op2, int c, String op3, int d) {
        int[] nums = { a, b, c, d };
        String[] opArr = { op1, op2, op3 };

        for (int i = 0; i < opArr.length; i++) {
            if (opArr[i].equals("*") || opArr[i].equals("/")) {
                int val = opArr[i].equals("*") ? nums[i] * nums[i + 1] : nums[i] / nums[i + 1];
                nums[i] = val;

                for (int j = i + 1; j < nums.length - 1; j++) {
                    nums[j] = nums[j + 1];
                    opArr[j - 1] = opArr[j];
                }

                opArr = Arrays.copyOf(opArr, opArr.length - 1);
                nums = Arrays.copyOf(nums, nums.length - 1);
                i--;
            }
        }

        int resultado = nums[0];

        for (int i = 0; i < opArr.length; i++) {
            if (opArr[i].equals("+")) {
                resultado += nums[i + 1];
            } else if (opArr[i].equals("-")) {
                resultado -= nums[i + 1];
            }
        }

        return resultado;
    }
}
