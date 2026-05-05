package ucu.edu.labflow.modelo;

public class Experimento {
    private int id;
    private Dataset dataset;
    private Modelo modelo;
    private EstadoExperimento estado;
    private ResultadoExperimento resultado;

    public Experimento(int id, Dataset dataset, Modelo modelo) {
        this.id = id;
        this.dataset = dataset;
        this.modelo = modelo;
        this.estado = EstadoExperimento.PENDIENTE;
        this.resultado = null;
    }

    //getters

    public int getId() {
        return id;
    }

    public Dataset getDataset() {
        return dataset;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public EstadoExperimento getEstado() {
        return estado;
    }

    public ResultadoExperimento getResultado() {
        return resultado;
    }

    //setters

    public void setEstado(EstadoExperimento estadoNuevo) {
        this.estado = estadoNuevo;
    }

    public void setResultado(ResultadoExperimento resultadoNuevo) {
        this.resultado = resultadoNuevo;
    }
}