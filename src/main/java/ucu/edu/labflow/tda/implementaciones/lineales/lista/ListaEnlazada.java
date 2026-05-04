package ucu.edu.labflow.tda.implementaciones.lineales.lista;

import ucu.edu.labflow.tda.lineales.TDALista;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Implementación de {@link TDALista} sobre una lista enlazada simple.
 *
 * <h2>Estructura interna</h2>
 * <ul>
 *   <li>{@code Nodo}: clase interna con un dato y una referencia al
 *       siguiente nodo. La cadena termina en {@code null}.</li>
 *   <li>{@code cabeza}: primer nodo de la lista, o {@code null} si está
 *       vacía.</li>
 *   <li>{@code tamaño}: contador mantenido en cada inserción/eliminación
 *       para que {@link #tamaño()} sea O(1).</li>
 * </ul>
 *
 * <h2>Característica clave</h2>
 * <p>No mantiene puntero al último nodo. Esto la hace ideal cuando el
 * trabajo principal es sobre la cabeza (como en una pila), pero penaliza
 * inserciones al final con O(n) por requerir recorrer hasta el último
 * nodo. Cuando se necesita inserción al final O(1) (por ejemplo para una
 * cola FIFO), conviene una variante con puntero a la cola — ver
 * {@link ColaEnlazada}.</p>
 *
 * <h2>Costos por operación</h2>
 * <table>
 *   <caption>Costos por operación</caption>
 *   <tr><th>Operación</th><th>Costo</th></tr>
 *   <tr><td>{@code agregar(0, T)} (insertar al inicio)</td><td>O(1)</td></tr>
 *   <tr><td>{@code agregar(T)} (insertar al final)</td><td>O(n)</td></tr>
 *   <tr><td>{@code remover(0)}</td><td>O(1)</td></tr>
 *   <tr><td>{@code obtener(int)}</td><td>O(n)</td></tr>
 *   <tr><td>{@code contiene}, {@code indiceDe}, {@code buscar}</td><td>O(n)</td></tr>
 * </table>
 *
 * @param <T> tipo de los elementos almacenados
 * @see TDALista
 * @see ListaArreglo
 */
public class ListaEnlazada<T> implements TDALista<T> {

    /**
     * Nodo de la lista enlazada. Encapsula un dato y un enlace al siguiente
     * nodo. Es {@code static} (no depende de la instancia externa) y privado
     * para que su estructura sea un detalle de implementación.
     */
    private static class Nodo<T> {
        T dato;
        Nodo<T> siguiente;

        Nodo(T dato) {
            this.dato = dato;
        }
    }

    /** Primer nodo de la lista, o {@code null} si la lista está vacía. */
    private Nodo<T> cabeza;

    /** Cantidad de elementos. Se mantiene sincronizado con la cadena de nodos. */
    private int tamaño;

    /**
     * Inserta {@code elem} al final recorriendo toda la cadena hasta el
     * último nodo. Costo: O(n).
     */
    @Override
    public void agregar(T elem) {
        Nodo<T> nuevo = new Nodo<>(elem);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo<T> actual = cabeza;
            while (actual.siguiente != null) actual = actual.siguiente;
            actual.siguiente = nuevo;
        }
        tamaño++;
    }

    /**
     * Inserta {@code elem} en la posición {@code index}.
     *
     * <p>Casos: {@code index == 0} es O(1) (sólo se reescribe la cabeza);
     * cualquier otro índice requiere localizar el nodo previo recorriendo
     * la lista, costo O(index).</p>
     *
     * @throws IndexOutOfBoundsException si {@code index < 0} o {@code index > tamaño}
     */
    @Override
    public void agregar(int index, T elem) {
        if (index < 0 || index > tamaño) throw new IndexOutOfBoundsException(index);
        if (index == 0) {
            Nodo<T> nuevo = new Nodo<>(elem);
            nuevo.siguiente = cabeza;
            cabeza = nuevo;
            tamaño++;
            return;
        }
        Nodo<T> previo = nodoEn(index - 1);
        Nodo<T> nuevo = new Nodo<>(elem);
        nuevo.siguiente = previo.siguiente;
        previo.siguiente = nuevo;
        tamaño++;
    }

    /** Acceso por índice. Costo: O(index) por el recorrido. */
    @Override
    public T obtener(int index) {
        validarIndice(index);
        return nodoEn(index).dato;
    }

    /**
     * Remueve y devuelve el elemento en la posición indicada.
     *
     * <p>{@code index == 0} es O(1); el resto requiere recorrer hasta el
     * nodo previo (O(index)). El nodo eliminado queda inalcanzable y será
     * recolectado.</p>
     *
     * @throws IndexOutOfBoundsException si {@code index} no está en {@code [0, tamaño)}
     */
    @Override
    public T remover(int index) {
        validarIndice(index);
        T dato;
        if (index == 0) {
            dato = cabeza.dato;
            cabeza = cabeza.siguiente;
        } else {
            Nodo<T> previo = nodoEn(index - 1);
            dato = previo.siguiente.dato;
            previo.siguiente = previo.siguiente.siguiente;
        }
        tamaño--;
        return dato;
    }

    /**
     * Remueve la primera ocurrencia que iguale a {@code elem} con
     * {@link Objects#equals}. Recorre la lista manteniendo el nodo previo
     * para poder hacer el splice. Costo: O(n).
     */
    @Override
    public boolean remover(T elem) {
        Nodo<T> previo = null;
        Nodo<T> actual = cabeza;
        while (actual != null) {
            if (Objects.equals(actual.dato, elem)) {
                if (previo == null) cabeza = actual.siguiente;
                else previo.siguiente = actual.siguiente;
                tamaño--;
                return true;
            }
            previo = actual;
            actual = actual.siguiente;
        }
        return false;
    }

    /** Equivale a {@code indiceDe(elem) >= 0}. Costo: O(n). */
    @Override
    public boolean contiene(T elem) {
        return indiceDe(elem) >= 0;
    }

    /**
     * Búsqueda lineal por igualdad. Devuelve {@code -1} si no se encuentra.
     * Costo: O(n).
     */
    @Override
    public int indiceDe(T elem) {
        int i = 0;
        for (Nodo<T> n = cabeza; n != null; n = n.siguiente, i++) {
            if (Objects.equals(n.dato, elem)) return i;
        }
        return -1;
    }

    /** Recorre la cadena devolviendo el primer dato que cumple el predicado. */
    @Override
    public T buscar(Predicate<T> criterio) {
        for (Nodo<T> n = cabeza; n != null; n = n.siguiente) {
            if (criterio.test(n.dato)) return n.dato;
        }
        return null;
    }

    /**
     * Devuelve una <strong>nueva</strong> lista enlazada con los mismos
     * elementos ordenados según {@code comparator}.
     *
     * <p>Implementación: vuelca los datos a un arreglo (para aprovechar
     * {@link Arrays#sort}, O(n log n)) y reconstruye una nueva lista
     * enlazada con el resultado. La instancia actual no se modifica.</p>
     */
    @Override
    @SuppressWarnings("unchecked")
    public TDALista<T> ordenar(Comparator<T> comparator) {
        Object[] buffer = new Object[tamaño];
        int i = 0;
        for (Nodo<T> n = cabeza; n != null; n = n.siguiente) buffer[i++] = n.dato;
        Arrays.sort((T[]) buffer, comparator);
        ListaEnlazada<T> resultado = new ListaEnlazada<>();
        for (Object o : buffer) resultado.agregar((T) o);
        return resultado;
    }

    /** O(1): se mantiene un contador. */
    @Override
    public int tamaño() {
        return tamaño;
    }

    /** O(1): basta con mirar la cabeza. */
    @Override
    public boolean esVacio() {
        return cabeza == null;
    }

    /**
     * Reinicia la lista a vacía. Soltar la referencia a la cabeza alcanza
     * para que el GC libere todos los nodos.
     */
    @Override
    public void vaciar() {
        cabeza = null;
        tamaño = 0;
    }

    /**
     * Aplica una acción sobre cada elemento, en orden, recorriendo la
     * cadena de nodos una única vez. Costo: O(n).
     *
     * <p>Ofrece a las subclases una manera eficiente de iterar sin pasar
     * por {@link #obtener(int)}, que en una lista enlazada es O(i) por
     * llamada y degenera el costo total a O(n²).</p>
     */
    public void paraCada(java.util.function.Consumer<? super T> accion) {
        for (Nodo<T> n = cabeza; n != null; n = n.siguiente) accion.accept(n.dato);
    }

    /**
     * Localiza el nodo en la posición indicada recorriendo desde la cabeza.
     * Sólo se llama con índices ya validados.
     */
    private Nodo<T> nodoEn(int index) {
        Nodo<T> n = cabeza;
        for (int i = 0; i < index; i++) n = n.siguiente;
        return n;
    }

    private void validarIndice(int index) {
        if (index < 0 || index >= tamaño) throw new IndexOutOfBoundsException(index);
    }
}
