package ucu.edu.labflow.tda.jerarquicos;


import java.util.function.Consumer;

/**
 * Define un Tipo de Dato Abstracto (TDA) Árbol Binario genérico.
 *
 * <p>Un árbol binario es una estructura jerárquica donde cada nodo tiene a lo sumo dos hijos:
 * un hijo izquierdo y un hijo derecho.</p>
 *
 * <p>Esta interfaz expone solamente operaciones que <strong>no requieren orden total</strong>
 * sobre las claves: acceso a la raíz, recorridos y métricas estructurales (cantidades, altura).
 * Para árboles binarios <em>de búsqueda</em> — donde sí hay orden y se admite búsqueda,
 * inserción y eliminación por criterio comparable — ver {@link TDAArbolBusqueda}. Para árboles
 * con garantía de altura logarítmica, ver {@link TDAArbolBalanceado}.</p>
 *
 * @param <T> el tipo de los elementos almacenados en el árbol
 * @see TDAArbolBusqueda
 * @see TDAArbolBalanceado
 */
public interface TDAArbolBinario<T> {

    /**
     * @return el elemento raíz del árbol, o {@code null} si el árbol está vacío
     */
    TDAElemento<T> obtenerRaiz();

    /**
     * Recorre el árbol en in-order (izq, raíz, der). En un árbol de búsqueda los datos se
     * entregan en orden ascendente de clave.
     */
    void inOrder(Consumer<T> consumidor);

    /**
     * Recorre el árbol en pre-order (raíz, izq, der). Útil para serializar o duplicar la
     * estructura.
     */
    void preOrder(Consumer<T> consumidor);

    /**
     * Recorre el árbol en post-order (izq, der, raíz). Útil para liberar recursos hijos antes
     * que el padre.
     */
    void postOrder(Consumer<T> consumidor);

    /**
     * @return {@code true} si el árbol no contiene nodos
     */
    boolean esVacio();

    /**
     * @return cantidad total de nodos del árbol
     */
    int cantidadNodos();

    /**
     * @return cantidad de nodos hoja del árbol
     */
    int cantidadHojas();

    /**
     * @return cantidad de nodos internos (no hoja) del árbol
     */
    int cantidadNodosInternos();

    /**
     * @return altura del árbol (cantidad de niveles), o {@code 0} si el árbol está vacío
     */
    int altura();
}
