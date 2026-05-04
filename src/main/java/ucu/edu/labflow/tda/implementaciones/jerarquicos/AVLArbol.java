package ucu.edu.labflow.tda.implementaciones.jerarquicos;

import ucu.edu.labflow.tda.jerarquicos.TDAArbolBalanceado;

/**
 * Implementación de {@link TDAArbolBalanceado} (AVL) sobre {@link AVLElemento}.
 *
 * <h2>Relación con {@link BSTArbol}</h2>
 * <p>{@code AVLArbol} extiende {@link BSTArbol} reaprovechando la lógica del contenedor:
 * acceso a la raíz, recorridos, conteos, búsqueda, altura y nivel se heredan tal cual.
 * Solo se redefinen las dos operaciones cuya semántica difiere:</p>
 * <ul>
 *   <li>{@link #insertar(Object)}: crea raíz como {@link AVLElemento} (no BST) — lo que
 *       hace que la inserción recursiva en los nodos sea AVL y mantenga el balance.</li>
 *   <li>{@link #eliminar(java.lang.Comparable)}: stub que devuelve {@code false} para
 *       no invocar el {@link AVLElemento#eliminar} heredado, que está marcado como
 *       no soportado.</li>
 * </ul>
 *
 * <p>Garantiza tiempo de búsqueda <strong>O(log n) en peor caso</strong> sin importar el
 * orden de inserción.</p>
 *
 * @param <T> tipo de dato almacenado
 * @see BSTArbol
 * @see AVLElemento
 */
public class AVLArbol<T> extends BSTArbol<T> implements TDAArbolBalanceado<T> {

    /**
     * Inserción auto-balanceada.
     *
     * <p>Override del método de {@link BSTArbol} para que la raíz sea un
     * {@link AVLElemento} (no un {@link BSTElemento}). Una vez creada como AVL, la
     * recursión polimórfica en {@link AVLElemento#insertar} se encarga de las rotaciones.</p>
     */
    @Override
    public boolean insertar(T dato) {
        if (raiz == null) {
            raiz = new AVLElemento<>(dato);
            return true;
        }
        boolean[] insertado = {false};
        raiz = (BSTElemento<T>) raiz.insertar(dato, insertado);
        return insertado[0];
    }

    /**
     * Eliminación: <strong>no implementada</strong>.
     *
     * <p>La eliminación AVL completa requiere rotaciones post-borrado que esta clase no
     * implementa, y la versión heredada de BST no rebalancearía y dejaría el árbol fuera
     * del invariante AVL. Se devuelve {@code false} sin tocar la estructura.</p>
     */
    @Override
    public boolean eliminar(Comparable<T> criterioBusqueda) {
        return false;
    }

    
}
