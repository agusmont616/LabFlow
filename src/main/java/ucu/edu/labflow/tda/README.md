# Paquete `ucu.edu.aed.tda`

Tipos de Dato Abstracto (TDA) usados en la materia, junto con sus implementaciones de referencia. Las **interfaces** definen el contrato que estudiantes y código cliente deben programar; las **implementaciones** son las clases concretas que cumplen esos contratos.

## Estructura del paquete

```
tda/
├── lineales/                        ← interfaces de estructuras lineales
│   ├── TDALista.java
│   ├── TDAPila.java                  (extends TDALista)
│   ├── TDACola.java                  (extends TDALista)
│   └── TDAConjunto.java              (extends TDALista)
├── jerarquicos/                     ← interfaces de estructuras jerárquicas
│   ├── TDAArbolBinario.java
│   └── TDAElemento.java
└── implementaciones/
    ├── lineales/                    ← implementaciones de los TDA lineales
    │   ├── ListaArreglo.java
    │   ├── ListaEnlazada.java
    │   ├── PilaSobreArreglo.java
    │   ├── PilaSobreEnlazada.java
    │   ├── ColaArregloCircular.java
    │   ├── ColaEnlazada.java
    │   ├── ConjuntoSobreArreglo.java
    │   └── ConjuntoSobreEnlazada.java
    └── jerarquicos/                 ← implementaciones de los TDA jerárquicos
        ├── AVLArbol.java
        ├── AVLElemento.java
        ├── BSTArbol.java
        └── BSTElemento.java
```

## Tabla de resumen

| TDA | Interfaz | Implementación | Almacenamiento | Costos clave |
|---|---|---|---|---|
| Lista | [`TDALista`](lineales/TDALista.java) | [`ListaArreglo`](implementaciones/lineales/ListaArreglo.java) | Arreglo redimensionable | `agregar` O(1) am. · `obtener` O(1) · `remover(i)` O(n) |
| Lista | [`TDALista`](lineales/TDALista.java) | [`ListaEnlazada`](implementaciones/lineales/ListaEnlazada.java) | Lista enlazada simple | `agregar(0,·)` O(1) · `obtener` O(n) |
| Pila | [`TDAPila`](lineales/TDAPila.java) | [`PilaSobreArreglo`](implementaciones/lineales/PilaSobreArreglo.java) | Hereda `ListaArreglo` (tope = final) | `mete`/`saca`/`tope` O(1) am. |
| Pila | [`TDAPila`](lineales/TDAPila.java) | [`PilaSobreEnlazada`](implementaciones/lineales/PilaSobreEnlazada.java) | Hereda `ListaEnlazada` (tope = cabeza) | `mete`/`saca`/`tope` O(1) |
| Cola | [`TDACola`](lineales/TDACola.java) | [`ColaArregloCircular`](implementaciones/lineales/ColaArregloCircular.java) | Buffer circular en arreglo | `poneEnCola`/`quitaDeCola`/`frente` O(1) am. |
| Cola | [`TDACola`](lineales/TDACola.java) | [`ColaEnlazada`](implementaciones/lineales/ColaEnlazada.java) | Enlazada con puntero al último | `poneEnCola`/`quitaDeCola`/`frente` O(1) |
| Conjunto | [`TDAConjunto`](lineales/TDAConjunto.java) | [`ConjuntoSobreArreglo`](implementaciones/lineales/ConjuntoSobreArreglo.java) | Hereda `ListaArreglo` + chequeo de unicidad | `agregar` O(n) · operaciones binarias O(n·m) |
| Conjunto | [`TDAConjunto`](lineales/TDAConjunto.java) | [`ConjuntoSobreEnlazada`](implementaciones/lineales/ConjuntoSobreEnlazada.java) | Hereda `ListaEnlazada` + unicidad | `agregar` O(n) · operaciones binarias O(n·m) |
| Árbol binario | [`TDAArbolBinario`](jerarquicos/TDAArbolBinario.java) | [`BSTArbol`](implementaciones/jerarquicos/BSTArbol.java) + [`BSTElemento`](implementaciones/jerarquicos/BSTElemento.java) | BST clásico, sin balanceo | `insertar`/`buscar`/`eliminar` O(h): O(log n) prom., O(n) peor |
| Árbol binario | [`TDAArbolBinario`](jerarquicos/TDAArbolBinario.java) | [`AVLArbol`](implementaciones/jerarquicos/AVLArbol.java) + [`AVLElemento`](implementaciones/jerarquicos/AVLElemento.java) | BST autobalanceado por rotaciones | `insertar`/`buscar` O(log n) garantizado |

