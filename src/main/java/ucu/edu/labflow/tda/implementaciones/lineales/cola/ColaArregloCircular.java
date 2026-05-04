package ucu.edu.labflow.tda.implementaciones.lineales.cola;

import ucu.edu.labflow.tda.lineales.TDACola;
import ucu.edu.labflow.tda.lineales.TDALista;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Implementación de {@link TDACola} sobre un buffer circular en arreglo.
 *
 * <h2>Por qué <em>circular</em>?</h2>
 * <p>Una cola FIFO inserta al final y remueve del frente. Si los datos
 * vivieran siempre en {@code [0, tamaño)} de un arreglo, cada
 * desencolado costaría O(n) por el corrimiento. Para evitarlo se permite
 * que los elementos "den la vuelta": el frente se mueve hacia adelante
 * conforme se desencolan elementos, y los nuevos se ubican aprovechando
 * las celdas que quedaron libres al inicio.</p>
 *
 * <h2>Estructura interna</h2>
 * <ul>
 *   <li>{@code datos}: arreglo de capacidad fija (se duplica al llenarse).</li>
 *   <li>{@code frente}: índice físico del primer elemento (válido si
 *       {@code tamaño > 0}).</li>
 *   <li>{@code tamaño}: cantidad de elementos. El último ocupa la posición
 *       física {@code (frente + tamaño - 1) % datos.length}.</li>
 * </ul>
 *
 * <h2>Mapeo lógico → físico</h2>
 * <p>El método {@link #fisico(int)} traduce un índice lógico {@code i} (lo
 * que el usuario ve, {@code 0..tamaño-1}) al índice físico real:
 * {@code (frente + i) % datos.length}. Esto permite implementar todas las
 * operaciones de {@code TDALista} sobre una vista lineal sin saber de la
 * estructura circular.</p>
 *
 * <h2>Crecimiento y reubicación</h2>
 * <p>Cuando hace falta más capacidad, en {@link #asegurarCapacidad(int)} se
 * crea un arreglo del doble de tamaño y los elementos se copian
 * <strong>linealizados</strong> (volcados desde {@code frente} al inicio
 * del nuevo arreglo). Después, {@code frente} se reinicia a 0. Esto evita
 * complicar el resto de los métodos.</p>
 *
 * <h2>Costos por operación</h2>
 * <table>
 *   <caption>Costos por operación</caption>
 *   <tr><th>Operación</th><th>Costo</th></tr>
 *   <tr><td>{@code poneEnCola}, {@code agregar(T)}</td><td>O(1) amortizado</td></tr>
 *   <tr><td>{@code quitaDeCola}, {@code frente()}</td><td>O(1)</td></tr>
 *   <tr><td>{@code obtener(int)}</td><td>O(1)</td></tr>
 *   <tr><td>{@code agregar(int, T)}, {@code remover(int)}</td><td>O(n)</td></tr>
 * </table>
 *
 * @param <T> tipo de los elementos almacenados
 * @see TDACola
 * @see ColaEnlazada
 */
public class ColaArregloCircular<T> implements TDACola<T> {

    private static final int CAPACIDAD_INICIAL = 8;

    /** Buffer subyacente. Sus celdas válidas dependen de {@code frente} y {@code tamaño}. */
    private Object[] datos;

    /** Índice físico del primer elemento. Carece de sentido si {@code tamaño == 0}. */
    private int frente;

    /** Cantidad de elementos en la cola. */
    private int tamaño;

    /** Construye una cola vacía con la capacidad inicial. */
    public ColaArregloCircular() {
        this.datos = new Object[CAPACIDAD_INICIAL];
    }

    /**
     * Devuelve el elemento del frente sin removerlo. O(1).
     *
     * @throws NoSuchElementException si la cola está vacía
     */
    @Override
    public T frente() {
        if (esVacio()) throw new NoSuchElementException();
        return obtenerFisico(frente);
    }

    /** Encolar al final. O(1) amortizado. */
    @Override
    public boolean poneEnCola(T dato) {
        agregar(dato);
        return true;
    }

    /**
     * Desencolar el elemento del frente. Avanza {@code frente} con módulo
     * para mantener el carácter circular y libera la referencia anterior.
     * O(1).
     *
     * @throws NoSuchElementException si la cola está vacía
     */
    @Override
    public T quitaDeCola() {
        if (esVacio()) throw new NoSuchElementException();
        T removido = obtenerFisico(frente);
        datos[frente] = null;
        frente = (frente + 1) % datos.length;
        tamaño--;
        return removido;
    }

    /** Inserta al final lógico. Equivale a {@code poneEnCola}. O(1) amortizado. */
    @Override
    public void agregar(T elem) {
        asegurarCapacidad(tamaño + 1);
        datos[fisico(tamaño)] = elem;
        tamaño++;
    }

    /**
     * Inserta en una posición intermedia. Para mantener consistencia con
     * el contrato de {@link TDALista} se desplazan los elementos
     * posteriores una posición a la derecha, calculando los índices
     * físicos uno por uno con {@link #fisico(int)}. O(n).
     *
     * @throws IndexOutOfBoundsException si {@code index} no está en {@code [0, tamaño]}
     */
    @Override
    public void agregar(int index, T elem) {
        if (index < 0 || index > tamaño) throw new IndexOutOfBoundsException(index);
        asegurarCapacidad(tamaño + 1);
        for (int i = tamaño; i > index; i--) {
            datos[fisico(i)] = datos[fisico(i - 1)];
        }
        datos[fisico(index)] = elem;
        tamaño++;
    }

    /** Acceso por índice lógico. O(1). */
    @Override
    public T obtener(int index) {
        validarIndice(index);
        return obtenerFisico(fisico(index));
    }

    /**
     * Remueve un elemento intermedio desplazando los posteriores una
     * posición a la izquierda. O(n).
     *
     * @throws IndexOutOfBoundsException si {@code index} no está en {@code [0, tamaño)}
     */
    @Override
    public T remover(int index) {
        validarIndice(index);
        T removido = obtenerFisico(fisico(index));
        for (int i = index; i < tamaño - 1; i++) {
            datos[fisico(i)] = datos[fisico(i + 1)];
        }
        datos[fisico(tamaño - 1)] = null;
        tamaño--;
        return removido;
    }

    /** Remueve la primera ocurrencia. O(n) para localizarla, O(n) para el corrimiento. */
    @Override
    public boolean remover(T elem) {
        int idx = indiceDe(elem);
        if (idx < 0) return false;
        remover(idx);
        return true;
    }

    @Override
    public boolean contiene(T elem) {
        return indiceDe(elem) >= 0;
    }

    /** Búsqueda lineal en orden lógico. Maneja {@code null} con {@link Objects#equals}. */
    @Override
    public int indiceDe(T elem) {
        for (int i = 0; i < tamaño; i++) {
            if (Objects.equals(obtenerFisico(fisico(i)), elem)) return i;
        }
        return -1;
    }

    @Override
    public T buscar(Predicate<T> criterio) {
        for (int i = 0; i < tamaño; i++) {
            T elem = obtenerFisico(fisico(i));
            if (criterio.test(elem)) return elem;
        }
        return null;
    }

    /**
     * Devuelve una nueva cola con los elementos ordenados según el
     * comparador. La cola actual no se modifica. O(n log n).
     */
    @Override
    public TDALista<T> ordenar(Comparator<T> comparator) {
        Object[] buffer = new Object[tamaño];
        for (int i = 0; i < tamaño; i++) buffer[i] = obtenerFisico(fisico(i));
        @SuppressWarnings("unchecked")
        T[] tipado = (T[]) buffer;
        Arrays.sort(tipado, comparator);
        ColaArregloCircular<T> resultado = new ColaArregloCircular<>();
        for (T t : tipado) resultado.agregar(t);
        return resultado;
    }

    @Override
    public int tamaño() {
        return tamaño;
    }

    @Override
    public boolean esVacio() {
        return tamaño == 0;
    }

    /**
     * Vacía la cola. Limpia las referencias y reinicia {@code frente} a 0
     * para mantener la invariante de que un buffer vacío empieza desde el
     * inicio.
     */
    @Override
    public void vaciar() {
        for (int i = 0; i < tamaño; i++) datos[fisico(i)] = null;
        frente = 0;
        tamaño = 0;
    }

    /**
     * Traduce un índice lógico (visible al usuario) al índice físico
     * dentro del arreglo, aplicando módulo sobre la capacidad. Esta es la
     * abstracción central de la cola circular.
     */
    private int fisico(int indiceLogico) {
        return (frente + indiceLogico) % datos.length;
    }

    @SuppressWarnings("unchecked")
    private T obtenerFisico(int indiceFisico) {
        return (T) datos[indiceFisico];
    }

    private void validarIndice(int index) {
        if (index < 0 || index >= tamaño) throw new IndexOutOfBoundsException(index);
    }

    /**
     * Garantiza capacidad mínima. Si hace falta crecer, copia los
     * elementos al inicio del nuevo arreglo en orden lógico (linealiza la
     * estructura) y reinicia {@code frente} a 0. Sin esta linealización
     * el código del módulo se complicaría tras cada redimensión.
     */
    private void asegurarCapacidad(int requerida) {
        if (requerida <= datos.length) return;
        int nuevaCap = Math.max(datos.length * 2, requerida);
        Object[] nuevo = new Object[nuevaCap];
        for (int i = 0; i < tamaño; i++) {
            nuevo[i] = datos[fisico(i)];
        }
        datos = nuevo;
        frente = 0;
    }
}
