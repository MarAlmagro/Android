package mar.practices.myapplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

// Representa un objeto del juego(cesta, frutas...)
public abstract class GameObject {
    // Coordenadas x,y (en pantalla) del
    // borde superior izdo del objeto
    protected  int x, y;

    // Ancho y alto de la imagen que representa
    // al objeto al dibujarse en la pantalla
    protected int alto, ancho;

    // Imagen(sprite) del objeto al dibujarse en la pantalla
    protected Bitmap imagen;

    // Constructor que establece la imagen y las coordenadas x,y
    // del objeto a partir de los valores que se pasan como
    // parámetros
    public GameObject(Bitmap imagen, int x, int y) {
        this.imagen = imagen;
        this.alto = imagen.getHeight();
        this.ancho = imagen.getWidth();
        this.x = x;
        this.y = y;
    }
    // Constructor que establece la imagen del objeto
    // a partir del valor que se pasa como parámetro
    public GameObject(Bitmap imagen) {
        // Por defecto se inicializa la posición
        // del objeto en el borde superior izdo pantalla
        this(imagen, 0, 0);
    }

    // Actualiza la posición del objeto en el juego
    public abstract void actualizarPosicion ();


    // Devuelve alto imagen del objeto
    public int getAlto() {
        return imagen.getHeight();
    }
    // Devuelve ancho imagen del objeto
    public int getAncho() {
        return imagen.getWidth();
    }
    // Devuelve el rectángulo que determina la zona
    // de la pantalla en la que se dibuja este objeto
    public Rect getZonaDibujo (){
        return new Rect(x, y, x + ancho, y + alto);
    }

    // Dibuja la imagen de este objeto en el canvas que
    // se pasa como parámetro
    public void dibujar (Canvas canvas){
        canvas.drawBitmap(imagen, x, y, null);
    }

    // Devuelve coordenada x posición objeto en pantalla
    public int getX() {
        return x;
    }

    // Establece coordenada x posición objeto en pantalla
    public void setX(int x) {
        this.x = x;
    }

    // Devuelve coordenada y posición objeto en pantalla
    public int getY() {
        return y;
    }

    // Establece coordenada y posición objeto en pantalla
    public void setY(int y) {
        this.y = y;
    }

    // Devuelve la imagen que representa a este objeto
    public Bitmap getImagen() {
        return imagen;
    }

    // Establece la imagen que representa a este objeto
    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
        alto = imagen.getHeight();
        ancho = imagen.getWidth();
    }
}
