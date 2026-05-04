package ucu.edu.labflow.modelo;

public class ResultadoExperimento {
    private double accuracy;
    private double precision;

    public ResultadoExperimento(double accuracy, double precision) {
        this.accuracy = accuracy;
        this.precision = precision;
    }

    //getters
    
    public double getAccuracy() {
        return accuracy;
    }

    public double getPrecision() {
        return precision;
    }
}