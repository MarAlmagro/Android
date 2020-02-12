package mar.linkia.reproductor;

import androidx.appcompat.app.AppCompatActivity;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import com.airbnb.lottie.LottieAnimationView;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    // Nombre carpeta que contiene las canciones mp3
    private static final String CARPETA_CANCIONES = "musica";
    // Extensión archivos mp3
    private static final String MP3 = ".mp3";
    // Nombre de la canción seleccionada/en reproducción
    private String cancionActual = "";
    // Reproductor música/canciones
    private MediaPlayer player = new MediaPlayer();
    // Botones para inciar, parar y detener reproducción
    private ImageButton btPlay, btStop, btPause;
    // Indica si la reproducción está pausada(true) o no (false)
    boolean pausa = false;
    // Indica si se están reproduciendo las canciones contenidas
    // en la sdcard (true) o desde la memoria interna(false)
    boolean fromSdcard = true;
    // ruta del directorio canciones contenidas en la sdcard
    String rutaCancionesSD = null;
    // Nombres de las canciones
    String[] nombreCanciones;
    // Lista para seleccionar la canción a reproducir
    Spinner listaCanciones;
    // Muestra mensajes información al usuario
    TextView mensajes;
    // Animación que acompaña la reproducción de una canción
    LottieAnimationView soundAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mensajes = findViewById(R.id.tv_mensaje);
        player.setVolume(1f, 1f);
        listaCanciones = findViewById(R.id.sp_canciones);
        listaCanciones.setSelection(0);
        soundAnimation = findViewById(R.id.play_animation);

        btPlay =  findViewById(R.id.bt_play);
        btStop = findViewById(R.id.bt_stop);
        btPause = findViewById(R.id.bt_pause);

        btPlay.setEnabled(false);
        btPause.setEnabled(false);
        btStop.setEnabled(false);

        player.setOnCompletionListener(this);

        try{
            // Obtener directorio sdCard y buscar carpeta musica
            File dir =  new File( Environment.getExternalStorageDirectory(), CARPETA_CANCIONES );
            rutaCancionesSD = dir.getAbsolutePath();
            if ( dir != null && dir.isDirectory()) {
                nombreCanciones = getCancionesMp3(dir.list());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Si no se han podido recuperar canciones de la sdCard,
        // Buscamos canciones en la carpeta del proyecto(memoria interna)
        if (nombreCanciones == null || nombreCanciones.length < 1) {
            fromSdcard = false;
            // Obtenemos listado canciones por defecto
            // (están en la carpeta assets/musica del proyecto)
            try {
                nombreCanciones = getCancionesMp3(this.getAssets().list(CARPETA_CANCIONES));
                if ( nombreCanciones.length > 0)
                    mensajes.setText("Canciones por defecto");
                else{
                    // no se han encontrado canciones
                    mensajes.setText("No hay canciones disponibles");
                    // Salir del método. Parar applicación
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else{
            // Se van a reproducir las canciones de la tarjeta SD
            mensajes.setText("Canciones desde tarjeta SD");
        }


        // Cargar nombre canciones en el spinner
        ArrayAdapter<String> adaptador =  new ArrayAdapter<String>(
                this,
                R.layout.elemento_sp,
                nombreCanciones);
        listaCanciones.setAdapter(adaptador);

        // Canciones cargadas correctamente en el spinner
        // Habilitar botones para que usuario pueda reproducir canciones
        btPlay.setEnabled(true);
        btPause.setEnabled(true);
        btStop.setEnabled(true);
    }

    // Devuelve un array String con el nombre de las canciones sin
    // la extensión .mp3
    private String[] getCancionesMp3 (String[] listaArchivos){
        List<String> mp3Files = new ArrayList<String>();

        for(String archivo : listaArchivos){
            if(archivo.endsWith(MP3)){
                // Guardamos nombre canción sin la extensión .mp3
                mp3Files.add(archivo.substring(0,archivo.length()-4));
            }
        }

        return mp3Files.toArray(new String[mp3Files.size()]);
    }

    // Al hacer click en el botón play
    public void play(View v){
        try {
            // Obtener canción seleccionada en el spinner
            String cancionSpinner = listaCanciones.getSelectedItem().toString();
            // Si ya se está reproduciendo la canción seleccionada en el
            // spinner no hacemos nada (salir de este método)
            if (cancionActual.equals(cancionSpinner) && player.isPlaying())
                return;
            // Si se está reproduciendo la canción seleccionada en el
            // spinner y está en pausa, reanudar reproducción
            if (cancionActual.equals(cancionSpinner) && pausa) {
                player.seekTo(player.getCurrentPosition());
                player.start();
            }else
            {
                // Reproducir nueva canción
                cancionActual = cancionSpinner;
                pausa = false;
                if (player.isPlaying()) {
                    player.stop();
                    player.reset();
                    soundAnimation.pauseAnimation();
                }
                // Cargar cancion de la sdcard
                if (fromSdcard)
                {
                    player.setDataSource(rutaCancionesSD + "/" + cancionActual + MP3);
                }
                else
                {
                    // Cargar canción desde assets/memoria interna
                    AssetFileDescriptor afd =
                            getAssets().openFd(CARPETA_CANCIONES + "/" + cancionActual + MP3);
                    player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    afd.close();
                }
                // Iniciar reproducción canción y animación
                player.prepare();
                player.start();
                soundAnimation.playAnimation();
            }
            // Mostar mensaje con el nombre canción que se está reproduciendo e
            // iniciar la animación
            mensajes.setText("Reproduciendo  =>  " + cancionActual);
            soundAnimation.playAnimation();
            pausa = false;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Si se está reproduciendo, se pausa la reproducción mp3 y la animación
    public void pausar(View v){
        if(player.isPlaying()){
            player.pause();
            pausa = true;
            mensajes.setText("Pausa  =>  " + cancionActual);
        }
        soundAnimation.pauseAnimation();
    }

    // Detener la reproducción y la animación
    // Sólo se detiene si está reproduciendo o en pausa
    public void stop(View v){
        soundAnimation.pauseAnimation();
        if(player.isPlaying() || pausa){
            pausa = false;
            player.stop();
            player.reset();
            mensajes.setText("Stop  =>  " + cancionActual);
        }
    }

    // Liberar el reproductor cuando termine la aplicación principal/MainActivity
    @Override
    public void onStop() {
        super.onStop();
        if (player.isPlaying()) player.release();
    }

    // Ha terminado de reproducirse la canción actual
    // Mostrar mensaje finalizada y detener animación
    @Override
    public void onCompletion(MediaPlayer mp) {
        mensajes.setText("Finalizada  =>  " + cancionActual);
        soundAnimation.pauseAnimation();
    }
}
