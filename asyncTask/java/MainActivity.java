package mar.linkia.conversorlinkia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {


    // Nombre de las monedas
    private static final String[] NOMBRE_MONEDAS = {
            "BRL - Reales Brasil",
            "DKK - Coronas Dinamarca",
            "EUR - Euros",
            "IDR - Rupias Indonesia",
            "JPY - Yenes Japón",
            "MXN - Pesos México",
            "RUB - Rublos Rusia",
            "SEK - Coronas Suecia",
            "TRY - Liras Turquía",
            "USD - Dólares USA",
            "ZAR - Rands Suráfrica"};

    // Tarifas cambio por defecto que usa la aplicación si no
    // puede obtener de internet las tarifas actualizadas
    // Tipos de cambio para realizar las conversiones
    // de un tipo de moneda a euros
    // El tipo cambio de posición se corresponde con el nombre
    // la moneda almacenado en la misma posición del array
    // "NOMBRE_MONEDAS"
    private static final double[] TARIFAS_POR_DEFECTO = {
            4.58888,7.47614, 1.0, 15763.28, 120.988,71.3555,
            10.7928, 6.46601, 1.11583, 41.6632, 11.147};

    // Posición el array "NOMBRE_MONEDAS"
    // donde está el tipo de moneda Euros
    private static final int POS_EURO = 2;

    // Spinners para selección tipo monedas a convertir
    private Spinner origen, destino;

    // Selección por defecto en los Spinners al
    // iniciar la aplicación
    private int posOrigen = 0;
    private int posDestino =  POS_EURO;

    // Para mostra al usuario el resultado de la conversión
    private TextView mensajeResultado;

    // Nombre de la moneda a la que se va a convertir
    // se muestra junto a la cantidad introducida por
    // el usuario
    private TextView lbTipoMoneda;

    // botón convertir
    private Button btconvertir;

    // botón mostrar tarifas
    private Button btmostrar;

    // URL servidor remoto para obtener las tarifas en formato JSON
    private static final String URL_TARIFAS = "https://api.exchangeratesapi.io/latest";


    // Tipos de cambio para realizar las conversiones
    // de un tipo de moneda a euros
    // El tipo cambio de posición se corresponde con el nombre
    // la moneda almacenado en la misma posición del array
    // "NOMBRE_MONEDAS"
    private double[] tipos_cambio = new double[NOMBRE_MONEDAS.length];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        origen = (Spinner) findViewById(R.id.sp_origen);
        destino = (Spinner) findViewById(R.id.sp_destino);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, NOMBRE_MONEDAS);
        origen.setAdapter(adaptador);
        origen.setSelection(posOrigen);
        destino.setAdapter(adaptador);
        destino.setSelection(posDestino);
        mensajeResultado = ((TextView) findViewById(R.id.lb_resultado));
        lbTipoMoneda = ((TextView) findViewById(R.id.lb_tipo_moneda));
        lbTipoMoneda.setText(
                getNombreMoneda(origen.getSelectedItem().toString(), 0));

        // Añadir un controlador eventos al Spinner que contiene la moneda
        // origen. Cada vez que cambie el elemento seleccionado, se actualiza el
        // contenido del TextView "lb_tipo_moneda" que indica el tipo de moneda
        // que se ha seleccionado como origen del cambio
        origen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View selectedItemView,
                                       int pos,
                                       long id)
            {
                lbTipoMoneda.setText(
                        getNombreMoneda(parent.getItemAtPosition(pos).toString(), 0));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
            }
        });

        // Para que el usuario no pueda utilizar el conversor hasta
        // que termine el proceso de actualización de tarifas se desactivan
        // botones "CONVERTIR" y "MOSTRAR TARIFAS"
        btconvertir = (Button)findViewById(R.id.bt_convertir);
        btconvertir.setEnabled(false);// botón convertir
        btmostrar = (Button)findViewById(R.id.bt_mostrarTarifas);
        btmostrar.setEnabled(false);// botón convertir


        // Actualizar las tarifas cambio en segundo plano
        new GetTarifas().execute();
    }

    public void calcularCambio (View v){
        posOrigen = origen.getSelectedItemPosition();
        String monedaOrigen = getNombreMoneda(NOMBRE_MONEDAS[posOrigen], 2);
        posDestino = destino.getSelectedItemPosition();
        String monedaDestino = getNombreMoneda(NOMBRE_MONEDAS[posDestino], 2);
        String mensaje = "";
        String textoCantidad;
        try {
            textoCantidad = ((EditText) findViewById(R.id.input_cantidad)).getText().toString();
            double cantidad = Double.parseDouble(textoCantidad);
            double resultado = cantidad;
            // Si moneda origen y destino son iguales no hacemos nada
            if (posOrigen != posDestino) {
                // Cambiamos a euros la moneda origen y guardamos su valor en "resultado"
                if (posOrigen != POS_EURO)
                    resultado /= tipos_cambio[posOrigen];
                // cambiamos el valor en euros de "resultado" a la moneda destino
                if (posDestino != POS_EURO)
                    resultado *= tipos_cambio[posDestino];
            }
            DecimalFormat df = new DecimalFormat("0.####");
            mensaje = df.format(cantidad) + " " + monedaOrigen + " son " +
                    df.format(resultado)+ " " + monedaDestino;
        }catch (Exception e){
            mensaje = "Debe verificar la cantidad";
        }finally {
            mensajeResultado.setText(mensaje);
        }
    }

    // Lanzar una nueva actividad que muestra las tarifas cambio
    public void mostarTarifas(View v){
        Intent intent = new Intent(this,MostrarTarifas.class);
        intent.putExtra("Array_Tarifas", tipos_cambio);
        intent.putExtra("Array_Monedas", NOMBRE_MONEDAS);
        startActivity(intent);
    }

