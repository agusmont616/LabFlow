package ucu.edu.labflow.servicio;

import ucu.edu.labflow.tda.implementaciones.lineales.conjunto.ConjuntoSobreEnlazada;
import ucu.edu.labflow.modelo.Modelo;

public class GestorModelos {
    
    /*Requerimientos:
        1. registrar modelos
        2. buscar, eliminar y listar modelos */ 

    private ConjuntoSobreEnlazada<Modelo> modelos; //conjunto para garantizar unicidad

    public GestorModelos() {
        this.modelos = new ConjuntoSobreEnlazada<>();
    }

    public void registrarModelo(Modelo modelo) {
        modelos.agregar(modelo);
    }

    public Modelo buscarPorId(String id) {
        return modelos.buscar(m -> m.getId().equals(id));
    }

    public void eliminarModelo(String id) {
        Modelo modelo = buscarPorId(id);
        modelos.remover(modelo);
    }

    public ConjuntoSobreEnlazada<Modelo> listarModelos() {
        return modelos;
    }
}