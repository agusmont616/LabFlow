package ucu.edu.labflow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import ucu.edu.labflow.servicio.GestorModelos;
import ucu.edu.labflow.modelo.Modelo;
import ucu.edu.labflow.tda.implementaciones.lineales.conjunto.ConjuntoSobreEnlazada;

public class GestorModelosTest {

    private GestorModelos gestor;

    @BeforeEach
    public void setUp() {
        gestor = new GestorModelos();
    }

    private Modelo crearModelo(String id) {
        return new Modelo(
                id,
                "Modelo " + id,
                "tipo",
                "parametros"
        );
    }

    @Test
    public void testRegistrarYBuscar() {
        Modelo m1 = crearModelo("1");

        gestor.registrarModelo(m1);

        Modelo resultado = gestor.buscarPorId("1");

        assertNotNull(resultado);
        assertEquals("1", resultado.getId());
    }

    @Test
    public void testBuscarInexistente() {
        Modelo resultado = gestor.buscarPorId("no-existe");

        assertNull(resultado);
    }

    @Test
    public void testEliminarExistente() {
        Modelo m1 = crearModelo("1");

        gestor.registrarModelo(m1);
        gestor.eliminarModelo("1");

        Modelo resultado = gestor.buscarPorId("1");

        assertNull(resultado);
    }

    @Test
    public void testEliminarInexistente() {
        Modelo m1 = crearModelo("1");
        gestor.registrarModelo(m1);
        gestor.eliminarModelo("999");
        Modelo resultado = gestor.buscarPorId("1");
        assertNotNull(resultado);
    }

    @Test
    public void testListarModelos() {
        Modelo m1 = crearModelo("1");
        Modelo m2 = crearModelo("2");
        gestor.registrarModelo(m1);
        gestor.registrarModelo(m2);
        ConjuntoSobreEnlazada<Modelo> lista = gestor.listarModelos();
        assertNotNull(lista);
        assertTrue(lista.buscar(m -> m.getId().equals("1")) != null);
        assertTrue(lista.buscar(m -> m.getId().equals("2")) != null);
    }

    @Test
    public void testNoDuplicados() {
        Modelo m1 = crearModelo("1");
        Modelo m2 = crearModelo("1"); 
        gestor.registrarModelo(m1);
        gestor.registrarModelo(m2);
        ConjuntoSobreEnlazada<Modelo> lista = gestor.listarModelos();
        int contador = 0;
        if (lista.buscar(m -> m.getId().equals("1")) != null) {
            contador++;
        }
        assertEquals(1, contador);
    }
}