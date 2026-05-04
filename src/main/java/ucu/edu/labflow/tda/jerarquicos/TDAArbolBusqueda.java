package ucu.edu.labflow.tda.jerarquicos;


/**
 * Define un Árbol Binario de Búsqueda (BST): un {@link TDAArbolBinario} en el que las claves
 * tienen orden total y se admite búsqueda, inserción y eliminación por criterio comparable.
 *
 * <h2>Invariante BST</h2>
 * <ul>
 *   <li>Toda clave del subárbol izquierdo es menor que la del nodo.</li>
 *   <li>Toda clave del subárbol derecho es mayor.</li>
 *   <li>No se admiten claves duplicadas.</li>
 * </ul>
 *
 * <h2>Costos</h2>
 * <p>El costo de las operaciones {@link #buscar}, {@link #insertar}, {@link #eliminar} y
 * {@link #obtenerNivel} es proporcional a la altura del árbol. En un BST sin balanceo el
 * peor caso es O(n); para garantía O(log n) en peor caso ver {@link TDAArbolBalanceado}.</p>
 *
 * @param <T> el tipo de los elementos almacenados
 * @see TDAArbolBinario
 * @see TDAArbolBalanceado
 */
public interface TDAArbolBusqueda<T> extends TDAArbolBinario<T> {

    /**
     * Busca el primer dato del árbol que coincide con el criterio.
     *
     * @param criterioBusqueda criterio comparable contra T (admite criterios sintéticos)
     * @return el dato encontrado, o {@code null} si no existe
     */
    T buscar(Comparable<T> criterioBusqueda);

    /**
     * Inserta un dato en el árbol respetando el invariante BST.
     *
     * <p>Si la clave ya existe, no se inserta nuevamente.</p>
     *
     * @param dato el dato a insertar
     * @return {@code true} si el dato fue agregado; {@code false} si la clave ya existía
     */
    boolean insertar(T dato);

    /**
     * Elimina el nodo cuya clave coincide con el criterio.
     *
     * <p>Como el árbol no admite duplicados, esta operación elimina como máximo un nodo por
     * invocación.</p>
     *
     * @param criterioBusqueda criterio que identifica el nodo a eliminar
     * @return {@code true} si se eliminó un nodo; {@code false} si la clave no estaba presente
     */
    boolean eliminar(Comparable<T> criterioBusqueda);

    /**
     * Devuelve el nivel (cantidad de aristas desde la raíz) del primer nodo cuya clave
     * coincide con el criterio.
     *
     * @param criterioBusqueda criterio comparable contra T
     * @return el nivel, o {@code -1} si la clave no está en el árbol
     */
    int obtenerNivel(Comparable<T> criterioBusqueda);
}
