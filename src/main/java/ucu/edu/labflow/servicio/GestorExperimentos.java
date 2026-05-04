package ucu.edu.labflow.servicio;

import ucu.edu.labflow.tda.implementaciones.lineales.lista.ListaEnlazada;
import ucu.edu.labflow.modelo.Experimento;
import ucu.edu.labflow.modelo.Dataset;


public class GestorExperimentos {
    
    /*Requerimientos:
        1. crear experimentos.
        2. permitir registrar múltiples experimentos
        3. listar experimentos por dataset o por modelo */ 

    private ListaEnlazada<Experimento> experimentos;

    public GestorExperimentos() {
        this.experimentos = new ListaEnlazada<>();
    }

    public void registrar(Experimento experimento) {
        experimentos.agregar(experimento);
    }

    public ListaEnlazada<Experimento> listarExperimentos() {
        return experimentos;
    }

    public ListaEnlazada<Experimento> listarPorDataset(Dataset dataset) {
        ListaEnlazada <Experimento> resultado = new ListaEnlazada<>();

        for (int i = 0; i < experimentos.getTamanio(); i++) {

            Experimento experimento = experimentos.obtener(i);

            if (experimento.getDataset() == dataset) {
                resultado.agregar(experimento);
            }
        }

        return resultado;
    }
}