package com.example.danielprimo.nextplace;

import java.util.ArrayList;

/**
 * Created by Daniel Primo on 15/06/2016.
 */
public class MatrizConfusao {
    public ArrayList<DadosMatriz> dados=new ArrayList<DadosMatriz>();
    public String nome;
    public MatrizConfusao(String aux,String diretivo){
        nome=aux;
        dados.add(new DadosMatriz(diretivo));

    }

    public void setDadosMatriz(String diretivo) {
        int i;
        int found;
        found = 0;
        for (i = 0; i < dados.size(); i++) {
            if (dados.get(i).nome.equals(diretivo)) {
                found = 1;
                break;
            }
        }
        if (found == 1) {
            dados.get(i).numero++;
        } else {
            dados.add(new DadosMatriz(diretivo));
        }
    }
}
