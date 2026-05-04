package ucu.edu.labflow.tda.implementaciones.jerarquicos;

import ucu.edu.labflow.tda.jerarquicos.TDAElemento;

import java.util.function.Consumer;

/**
 * Implementación recursiva de un nodo de árbol binario de búsqueda (BST)
 * sin balanceo automático.
 *
 * <h2>Rol</h2>
 * <p>Cada nodo guarda un dato y dos enlaces (hijo izquierdo y derecho), y sabe cómo aplicar
 * las operaciones del árbol sobre el subárbol enraizado en él. La clase contenedora
 * ({@link BSTArbol}) delega casi toda la lógica a esta clase y se limita a manejar la
 * raíz absoluta.</p>
 *
 * <h2>Invariante BST</h2>
 * <ul>
 *   <li>Todo dato en el subárbol izquierdo es menor que {@code dato}.</li>
 *   <li>Todo dato en el subárbol derecho es mayor que {@code dato}.</li>
 *   <li>No hay claves repetidas (las inserciones de duplicados se rechazan).</li>
 * </ul>
 *
 * <h2>Mutación estructural por retorno</h2>
 * <p>{@link #insertar} y {@link #eliminar} devuelven la <strong>nueva raíz del subárbol</strong>
 * tras la operación. En BST sin balanceo el retorno es siempre {@code this} salvo cuando se
 * elimina el propio nodo (donde un hijo o el sucesor toma su lugar). El padre debe reasignar
 * su puntero al valor retornado; este patrón habilita que subclases auto-balanceadas
 * (p.ej. AVL) cambien la raíz del subárbol sin necesidad de exponer setters peligrosos.</p>
 *
 * @param <T> tipo del dato almacenado en el nodo
 * @see BSTArbol
 * @see AVLElemento
 */
public class BSTElemento<T> implements TDAElemento<T> {

    protected T dato;
    protected TDAElemento<T> hijoIzquierdo;
    protected TDAElemento<T> hijoDerecho;

    /**
     * Construye un nodo hoja con el dato indicado. El árbol contenedor decide cuándo y
     * cómo enlazarlo.
     */
    public BSTElemento(T dato) {
        this.dato = dato;
    }

    @Override
    public TDAElemento<T> getHijoIzquierdo() {
        return hijoIzquierdo;
    }

    @Override
    public TDAElemento<T> getHijoDerecho() {
        return hijoDerecho;
    }

    @Override
    public T getDato() {
        return dato;
    }

    /**
     * Búsqueda binaria recursiva por clave. Costo: O(h).
     */
    @Override
    public TDAElemento<T> buscar(Comparable<T> criterioBusqueda) {
        int cmp = criterioBusqueda.compareTo(this.dato);
        if (cmp == 0) return this;
        if (cmp < 0) {
            return hijoIzquierdo == null ? null : hijoIzquierdo.buscar(criterioBusqueda);
        }
        return hijoDerecho == null ? null : hijoDerecho.buscar(criterioBusqueda);
    }

    /**
     * Inserción BST recursiva. Si el dato es igual al actual no se inserta (sin duplicados);
     * si no, baja por el hijo correspondiente. Cuando llega a un enlace {@code null}, ahí
     * mismo cuelga el nuevo nodo. Costo: O(h).
     *
     * <p>El retorno es siempre {@code this} en BST sin balanceo: la raíz del subárbol no
     * cambia. La firma respeta el contrato general de {@link TDAElemento#insertar} para
     * permitir que implementaciones balanceadas devuelvan otra raíz.</p>
     */
    @Override
    public TDAElemento<T> insertar(T nuevoDato, boolean[] insertado) {
        @SuppressWarnings("unchecked")
        Comparable<T> clave = (Comparable<T>) nuevoDato;
        int cmp = clave.compareTo(this.dato);
        if (cmp == 0) return this;
        if (cmp < 0) {
            if (hijoIzquierdo == null) {
                hijoIzquierdo = new BSTElemento<>(nuevoDato);
                insertado[0] = true;
            } else {
                hijoIzquierdo = hijoIzquierdo.insertar(nuevoDato, insertado);
            }
        } else {
            if (hijoDerecho == null) {
                hijoDerecho = new BSTElemento<>(nuevoDato);
                insertado[0] = true;
            } else {
                hijoDerecho = hijoDerecho.insertar(nuevoDato, insertado);
            }
        }
        return this;
    }

