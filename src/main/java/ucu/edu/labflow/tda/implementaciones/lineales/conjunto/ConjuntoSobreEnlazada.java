package ucu.edu.labflow.tda.implementaciones.lineales.conjunto;

import ucu.edu.labflow.tda.implementaciones.lineales.lista.ListaEnlazada;
import ucu.edu.labflow.tda.lineales.TDAConjunto;

import java.util.function.Consumer;

/**
 * Implementación de {@link TDAConjunto} <em>por herencia</em> de
 * {@link ListaEnlazada}.
 *
 * <h2>Idea</h2>
 * <p>Equivalente a {@link ConjuntoSobreArreglo}, pero con una lista
 * enlazada como almacenamiento. Hereda toda la maquinaria de la lista y
 * sólo sobrescribe las inserciones para vetar duplicados.</p>
 *
 * <h2>Por qué redefinir las operaciones de teoría de conjuntos</h2>
 * <p>En una lista enlazada {@code obtener(i)} es O(i), por lo que un bucle
 * {@code for (i...) obtener(i)} cuesta O(n²) — un orden de magnitud peor
 * que lo documentado. Por eso aquí las cuatro operaciones binarias
 * iteran con {@link ListaEnlazada#paraCada}, que recorre la cadena una
 * sola vez. Para iterar el otro conjunto {@link #iterarConjunto}
 * dispara un dispatch sobre el tipo: si {@code otro} también es
 * {@code ListaEnlazada} se aprovecha {@code paraCada}; en cualquier
 * otro caso se cae al recorrido por índice.</p>
 *
 * <h2>Costos</h2>
 * <p>{@code agregar}: O(n) por la verificación de unicidad. Operaciones
 * binarias: O(n·m).</p>
 *
 * @param <T> tipo de los elementos almacenados
 * @see TDAConjunto
 * @see ConjuntoSobreArreglo
 */
public class ConjuntoSobreEnlazada<T> extends ListaEnlazada<T> implements TDAConjunto<T> {

    /**
     * Inserta sólo si {@code elem} no estaba ya presente (semántica de
     * conjunto). Es un no-op silencioso si ya existía.
     */
    @Override
    public void agregar(T elem) {
        if (!contiene(elem)) super.agregar(elem);
    }

    /**
     * Variante posicional con semántica de unicidad. Valida el índice
     * antes de chequear duplicados, para que un índice fuera de rango
     * lance {@link IndexOutOfBoundsException} aunque {@code elem} ya
     * estuviera presente.
     */
    @Override
    public void agregar(int index, T elem) {
        if (index < 0 || index > tamaño()) throw new IndexOutOfBoundsException(index);
        if (!contiene(elem)) super.agregar(index, elem);
    }

    /**
     * Unión: el nuevo conjunto recibe primero los elementos de {@code this}
     * y después los de {@code otro}; el chequeo de unicidad evita
     * duplicados. O(n·m).
     */
    @Override
    public TDAConjunto<T> union(TDAConjunto<T> otro) {
        ConjuntoSobreEnlazada<T> resultado = new ConjuntoSobreEnlazada<>();
        paraCada(resultado::agregar);
        iterarConjunto(otro, resultado::agregar);
        return resultado;
    }

    /** Intersección: elementos de {@code this} que también están en {@code otro}. O(n·m). */
    @Override
    public TDAConjunto<T> interseccion(TDAConjunto<T> otro) {
        ConjuntoSobreEnlazada<T> resultado = new ConjuntoSobreEnlazada<>();
        paraCada(elem -> {
            if (otro.contiene(elem)) resultado.agregar(elem);
        });
        return resultado;
    }

    /** Diferencia: elementos de {@code this} que no están en {@code otro}. O(n·m). */
    @Override
    public TDAConjunto<T> diferencia(TDAConjunto<T> otro) {
        ConjuntoSobreEnlazada<T> resultado = new ConjuntoSobreEnlazada<>();
        paraCada(elem -> {
            if (!otro.contiene(elem)) resultado.agregar(elem);
        });
        return resultado;
    }

    /**
     * @return {@code true} si todos los elementos de {@code this} están
     *         también en {@code otro}. El conjunto vacío cumple la condición
     *         vacuamente.
     *
     * <p>Implementación: itera {@code this} con {@code paraCada} (O(n))
     * y va llevando una bandera mutable. No corta el recorrido al
     * encontrar un contraejemplo — el resto del bucle se vuelve un no-op
     * gracias al chequeo de la bandera —; la complejidad asintótica
     * (O(n·m)) no cambia.</p>
     */
    @Override
    public boolean esSubconjuntoDe(TDAConjunto<T> otro) {
        boolean[] hayContraejemplo = {false};
        paraCada(elem -> {
            if (!hayContraejemplo[0] && !otro.contiene(elem)) {
                hayContraejemplo[0] = true;
            }
        });
        return !hayContraejemplo[0];
    }

    /**
     * Itera {@code conjunto} aplicándole {@code accion} en O(tamaño).
     *
     * <p>Si la implementación concreta es una {@link ListaEnlazada} se
     * usa su recorrido por nodos; en cualquier otro caso se cae al acceso
     * por índice — que es O(1) en una {@link ListaArreglo} y por lo tanto
     * sigue dando O(tamaño) total. El dispatch evita que la iteración
     * degenere a O(m²) cuando {@code otro} también está respaldado por
     * una lista enlazada.</p>
     */
    private static <T> void iterarConjunto(TDAConjunto<T> conjunto, Consumer<? super T> accion) {
        if (conjunto instanceof ListaEnlazada<?>) {
            @SuppressWarnings("unchecked")
            ListaEnlazada<T> enlazada = (ListaEnlazada<T>) conjunto;
            enlazada.paraCada(accion);
        } else {
            for (int i = 0; i < conjunto.tamaño(); i++) accion.accept(conjunto.obtener(i));
        }
    }
}
