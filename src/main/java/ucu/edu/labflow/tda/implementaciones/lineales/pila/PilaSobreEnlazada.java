package ucu.edu.labflow.tda.implementaciones.lineales.pila;

import ucu.edu.labflow.tda.implementaciones.lineales.lista.ListaEnlazada;
import ucu.edu.labflow.tda.lineales.TDAPila;

import java.util.NoSuchElementException;

/**
 * Implementación de {@link TDAPila} <em>por herencia</em> de
 * {@link ListaEnlazada}.
 *
 * <h2>Decisión de diseño: tope en la cabeza</h2>
 * <p>En una lista enlazada simple sin puntero al último nodo, sólo la
 * cabeza permite operar en O(1) sin recorrido. Por eso el tope de la
 * pila se ubica allí:</p>
 * <ul>
 *   <li>{@link #mete(Object) mete}: inserta al inicio con
 *       {@code agregar(0, dato)} → O(1).</li>
 *   <li>{@link #saca()}: remueve el primer nodo → O(1).</li>
 *   <li>{@link #tope()}: lee el dato de la cabeza → O(1).</li>
 * </ul>
 *
 * <h2>Comparación con {@link PilaSobreArreglo}</h2>
 * <p>Las dos pilas dan O(1) para sus operaciones, pero por motivos
 * distintos: el arreglo trabaja al final (acceso directo por índice), la
 * enlazada al principio (la cabeza siempre es accesible). La enlazada
 * nunca paga el costo de redimensionar; el arreglo aprovecha mejor la
 * memoria contigua y cachés del procesador.</p>
 *
 * @param <T> tipo de los elementos almacenados
 * @see TDAPila
 * @see PilaSobreArreglo
 */
public class PilaSobreEnlazada<T> extends ListaEnlazada<T> implements TDAPila<T> {

    /**
     * Devuelve el elemento del tope (cabeza) sin removerlo.
     *
     * @throws NoSuchElementException si la pila está vacía
     */
    @Override
    public T tope() {
        if (esVacio()) throw new NoSuchElementException();
        return obtener(0);
    }

    /**
     * Remueve y devuelve el tope (cabeza).
     *
     * @throws NoSuchElementException si la pila está vacía
     */
    @Override
    public T saca() {
        if (esVacio()) throw new NoSuchElementException();
        return remover(0);
    }

    /**
     * Apila un elemento en el tope. Como el tope es la cabeza, se inserta
     * en el índice 0 — operación O(1) en una lista enlazada.
     */
    @Override
    public void mete(T dato) {
        agregar(0, dato);
    }
}
