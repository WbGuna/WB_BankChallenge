package br.com.compass.util;

import org.apache.commons.codec.digest.Sha2Crypt;

public class SenhaUtils {

	public static String criptografarSenha(String senha) {
        return Sha2Crypt.sha256Crypt(senha.getBytes());
    }

    public static boolean validarSenha(String senhaDigitada, String senhaArmazenada) {
        String[] parts = senhaArmazenada.split("\\$");
        String salt = "$" + parts[1] + "$" + parts[2] + "$";
        return Sha2Crypt.sha256Crypt(senhaDigitada.getBytes(), salt).equals(senhaArmazenada);
    }
}