> *am.* = costo amortizado · *h* = altura del árbol · *n*, *m* = tamaños de los conjuntos involucrados.

---

# Interfaces

## `TDALista<T>`

Secuencia ordenada de elementos accesibles por índice. Permite duplicados y posiciones basadas en 0 (salvo que la implementación indique otra cosa).

Operaciones: `agregar(T)`, `agregar(int, T)`, `obtener(int)`, `remover(int)`, `remover(T)`, `contiene(T)`, `indiceDe(T)`, `buscar(Predicate<T>)`, `ordenar(Comparator<T>)`, `tamaño()`, `esVacio()`, `vaciar()`.

`ordenar` devuelve una **nueva** lista; no muta la original.

## `TDAPila<T> extends TDALista<T>`

Pila LIFO. Suma `tope()`, `saca()` y `mete(T)`. Hereda toda la interfaz de lista.

`tope()` y `saca()` lanzan `NoSuchElementException` si la pila está vacía.

## `TDACola<T> extends TDALista<T>`

Cola FIFO. Suma `frente()`, `poneEnCola(T)` y `quitaDeCola()`. Hereda toda la interfaz de lista.

`frente()` y `quitaDeCola()` lanzan `NoSuchElementException` si la cola está vacía.

## `TDAConjunto<T> extends TDALista<T>`

Colección sin duplicados. Suma las operaciones de teoría de conjuntos: `union`, `interseccion`, `diferencia` y `esSubconjuntoDe`. Cada una de las tres primeras devuelve un **nuevo** conjunto sin modificar los operandos.

La inserción heredada de lista se redefine para rechazar duplicados silenciosamente.

## `TDAArbolBinario<T>`

Árbol binario con orden BST sobre las claves. Operaciones: `insertar`, `buscar(Comparable<T>)`, `eliminar`, los tres recorridos (`inOrder`, `preOrder`, `postOrder`), métricas (`cantidadNodos`, `cantidadHojas`, `cantidadNodosInternos`), y acceso a la raíz como `TDAElemento<T>`.

`insertar` y `buscar` reciben un `Comparable<T>` para permitir criterios de búsqueda sintéticos (no hace falta un T real para localizar un nodo).

## `TDAElemento<T>`

Nodo de un `TDAArbolBinario`. Cada nodo guarda un dato y referencias a sus hijos izquierdo/derecho, y expone las operaciones recursivas (búsqueda, inserción, eliminación, recorridos y métricas) sobre el subárbol enraizado en él. La implementación es **recursiva**: el árbol se construye delegando cada operación en los hijos.

---

# Implementaciones lineales

## `ListaArreglo<T>` — `TDALista` sobre arreglo redimensionable

Mantiene un arreglo `Object[]` y un contador `tamaño`. Cuando se llena, el arreglo se duplica (`max(capacidad·2, requerido)`), lo que da `agregar` al final en O(1) **amortizado**.

- Acceso por índice O(1).
- Inserciones/eliminaciones intermedias O(n) por el corrimiento (`System.arraycopy`).
- `remover` deja la celda liberada en `null` para que el GC pueda reclamar el objeto.

## `ListaEnlazada<T>` — `TDALista` sobre lista enlazada simple

Cada nodo (clase privada `Nodo`) guarda un dato y un enlace al siguiente. Mantiene sólo el puntero a la cabeza; **no** mantiene puntero al último.

- Inserción/remoción al inicio O(1).
- Inserción/remoción en otra posición O(index) (recorrido + splice).
- `paraCada(Consumer<? super T>)`: utilidad pública para iterar la cadena en una sola pasada (O(n)). Las subclases la usan internamente para evitar la trampa de iterar con `obtener(i)`.

## `PilaSobreArreglo<T>` extends `ListaArreglo<T>` implements `TDAPila<T>`

Reutiliza por herencia toda la maquinaria de `ListaArreglo`. El **tope** es el final del arreglo: `mete` delega en `agregar` (al final), `saca` remueve la última posición y `tope` devuelve `obtener(tamaño-1)`. Todas estas operaciones son O(1) amortizado.

## `PilaSobreEnlazada<T>` extends `ListaEnlazada<T>` implements `TDAPila<T>`

Mismo patrón pero con la cabeza como tope. `mete(d)` inserta en posición 0 (O(1)), `saca()` remueve la cabeza (O(1)), `tope()` devuelve `obtener(0)` (O(1)). Nunca paga el costo de redimensionar el arreglo.

## `ColaArregloCircular<T>` implements `TDACola<T>`

