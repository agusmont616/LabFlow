package ucu.edu.labflow.tda.implementaciones.jerarquicos;

import ucu.edu.labflow.tda.jerarquicos.TDAElemento;

/**
 * Implementación recursiva de un nodo de árbol AVL (auto-balanceado).
 *
 * <p>Un AVL es un BST que mantiene la diferencia de alturas entre los subárboles izquierdo
 * y derecho de cada nodo (factor de balance) acotada en {-1, 0, 1}. Esto garantiza altura
 * O(log n) y por lo tanto búsquedas, inserciones y eliminaciones en O(log n)
 * <strong>independientemente del orden de inserción</strong>.</p>
 *
 * <h2>Relación con {@link BSTElemento}</h2>
 * <p>{@code AVLElemento} extiende {@link BSTElemento} reaprovechando todas las operaciones
 * que no cambian con el balanceo (búsqueda, recorridos, conteos, accesores, {@code obtenerNivel}).
 * Solo se redefinen los tres puntos donde la semántica AVL difiere:</p>
 * <ul>
 *   <li>{@link #insertar(Object, boolean[])}: crea hijos {@link AVLElemento}, actualiza la
 *       altura cacheada y aplica rotaciones según el factor de balance.</li>
 *   <li>{@link #altura()}: devuelve la altura cacheada en O(1) en lugar de la versión O(n)
 *       recursiva del BST. Es crítico para {@link #factorBalance}.</li>
 *   <li>{@link #eliminar(java.lang.Comparable, boolean[])}: lanza {@link UnsupportedOperationException}
 *       — la eliminación con rebalanceo AVL no está implementada en esta clase.</li>
 * </ul>
 *
 * @param <T> tipo de dato almacenado en el nodo
 * @see BSTElemento
 */
public class AVLElemento<T> extends BSTElemento<T> {

    private int altura;

    /**
     * Construye un nodo hoja AVL con el dato indicado y altura inicial 1.
     */
    public AVLElemento(T dato) {
        super(dato);
        this.altura = 1;
    }

    /**
     * Inserción AVL: respeta el invariante BST y rebalancea con rotaciones (simples
     * izquierda/derecha, dobles izquierda-derecha/derecha-izquierda).
     *
     * <p>Devuelve la nueva raíz del subárbol — necesariamente, porque las rotaciones
     * pueden cambiar el nodo raíz. El padre o el árbol contenedor debe reasignar su
     * puntero al valor retornado.</p>
     *
     * <p>Costo: O(log n).</p>
     */
    @Override
    public TDAElemento<T> insertar(T nuevoDato, boolean[] insertado) {
        @SuppressWarnings("unchecked")
        Comparable<T> clave = (Comparable<T>) nuevoDato;
        int cmp = clave.compareTo(this.dato);
        if (cmp == 0) return this;
        if (cmp < 0) {
            if (hijoIzquierdo == null) {
                hijoIzquierdo = new AVLElemento<>(nuevoDato);
                insertado[0] = true;
            } else {
                hijoIzquierdo = hijoIzquierdo.insertar(nuevoDato, insertado);
            }
        } else {
            if (hijoDerecho == null) {
                hijoDerecho = new AVLElemento<>(nuevoDato);
                insertado[0] = true;
            } else {
                hijoDerecho = hijoDerecho.insertar(nuevoDato, insertado);
            }
        }
        actualizarAltura();
        return balancear();
    }

    /**
     * Eliminación AVL: <strong>no implementada</strong>.
     *
     * <p>Una eliminación correcta exige rotaciones post-borrado que esta clase no provee.
     * Lanza {@link UnsupportedOperationException} en vez de heredar la versión BST sin
     * rebalanceo, que dejaría el árbol potencialmente desbalanceado y rompería la
     * garantía AVL.</p>
     */
    @Override
    public TDAElemento<T> eliminar(Comparable<T> criterioBusqueda, boolean[] removido) {
        throw new UnsupportedOperationException(
                "AVLElemento.eliminar no está implementado: requiere rotaciones post-borrado."
        );
    }

    /**
     * @return altura cacheada del subárbol enraizado en este nodo. O(1).
     */
    @Override
    public int altura() {
        return altura;
    }

    /**
     * Recalcula la altura del nodo a partir de la altura de sus hijos. Debe invocarse
     * tras cada modificación estructural (inserción o rotación).
     */
    private void actualizarAltura() {
        this.altura = 1 + Math.max(alturaDe(hijoIzquierdo), alturaDe(hijoDerecho));
    }

    /**
     * Devuelve la altura de un nodo, tratando el caso {@code null} como altura 0.
     *
     * <p>Despacha polimórficamente a {@link #altura()}: si {@code n} es un AVL devuelve
     * la altura cacheada en O(1); el invariante de la clase asegura que todos los hijos
     * descendientes son AVL.</p>
     */
    private static <T> int alturaDe(TDAElemento<T> n) {
        return n == null ? 0 : n.altura();
    }

    /**
     * Calcula el factor de balance: altura(izq) - altura(der). Un AVL mantiene este valor
     * en {-1, 0, 1}.
     */
    private int factorBalance() {
        return alturaDe(hijoIzquierdo) - alturaDe(hijoDerecho);
    }

    /**
     * Aplica las rotaciones necesarias para restaurar la propiedad AVL en este nodo.
     *
     * <ul>
     *   <li><b>Izquierda-Izquierda</b> (fb&gt;1, hijo izq con fb&gt;=0): rotación simple a la derecha.</li>
     *   <li><b>Izquierda-Derecha</b> (fb&gt;1, hijo izq con fb&lt;0): doble — primero izq sobre el hijo, luego der.</li>
     *   <li><b>Derecha-Derecha</b> (fb&lt;-1, hijo der con fb&lt;=0): rotación simple a la izquierda.</li>
     *   <li><b>Derecha-Izquierda</b> (fb&lt;-1, hijo der con fb&gt;0): doble — primero der sobre el hijo, luego izq.</li>
     * </ul>
     */
    private AVLElemento<T> balancear() {
        int fb = factorBalance();
        if (fb > 1) {
            AVLElemento<T> izq = (AVLElemento<T>) hijoIzquierdo;
            if (izq.factorBalance() < 0) {
                hijoIzquierdo = izq.rotarIzquierda();
            }
            return rotarDerecha();
        }
        if (fb < -1) {
            AVLElemento<T> der = (AVLElemento<T>) hijoDerecho;
            if (der.factorBalance() > 0) {
                hijoDerecho = der.rotarDerecha();
            }
            return rotarIzquierda();
        }
        return this;
    }

    /**
     * Rotación simple a la derecha. Promueve al hijo izquierdo a raíz del subárbol.
     */
    private AVLElemento<T> rotarDerecha() {
        AVLElemento<T> nuevaRaiz = (AVLElemento<T>) this.hijoIzquierdo;
        this.hijoIzquierdo = nuevaRaiz.hijoDerecho;
        nuevaRaiz.hijoDerecho = this;
        this.actualizarAltura();
        nuevaRaiz.actualizarAltura();
        return nuevaRaiz;
    }

    /**
     * Rotación simple a la izquierda. Promueve al hijo derecho a raíz del subárbol.
     */
    private AVLElemento<T> rotarIzquierda() {
        AVLElemento<T> nuevaRaiz = (AVLElemento<T>) this.hijoDerecho;
        this.hijoDerecho = nuevaRaiz.hijoIzquierdo;
        nuevaRaiz.hijoIzquierdo = this;
        this.actualizarAltura();
        nuevaRaiz.actualizarAltura();
        return nuevaRaiz;
    }
}
