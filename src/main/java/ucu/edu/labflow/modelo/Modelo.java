package ucu.edu.labflow.modelo;

public class Modelo {
    private int id;
    private String nombre;
    private String tipoModelo;
    private String parametros; //simulado

    public Modelo(int id, String nombre, String tipoMod, String param) {
        this.id = id;
        this.nombre = nombre;
        this.tipoModelo = tipoMod;
        this.parametros = param;
    }
}