package ucu.edu.labflow.tda.jerarquicos;


import java.util.function.Consumer;

/**
 * Define un elemento del TDA Árbol Binario ({@link TDAArbolBinario}).
 *
 * <h2>Conceptualización: "Elemento" = "subárbol enraizado en este nodo"</h2>
 * <p>Pese al nombre, un {@code TDAElemento} no representa solamente un nodo aislado: representa
 * el <strong>subárbol completo enraizado en este nodo</strong>. Cada operación
 * ({@link #buscar}, {@link #insertar}, {@link #eliminar}, recorridos, conteos, {@link #altura})
 * opera sobre todo el subárbol, no sólo sobre el nodo actual. La implementación es
 * <strong>recursiva</strong>: la operación se delega en los hijos y los resultados se combinan.</p>
 *
 * <h2>Mutación estructural por retorno, no por setters</h2>
 * <p>Las operaciones que modifican la estructura ({@code insertar}, {@code eliminar}) devuelven
 * la <strong>nueva raíz del subárbol</strong> resultante. El padre — o el árbol contenedor en
 * el caso de la raíz absoluta — debe reasignar su puntero al valor devuelto. Esto permite que
 * implementaciones auto-balanceadas (p.ej. AVL) cambien la raíz del subárbol durante una
 * rotación sin necesidad de exponer setters peligrosos en la interfaz pública.</p>
 *
 * <h2>Criterios de búsqueda vs. datos a insertar</h2>
 * <p>Los métodos {@link #buscar} y {@link #eliminar} reciben {@link Comparable Comparable&lt;T&gt;}
 * porque admiten <em>criterios sintéticos</em> (claves parciales, comparadores ad-hoc). En
 * cambio {@link #insertar} recibe {@code T} directamente: el dato a almacenar tiene que ser
 * una instancia real del tipo del árbol.</p>
 *
 * @param <T> el tipo del dato almacenado en el subárbol
 * @see TDAArbolBinario
 */
public interface TDAElemento<T> {

    /**
     * @return el hijo izquierdo de este nodo, o {@code null} si no tiene
     */
    TDAElemento<T> getHijoIzquierdo();

    /**
     * @return el hijo derecho de este nodo, o {@code null} si no tiene
     */
    TDAElemento<T> getHijoDerecho();

    /**
     * @return el dato almacenado en este nodo
     */
    T getDato();

    /**
     * Busca el primer nodo del subárbol cuya clave coincide con el criterio.
     *
     * @param criterioBusqueda criterio comparable contra T
     * @return el nodo encontrado, o {@code null} si no existe
     */
    TDAElemento<T> buscar(Comparable<T> criterioBusqueda);

    /**
     * Inserta {@code nuevoDato} en el subárbol manteniendo el invariante BST y devuelve la
     * <strong>nueva raíz del subárbol</strong> tras la operación.
     *
     * <p>En árboles sin rebalanceo el retorno es siempre {@code this}; en árboles
     * auto-balanceados (AVL) una rotación puede promover otro nodo a raíz, en cuyo caso
     * el padre debe reasignar su puntero al valor retornado.</p>
     *
     * <p>{@code insertado} actúa como bandera de salida: queda en {@code true} si se agregó
     * el dato, {@code false} si la clave ya existía (no se admiten duplicados).</p>
     *
     * @param nuevoDato dato a insertar (debe ser instancia de T comparable contra T)
     * @param insertado bandera de salida: {@code true} si se insertó, {@code false} si ya existía
     * @return la nueva raíz del subárbol resultante
     */
    TDAElemento<T> insertar(T nuevoDato, boolean[] insertado);

    /**
     * Elimina del subárbol el nodo cuya clave coincide con el criterio y devuelve la
     * <strong>nueva raíz del subárbol</strong> tras la eliminación.
     *
     * <p>El retorno puede ser:
     * <ul>
     *   <li>{@code null}, si el subárbol queda vacío;</li>
     *   <li>{@code this}, si la eliminación ocurrió en un descendiente y la raíz no cambió;</li>
     *   <li>otro nodo, si se eliminó esta misma raíz y otro hijo o sucesor toma su lugar.</li>
     * </ul>
     * El padre debe reasignar su puntero al valor retornado.</p>
     *
     * <p>{@code removido} actúa como bandera de salida: queda en {@code true} si se eliminó
     * efectivamente un nodo, {@code false} si la clave no estaba presente.</p>
     *
     * @param criterioBusqueda criterio que define qué nodo eliminar
     * @param removido bandera de salida: {@code true} si se eliminó algo
     * @return la nueva raíz del subárbol resultante
     */
    TDAElemento<T> eliminar(Comparable<T> criterioBusqueda, boolean[] removido);

    /**
     * Recorrido in-order (izq, raíz, der). En un BST genera las claves en orden ascendente.
     */
    void inOrder(Consumer<TDAElemento<T>> consumidor);

    /**
     * Recorrido pre-order (raíz, izq, der).
     */
    void preOrder(Consumer<TDAElemento<T>> consumidor);

    /**
     * Recorrido post-order (izq, der, raíz).
     */
    void postOrder(Consumer<TDAElemento<T>> consumidor);

    /**
     * @return {@code true} si el nodo no tiene hijos
     */
    boolean esHoja();

    /**
     * @return cantidad de hojas en el subárbol enraizado en este nodo
     */
    int cantidadHojas();

    /**
     * @return cantidad de nodos internos (no hojas) del subárbol
     */
    int cantidadNodosInternos();

    /**
     * @return cantidad total de nodos del subárbol
     */
    int cantidadNodos();

    /**
     * @return altura del subárbol enraizado en este nodo (1 si es hoja)
     */
    int altura();

    /**
     * @param criterioBusqueda criterio que define la clave a localizar
     * @return nivel relativo (aristas desde este nodo) del primer nodo coincidente,
     *         o {@code -1} si no se encuentra
     */
    int obtenerNivel(Comparable<T> criterioBusqueda);
}
