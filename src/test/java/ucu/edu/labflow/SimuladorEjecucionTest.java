package ucu.edu.labflow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import ucu.edu.labflow.servicio.SimuladorEjecucion;
import ucu.edu.labflow.modelo.*;
import ucu.edu.labflow.tda.implementaciones.lineales.cola.ColaEnlazada;

public class SimuladorEjecucionTest {

    private SimuladorEjecucion simulador;

    @BeforeEach
    public void setUp() {
        simulador = new SimuladorEjecucion();
    }

    private Experimento crearExperimento(int id) {
        Dataset dataset = new Dataset("d" + id, "Dataset " + id, 100, "clasificacion");
        Modelo modelo = new Modelo(
                "m" + id,
                "Modelo " + id,
                "tipo",
                "parametros"
        );
        return new Experimento(id, dataset, modelo, EstadoExperimento.PENDIENTE);
    }

    @Test
    public void testEjecutarConColaVacia() {
        simulador.ejecutarSiguiente();
        ColaEnlazada<Experimento> historial = simulador.getHistorial();
        assertTrue(historial.esVacio());
    }

    @Test
    public void testPonerEnColaYEjecutar() {
        Experimento exp = crearExperimento(1);
        simulador.ponerEnCola(exp);
        simulador.ejecutarSiguiente();
        assertEquals(EstadoExperimento.EJECUTADO, exp.getEstado());
        ResultadoExperimento res = exp.getResultado();
        assertNotNull(res);
        assertTrue(res.getAccuracy() >= 0.5 && res.getAccuracy() <= 1.0);
        assertTrue(res.getPrecision() >= 0.5 && res.getPrecision() <= 1.0);
    }

    @Test
    public void testSeAgregaAlHistorial() {
        Experimento exp = crearExperimento(1);
        simulador.ponerEnCola(exp);
        simulador.ejecutarSiguiente();
        ColaEnlazada<Experimento> historial = simulador.getHistorial();
        assertFalse(historial.esVacio());
        Experimento enHistorial = historial.quitaDeCola();
        assertEquals(exp.getId(), enHistorial.getId()); // más robusto
    }

    @Test
    public void testEjecutarUnoNoAfectaPendientesRestantes() {
        Experimento exp1 = crearExperimento(1);
        Experimento exp2 = crearExperimento(2);
        simulador.ponerEnCola(exp1);
        simulador.ponerEnCola(exp2);
        simulador.ejecutarSiguiente();
        assertEquals(EstadoExperimento.EJECUTADO, exp1.getEstado());
        assertEquals(EstadoExperimento.PENDIENTE, exp2.getEstado());
    }
}