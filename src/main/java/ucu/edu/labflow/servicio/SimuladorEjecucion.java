package ucu.edu.labflow.servicio;

import ucu.edu.labflow.modelo.*;
import ucu.edu.labflow.tda.implementaciones.lineales.cola.ColaEnlazada;
import ucu.edu.labflow.tda.implementaciones.lineales.lista.ListaEnlazada;
import java.util.Random;

public class SimuladorEjecucion {

    /*Requerimientos:
        1. registrar  accuracy y precision (pueden ser más)
        2. cambiar estado de experimento a ejecutado
        3. historial de experimentos*/ 
    
    private ColaEnlazada<Experimento> pendientes;
    private ListaEnlazada<Experimento> historial;
    private Random random;

    public SimuladorEjecucion() {
        this.pendientes = new ColaEnlazada<>();
        this.historial = new ListaEnlazada<>();
        this.random = new Random();
    }

    public void ponerEnCola(Experimento experimento) {
        this.pendientes.poneEnCola(experimento);
    }

    public void ejecutarSiguiente() {
        if (pendientes.esVacio()) {
            return;
        }

        Experimento e = pendientes.quitaDeCola();

        double accuracy = 0.5 + (1.0 - 0.5) * random.nextDouble(); // genera un double entre 0.5 y 1.0
        double precision = 0.5 + (1.0 - 0.5) * random.nextDouble();

        ResultadoExperimento resultado = new ResultadoExperimento(accuracy, precision);

        e.setResultado(resultado);
        e.setEstado(EstadoExperimento.EJECUTADO);

        this.historial.agregar(e);
    }

    public ListaEnlazada<Experimento> getHistorial() {
        return historial;
    }
}