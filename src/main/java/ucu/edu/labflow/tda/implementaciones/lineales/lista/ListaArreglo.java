package ucu.edu.labflow.tda.implementaciones.lineales.lista;

import ucu.edu.labflow.tda.lineales.TDALista;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Implementación de {@link TDALista} sobre un arreglo redimensionable.
 *
 * <h2>Estructura interna</h2>
 * <ul>
 *   <li>{@code datos}: arreglo de {@code Object} cuya capacidad puede ser
 *       mayor que la cantidad real de elementos.</li>
 *   <li>{@code tamaño}: cantidad de elementos válidos. Las posiciones
 *       {@code [0, tamaño)} contienen los datos en el mismo orden que la
 *       lista lógica; el resto debe permanecer en {@code null}.</li>
 * </ul>
 *
 * <h2>Política de crecimiento</h2>
 * <p>Cuando una inserción supera la capacidad actual el arreglo se duplica
 * (capacidad nueva = max(capacidad·2, requerido)). Esto da inserción al final
 * en O(1) <em>amortizado</em>: aunque algunas inserciones cuestan O(n) por
 * el copiado, el costo promedio sobre una secuencia de n inserciones es
 * O(1).</p>
 *
 * <h2>Costos por operación</h2>
 * <table>
 *   <caption>Costos por operación</caption>
 *   <tr><th>Operación</th><th>Costo</th><th>Comentario</th></tr>
 *   <tr><td>{@code agregar(T)}</td><td>O(1) amortizado</td><td>Inserción al final</td></tr>
 *   <tr><td>{@code agregar(int, T)}</td><td>O(n)</td><td>Por el corrimiento</td></tr>
 *   <tr><td>{@code obtener(int)}</td><td>O(1)</td><td>Acceso aleatorio directo</td></tr>
 *   <tr><td>{@code remover(int)}</td><td>O(n)</td><td>Por el corrimiento</td></tr>
 *   <tr><td>{@code contiene}, {@code indiceDe}, {@code buscar}</td><td>O(n)</td><td>Recorrido lineal</td></tr>
 *   <tr><td>{@code ordenar}</td><td>O(n log n)</td><td>Devuelve una nueva lista</td></tr>
 * </table>
 *
 * @param <T> tipo de los elementos almacenados
 * @see TDALista
 * @see ListaEnlazada
 */
public class ListaArreglo<T> implements TDALista<T> {

    /** Capacidad inicial del arreglo subyacente (potencia de 2 razonable para muchos casos). */
    private static final int CAPACIDAD_INICIAL = 8;

    /** Almacenamiento contiguo. Las posiciones {@code [tamaño, length)} se mantienen en {@code null}. */
    private Object[] datos;

    /** Cantidad de elementos efectivos. {@code tamaño <= datos.length}. */
    private int tamaño;

    /**
     * Construye una lista vacía con capacidad inicial fija. El arreglo
     * crecerá automáticamente al agregar elementos.
     */
    public ListaArreglo() {
        this.datos = new Object[CAPACIDAD_INICIAL];
        this.tamaño = 0;
    }

    /**
     * Inserta {@code elem} al final de la lista en O(1) amortizado.
     *
     * <p>Si el arreglo está lleno se invoca {@link #asegurarCapacidad(int)},
     * que puede provocar un copiado O(n). Sobre una secuencia larga de
     * inserciones el costo promedio sigue siendo constante.</p>
     */
    @Override
    public void agregar(T elem) {
        asegurarCapacidad(tamaño + 1);
        datos[tamaño++] = elem;
    }

    /**
     * Inserta {@code elem} en la posición {@code index} desplazando los
     * elementos posteriores una posición a la derecha.
     *
     * <p>Acepta {@code index == tamaño} (equivale a agregar al final).
     * Costo: O(n) por el corrimiento usando {@code System.arraycopy}.</p>
     *
     * @throws IndexOutOfBoundsException si {@code index < 0} o {@code index > tamaño}
     */
    @Override
    public void agregar(int index, T elem) {
        if (index < 0 || index > tamaño) throw new IndexOutOfBoundsException(index);
        asegurarCapacidad(tamaño + 1);
        System.arraycopy(datos, index, datos, index + 1, tamaño - index);
        datos[index] = elem;
        tamaño++;
    }

