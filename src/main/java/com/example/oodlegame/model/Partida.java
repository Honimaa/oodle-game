package com.example.oodlegame.model;

import java.time.LocalDateTime;
import java.util.Random;

public class Partida {

    private int id;
    private Usuario usuario;
    private String ecuacionObjetivo;
    private int intentosUsados;
    private boolean victoria;
    private LocalDateTime fecha;

    public Partida() {
    }

    public Partida(int id, Usuario usuario, String ecuacionObjetivo, int intentosUsados, boolean victoria, LocalDateTime fecha) {
        this.id = id;
        this.usuario = usuario;
        this.ecuacionObjetivo = ecuacionObjetivo;
        this.intentosUsados = intentosUsados;
        this.victoria = victoria;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getEcuacionObjetivo() {
        return ecuacionObjetivo;
    }

    public void setEcuacionObjetivo(String ecuacionObjetivo) {
        this.ecuacionObjetivo = ecuacionObjetivo;
    }

    public int getIntentosUsados() {
        return intentosUsados;
    }

    public void setIntentosUsados(int intentosUsados) {
        this.intentosUsados = intentosUsados;
    }

    public boolean isVictoria() {
        return victoria;
    }

    public void setVictoria(boolean victoria) {
        this.victoria = victoria;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }


    private static boolean esDivisionValida(int a, String op1, int b, String op2, int c, String op3, int d){
        int[] nums = {a, b, c, d};
        String[] ops = {op1, op2, op3};

        for (int i = 0; i< ops.length; i++){
            if (ops[i].equals("/")){
                if (nums[i+1] == 0 || nums[i] % nums[i+1] !=0){
                    return false;
                }
            }
        }
        return true;
    }


    private static int calcularResultado(int a, String op1, int b, String op2, int c, String op3, int d) {
        int[] nums = {a, b, c, d};
        String[] ops = {op1, op2, op3};

        for (int i = 0; i < ops.length; i++) {
            if (ops[i].equals("*")) {
                nums[i] = nums[i] * nums[i + 1];
                for (int j = i + 1; j < nums.length - 1; j++) {
                    nums[j] = nums[j + 1];
                    ops[j - 1] = ops[j];
                }
                ops = java.util.Arrays.copyOf(ops, ops.length - 1);
                nums = java.util.Arrays.copyOf(nums, nums.length - 1);
            }
        }
        int resultado = nums[0];
        for (int i = 0; i < ops.length; i++) {
            if (ops[i].equals("+")) {
                resultado += nums[i + 1];
            } else if (ops[i].equals(("-"))) {
                resultado -= nums[i + 1];
            }
        }
        return resultado;
    }


    public static String generarEcuacion(){
        Random rand = new Random();
        String[] operadores = {"+", "-", "*", "/"};

        int intentos = 0;
        while (intentos < 1000){
            int a = rand.nextInt(9) + 1;
            int b = rand.nextInt(9) + 1;
            int c = rand.nextInt(9) + 1;
            int d = rand.nextInt(9) + 1;

            String op1 = operadores[rand.nextInt(operadores.length)];
            String op2 = operadores[rand.nextInt(operadores.length)];
            String op3 = operadores[rand.nextInt(operadores.length)];

            if (!esDivisionValida(a, op1, b, op2, c, op3, d)){
                intentos ++;
                continue;
            }

            int resultado = calcularResultado(a, op1, b, op2, c, op3, d);

            if (Math.abs(resultado) <= 99) {
                return a + op1 + b + op2 + c + op3 + d + "=" + resultado;
            }
            intentos ++;
        }
        return "2*3+4-2=8";
    }
}

