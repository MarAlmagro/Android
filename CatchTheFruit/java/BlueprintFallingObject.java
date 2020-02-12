package mar.practices.myapplication;

import android.graphics.Bitmap;

// Plantilla para crear los objetos juego
// que puede recoger el jugador
public class BlueprintFallingObject {

    /**
     * Imagen asociada al objeto juego
     */
    private Bitmap imagen;

    /**
     * Puntos que proporciona al jugador el objeto
     */
    private int puntos;

    /**
     *  Constructor que establece los valores de todas las
     *  propiedades
     * @param imagen imagen asociada al objeto
     * @param puntos puntos que acumula el jugador al capturar el objeto
     */
    public BlueprintFallingObject(Bitmap imagen, int puntos) {
        this.imagen = imagen;
        this.puntos = puntos;
    }

    // Devuelve los puntos asociados a este objeto
    public int getPuntos() {
        return puntos;
    }
    // Establece la puntuación que acumula el jugador
    // al recoger este objeto
    // return valor puntuación objeto
    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }
    // Establece la imagen asociada a este objeto del juego
    public Bitmap getImagen() {
        return imagen;
    }
    // Establece la imagen asociada a este objeto del juego
    // return imagen asociada
    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }
}
