package ucu.edu.labflow.tda.jerarquicos;

/**
 * Marker que distingue a un {@link TDAArbolBusqueda} con <strong>garantía de altura
 * O(log n) en peor caso</strong>, y por lo tanto búsqueda, inserción y eliminación
 * O(log n) <em>independientemente del orden de inserción</em>.
 *
 * <p>Esta interfaz no agrega métodos: actúa como tipo marca. Permite a un consumidor
 * declarar la dependencia sobre la garantía de balance directamente en la firma
 * (p.ej. {@code TDAArbolBalanceado<Clave>}), en lugar de confiarla a la documentación.</p>
 *
 * @param <T> el tipo de los elementos almacenados
 */
public interface TDAArbolBalanceado<T> extends TDAArbolBusqueda<T> {
}
