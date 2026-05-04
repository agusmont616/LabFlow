package ucu.edu.labflow.modelo;

public class Experimento {
    private int id;
    private Dataset dataset;
    private Modelo modelo;
    private EstadoExperimento estado;

    public Experimento(int id, Dataset dataset, Modelo modelo, EstadoExperimento estado) {
        this.id = id;
        this.dataset = dataset;
        this.modelo = modelo;
        this.estado = estado;
    }
}