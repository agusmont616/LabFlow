package ucu.edu.labflow.ui;

import ucu.edu.labflow.modelo.*;
import ucu.edu.labflow.servicio.*;

import ucu.edu.labflow.tda.implementaciones.lineales.lista.ListaEnlazada;
import ucu.edu.labflow.tda.implementaciones.lineales.conjunto.ConjuntoSobreEnlazada;


public class Facade {
    private final GestorDatasets gestorDatasets;
    private final GestorModelos gestorModelos;
    private final GestorExperimentos gestorExperimentos;
    private final SimuladorEjecucion simulador;

    public Facade(GestorDatasets gestorDatasets,
                       GestorModelos gestorModelos, 
                       GestorExperimentos gestorExperimentos, 
                       SimuladorEjecucion simulador) {
        this.gestorDatasets = gestorDatasets;
        this.gestorModelos = gestorModelos;
        this.gestorExperimentos = gestorExperimentos;
        this.simulador = simulador;
    }

    // datasets

    public boolean registrarDataset(String id, String nombre, int tamanio, String tipo) {
        return gestorDatasets.registrar(new Dataset(id, nombre, tamanio, tipo));
    }

    public Dataset buscarDataset(String id) {
        return gestorDatasets.buscarPorId(id);
    }

    public boolean eliminarDataset(String id) {
        return gestorDatasets.eliminar(id);
    }

    public ConjuntoSobreEnlazada<Dataset> listarDatasets() {
        return gestorDatasets.listarDatasets();
    }


    // modelos

    public boolean registrarModelo(String id, String nombre, String tipo, String params) {
        return gestorModelos.registrarModelo(new Modelo(id, nombre, tipo, params));
    }

    public Modelo buscarModelo(String id) {
        return gestorModelos.buscarPorId(id);
    }

    public boolean eliminarModelo(String id) {
        return gestorModelos.eliminarModelo(id);
    }

    public ConjuntoSobreEnlazada<Modelo> listarModelos() {
        return gestorModelos.listarModelos();
    }


    // experimentos

    public Experimento crearExperimento(int id, Dataset dataset, Modelo modelo) {
        Experimento e = new Experimento(id, dataset, modelo);
        gestorExperimentos.registrar(e);
        simulador.ponerEnCola(e);
        return e;
    }

    public ListaEnlazada<Experimento> listarTodosLosExperimentos() {
        ListaEnlazada<Experimento> lista = gestorExperimentos.listarExperimentos();
        return lista;
    }

    public ListaEnlazada<Experimento> listarPorDataset(Dataset d) {
        return gestorExperimentos.listarPorDataset(d);
    }

    public ListaEnlazada<Experimento> listarPorModelo(Modelo m) {
        return gestorExperimentos.listarPorModelo(m);
    }

    // simulador

    public void ejecutarSiguiente() {
        simulador.ejecutarSiguiente();
    }

    public ListaEnlazada<Experimento> getHistorial() {
        return simulador.getHistorial();
    }
}

