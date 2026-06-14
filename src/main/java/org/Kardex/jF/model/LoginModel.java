package org.Kardex.jF.model;

import java.util.Scanner;

public class LoginModel {

    private static final String USUARIO_VALIDO = "admin";
    private static final String CLAVE_VALIDA = "1234";

    public boolean login() {

        Scanner sc = new Scanner(System.in);

        System.out.println("=== LOGIN ===");
        System.out.print("Ingrese usuario: ");
        String inputUsuario = sc.nextLine();

        System.out.print("Ingrese contraseña: ");
        String inputClave = sc.nextLine();

        sc.close();

        boolean accesoCorrecto = inputUsuario.equals(USUARIO_VALIDO) && inputClave.equals(CLAVE_VALIDA);

        if (accesoCorrecto) {
            System.out.println("Bienvenido al sistema");
            return true;
        }

        System.out.println("Usuario o contraseña incorrectos");
        return false;
    }
}