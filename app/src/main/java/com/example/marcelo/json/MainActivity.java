package com.example.marcelo.json;

import com.example.marcelo.json.dominio.ApiEndpoint;
import com.example.marcelo.json.dominio.Post;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.PATCH;

public class MainActivity extends Activity implements View.OnKeyListener {
    private TextView mensagem;
    private EditText entrada;
    private int n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preparacaoInicial();
    }

    private void preparacaoInicial() {
        entrada = findViewById(R.id.editTextEntrada);
        entrada.setOnKeyListener(this);
    }

    private void tratarDadosEntrada() {
        entrada = findViewById(R.id.editTextEntrada);
        try {
            if (Integer.parseInt(entrada.getText().toString()) > 0){
                n = Integer.parseInt(entrada.getText().toString());
                construirJSON();
            }else {
                caixaDeTexto();
            }
        } catch (Exception e) {
            caixaDeTexto();
        }
    }

    private void construirJSON() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create(gson)).build();
        ApiEndpoint apiService = retrofit.create(ApiEndpoint.class);
        Call<Post> call = apiService.obeterPost(n);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                int statusCode = response.code();
                if (statusCode != 404){
                    Post post = response.body();
                    mensagem = findViewById(R.id.txtCampo);
                    entrada = findViewById(R.id.editTextEntrada);

                    mensagem.setText(post.getTitle());
                    entrada.setText("");
                }else{
                    caixaDeTexto(String.valueOf(statusCode));
                }

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                mensagem.setText(t.toString());
            }
        });
    }
    public void acaoBotao (View v){
        tratarDadosEntrada();
    }
    private void caixaDeTexto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        entrada = findViewById(R.id.editTextEntrada);

        builder.setTitle("Atenção");

        builder.setMessage("Só é permitido números inteiros natuais positivos, " + entrada.getText().toString() + ", não se aplica.")
                .setCancelable(false)
                .setPositiveButton("OK (reiniciar)", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancelar (exit)", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                        System.exit(0);
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void caixaDeTexto(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        entrada = findViewById(R.id.editTextEntrada);

        builder.setTitle("Atenção");

        builder.setMessage("Erro, " + s +", Post: "+ entrada.getText().toString() + ", não encontrado.")
                .setCancelable(false)
                .setPositiveButton("OK (reiniciar)", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancelar (exit)", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                        System.exit(0);
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
            tratarDadosEntrada();
            return true;
        }
        return false;
    }
}
