package ucu.edu.labflow.modelo;

public class Modelo {
    private String id;
    private String nombre;
    private String tipoModelo;
    private String parametros; //simulado

    public Modelo(String id, String nombre, String tipoMod, String param) {
        this.id = id;
        this.nombre = nombre;
        this.tipoModelo = tipoMod;
        this.parametros = param;
    }

    //getters

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipoModelo() {
        return tipoModelo;
    }

    public String getParametros() {
        return parametros;
    }
}