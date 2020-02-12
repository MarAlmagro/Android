package mar.practices.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class VistaJuego extends View{

    // Número total de vidas que tiene el jugador
    private static final int NUMERO_VIDAS = 3;

    // Puntos que ha acumulado el jugador en la partida actual
    private int puntos = 0;

    //  Número de vidas que le quedan al jugador
    private int contadorVidas = 0;

    // Rectángulo que representa la pantalla del dispositivo
    Rect rectPantalla;

    // Alto y anto de la pantalla dispositivo móvil
    private int anchoPantalla;
    private int altoPantalla;

    // Imagen fondo de la pantalla del juego
    private Bitmap fondo;

    // Imagen cesta
    private Bitmap imgCesta;


    // Array que contiene las plantilla a partir de las que se
    // van a crear los objetos del juego que puede recoger el usuario.
    // Cada plantilla está formada por una imagen/sprite del objeto y
    // la puntuación que obtiene el jugador al capturarlo
    private ArrayList<BlueprintFallingObject> blueprints = new ArrayList();

    // Cesta recoge-frutas
    private Cesta cesta;

    // Frecuencia redibujado pantalla juego
    final long UPDATE_MILLIS = 30;

    // Frecuencia añadir nueva fruta/trampa al juego
    final int ADD_FALLING_OBJECT_INTERVAL = 800;

    // Objetos que hay que dibujar en la pantalla del juego
    private ArrayList<GameObject> objetosEnPantalla = new ArrayList();

    // Generador de números aleatorios
    private Random rand = new Random();

    // True si se está arrastrando la cesta y false en otro caso
    private boolean arrastrandoCesta = true;

    // Coordenada x de la posición pantalla donde se
    // inicia la operación de arrastre de la cesta
    private int arrastreInicioX;

    // Coordenada x posición de la cesta cuando se inicia el arrastre
    private int arrastreInicioCestaX;

    // Ejecuta la tarea que llama al método onDraw
    private Handler handlerGameLoop = new Handler();

    // Código que llama al método onDraw para actualizar
    // el estado del juego
    private Runnable runnableGameLoop;

    // Ejecuta la tarea que crea una nueva fruta
    // y la añade al juego
    private Handler handlerAddFallingObject = new Handler();

    // Código que crea un nuevo objeto FallingObject y
    // lo añade al juego
    private Runnable runnableAddFallingObject;

    public VistaJuego(Context context) {

        super(context);

        // Cargar las imágenes que se van a utilizar en el juego
        inicializarObjetosJuego();

        // Se va a añadir un nuevo objeto (FallingObject) al juego
        // cada 800 milisegundos
        runnableAddFallingObject = new Runnable() {
                @Override
                public void run () {
                    objetosEnPantalla.add(0, getNewFallingObject());
                    handlerAddFallingObject.postDelayed( runnableAddFallingObject,
                                                         ADD_FALLING_OBJECT_INTERVAL);
                }
        };

        // Añadir a la cola la tarea que crea los "Falling Objects"
        handlerAddFallingObject.post(runnableAddFallingObject);

        // crear una nueva tarea que al ejecutarse
        // invoca al método invalidate para que se
        // actualice("se pinte") de nuevo la pantalla
        // del juego
        runnableGameLoop = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
    }


    // Actualiza el estado de los objetos del juego
    // y los muestra en pantalla dispositivo
    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        // Dibujar el fondo del juego
        canvas.drawBitmap(fondo,0,0, null);

        // Actualizar estado y dibujar objetos juego
        for (GameObject obj : objetosEnPantalla){
            obj.actualizarPosicion();
            obj.dibujar(canvas);
        }

        mostrarMarcador(canvas);

        // Se va a volver a actualizar el contenido de la
        // pantalla del juego pasados 30 milisegundos
        handlerGameLoop.postDelayed(runnableGameLoop, UPDATE_MILLIS);

    }

    // Inicialización objetos del juego
    private void inicializarObjetosJuego(){

        // Imágenes de las frutas y de los objetos trampa
        Bitmap imgManzana, imgSandia, imgPlatano, imgCereza, imgPera, imgMelocoton,
               imgBomba, imgFries, imgPastel, imgBurger, imgCiruela, imgLimon,
               imgPigna, imgNaranja, imgUvas;

        // Obtener el ancho y alto de la pantalla dispositivo móvil
        Display pantalla = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        Point dimensionesPantalla = new Point();
        pantalla.getSize(dimensionesPantalla);
        altoPantalla = dimensionesPantalla.y;
        anchoPantalla = dimensionesPantalla.x;

        // Carga las imágenes que forman parte del juego y les asigna el
        // tamaño con el que van a dibujarse en la pantalla del dispositivo
        Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.fondo_juego);
        fondo = Bitmap.createScaledBitmap(img, anchoPantalla, altoPantalla, false);

        img = BitmapFactory.decodeResource(getResources(), R.drawable.cesta);
        imgCesta = escalarBitmap(img, anchoPantalla/6);

        img = BitmapFactory.decodeResource(getResources(), R.drawable.manzana);
        imgManzana = escalarBitmap(img, anchoPantalla/10);

        img = BitmapFactory.decodeResource(getResources(), R.drawable.limon);
        imgLimon = escalarBitmap(img, anchoPantalla/10);

        img = BitmapFactory.decodeResource(getResources(), R.drawable.melocoton);
        imgMelocoton = escalarBitmap(img, anchoPantalla/10);

        img = BitmapFactory.decodeResource(getResources(), R.drawable.uvas);
        imgUvas = escalarBitmap(img, anchoPantalla/10);

        img = BitmapFactory.decodeResource(getResources(), R.drawable.naranja);
        imgNaranja = escalarBitmap(img, anchoPantalla/10);

        img = BitmapFactory.decodeResource(getResources(), R.drawable.pigna);
        imgPigna = escalarBitmap(img, anchoPantalla/10);

        img = BitmapFactory.decodeResource(getResources(), R.drawable.ciruela);
        imgCiruela = escalarBitmap(img, anchoPantalla/10);

        img = BitmapFactory.decodeResource(getResources(), R.drawable.sandia);
        imgSandia = escalarBitmap(img, anchoPantalla/10);

        img = BitmapFactory.decodeResource(getResources(), R.drawable.cupcake);
        imgPastel = escalarBitmap(img, anchoPantalla/10);

        img = BitmapFactory.decodeResource(getResources(), R.drawable.bananas);
        imgPlatano = escalarBitmap(img, anchoPantalla/10);

        img = BitmapFactory.decodeResource(getResources(), R.drawable.bomba);
        imgBomba = escalarBitmap(img, anchoPantalla/10);

        img = BitmapFactory.decodeResource(getResources(), R.drawable.cherry);
        imgCereza = escalarBitmap(img, anchoPantalla/10);

        img = BitmapFactory.decodeResource(getResources(), R.drawable.pera);
        imgPera  = escalarBitmap(img, anchoPantalla/10);

        img = BitmapFactory.decodeResource(getResources(), R.drawable.fries);
        imgFries = escalarBitmap(img, anchoPantalla/10);

        img = BitmapFactory.decodeResource(getResources(), R.drawable.burger);
        imgBurger = escalarBitmap(img, anchoPantalla/10);

        // Inicializar plantillas para crear los objetos que recoge el jugador
        blueprints.add ( new BlueprintFallingObject (imgManzana,5) );
        blueprints.add ( new BlueprintFallingObject (imgPera, 10) );
        blueprints.add ( new BlueprintFallingObject (imgMelocoton, 5) );
        blueprints.add ( new BlueprintFallingObject (imgFries, -5 ));
        blueprints.add ( new BlueprintFallingObject (imgCereza, 10) );
        blueprints.add ( new BlueprintFallingObject (imgPastel, -10) );
        blueprints.add ( new BlueprintFallingObject (imgPlatano, 5) );
        blueprints.add ( new BlueprintFallingObject (imgSandia, 10) );
        blueprints.add ( new BlueprintFallingObject (imgBomba, -15) );
        blueprints.add ( new BlueprintFallingObject (imgLimon, 5) );
        blueprints.add ( new BlueprintFallingObject (imgUvas, 10) );
        blueprints.add ( new BlueprintFallingObject (imgBurger, -10) );
        blueprints.add ( new BlueprintFallingObject (imgPera, 5) );
        blueprints.add ( new BlueprintFallingObject (imgNaranja, 10) );
        blueprints.add ( new BlueprintFallingObject (imgPigna, 10) );
        blueprints.add ( new BlueprintFallingObject (imgCiruela, 5) );

        // Crear cesta
        cesta = new Cesta(  imgCesta,
                         anchoPantalla/2 - imgCesta.getWidth()/2,
                         altoPantalla - imgCesta.getHeight() );

        objetosEnPantalla.add(cesta);
    }

    // Escala una imagen conservando sus proporciones ancho/alto originales
    // Parámetros : imagen a redimensionar, ancho de la nueva imagen
    // Devuelve la imagen redimensionada
    private Bitmap escalarBitmap( Bitmap origen, int ancho ){
        Bitmap resultado;

        // Obtener relación entre las proporciones (ancho y alto) de la
        // imagen original y calcular el nuevo alto manteniendo dichas
        // proporciones
        float proporcion = (float) origen.getWidth() /  origen.getHeight();
        int alto = Math.round(ancho / proporcion);

        return Bitmap.createScaledBitmap(origen, ancho, alto, false);
    }

    // Devuelve un nuevo objeto seleccionado al azar entre
    // los objetos que puede recoger el jugador y lo situa en
    // justo encima del borde superior de la pantalla y situado
    // al azar en el ancho de la pantalla
    // Devuelve el objeto creado
    private FallingObject getNewFallingObject(){

        BlueprintFallingObject blueprint = blueprints.get( rand.nextInt(blueprints.size()) );

        FallingObject nuevo = new FallingObject( blueprint.getImagen());
        int posX = (int) (10 + Math.random()* (anchoPantalla - 20 - nuevo.getAncho()));
        nuevo.setX ( posX );
        nuevo.setY ( -1 *  nuevo.getAlto() );
        nuevo.setPuntos(blueprint.getPuntos());

        return nuevo;
    }
    @Override
    public boolean onTouchEvent(MotionEvent evento) {
        // Recuperar coordenada X actual de la posición
        // donde se está tocando la pantalla
        int coordX = (int) evento.getX();

        // Según el gesto realizado por el jugador
        // se realiza la acción correspondiente
        switch (evento.getAction()){
            case ( MotionEvent.ACTION_UP ):
            case ( MotionEvent.ACTION_CANCEL ):
                // finalizar operación movimiento/arrastre cesta
                arrastrandoCesta = false;
                break;
            case ( MotionEvent.ACTION_DOWN ):
                // Si se toca en la cesta : iniciar operación movimiento/arrastre cesta
                if ( cesta.getZonaDibujo().contains(coordX, (int) evento.getY())){
                    arrastrandoCesta = true;
                    arrastreInicioX = coordX;
                    arrastreInicioCestaX = cesta.getX();
                }
                break;
            case ( MotionEvent.ACTION_MOVE ):
                if (arrastrandoCesta){
                    // Actualizar posición cesta
                    cesta.x = arrastreInicioCestaX - arrastreInicioX + coordX;
                    int dif = (int)evento.getRawX() - (int)evento.getX();

                    // Mantener la cesta dentro del ancho pantalla
                    if (cesta.x < 0) {
                        cesta.x = 0;
                    }else if ( cesta.x + cesta.getAncho() > anchoPantalla  ){
                        cesta.x = anchoPantalla - cesta.getAncho() ;
                    }
                }
                break;
            default:
                return false;
        }
        // devolver true para indicar que ya se ha tratado el evento y
        // no hay que realizar más acciones
        return true;
    }

    // Visualiza en pantalla la puntuación actual del jugador
    // y el número de vidas que tiene disponibles
    void mostrarMarcador(Canvas canvas) {
        Paint pincel = new Paint();
        pincel.setColor(Color.BLACK);
        pincel.setTextSize(128);
        pincel.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Vidas: " + contadorVidas, getWidth()/2, 100, pincel);
        pincel.setTextSize(80);
        canvas.drawText("Ptos: " + puntos, 120, 80, pincel);
    }
}
