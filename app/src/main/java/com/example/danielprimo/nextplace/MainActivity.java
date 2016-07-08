package com.example.danielprimo.nextplace;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.xml.transform.Templates;

public class MainActivity extends AppCompatActivity {
    ArrayList<TabelaInformacao> tabela=new ArrayList<TabelaInformacao>();
    ArrayList<MatrizConfusao> auxMatrix=new ArrayList<MatrizConfusao>();
    public TabelaInformacao[] listaLocais;
    public TabelaInformacao[][] listaNonLinearLocais=new TabelaInformacao[5][];
    public int superIndex;
    public static int[] listaPontos;
    public static int[] listaDuracao;
    public static int[] listaStartTime;
    public static String[] listaNomes;
    public static String[][] listaLatLon;
    public static int superTamanho;
    public static int superFound;
    String globalTime;
    String globalPlace;

    Button okbutton;
    Button mapabutton;
    EditText place;
    EditText hora;
    TextView info;
    private int cond1,cond2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        int i;

        cond1=0;
        cond2=0;
        okbutton=(Button)findViewById(R.id.okButton);
        place=(EditText)findViewById(R.id.place);
        hora=(EditText)findViewById(R.id.hora);
        info=(TextView)findViewById(R.id.info);
        mapabutton=(Button)findViewById(R.id.mapaButton);
        mapabutton.setEnabled(false);
        okbutton.setEnabled(false);
        place.setText("");
        hora.setText("");
        place.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.equals("")) {
                    okbutton.setEnabled(false);
                    cond1=0;
                } else {
                    if (cond2 == 1) {
                        okbutton.setEnabled(true);
                    } else {
                        okbutton.setEnabled(false);
                    }
                    cond1 = 1;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        hora.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.equals("")) {
                    okbutton.setEnabled(false);
                    cond2=0;
                } else {
                    if (cond1 == 1) {
                        okbutton.setEnabled(true);
                    } else {
                        okbutton.setEnabled(false);
                    }
                    cond2=1;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int control;
                globalPlace=place.getText().toString().toLowerCase();
                globalTime=hora.getText().toString().toLowerCase();
                control=ondeVouEstar(tabela.size()-1);
                if(control==1){
                    calculaSitio();
                }
                else{
                    control=ondeEstouNonLinear(tabela.size()-1);
                    if(control==1){
                        calculaSitio();
                    }
                    mapabutton.setEnabled(true);
                }

            }
        });
        try {
            carregaBD();
            complementaBD();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int j;
        //matrixTest();
        System.out.println("\n\n\tMatrix Confusão:");
        for(i=0;i<auxMatrix.size();i++){
            System.out.println("\t\tNome:"+auxMatrix.get(i).nome);
            for(j=0;j<auxMatrix.get(i).dados.size();j++){
                System.out.println("\t\t\t\tNome "+auxMatrix.get(i).dados.get(j).nome+"\tNumero "+auxMatrix.get(i).dados.get(j).numero);
            }
        }
        /*
        for(i=0;i<tabela.size();i++){
            System.out.println("------>Data: "+tabela.get(i).data+"\tTipo: "+tabela.get(i).tipo+"\tNome: "+tabela.get(i).nome+"\tStart: "+tabela.get(i).start+"\tEnd: "+tabela.get(i).end+"\tStartTime: "+tabela.get(i).startSeconds+"\nDuration: "+tabela.get(i).duracao+"\tLatitude: "+tabela.get(i).latitude+"\tLongitude: "+tabela.get(i).longitude);
        }*/

        //ondeEstouNonLinear();
        //calculaSitio();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void matrixTest(){
        int i;
        int[] auxTempo;
        int tama;
        TabelaInformacao auxTabela=new TabelaInformacao();
        for(i=884;i<1328;i++){
            if(tabela.get(i).tipo.equals("place")){
                globalPlace=tabela.get(i).nome;
                auxTempo=defineHoradetalhada(tabela.get(i).startSeconds+(tabela.get(i).duracao/2));
                globalTime=auxTempo[0]+":"+auxTempo[1];
                int control;
                control=ondeVouEstar(i);
                if(control==1){
                    calculaSitio();
                    tama=listaLocais.length-1;
                    auxTabela=foundNextPlace(i,0);
                    verificaExistencia(auxTabela.nome,listaLocais[tama].nome);
                }
                else{
                    control=ondeEstouNonLinear(i);
                    if(control==1){
                        calculaSitio();
                        tama=listaLocais.length-1;
                        auxTabela=foundNextPlace(i,0);
                        verificaExistencia(auxTabela.nome, listaLocais[tama].nome);
                    }
                    else{
                        auxTabela=foundNextPlace(i,0);
                        verificaExistencia(auxTabela.nome,"Outro");
                    }
                }/*
                control=ondeEstouNonLinear(i);
                if(control==1){
                    calculaSitio();
                    tama=listaLocais.length-1;
                    auxTabela=foundNextPlace(i,0);
                    verificaExistencia(auxTabela.nome, listaLocais[tama].nome);
                }
                else{
                    auxTabela=foundNextPlace(i,0);
                    verificaExistencia(auxTabela.nome,"Outro");
                }*/

            }
        }
    }

    public void verificaExistencia(String origem, String resultado){
        int i;
        int found;
        found=0;
        for(i=0;i<auxMatrix.size();i++){
            if(auxMatrix.get(i).nome.equals(origem)){
                found=1;
                break;
            }
        }
        if(found==1){
            auxMatrix.get(i).setDadosMatriz(resultado);
        }
        else{
            auxMatrix.add(new MatrizConfusao(origem,resultado));
        }

    }

    public void complementaBD() throws IOException {
        // String csvFile=""
        //BufferedReader buff =new BufferedReader(new InputStreamReader(getResources().getAssets().open("places_2016-05.csv")));
        BufferedReader buff=null;
        String linha ="";
        String cvsSplitBy=",";
        int i=0;
        int j=0;
        try{
            buff =new BufferedReader(new InputStreamReader(getResources().getAssets().open("places_2016.csv")));
            //linha=buff.readLine();
            while((linha=buff.readLine())!=null){
                //System.out.println("____________________________________________----"+i);
                if(i!=0) {
                    //adicionaTabela(linha);
                    String[] informacao=linha.split(cvsSplitBy);
                    while(j<tabela.size()){
                        if(tabela.get(j).tipo.equals("place")){
                            tabela.get(j).latitude=informacao[5];
                            tabela.get(j).longitude=informacao[6];
                            j++;
                            break;
                        }
                        j++;
                    }
                }
                i++;
            }

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (buff != null) {
                try {
                    buff.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void carregaBD() throws IOException {
        // String csvFile=""
        //BufferedReader buff =new BufferedReader(new InputStreamReader(getResources().getAssets().open("places_2016-05.csv")));
        BufferedReader buff=null;
        String linha ="";
        int i=0;
        try{
            buff =new BufferedReader(new InputStreamReader(getResources().getAssets().open("storyline_2016.csv")));
            //linha=buff.readLine();
            while((linha=buff.readLine())!=null){
                //System.out.println("____________________________________________----"+i);
                if(i!=0) {
                    tabela.add(new TabelaInformacao(linha));
                }
                i++;
            }

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (buff != null) {
                try {
                    buff.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int ondeVouEstar(int inicio){
        TabelaInformacao auxTabela=new TabelaInformacao();
        int i;
        int found;
        int c,index;
        int time;
        String[] auxTime;
        String place;
        String data;
        c=10;
        index=0;
        place=globalPlace;
        auxTime=globalTime.split(":");
        time=(Integer.parseInt(auxTime[0])*60*60)+(Integer.parseInt(auxTime[1])*60);
        //time=(10*60*60)+(45*60);
        //place="DEI";
        //data="2016-06-11";
        listaLocais=new TabelaInformacao[c];
        found=0;
        for(i=inicio;i>=0;i--){
            auxTabela=tabela.get(i);
            //System.out.println("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD-> PASSEI");
            if((auxTabela.startSeconds-600)<=time && (auxTabela.endSeconds+600)>=time && i!=tabela.size()-1){
                if(auxTabela.nome.equals(place)){
                    //System.out.println("IIIIIIIIIIIIIIIIIIIII-> "+i+"ppppppp->"+auxTabela.nome);
                    listaLocais[index]=foundNextPlace(i,1);
                    //System.out.println("-OOOOOOOOOOOOOOOOOOOOOOOO----->Data: "+listaLocais[index].data+"\tTipo: "+listaLocais[index].tipo+"\tNome: "+listaLocais[index].nome+"\tStart: "+listaLocais[index].start+"\tEnd: "+listaLocais[index].end+"\tStartTime: "+listaLocais[index].startSeconds+"\nDuration: "+listaLocais[index].duracao);
                    index++;
                }
            }
            if(index>=c){
                //System.out.println("çççççççççççççççççççççççççççççççççççççççççççççç-> FOUND FOUND FOUND");
                found=1;
                break;
            }
        }

        return found;
    }

    public TabelaInformacao foundNextPlace(int indice, int type){
        int i;
        int count;
        //System.out.println("-----------------------------------BAAAAAAAAAAAAAAAAAAH!!!");
        TabelaInformacao auxTabela=new TabelaInformacao();
        count=0;
        for(i=indice+1;i<tabela.size();i++){
            auxTabela=tabela.get(i);
            if(type==1){
                if(auxTabela.tipo.equals("place")){
                    break;
                }
            }
            else{
                if(auxTabela.tipo.equals("place")){
                    count++;
                }
                if(count==2){
                    break;
                }
            }

        }
        return auxTabela;
    }

    public void calculaSitio(){
        int i,j,found,auxTam,tamanho;
        auxTam=0;
        found=0;
        String InformacaoTotal="";
        tamanho=listaLocais.length;
        //superTamanho=tamanho;
        listaPontos=new int[tamanho];
        listaDuracao=new int[tamanho];
        listaStartTime=new int[tamanho];
        listaNomes=new String[tamanho];
        listaLatLon=new String[tamanho][];
        for(i=0;i<tamanho;i++){
            listaPontos[i]=0;
            listaDuracao[i]=0;
            listaStartTime[i]=0;
            listaLatLon[i]=new String[2];
        }
        for(i=0;i<tamanho;i++){
            found=0;
            //System.out.println("------>Data: "+listaLocais[i].data+"\tTipo: "+listaLocais[i].tipo+"\tNome: "+listaLocais[i].nome+"\tStart: "+listaLocais[i].start+"\tEnd: "+listaLocais[i].end+"\tStartTime: "+listaLocais[i].startSeconds+"\nDuration: "+listaLocais[i].duracao);
            for(j=0;j<auxTam;j++){
                if(listaNomes[j].equals(listaLocais[i].nome)){
                    listaPontos[j]++;
                    listaDuracao[j]= listaDuracao[j]+listaLocais[i].duracao;
                    listaStartTime[j]=listaStartTime[j]+listaLocais[i].startSeconds;
                    found=1;
                    break;
                }
            }
            if(found==0){
                listaNomes[auxTam]=listaLocais[i].nome;
                listaDuracao[auxTam]= listaLocais[i].duracao;
                listaStartTime[auxTam]=listaLocais[i].startSeconds;
                listaPontos[auxTam]=1;
                listaLatLon[auxTam][0]=listaLocais[i].latitude;
                listaLatLon[auxTam][1]=listaLocais[i].longitude;
                auxTam++;
            }
        }
        superTamanho=auxTam;
        j=0;
        double auxHoraMin;
        int auxHora;
        int auxMin;
        int[] horaStartDetalhe=new int[3];
        int[] horaStayDetalhe=new int[3];
        int[] horaEndDetalhe=new int[3];
        InformacaoTotal=InformacaoTotal+"Proximas possiveis posições:\n";
        ordenaLista(0,auxTam-1);
        for(i=auxTam-1;i>=0;i--){
            //System.out.println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz: "+listaPontos[i]+" ------> "+listaStartTime[i]);
            if(j<listaPontos[i]){
                j=listaPontos[i];
                found=i;
            }
            horaStartDetalhe=defineHoradetalhada(listaStartTime[i]/listaPontos[i]);
            horaStayDetalhe=defineHoradetalhada(listaDuracao[i]/listaPontos[i]);
            horaEndDetalhe=defineHoradetalhada((listaStartTime[i]/listaPontos[i])+(listaDuracao[i]/listaPontos[i]));

            InformacaoTotal=InformacaoTotal+"\nPlace: "+listaNomes[i]+"\nChegada Prevista: "+horaStartDetalhe[0]+":"+horaStartDetalhe[1]+"\nDuracao: "+horaStayDetalhe[0]+":"+horaStayDetalhe[1]+"\nPartida Prevista: "+horaEndDetalhe[0]+":"+horaEndDetalhe[1]+"\nPontos: "+listaPontos[i]+"\n";
        }
        superFound=found;
        info.setText(InformacaoTotal);
        System.out.println("ENCONTREI ONDE VAIS ESTAR A SEGUIR!!!! --> "+listaNomes[found]+" -Duracao-> "+(listaDuracao[found]/listaPontos[found])+" -StartTime->"+(listaStartTime[found]/listaPontos[found]));
    }

    public static int[] defineHoradetalhada(int total)
    {
        //long longVal = total.longValue();
        int hours = total / 3600;
        int remainder = total - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        int[] ints = {hours , mins , secs};
        return ints;
    }

    public void ordenaLista(int low, int high){
        if(listaPontos==null || listaPontos.length==0){
            return;
        }
        if(low>=high){
            return;
        }

        int middle=low+(high-low);
        int pivot=listaPontos[middle];

        int i=low,j=high;

        while (i<=j){
            while(listaPontos[i]<pivot){
                i++;
            }
            while(listaPontos[j]>pivot){
                j--;
            }
            if(i<=j){
                int temp=listaPontos[i];
                listaPontos[i]=listaPontos[j];
                listaPontos[j]=temp;

                int dur=listaDuracao[i];
                listaDuracao[i]=listaDuracao[j];
                listaDuracao[j]=dur;

                int star=listaStartTime[i];
                listaStartTime[i]=listaStartTime[j];
                listaStartTime[j]=star;

                String nome=listaNomes[i];
                listaNomes[i]=listaNomes[j];
                listaNomes[j]=nome;

                String[] coord=listaLatLon[i];
                listaLatLon[i]=listaLatLon[j];
                listaLatLon[j]=coord;

                TabelaInformacao tabInfo=listaLocais[i];
                listaLocais[i]=listaLocais[j];
                listaLocais[j]=tabInfo;

                i++;
                j--;
            }
        }

        if(low<j){
            ordenaLista(low,j);
        }
        if(high>i){
            ordenaLista(i,high);
        }
    }

    public int ondeEstouNonLinear(int inicio){
        TabelaInformacao auxTabela=new TabelaInformacao();
        int i,j;
        int found;
        int m,index;
        int time;
        String[] auxTime;
        String place;
        String data;
        m=3;
        index=0;
        place=globalPlace;
        auxTime=globalTime.split(":");
        time=(Integer.parseInt(auxTime[0])*60*60)+(Integer.parseInt(auxTime[1])*60);
        //time=(10*60*60)+(45*60);
        //place="DEI";
        //data="2016-06-11";
        found=0;
        listaLocais=new TabelaInformacao[4];
        //System.out.println("--------------------------------------------------------------Ca esta ele!!");
        for(i=0;i<5;i++){
            listaNonLinearLocais[i]=new TabelaInformacao[m];
        }

        for(i=inicio;i>=0;i--){
            //System.out.println("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ ");
            //System.out.println("-->Nome: "+auxTabela.nome+"starTime: "+auxTabela.startSeconds+"EndTime: "+auxTabela.endSeconds+"Data: "+auxTabela.data);
            auxTabela=tabela.get(i);
            if((auxTabela.startSeconds-600)<=time && (auxTabela.endSeconds+600)>=time && i!=tabela.size()-1){
                break;
            }
            else if((auxTabela.startSeconds-600)<=time && (auxTabela.startSeconds+auxTabela.duracao)>86400){
                break;
            }
            else if(((auxTabela.startSeconds-600)>=time) && (auxTabela.endSeconds+600)>=time){
                break;
            }
        }
        for(j=i-1;j>=0;j--){
            auxTabela=tabela.get(j);
            //System.out.println("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ");
            if(auxTabela.tipo.equals("place")){
                listaNonLinearLocais[0][index]=auxTabela;
                //System.out.println("------>Data: "+listaNonLinearLocais[0][index].data+"\tTipo: "+listaNonLinearLocais[0][index].tipo+"\tNome: "+listaNonLinearLocais[0][index].nome+"\tStart: "+listaNonLinearLocais[0][index].start+"\tEnd: "+listaNonLinearLocais[0][index].end+"\tStartTime: "+listaNonLinearLocais[0][index].startSeconds+"\nDuration: "+listaNonLinearLocais[0][index].duracao);
                index++;
            }
            if(index>=m){
                break;
            }
        }

        superIndex=1;
        int auxI;
        for(i=j-1;i>=0;i--){
            auxTabela=tabela.get(i);
            if(listaNonLinearLocais[0][0].endSeconds<=auxTabela.endSeconds+600 && listaNonLinearLocais[0][0].endSeconds>=auxTabela.startSeconds && listaNonLinearLocais[0][0].nome.equals(auxTabela.nome)){
                //System.out.println("--------------------------------------------------->BLUUUUUUUUUUUUUUUH!! -->"+i);
                listaLocais[superIndex-1]=foundNextPlace(i,2);
                auxI=constroiNonLinear(i,m);
                i=auxI;
            }
            if(superIndex>=5){
                found=1;
                break;
            }
        }

        return found;
    }

    public int constroiNonLinear(int pos, int m){
        TabelaInformacao auxTabela=new TabelaInformacao();
        int i,j;
        int auxM;
        int auxCond;
        auxCond=0;
        auxM=0;
        for(i=pos;i>=0;i--){
            auxTabela=tabela.get(i);
            if(auxTabela.tipo.equals("place")){
                //System.out.println("--------------------------------------------------->ENTREIIIIIIIIII!! -->"+i);
                if(listaNonLinearLocais[0][auxM].nome.equals(auxTabela.nome)){
                    //System.out.println("---------------------------------------------------> "+auxM+"-----"+auxCond);
                    listaNonLinearLocais[superIndex][auxM]=auxTabela;
                    auxM++;
                    auxCond++;
                }
                else{
                    //System.out.println("---------------------------------------------------> "+auxCond);
                    auxCond++;
                }

            }
            if(auxM==m || auxCond==m){
                break;
            }
        }

        if(auxCond==m && auxM==m){
            //System.out.println("---------------------------------------------------> "+superIndex);
            /*for(j=0;j<auxCond;j++){
                System.out.println("------>Data: "+listaNonLinearLocais[superIndex][j].data+"\tTipo: "+listaNonLinearLocais[superIndex][j].tipo+"\tNome: "+listaNonLinearLocais[superIndex][j].nome+"\tStart: "+listaNonLinearLocais[superIndex][j].start+"\tEnd: "+listaNonLinearLocais[superIndex][j].end+"\tStartTime: "+listaNonLinearLocais[superIndex][j].startSeconds+"\nDuration: "+listaNonLinearLocais[superIndex][j].duracao);
            }*/
            superIndex++;
        }
        return i;
    }

    /*
    public void constroiMapa(View view){
        Intent intent=new Intent(this,Criacao.class);
        intent.putExtra("EXTRA", "Polo2");
        startActivity(intent);
    }*/

    public void mapa(View view){
        Intent intent=new Intent(this,showMap.class);
        //intent.putExtra("EXTRA", );
        startActivity(intent);
    }
}
