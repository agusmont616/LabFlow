package ucu.edu.labflow;

import ucu.edu.labflow.servicio.*;
import ucu.edu.labflow.ui.Facade;
import ucu.edu.labflow.ui.MenuConsola;

public class Main {

    public static void main(String[] args) {

        // creo gestores y simulador
        GestorDatasets gd = new GestorDatasets();
        GestorModelos gm = new GestorModelos();
        GestorExperimentos ge = new GestorExperimentos();
        SimuladorEjecucion sim = new SimuladorEjecucion();

        //creo fachada
        Facade facade = new Facade(gd, gm, ge, sim);

        // iniciar menú

        MenuConsola menu = new MenuConsola(facade);
        menu.iniciar();
    }
}