Buffer circular sobre arreglo. Mantiene un índice `frente` y el `tamaño`; el último elemento ocupa la posición física `(frente + tamaño - 1) % capacidad`. Esto permite que `poneEnCola` y `quitaDeCola` sean O(1) sin desplazar elementos.

- Mapeo lógico→físico encapsulado en `fisico(i) = (frente + i) % datos.length`.
- Al redimensionar (cuando se llena), los elementos se **linealizan** comenzando en el índice 0 del nuevo arreglo y `frente` se reinicia a 0.
- Las operaciones de lista intermedias siguen funcionando sobre la vista lógica (con costo O(n) por el corrimiento).

## `ColaEnlazada<T>` implements `TDACola<T>`

Lista enlazada simple con puntero adicional al **último** nodo. Esto da inserción al final en O(1) sin recorrer la cadena. Mantiene el invariante:

```
(cabeza == null) ⇔ (ultimo == null) ⇔ (tamaño == 0)
```

Todas las mutaciones preservan ese invariante (en particular, al remover el último nodo se actualiza `ultimo`).

## `ConjuntoSobreArreglo<T>` extends `ListaArreglo<T>` implements `TDAConjunto<T>`

Hereda toda la lógica de `ListaArreglo` y sólo sobrescribe `agregar(T)` y `agregar(int, T)` para chequear unicidad antes de delegar. Las cuatro operaciones binarias (`union`, `interseccion`, `diferencia`, `esSubconjuntoDe`) iteran usando la API pública del TDA y cuestan O(n·m).

## `ConjuntoSobreEnlazada<T>` extends `ListaEnlazada<T>` implements `TDAConjunto<T>`

Idéntica idea pero sobre una enlazada. Las operaciones binarias se sobrescriben para iterar con `paraCada` (heredado de `ListaEnlazada`) en lugar de `obtener(i)`: si lo hicieran con índice serían O(n²). Para iterar el otro conjunto se hace un dispatch (`instanceof ListaEnlazada`) que vuelve a usar `paraCada` cuando aplica.

---

# Implementaciones jerárquicas

## `BSTArbol<T>` + `BSTElemento<T>` — árbol binario de búsqueda sin balanceo

Implementación didáctica del BST clásico:

- **Inserción**: respeta el invariante `dato_izq < raíz < dato_der`. Duplicados se rechazan (devuelve `false`).
- **Eliminación**: una sola travesía recursiva; cubre los tres casos (sin hijos → se borra; un hijo → el hijo lo reemplaza; dos hijos → se copia el dato del sucesor in-order y se elimina al sucesor). Usa un `boolean[]` como bandera de salida para evitar buscar y luego volver a recorrer.
- **Recorridos** y métricas son recursivos directos.
- Sin rebalanceo: el peor caso (inserciones ya ordenadas) degenera a una cadena O(n). Sirve para comparar con el AVL.

## `AVLArbol<T>` + `AVLElemento<T>` — árbol binario de búsqueda autobalanceado

BST que mantiene en cada nodo un factor de balance acotado en `{-1, 0, 1}`:

- Cada nodo conserva su altura para evitar recalcularla.
- Tras cada inserción se reacomoda con rotaciones simples (izquierda/derecha) o dobles (izquierda-derecha/derecha-izquierda), elegidas según el factor de balance del nodo y de su hijo desbalanceado.
- Garantiza altura O(log n) → todas las búsquedas, inserciones e eliminaciones son O(log n) en peor caso.
- La operación `eliminar` está intencionalmente sin implementar (devuelve siempre `false`): la eliminación AVL completa requiere rotaciones post-borrado que esta clase no provee. Para ver la lógica de eliminación BST estándar, ver [`BSTElemento`](implementaciones/jerarquicos/BSTElemento.java).

---

# Convenciones del paquete

- **Nombres en español** para mantener consistencia con la materia.
- Las interfaces empiezan con prefijo `TDA`; las implementaciones, no.
- Las clases de implementación siguen el patrón `<Estructura>Sobre<Backing>` cuando heredan de otra (`PilaSobreArreglo`, `ConjuntoSobreEnlazada`) o `<Tipo><Variante>` cuando son independientes (`ColaArregloCircular`, `ColaEnlazada`).
- El casteo `Comparable<T>` → `T` se realiza puntualmente con `@SuppressWarnings("unchecked")` en el sitio de uso, dado que la verificación efectiva del tipo recae en el cliente que invoca `insertar`.
- Los archivos están en codificación **UTF-8**; el código usa caracteres acentuados (`tamaño`, `año`) en identificadores. Asegurarse de compilar con `-encoding UTF-8` o configurar el IDE/Maven en consecuencia (el `pom.xml` ya define `project.build.sourceEncoding=UTF-8`).