// Este método ha cambiado. Ahora le paso el número token que quiero obtener
    private String getNombreMoneda(String cadena, int numToken){
        String[] tokens = cadena.split(" ");
        return tokens [numToken];
    }

    private class GetTarifas extends AsyncTask<Void, Void, String> {

        // Indica si se ha producido error al descargar/actualizar
        // las tarifas cambio
        boolean errorDescarga = false;

        // Mostrar, en la actividad principal, mensaje informando
        // que se están actualizando las tarifas cambio
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Mostrar notificación en actividad principal
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Actualizando Tarifas Cambio...",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 80, -100);
                    toast.show();
                }

            });
        }

        // Descarga de internet las tarifas cambio y utiliza dichos
        // datos para actualizar el contenido del array "TIPOS_CAMBIO".
        // Devuelve un String informando si se han podido actualizar
        // o no las tarifas
        @Override
        protected String doInBackground(Void... voids) {
            errorDescarga = true;
            String resultadoDescarga = "Error actualizando tarifas";

            try {

                URL url = new URL(URL_TARIFAS);
                HttpURLConnection conex = (HttpURLConnection) url.openConnection();

                // Obtener la respuesta del servidor
                InputStream in = new BufferedInputStream(conex.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                StringBuffer sb = new StringBuffer();
                String linea = "";
                while ((linea = br.readLine()) != null) {
                    sb.append(linea + "\n");
                }

                JSONObject jsonObj = new JSONObject(sb.toString());
                JSONObject jsonTarifas = jsonObj.getJSONObject("rates");


                // Buscamos en el objeto "jsonTarifas" los cambios que
                // corresponden a cada uno de los tipos moneda que
                // tenemos en el array "NOMBRE_MONEDAS" y guardamos dicho
                // valor en el array "TIPOS_CAMBIO"
                for (int i = 0; i < NOMBRE_MONEDAS.length; i++) {

                    // La tarifa cambio del Euro no se modifica
                    if (i !=POS_EURO ){
                        tipos_cambio[i] =
                        jsonTarifas.getDouble(getNombreMoneda(NOMBRE_MONEDAS[i],0));
                    }else
                        tipos_cambio[i] = 1.0;
                }

                errorDescarga = false;
                resultadoDescarga = "Tarifas Cambio Actualizadas !!";

            } catch (Exception e) {

            } finally {
                // enviar "resultadoDescarga" al método "onPostExecute"
                // para que muestre su contenido en Toast en actividad principal
                return resultadoDescarga;
            }
        }

        // Ya ha terminado el proceso descarga y actualización de las tarifas.
        // Detener la barra progreso de la actividad principal para que se
        // habilite su interfaz y el usuario pueda utilizar el conversor.
        // Mediante clase "Toast" muestra, en ventana actividad principal,
        // una notificación informando del resultado proceso actualización
        // tarifas cambio
        @Override
        protected void onPostExecute ( final String resultadoDescarga){

            super.onPostExecute(resultadoDescarga);


            // Si no se han podido actualizar las tarifas
            // usamos los valores por defecto
            if (errorDescarga){
               System.arraycopy( TARIFAS_POR_DEFECTO,0,
                                 tipos_cambio, 0,
                                 tipos_cambio.length);
            }


            // Mostrar notificación en actividad principal
            runOnUiThread(new Runnable() {
                public void run(){
                    Toast toast = Toast.makeText(getApplicationContext(),
                                                 resultadoDescarga,
                                                 Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 80, -100);
                    toast.show();
                }
            });

            // Activar botones "CONVERTIR" y "MOSTRAR TARIFAS" para que
            // usuario pueda utilizar la aplicación
            btconvertir.setEnabled(true);
            btmostrar.setEnabled(false);
        }

    }
}
