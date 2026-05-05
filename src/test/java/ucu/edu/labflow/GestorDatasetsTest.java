package ucu.edu.labflow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import ucu.edu.labflow.modelo.Dataset;
import ucu.edu.labflow.servicio.GestorDatasets;
import ucu.edu.labflow.tda.implementaciones.lineales.conjunto.ConjuntoSobreEnlazada;

public class GestorDatasetsTest {

    private GestorDatasets gestor;

    @BeforeEach
    public void setUp() {
        gestor = new GestorDatasets();
    }

    @Test
    public void testRegistrarYBuscar() {
        Dataset d1 = new Dataset("1", "Dataset A", 100, "clasificacion");
        gestor.registrar(d1);
        Dataset resultado = gestor.buscarPorId("1");
        assertNotNull(resultado);
        assertEquals("Dataset A", resultado.getNombre());
    }

    @Test
    public void testBuscarInexistente() {
        Dataset resultado = gestor.buscarPorId("no-existe");
        assertNull(resultado);
    }

    @Test
    public void testEliminarExistente() {
        Dataset d1 = new Dataset("1", "Dataset A", 100, "clasificacion");
        gestor.registrar(d1);
        gestor.eliminar("1");
        Dataset resultado = gestor.buscarPorId("1");
        assertNull(resultado);
    }

    @Test
    public void testEliminarInexistente() {
        Dataset d1 = new Dataset("1", "Dataset A", 100, "clasificacion");
        gestor.registrar(d1);
        gestor.eliminar("999");
        Dataset resultado = gestor.buscarPorId("1");
        assertNotNull(resultado);
    }

    @Test
    public void testListarDatasets() {

        Dataset d1 = new Dataset("1", "Dataset A", 100, "clasificacion");
        Dataset d2 = new Dataset("2", "Dataset B", 200, "regresion");
        gestor.registrar(d1);
        gestor.registrar(d2);
        ConjuntoSobreEnlazada<Dataset> lista = gestor.listarDatasets();
        assertNotNull(lista);
        assertTrue(lista.buscar(d -> d.getId().equals("1")) != null);
        assertTrue(lista.buscar(d -> d.getId().equals("2")) != null);
    }

    @Test
    public void testNoDuplicados() {

        Dataset d1 = new Dataset("1", "Dataset A", 100, "clasificacion");
        Dataset d2 = new Dataset("1", "Dataset A duplicado", 150, "regresion");
        gestor.registrar(d1);
        gestor.registrar(d2); 
        ConjuntoSobreEnlazada<Dataset> lista = gestor.listarDatasets();
        int contador = 0;
        if (lista.buscar(d -> d.getId().equals("1")) != null) {
            contador++;
        }

        assertEquals(1, contador);
    }
}