package ucu.edu.labflow.tda.implementaciones.lineales.pila;

import ucu.edu.labflow.tda.implementaciones.lineales.lista.ListaArreglo;
import ucu.edu.labflow.tda.lineales.TDAPila;

import java.util.NoSuchElementException;

/**
 * Implementación de {@link TDAPila} <em>por herencia</em> de
 * {@link ListaArreglo}.
 *
 * <h2>Decisión de diseño: tope al final del arreglo</h2>
 * <p>Las operaciones de pila son LIFO: empujar y sacar siempre del mismo
 * extremo. En un arreglo redimensionable, ese extremo debe ser el final
 * para evitar el corrimiento de elementos. Por eso:</p>
 * <ul>
 *   <li>{@link #mete(Object) mete}: delega en {@link ListaArreglo#agregar(Object)} → O(1) amortizado.</li>
 *   <li>{@link #saca()}: delega en {@code remover(tamaño - 1)} → O(1).</li>
 *   <li>{@link #tope()}: lee la última posición → O(1).</li>
 * </ul>
 *
 * <h2>Por qué heredar y no componer</h2>
 * <p>{@code TDAPila} extiende {@code TDALista}, así que esta clase debe
 * proveer todas las operaciones de lista. Heredando de {@code ListaArreglo}
 * se reutilizan automáticamente. La alternativa (componer una
 * {@code ListaArreglo} interna y delegar) duplica código sin beneficio
 * para este TDA. La consecuencia es que las operaciones de lista quedan
 * accesibles también desde una pila — el contrato de {@code TDAPila} ya
 * lo permite explícitamente.</p>
 *
 * @param <T> tipo de los elementos almacenados
 * @see TDAPila
 * @see PilaSobreEnlazada
 */
public class PilaSobreArreglo<T> extends ListaArreglo<T> implements TDAPila<T> {

    /**
     * Devuelve el elemento del tope sin removerlo.
     *
     * @throws NoSuchElementException si la pila está vacía
     */
    @Override
    public T tope() {
        if (esVacio()) throw new NoSuchElementException();
        return obtener(tamaño() - 1);
    }

    /**
     * Remueve y devuelve el tope.
     *
     * @throws NoSuchElementException si la pila está vacía
     */
    @Override
    public T saca() {
        if (esVacio()) throw new NoSuchElementException();
        return remover(tamaño() - 1);
    }

    /** Apila un elemento en el tope (final del arreglo). */
    @Override
    public void mete(T dato) {
        agregar(dato);
    }
}
