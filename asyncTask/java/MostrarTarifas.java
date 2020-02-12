package mar.linkia.conversorlinkia;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;

public class MostrarTarifas extends AppCompatActivity {

    //  Lista que va a mostrar las tarifas de cambio
    private ListView lvTarifas;
    ArrayList<HashMap<String, String>> listaItemsListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_tarifas);

        lvTarifas = (ListView) findViewById(R.id.listaTarifas);
        Bundle datosRecibidos =  getIntent().getExtras();
        String[] arrayMonedas = datosRecibidos.getStringArray("Array_Monedas");
        double[] arrayTarifas =  datosRecibidos.getDoubleArray("Array_Tarifas");

        listaItemsListView = new ArrayList<>();
        for (int i=0; i<arrayMonedas.length; i++){
            HashMap<String, String> itemLista = new HashMap<>();
            itemLista.put("lvmoneda",arrayMonedas[i]);
            itemLista.put("lvtarifa", arrayTarifas[i] + "");
            listaItemsListView.add(itemLista);
        }

        ListAdapter adaptador = new SimpleAdapter(
                MostrarTarifas.this, listaItemsListView,
                R.layout.elemento_lista, new String[]{"lvmoneda", "lvtarifa"},
                new int[]{R.id.lvmoneda, R.id.lvtarifa});

        lvTarifas.setAdapter(adaptador);
    }
}
