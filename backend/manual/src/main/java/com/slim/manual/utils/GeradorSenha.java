package com.slim.manual.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public final class GeradorSenha {
    String[] caracteres = {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    String senha = "";


    public String gerarSenha(){
        for (int x=0; x<8; x++){
            int j = (int) (Math.random()*caracteres.length);
            senha += caracteres[j];
        }
        return senha;
    }
}
