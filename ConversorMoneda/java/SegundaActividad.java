package com.example.appdos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SegundaActividad extends AppCompatActivity {

    private TextView cartel;
    private ListView listado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda_actividad);


        Intent intent = getIntent();
        HashMap<String, Double> tiposDeCambio = new HashMap<String, Double>();
        tiposDeCambio = (HashMap<String, Double>)intent.getSerializableExtra("tiposdecambio");
        listado = findViewById(R.id.lvDivisas);
        ArrayAdapter<String> adaptador = new ArrayAdapter<>(
                this,android.R.layout.simple_list_item_1, cargarValores(tiposDeCambio) );
        listado.setAdapter(adaptador);
    }

    public List<String> cargarValores(HashMap<String, Double> tiposDeCambio){
        List<String> listaStrings = new ArrayList<>();

        for (HashMap.Entry<String, Double> elemento: tiposDeCambio.entrySet()){
            String claveDivisa = elemento.getKey();
            String valorDivisa = elemento.getValue().toString();
            listaStrings.add(claveDivisa + "            " +  valorDivisa);
        }

        return listaStrings;
    }
}
