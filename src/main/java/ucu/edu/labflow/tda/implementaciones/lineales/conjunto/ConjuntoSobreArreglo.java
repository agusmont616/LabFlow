package ucu.edu.labflow.tda.implementaciones.lineales.conjunto;

import ucu.edu.labflow.tda.implementaciones.lineales.lista.ListaArreglo;
import ucu.edu.labflow.tda.lineales.TDAConjunto;

/**
 * Implementación de {@link TDAConjunto} <em>por herencia</em> de
 * {@link ListaArreglo}.
 *
 * <h2>Idea</h2>
 * <p>Un conjunto es una lista con una restricción extra: no puede tener
 * elementos repetidos. Por lo tanto basta con heredar todas las
 * operaciones de {@link ListaArreglo} y sobrescribir las inserciones para
 * que verifiquen unicidad antes de delegar al método de la clase base.</p>
 *
 * <h2>Costos</h2>
 * <p>El chequeo de pertenencia es O(n) (búsqueda lineal), por lo que cada
 * inserción cuesta O(n). Las operaciones binarias entre conjuntos
 * ({@code union}, {@code interseccion}, {@code diferencia}) son O(n·m)
 * porque para cada elemento del primer conjunto se hace un
 * {@code contiene} sobre el otro.</p>
 *
 * <h2>Comparación con un conjunto basado en hash</h2>
 * <p>Esta implementación es la más simple posible y mantiene el orden de
 * inserción (heredado de la lista). Una versión basada en una tabla de
 * hash daría {@code agregar}/{@code contiene} en O(1) promedio, pero
 * perdería el orden — sería un buen ejercicio adicional.</p>
 *
 * @param <T> tipo de los elementos almacenados
 * @see TDAConjunto
 * @see ConjuntoSobreEnlazada
 */
public class ConjuntoSobreArreglo<T> extends ListaArreglo<T> implements TDAConjunto<T> {

    /**
     * Inserta sólo si {@code elem} no estaba ya presente. Si ya existía,
     * la operación es un no-op silencioso (semántica de conjunto).
     */
    @Override
    public void agregar(T elem) {
        if (!contiene(elem)) super.agregar(elem);
    }

    /**
     * Variante posicional con semántica de unicidad. Valida el índice
     * antes de chequear duplicados, para que un índice fuera de rango
     * lance {@link IndexOutOfBoundsException} aunque {@code elem} ya
     * estuviera presente.
     */
    @Override
    public void agregar(int index, T elem) {
        if (index < 0 || index > tamaño()) throw new IndexOutOfBoundsException(index);
        if (!contiene(elem)) super.agregar(index, elem);
    }

    /**
     * Unión: nuevo conjunto que contiene los elementos de {@code this}
     * seguidos de los de {@code otro} que no estaban presentes. El uso de
     * {@link #agregar(Object)} dispara automáticamente el chequeo de
     * unicidad. O(n·m).
     */
    @Override
    public TDAConjunto<T> union(TDAConjunto<T> otro) {
        ConjuntoSobreArreglo<T> resultado = new ConjuntoSobreArreglo<>();
        for (int i = 0; i < tamaño(); i++) resultado.agregar(obtener(i));
        for (int i = 0; i < otro.tamaño(); i++) resultado.agregar(otro.obtener(i));
        return resultado;
    }

    /** Intersección: elementos de {@code this} que también están en {@code otro}. O(n·m). */
    @Override
    public TDAConjunto<T> interseccion(TDAConjunto<T> otro) {
        ConjuntoSobreArreglo<T> resultado = new ConjuntoSobreArreglo<>();
        for (int i = 0; i < tamaño(); i++) {
            T elem = obtener(i);
            if (otro.contiene(elem)) resultado.agregar(elem);
        }
        return resultado;
    }

    /** Diferencia: elementos de {@code this} que no están en {@code otro}. O(n·m). */
    @Override
    public TDAConjunto<T> diferencia(TDAConjunto<T> otro) {
        ConjuntoSobreArreglo<T> resultado = new ConjuntoSobreArreglo<>();
        for (int i = 0; i < tamaño(); i++) {
            T elem = obtener(i);
            if (!otro.contiene(elem)) resultado.agregar(elem);
        }
        return resultado;
    }

    /**
     * @return {@code true} si todos los elementos de {@code this} están en
     *         {@code otro}. Por convención, el conjunto vacío es subconjunto
     *         de cualquier otro (el bucle no encuentra contraejemplos y se
     *         devuelve {@code true}).
     */
    @Override
    public boolean esSubconjuntoDe(TDAConjunto<T> otro) {
        for (int i = 0; i < tamaño(); i++) {
            if (!otro.contiene(obtener(i))) return false;
        }
        return true;
    }
}
