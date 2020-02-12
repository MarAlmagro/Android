package com.example.appdos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private HashMap<String, Double> tiposDeCambio = new HashMap<String, Double>();
    private TextView nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        nombre = findViewById(R.id.tvNombre);
    }

    public void iniciarSegundaActividad(View v){
        inicializarTiposDeCambio();
        Intent intent = new Intent(this,SegundaActividad.class);
        intent.putExtra("tiposdecambio", tiposDeCambio);
        startActivity(intent);
    }

    public void inicializarTiposDeCambio() {
        tiposDeCambio.put("EUR", 1.0);
        tiposDeCambio.put("USD", 1.103); // 1 euro es igual a 1.103 dólares
        tiposDeCambio.put("BTC", 0.00013); // 1 euro es igual a 0.00013 bitcoins
        tiposDeCambio.put("YEN", 120.0); // 1 euro es igual a 120 yenes
    }
}
