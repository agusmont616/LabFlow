package ucu.edu.labflow.tda.implementaciones.lineales.cola;

import ucu.edu.labflow.tda.lineales.TDACola;
import ucu.edu.labflow.tda.lineales.TDALista;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Implementación de {@link TDACola} sobre lista enlazada simple con
 * puntero al último nodo.
 *
 * <h2>Por qué un puntero al último nodo?</h2>
 * <p>Una cola FIFO debe insertar al final y remover del frente. Una lista
 * enlazada simple "pura" (sin puntero al final) sólo permite una de las
 * dos operaciones en O(1): la del lado de la cabeza. Para operar también
 * el final en O(1) basta agregar una referencia adicional, {@code ultimo},
 * que apunta al último nodo. Esto sigue siendo una "lista enlazada simple"
 * — los nodos siguen teniendo un único puntero al siguiente.</p>
 *
 * <h2>Estructura interna</h2>
 * <ul>
 *   <li>{@code Nodo}: clase privada con un dato y un enlace al siguiente.</li>
 *   <li>{@code cabeza}: primer nodo, o {@code null} si la cola está vacía.</li>
 *   <li>{@code ultimo}: último nodo, o {@code null} si la cola está vacía.</li>
 *   <li>{@code tamaño}: contador mantenido en cada operación de inserción
 *       o eliminación.</li>
 * </ul>
 *
 * <h2>Invariantes</h2>
 * <ul>
 *   <li>{@code (cabeza == null) ⇔ (ultimo == null) ⇔ (tamaño == 0)}.</li>
 *   <li>Si {@code tamaño == 1} entonces {@code cabeza == ultimo}.</li>
 *   <li>{@code ultimo.siguiente == null} siempre.</li>
 * </ul>
 *
 * <p>Los métodos mutadores deben preservar estas tres condiciones; en
 * particular, al remover el último nodo o vaciar la lista hay que
 * actualizar {@code ultimo} a {@code null}.</p>
 *
 * <h2>Costos por operación</h2>
 * <table>
 *   <caption>Costos por operación</caption>
 *   <tr><th>Operación</th><th>Costo</th></tr>
 *   <tr><td>{@code poneEnCola}, {@code agregar(T)}</td><td>O(1)</td></tr>
 *   <tr><td>{@code quitaDeCola}, {@code frente()}</td><td>O(1)</td></tr>
 *   <tr><td>{@code obtener(int)}, {@code agregar(int, T)}, {@code remover(int)}</td><td>O(n)</td></tr>
 * </table>
 *
 * @param <T> tipo de los elementos almacenados
 * @see TDACola
 * @see ColaArregloCircular
 */
public class ColaEnlazada<T> implements TDACola<T> {

    /** Nodo de la cadena. Privado para no exponer detalles de implementación. */
    private static class Nodo<T> {
        T dato;
        Nodo<T> siguiente;

        Nodo(T dato) {
            this.dato = dato;
        }
    }

    private Nodo<T> cabeza;
    private Nodo<T> ultimo;
    private int tamaño;

    /**
     * Devuelve el dato del primer nodo (frente) sin removerlo. O(1).
     *
     * @throws NoSuchElementException si la cola está vacía
     */
    @Override
    public T frente() {
        if (cabeza == null) throw new NoSuchElementException();
        return cabeza.dato;
    }

    /** Encolar al final. O(1). */
    @Override
    public boolean poneEnCola(T dato) {
        agregar(dato);
        return true;
    }

    /**
     * Desencolar el frente. Avanza la cabeza al siguiente nodo y, si la
     * cola queda vacía, también limpia el puntero {@code ultimo}.
     *
     * @throws NoSuchElementException si la cola está vacía
     */
    @Override
    public T quitaDeCola() {
        if (cabeza == null) throw new NoSuchElementException();
        T dato = cabeza.dato;
        cabeza = cabeza.siguiente;
        if (cabeza == null) ultimo = null;
        tamaño--;
        return dato;
    }

    /**
     * Inserta al final aprovechando el puntero {@code ultimo}.
     *
     * <p>Casos:</p>
     * <ul>
     *   <li>Cola vacía: el nuevo nodo se vuelve cabeza y último simultáneamente.</li>
     *   <li>Cola no vacía: el {@code siguiente} del antiguo último se reescribe
     *       hacia el nuevo nodo, que pasa a ser el nuevo {@code ultimo}.</li>
     * </ul>
     */
    @Override
    public void agregar(T elem) {
        Nodo<T> nuevo = new Nodo<>(elem);
        if (ultimo == null) {
            cabeza = nuevo;
        } else {
            ultimo.siguiente = nuevo;
        }
        ultimo = nuevo;
        tamaño++;
    }

