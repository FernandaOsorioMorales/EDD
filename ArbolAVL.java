package src.edd;

import java.lang.IllegalCallerException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import src.edd.ArbolBinario;
import src.edd.Lista;
import src.edd.Pila;

/**
 * Clase para árboles AVL. Un árbol AVL cumple las siguientes características
 * Donde la altura de cada uno de sus subárboles no pueden diferir en más de uno
 * y los dos subárboles deben ser también AVL.
 */
public class ArbolAVL<T extends Comparable> extends ArbolBinarioBusqueda<T> {
    /**
     * Clase interna protegida para vértices de árboles AVL. La única diferencia con
     * los
     * vértices de árbol binario, es que tienen una variable de clase para la altura
     * del vértice
     */
    protected class VerticeAVL extends Vertice {

        // la altura del vértice
        public int altura;

        /**
         * Constructor del vértice AVL que recibe un elemento como parámetro
         * 
         * @param elemento
         */
        public VerticeAVL(T elemento) {
            super(elemento);
            this.altura = this.altura();
        }

        /**
         * Método que devuelve una representación en cadena del vértice AVL
         */
        public String toString() {
            if (this == null)
                return "-1";
            return this.elemento.toString() + "cuya altura es (" + this.altura + ")";
        }

        /**
         * Método que devuelva la altura del vértice AVL
         */
        public int getAltura() {
            if (this == null) {
                return -1;
            }
            return super.altura();
        }

        /**
         * Método auxilliar que devuelve la diferencia entre las alturas de los dos
         * subárboles
         */
        private int diferencia() {
            if (this == null) {
                return 0;
            }
            VerticeAVL raiz = convertirAVL(this);
            return Math.abs(raiz.getAltura() - raiz.getAltura());
        }

        /**
         * Método equals que compara el vértice con otro objeto
         */
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass())
                return false;
            @SuppressWarnings("unchecked")
            VerticeAVL vertice = (VerticeAVL) o;
            if (this.altura == vertice.altura)
                return super.equals(o);
            return false;

        }
    }

    /**
     * Método que inserta un elemento al árbol AVL y luego rebalancea
     * 
     * @param elemento
     */
    public void add(T elemento) {
        insert(elemento);
        VerticeAVL verticeInsertado = new VerticeAVL(elemento);
        rebalancear(verticeInsertado);
    }

    /**
     * Método que elimina un elemento del árbol AVL y luego rebalancea
     */
    public void delete(T elemento) {
        delete(raiz, elemento);
        VerticeAVL VerticeEliminado = new VerticeAVL(elemento);
        VerticeAVL padreVerticeEliminado = new VerticeAVL(VerticeEliminado.padre.elemento);
        rebalancear(padreVerticeEliminado);
    }

    /**
     * Método que convierte un vértice del árbol binario a un vértice AVL
     * 
     * @param vertice
     */
    private VerticeAVL convertirAVL(Vertice vertice) {
        return (VerticeAVL) vertice;
    }

    /**
     * Método auxiliar que rebalancea el árbol para que continué cumpliendo las
     * condiciones
     * de los árboles AVL
     */
    private void rebalancear(VerticeAVL vertice) {
        // primero vemos si esta desbalanceado hacia la derecha
        // guardamos en una variable la altura del subarbol derecho e izquierdo
        VerticeAVL hijoDerecho = convertirAVL(vertice.derecho);
        VerticeAVL hijoIzquierdo = convertirAVL(vertice.izquierdo);
        int alturaHijoDerecho = hijoDerecho.getAltura();
        int alturaHijoIzquierdo = hijoIzquierdo.getAltura();

        // si esta desbalanceado hacia la derecha
        if (alturaHijoDerecho == alturaHijoIzquierdo + 2) {
            // si la altura del hijo derecho del hijo derecho es k+1
            VerticeAVL nietoDerechoD = convertirAVL(hijoDerecho.derecho);
            VerticeAVL nietoIzquierdoD = convertirAVL(hijoDerecho.izquierdo);
            int alturaNietoDerechoD = nietoDerechoD.getAltura();
            // si la altura del nieto derecho es igual a la altura del hijo izquierdo más
            // uno
            if (alturaNietoDerechoD == alturaHijoIzquierdo + 1) {
                hijoDerecho.izquierdo = vertice;
                vertice.padre = hijoDerecho;
                vertice.izquierdo = hijoIzquierdo;
                vertice.derecho = nietoIzquierdoD;

            }
            // si la altura del nieto derecho es igual a la altura del hijo izquierdo
            if (alturaNietoDerechoD == alturaHijoIzquierdo) {
                vertice.derecho = nietoIzquierdoD;
                nietoIzquierdoD.derecho = hijoDerecho;
                hijoDerecho.derecho = nietoDerechoD;

                nietoIzquierdoD.derecho = hijoDerecho;
                nietoIzquierdoD.izquierdo = vertice;
                vertice.padre = nietoIzquierdoD;
                hijoDerecho.padre = nietoIzquierdoD;
            }
        }

        // si esta desbalanceado hacia la izquierda
        if (alturaHijoIzquierdo == alturaHijoDerecho + 2) {
            VerticeAVL nietoDerechoI = convertirAVL(hijoIzquierdo.derecho);
            VerticeAVL nietoIzquierdoI = convertirAVL(hijoIzquierdo.izquierdo);
            int alturaNietoIzquierdoI = nietoIzquierdoI.getAltura();
            // si la altura del nieto izquierdo es igual a la altura del hijo derecho más
            // uno
            if (alturaNietoIzquierdoI == alturaHijoDerecho + 1) {
                hijoIzquierdo.derecho = vertice;
                vertice.padre = hijoIzquierdo;
                vertice.derecho = hijoDerecho;
                vertice.izquierdo = nietoDerechoI;
            }
            // si la altura del nieto izquierdo es igual a la altura del hijo derecho
            if (alturaNietoIzquierdoI == alturaHijoDerecho) {
                vertice.izquierdo = nietoDerechoI;
                nietoDerechoI.padre = vertice;
                nietoDerechoI.izquierdo = hijoIzquierdo;
                hijoIzquierdo.izquierdo = nietoIzquierdoI;
                nietoDerechoI.derecho = vertice;
                vertice.padre = nietoDerechoI;
                vertice.derecho = hijoDerecho;
            }

        }
        VerticeAVL padre = convertirAVL(vertice.padre);
        rebalancear(padre);
        if (padre == null) {
            return;
        }
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la derecha por los usuarios de la clase, porque
     * se desbalancean.
     * 
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                "girar a la izquierda por el " +
                "usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la izquierda por los usuarios de la clase, porque
     * se desbalancean.
     * 
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                "girar a la derecha por el " +
                "usuario.");
    }

}
