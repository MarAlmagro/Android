package mar.practices.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;


public class MyGameActivity extends Activity {

    // Vista que contiene la pantalla del juego
    VistaJuego pantallaJuego;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Hacer que el juego se ejecute a pantalla completa y
        // que la pantalla siempre esté activa mientras se juega
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(  WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        // Cargar la vista con la pantalla del juego
        pantallaJuego = new VistaJuego(this);
        setContentView(pantallaJuego);
    }
}