    /**
     * Inserta en una posición intermedia. Tres casos:
     * <ul>
     *   <li>{@code index == tamaño}: equivalente a {@link #agregar(Object)} → O(1).</li>
     *   <li>{@code index == 0}: nuevo nodo como cabeza → O(1).</li>
     *   <li>resto: localizar el nodo previo y enlazar → O(index).</li>
     * </ul>
     *
     * @throws IndexOutOfBoundsException si {@code index} no está en {@code [0, tamaño]}
     */
    @Override
    public void agregar(int index, T elem) {
        if (index < 0 || index > tamaño) throw new IndexOutOfBoundsException(index);
        if (index == tamaño) {
            agregar(elem);
            return;
        }
        if (index == 0) {
            Nodo<T> nuevo = new Nodo<>(elem);
            nuevo.siguiente = cabeza;
            cabeza = nuevo;
            if (ultimo == null) ultimo = nuevo;
            tamaño++;
            return;
        }
        Nodo<T> previo = nodoEn(index - 1);
        Nodo<T> nuevo = new Nodo<>(elem);
        nuevo.siguiente = previo.siguiente;
        previo.siguiente = nuevo;
        tamaño++;
    }

    /** Acceso por índice. O(index). */
    @Override
    public T obtener(int index) {
        validarIndice(index);
        return nodoEn(index).dato;
    }

    /**
     * Remueve por índice. Si se remueve el último nodo se actualiza
     * {@code ultimo} para preservar el invariante de que {@code ultimo}
     * apunta al verdadero final.
     */
    @Override
    public T remover(int index) {
        validarIndice(index);
        if (index == 0) return quitaDeCola();
        Nodo<T> previo = nodoEn(index - 1);
        Nodo<T> aRemover = previo.siguiente;
        previo.siguiente = aRemover.siguiente;
        if (aRemover == ultimo) ultimo = previo;
        tamaño--;
        return aRemover.dato;
    }

    /**
     * Remueve la primera ocurrencia que iguale a {@code elem} con
     * {@link Objects#equals}. Mantiene actualizado {@code ultimo} si el
     * nodo removido era el último.
     */
    @Override
    public boolean remover(T elem) {
        Nodo<T> previo = null;
        Nodo<T> actual = cabeza;
        while (actual != null) {
            if (Objects.equals(actual.dato, elem)) {
                if (previo == null) cabeza = actual.siguiente;
                else previo.siguiente = actual.siguiente;
                if (actual == ultimo) ultimo = previo;
                tamaño--;
                return true;
            }
            previo = actual;
            actual = actual.siguiente;
        }
        return false;
    }

    @Override
    public boolean contiene(T elem) {
        return indiceDe(elem) >= 0;
    }

    @Override
    public int indiceDe(T elem) {
        int i = 0;
        for (Nodo<T> n = cabeza; n != null; n = n.siguiente, i++) {
            if (Objects.equals(n.dato, elem)) return i;
        }
        return -1;
    }

    @Override
    public T buscar(Predicate<T> criterio) {
        for (Nodo<T> n = cabeza; n != null; n = n.siguiente) {
            if (criterio.test(n.dato)) return n.dato;
        }
        return null;
    }

    /**
     * Devuelve una nueva cola con los elementos ordenados según el
     * comparador. La cola actual no se modifica. O(n log n).
     */
    @Override
    @SuppressWarnings("unchecked")
    public TDALista<T> ordenar(Comparator<T> comparator) {
        Object[] buffer = new Object[tamaño];
        int i = 0;
        for (Nodo<T> n = cabeza; n != null; n = n.siguiente) buffer[i++] = n.dato;
        Arrays.sort((T[]) buffer, comparator);
        ColaEnlazada<T> resultado = new ColaEnlazada<>();
        for (Object o : buffer) resultado.agregar((T) o);
        return resultado;
    }

    @Override
    public int tamaño() {
        return tamaño;
    }

    @Override
    public boolean esVacio() {
        return cabeza == null;
    }

    /**
     * Reinicia la cola a vacía. Limpia los tres campos para mantener
     * el invariante {@code (cabeza == null) ⇔ (ultimo == null) ⇔ (tamaño == 0)}.
     */
    @Override
    public void vaciar() {
        cabeza = null;
        ultimo = null;
        tamaño = 0;
    }

    /** Recorre la cadena hasta el nodo en la posición lógica solicitada. */
    private Nodo<T> nodoEn(int index) {
        Nodo<T> n = cabeza;
        for (int i = 0; i < index; i++) n = n.siguiente;
        return n;
    }

    private void validarIndice(int index) {
        if (index < 0 || index >= tamaño) throw new IndexOutOfBoundsException(index);
    }
}
