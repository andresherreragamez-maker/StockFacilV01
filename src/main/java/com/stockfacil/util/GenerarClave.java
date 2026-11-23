package com.stockfacil.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Semana 10. Demostración: cómo se ve una contraseña cifrada con BCrypt. Se
 * ejecuta con Shift+F6.
 */
public class GenerarClave {

    public static void main(String[] args) {
        String clave = "admin123";
        String hash1 = BCrypt.hashpw(clave, BCrypt.gensalt());
        String hash2 = BCrypt.hashpw(clave, BCrypt.gensalt());
        System.out.println("Hash 1: " + hash1);
        System.out.println("Hash 2: " + hash2);
        
        System.out.println("¿admin123 coincide con el hash 1? " + BCrypt.checkpw("admin123", hash1));
        System.out.println("¿Admin123 coincide con el hash 1? " + BCrypt.checkpw("Admin123", hash1));
    }
}
