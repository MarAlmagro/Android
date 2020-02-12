package mar.practices.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Código que se ejecuta cuando el jugador
    // pulsa el botón play en la ventana inicial
    // de la aplicación.
    public void jugar(View view){
        Log.i ( "ImageButton", "clicked");

        //  Iniciar la actividad "MyGameActivity" que muestra la pantalla del juego
        Intent intent = new Intent (this, MyGameActivity.class);
        startActivity(intent);

        // Cerrar la actividad que muestra la pantalla de inicio juego
        finish();
    }
}
