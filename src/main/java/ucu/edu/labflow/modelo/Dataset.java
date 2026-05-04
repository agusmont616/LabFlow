package ucu.edu.labflow.modelo;

public class Dataset {
    private int id;
    private String nombre;
    private int tamanio;
    private String tipoProblema;

    public Dataset(int id, String nombre, int tamanio, String tipoProb) {
        this.id = id;
        this.nombre = nombre;
        this.tamanio = tamanio;
        this.tipoProblema = tipoProb;
    }

    //getters

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getTamanio() {
        return tamanio;
    }

    public String getTipoProblema() {
        return tipoProblema;
    }

    
}