package com.example.danielprimo.nextplace;

/**
 * Created by Daniel Primo on 11/06/2016.
 */
public class TabelaInformacao {
    public String data;
    public String tipo;
    public String nome;
    public String start;
    public int startSeconds;
    public String end;
    public int endSeconds;
    public int duracao;
    public String longitude;
    public String latitude;

    public TabelaInformacao(){

    }
    public TabelaInformacao(String linha){
        String cvsSplitBy=",";
        String dataSplit="T";
        String timeSplit=":";
        int segundos;
        //System.out.println("_________________________0");
        String[] informacao=linha.split(cvsSplitBy);
        tipo=informacao[1].toLowerCase();
        //System.out.println("_________________________1");
        nome=informacao[2].toLowerCase();
        //System.out.println("_________________________2");
        String[] datainformation = informacao[3].split(dataSplit);
        String[] timeinformation = datainformation[1].split(timeSplit);
        if(informacao[3].length()>10) {
            data = datainformation[0];
            //System.out.println("_________________________3-->" + informacao[3]);
            start = datainformation[1];
            //System.out.println("_________________________4");
            segundos = 0;
            segundos = segundos + (Integer.parseInt(timeinformation[0]) * 60 * 60);
            segundos = segundos + (Integer.parseInt(timeinformation[1]) * 60);
            segundos = segundos + (Integer.parseInt(timeinformation[1]));
            startSeconds = segundos;
        }
        else{
            data = "null";
            start = "null";
            startSeconds = 0;
        }
        if(informacao[4].length()>10) {
            //System.out.println("_________________________5");
            datainformation = informacao[4].split(dataSplit);
            end = datainformation[1];

            timeinformation = datainformation[1].split(timeSplit);
            segundos = 0;
            segundos = segundos + (Integer.parseInt(timeinformation[0]) * 60 * 60);
            segundos = segundos + (Integer.parseInt(timeinformation[1]) * 60);
            segundos = segundos + (Integer.parseInt(timeinformation[1]));
            endSeconds = segundos;
        }
        else{
            end="null";
        }
        //System.out.println("_________________________6-->"+informacao[5]);
        duracao=Integer.parseInt(informacao[5]);
        //System.out.println("_________________________7");
    }
}