    /**
     * Eliminación BST estándar en una sola travesía. Devuelve la nueva raíz del subárbol
     * tras remover el primer nodo cuya clave coincide con el criterio, e indica en
     * {@code removido[0]} si efectivamente se eliminó algo.
     *
     * <h3>Casos del nodo a eliminar</h3>
     * <ol>
     *   <li><b>Sin hijos</b>: el subárbol queda vacío; se devuelve {@code null}.</li>
     *   <li><b>Con un hijo</b>: ese hijo reemplaza al nodo eliminado.</li>
     *   <li><b>Con dos hijos</b>: se busca el sucesor in-order (mínimo del subárbol
     *       derecho), se copia su dato sobre {@code this} (este nodo sobrevive con dato
     *       nuevo) y se elimina el sucesor del subárbol derecho con una llamada
     *       recursiva.</li>
     * </ol>
     */
    @Override
    public TDAElemento<T> eliminar(Comparable<T> criterioBusqueda, boolean[] removido) {
        int cmp = criterioBusqueda.compareTo(this.dato);
        if (cmp < 0) {
            if (hijoIzquierdo != null) {
                hijoIzquierdo = hijoIzquierdo.eliminar(criterioBusqueda, removido);
            }
            return this;
        }
        if (cmp > 0) {
            if (hijoDerecho != null) {
                hijoDerecho = hijoDerecho.eliminar(criterioBusqueda, removido);
            }
            return this;
        }
        removido[0] = true;
        if (hijoIzquierdo == null) return hijoDerecho;
        if (hijoDerecho == null) return hijoIzquierdo;
        BSTElemento<T> sucesor = minimo((BSTElemento<T>) hijoDerecho);
        this.dato = sucesor.dato;
        @SuppressWarnings("unchecked")
        Comparable<T> claveSucesor = (Comparable<T>) sucesor.dato;
        hijoDerecho = hijoDerecho.eliminar(claveSucesor, removido);
        return this;
    }

    /**
     * Devuelve el nodo con la clave mínima del subárbol enraizado en {@code n}. Por el
     * invariante BST, basta bajar siempre por la izquierda hasta encontrar un nodo sin
     * hijo izquierdo.
     */
    private static <T> BSTElemento<T> minimo(BSTElemento<T> n) {
        while (n.hijoIzquierdo != null) n = (BSTElemento<T>) n.hijoIzquierdo;
        return n;
    }

    @Override
    public void inOrder(Consumer<TDAElemento<T>> consumidor) {
        if (hijoIzquierdo != null) hijoIzquierdo.inOrder(consumidor);
        consumidor.accept(this);
        if (hijoDerecho != null) hijoDerecho.inOrder(consumidor);
    }

    @Override
    public void preOrder(Consumer<TDAElemento<T>> consumidor) {
        consumidor.accept(this);
        if (hijoIzquierdo != null) hijoIzquierdo.preOrder(consumidor);
        if (hijoDerecho != null) hijoDerecho.preOrder(consumidor);
    }

    @Override
    public void postOrder(Consumer<TDAElemento<T>> consumidor) {
        if (hijoIzquierdo != null) hijoIzquierdo.postOrder(consumidor);
        if (hijoDerecho != null) hijoDerecho.postOrder(consumidor);
        consumidor.accept(this);
    }

    @Override
    public boolean esHoja() {
        return hijoIzquierdo == null && hijoDerecho == null;
    }

    @Override
    public int cantidadHojas() {
        if (esHoja()) return 1;
        int total = 0;
        if (hijoIzquierdo != null) total += hijoIzquierdo.cantidadHojas();
        if (hijoDerecho != null) total += hijoDerecho.cantidadHojas();
        return total;
    }

    @Override
    public int cantidadNodosInternos() {
        if (esHoja()) return 0;
        int total = 1;
        if (hijoIzquierdo != null) total += hijoIzquierdo.cantidadNodosInternos();
        if (hijoDerecho != null) total += hijoDerecho.cantidadNodosInternos();
        return total;
    }

    @Override
    public int cantidadNodos() {
        int total = 1;
        if (hijoIzquierdo != null) total += hijoIzquierdo.cantidadNodos();
        if (hijoDerecho != null) total += hijoDerecho.cantidadNodos();
        return total;
    }

    @Override
    public int altura() {
        int izq = hijoIzquierdo == null ? 0 : hijoIzquierdo.altura();
        int der = hijoDerecho == null ? 0 : hijoDerecho.altura();
        return 1 + Math.max(izq, der);
    }

    @Override
    public int obtenerNivel(Comparable<T> criterioBusqueda) {
        int cmp = criterioBusqueda.compareTo(this.dato);
        if (cmp == 0) return 0;
        TDAElemento<T> sig = cmp < 0 ? hijoIzquierdo : hijoDerecho;
        if (sig == null) return -1;
        int n = sig.obtenerNivel(criterioBusqueda);
        return n == -1 ? -1 : n + 1;
    }
}
