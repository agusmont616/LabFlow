package ucu.edu.labflow.servicio;

import ucu.edu.labflow.tda.implementaciones.lineales.conjunto.ConjuntoSobreEnlazada;
import ucu.edu.labflow.modelo.Dataset;

public class GestorDatasets {
    
    /*Requerimientos:
        1. registrar datasets
        2. buscar, eliminar y listar datasets */ 

    private ConjuntoSobreEnlazada<Dataset> datasets; //conjunto para garantizar unicidad
    
    public GestorDatasets() {
        this.datasets = new ConjuntoSobreEnlazada<>();
    }

    public void registrar(Dataset dataset) {
        datasets.agregar(dataset);
    }

    public Dataset buscarPorId(String id) {
        return datasets.buscar(d -> d.getId().equals(id));
    }

    public void eliminar(String id) {
        Dataset d = buscarPorId(id);
        if (d != null) {
            datasets.remover(d);
        }
    }

    public ConjuntoSobreEnlazada<Dataset> listarDatasets() {
        return datasets;
    }
}