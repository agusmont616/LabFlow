package ucu.edu.labflow.tda.implementaciones.jerarquicos;

import ucu.edu.labflow.tda.jerarquicos.TDAArbolBusqueda;
import ucu.edu.labflow.tda.jerarquicos.TDAElemento;

import java.util.function.Consumer;

/**
 * Implementación de {@link TDAArbolBusqueda} como árbol binario de búsqueda (BST)
 * <strong>sin balanceo</strong>.
 *
 * <h2>Invariante BST</h2>
 * <p>Para cada nodo n, todos los descendientes a su izquierda tienen claves menores y todos
 * los de la derecha mayores. Esto permite búsqueda, inserción y eliminación en tiempo
 * proporcional a la altura del árbol.</p>
 *
 * <h2>Por qué existe esta clase si ya hay {@link AVLArbol}</h2>
 * <p>El BST sin rebalanceo es la versión "didáctica" del AVL: la lógica de inserción y borrado
 * se ve sin la complicación de las rotaciones. Es útil compararlas para entender qué problema
 * resuelve el AVL: con inserciones ya ordenadas (1, 2, 3, ...) este BST degenera en una lista
 * enlazada y todas sus operaciones se vuelven O(n).</p>
 *
 * <h2>Costos por operación</h2>
 * <table>
 *   <caption>Costos por operación (h = altura del árbol)</caption>
 *   <tr><th>Operación</th><th>Caso promedio</th><th>Peor caso</th></tr>
 *   <tr><td>{@code insertar}, {@code buscar}, {@code eliminar}</td><td>O(log n)</td><td>O(n)</td></tr>
 *   <tr><td>recorridos, {@code cantidad*}, {@code altura}</td><td>O(n)</td><td>O(n)</td></tr>
 * </table>
 *
 * @param <T> tipo del dato almacenado
 * @see AVLArbol
 * @see BSTElemento
 */
public class BSTArbol<T> implements TDAArbolBusqueda<T> {

    /** Nodo raíz, o {@code null} si el árbol está vacío. */
    protected BSTElemento<T> raiz;

    @Override
    public T buscar(Comparable<T> criterioBusqueda) {
        if (raiz == null) return null;
        TDAElemento<T> encontrado = raiz.buscar(criterioBusqueda);
        return encontrado == null ? null : encontrado.getDato();
    }

    @Override
    public TDAElemento<T> obtenerRaiz() {
        return raiz;
    }

    /**
     * Inserción BST. Si el árbol está vacío crea la raíz; si no, delega en
     * {@link BSTElemento#insertar(Object, boolean[])}, que devuelve la nueva raíz del
     * subárbol y deja en {@code insertado[0]} si efectivamente se agregó el dato.
     */
    @Override
    public boolean insertar(T dato) {
        if (raiz == null) {
            raiz = new BSTElemento<>(dato);
            return true;
        }
        boolean[] insertado = {false};
        raiz = (BSTElemento<T>) raiz.insertar(dato, insertado);
        return insertado[0];
    }

    /**
     * Eliminación BST en una sola travesía.
     *
     * <p>Si el árbol está vacío devuelve {@code false} sin tocar la estructura. Si no, delega
     * la eliminación recursiva en {@link BSTElemento#eliminar(Comparable, boolean[])}, que
     * devuelve la nueva raíz del subárbol y deja en {@code removido[0]} si efectivamente se
     * borró un nodo. La nueva raíz puede ser un hijo del original (cuando la raíz misma se
     * eliminaba) o {@code null} (si el árbol queda vacío).</p>
     */
    @Override
    public boolean eliminar(Comparable<T> criterioBusqueda) {
        if (raiz == null) return false;
        boolean[] removido = {false};
        raiz = (BSTElemento<T>) raiz.eliminar(criterioBusqueda, removido);
        return removido[0];
    }

    @Override
    public void inOrder(Consumer<T> consumidor) {
        if (raiz != null) raiz.inOrder(n -> consumidor.accept(n.getDato()));
    }

    @Override
    public void preOrder(Consumer<T> consumidor) {
        if (raiz != null) raiz.preOrder(n -> consumidor.accept(n.getDato()));
    }

    @Override
    public void postOrder(Consumer<T> consumidor) {
        if (raiz != null) raiz.postOrder(n -> consumidor.accept(n.getDato()));
    }

    @Override
    public boolean esVacio() {
        return raiz == null;
    }

    @Override
    public int cantidadNodos() {
        return raiz == null ? 0 : raiz.cantidadNodos();
    }

    @Override
    public int cantidadHojas() {
        return raiz == null ? 0 : raiz.cantidadHojas();
    }

    @Override
    public int cantidadNodosInternos() {
        return raiz == null ? 0 : raiz.cantidadNodosInternos();
    }

    @Override
    public int altura() {
        return raiz == null ? 0 : raiz.altura();
    }

    @Override
    public int obtenerNivel(Comparable<T> criterioBusqueda) {
        return raiz == null ? -1 : raiz.obtenerNivel(criterioBusqueda);
    }
}
