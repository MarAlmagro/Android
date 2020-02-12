package mar.practices.myapplication;

import android.graphics.Bitmap;

// Representa una cesta que se desplaza en
// el juego horizontalmente(derecha o izquierda)
public class Cesta extends GameObject{

    // Unidades que se desplaza horizontalmente la "Cesta"
    private int desplazamiento;

    // Constructor que establece la imagen y las coordenadas x,y
    // de la cesta a partir de los valores que se pasan como
    // parámetros
    public Cesta( Bitmap imagen, int x, int y){
        super(  imagen, x, y);
        // Inicialización por defecto de la propiedad "desplazamiento"
        this.desplazamiento = 1;
    }

    // Constructor que establece la imagen de la cesta
    // a partir del valor que se pasa como parámetro
    public Cesta(Bitmap imagen) {
        // Por defecto se inicializa la posición de
        // la cesta en el borde superior izdo pantalla
        this(imagen, 0, 0);
    }

    // Actualiza la posición del objeto en el juego
    @Override
    public  void actualizarPosicion () {};

    // Mueve la cesta horizontalmente hacia  izda de
    // la pantalla tantas  unidades como indique la
    // propiedad "desplazamiento" de este objeto Cesta
    public void moverIzda (){
        this.x -= desplazamiento;
    }

    // Mueve la cesta horizontalmente hacia la dcha de
    // la pantalla tantas  unidades como indique la
    // propiedad "desplazamiento" de este objeto Cesta
    public void moverDcha (){
        this.x += desplazamiento;
    }

    // Devuelve el valor de la propiedad "desplazamiento"
    public int getDesplazamiento() {
        return desplazamiento;
    }
    // Establece el valor de la propiedad "desplazamiento"
    public void setDesplazamiento(int desplazamiento) {
        this.desplazamiento = desplazamiento;
    }
}