    /**
     * Devuelve el elemento en la posición indicada en O(1) (acceso aleatorio).
     *
     * @throws IndexOutOfBoundsException si {@code index} no está en {@code [0, tamaño)}
     */
    @Override
    @SuppressWarnings("unchecked")
    public T obtener(int index) {
        validarIndice(index);
        return (T) datos[index];
    }

    /**
     * Remueve y devuelve el elemento en {@code index} en O(n) por el
     * corrimiento de los elementos posteriores. La última celda se pone
     * en {@code null} para no retener referencias y permitir al GC liberar
     * el objeto.
     *
     * @throws IndexOutOfBoundsException si {@code index} no está en {@code [0, tamaño)}
     */
    @Override
    @SuppressWarnings("unchecked")
    public T remover(int index) {
        validarIndice(index);
        T removido = (T) datos[index];
        int aMover = tamaño - index - 1;
        if (aMover > 0) System.arraycopy(datos, index + 1, datos, index, aMover);
        datos[--tamaño] = null;
        return removido;
    }

    /**
     * Remueve la primera ocurrencia de {@code elem} comparando con
     * {@link Objects#equals}. Costo: O(n).
     *
     * @return {@code true} si se removió, {@code false} si no estaba presente
     */
    @Override
    public boolean remover(T elem) {
        int idx = indiceDe(elem);
        if (idx < 0) return false;
        remover(idx);
        return true;
    }

    /** Equivale a {@code indiceDe(elem) >= 0}. Costo: O(n). */
    @Override
    public boolean contiene(T elem) {
        return indiceDe(elem) >= 0;
    }

    /**
     * Búsqueda lineal por igualdad estructural ({@link Objects#equals}).
     * Maneja correctamente el caso {@code elem == null}.
     *
     * @return el índice de la primera ocurrencia, o {@code -1} si no se encuentra
     */
    @Override
    public int indiceDe(T elem) {
        for (int i = 0; i < tamaño; i++) {
            if (Objects.equals(datos[i], elem)) return i;
        }
        return -1;
    }

    /**
     * Devuelve el primer elemento que cumple el predicado, o {@code null}
     * si ninguno lo cumple. Costo: O(n) en el peor caso.
     */
    @Override
    @SuppressWarnings("unchecked")
    public T buscar(Predicate<T> criterio) {
        for (int i = 0; i < tamaño; i++) {
            T elem = (T) datos[i];
            if (criterio.test(elem)) return elem;
        }
        return null;
    }

    /**
     * Devuelve una <strong>nueva</strong> lista con los mismos elementos
     * ordenados según {@code comparator}. La instancia actual no se modifica.
     *
     * <p>Implementación: copia los elementos a un arreglo, los ordena con
     * {@link Arrays#sort(Object[], Comparator)} (TimSort, O(n log n)) y los
     * vuelca en una nueva {@code ListaArreglo}.</p>
     */
    @Override
    @SuppressWarnings("unchecked")
    public TDALista<T> ordenar(Comparator<T> comparator) {
        T[] copia = (T[]) Arrays.copyOf(datos, tamaño);
        Arrays.sort(copia, comparator);
        ListaArreglo<T> resultado = new ListaArreglo<>();
        for (T t : copia) resultado.agregar(t);
        return resultado;
    }

    /** @return cantidad de elementos válidos almacenados. */
    @Override
    public int tamaño() {
        return tamaño;
    }

    /** @return {@code true} si la lista no contiene elementos. */
    @Override
    public boolean esVacio() {
        return tamaño == 0;
    }

    /**
     * Vacía la lista. Pone explícitamente en {@code null} todas las celdas
     * usadas para no retener referencias innecesarias.
     */
    @Override
    public void vaciar() {
        for (int i = 0; i < tamaño; i++) datos[i] = null;
        tamaño = 0;
    }

    /** Lanza {@link IndexOutOfBoundsException} si {@code index} no está en {@code [0, tamaño)}. */
    private void validarIndice(int index) {
        if (index < 0 || index >= tamaño) throw new IndexOutOfBoundsException(index);
    }

    /**
     * Garantiza que el arreglo interno tenga al menos la capacidad
     * solicitada, duplicándolo si es necesario. Este método es la razón por
     * la que {@link #agregar(Object)} es O(1) <em>amortizado</em> y no O(n)
     * en el peor caso de cada inserción.
     */
    private void asegurarCapacidad(int requerida) {
        if (requerida > datos.length) {
            int nueva = Math.max(datos.length * 2, requerida);
            datos = Arrays.copyOf(datos, nueva);
        }
    }
}
