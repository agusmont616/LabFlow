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

    public boolean registrarModelo(Modelo modelo) {
        if (!modelos.contiene(modelo)){
            modelos.agregar(modelo);
            return true;
        }

        return false;
    }

    public Modelo buscarPorId(String id) {
        return modelos.buscar(m -> m.getId().equals(id));
    }

    public boolean eliminarModelo(String id) {
        Modelo modelo = buscarPorId(id);
        if (modelos.contiene(modelo)) {
            modelos.remover(modelo);
            return true;
        }
        return false;
        
    }

    public ConjuntoSobreEnlazada<Modelo> listarModelos() {
        return modelos;
    }
}