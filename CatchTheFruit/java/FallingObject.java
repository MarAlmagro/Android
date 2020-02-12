package mar.practices.myapplication;

import android.graphics.Bitmap;

// Representa un objeto que cae desde la parte
// superior de la pantalla hacia abajo
public class FallingObject extends GameObject {

    // Puntos que proporciona este objeto
    // si es capturado por el jugador
    private int puntos = 0;

    // fuerza con la que "cae" /se desplaza el
    // objeto hacia abajo en el juego
    private int gravedad ;

    // Constructor que establece la imagen y las coordenadas x,y
    // del objeto a partir de los valores que se pasan como
    // parámetros
    public FallingObject( Bitmap imagen, int x, int y){
        super(  imagen, x, y);
        // Inicialización por defecto de la propiedad "gravedad"
        this.gravedad = 30;
    }

    // Constructor que establece la imagen del objeto
    // a partir del valor que se pasa como parámetro
    public FallingObject(Bitmap imagen) {
        // Por defecto se inicializa la posición del
        // objeto en el borde superior izdo pantalla
        this(imagen, 0, 0);
    }

    // Determina si este objeto ha colisionado con otro.
    // True si la zona dibujo en pantalla de este objeto
    // se solapa con la zona dibujo del objeto que se pasa
    // como parámetro, false en otro caso
    public boolean hayColision (GameObject otroObjetoJuego){
        return this.getZonaDibujo().intersect(otroObjetoJuego.getZonaDibujo());
    }

    // Actualiza la posición vertical del objeto en
    // el juego (sumando a la posición actual el valor
    // de la gravedad)
    @Override
    public void actualizarPosicion (){
        this.y += gravedad;
    }

    // recuperar el valor de la propiedad "puntos"
    public int getPuntos() {
        return puntos;
    }

    // establecer el valor de la propiedad "puntos"
    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    // recuperar el valor de la propiedad "gravedad"
    public int getGravedad() {
        return gravedad;
    }

    // establecer el valor de la propiedad "gravedad"
    public void setGravedad(int gravedad) {
        this.gravedad = gravedad;
    }

}
