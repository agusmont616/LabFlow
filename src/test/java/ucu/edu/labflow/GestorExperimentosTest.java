package ucu.edu.labflow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import ucu.edu.labflow.modelo.Dataset;
import ucu.edu.labflow.modelo.Experimento;
import ucu.edu.labflow.modelo.Modelo;
import ucu.edu.labflow.servicio.GestorExperimentos;

public class GestorExperimentosTest {

    private GestorExperimentos gestor;

    private Dataset dataset1;
    private Dataset dataset2;

    private Modelo modelo;

    @BeforeEach
    public void setUp() {

        gestor = new GestorExperimentos();

        dataset1 = new Dataset(
                "D1",
                "Dataset 1",
                1000,
                "Clasificacion"
        );

        dataset2 = new Dataset(
                "D2",
                "Dataset 2",
                2000,
                "Regresion"
        );

        modelo = new Modelo(
                "M1",
                "Modelo 1",
                "RandomForest",
                "parametros"
        );
    }

    @Test
    public void registrarExperimentoTest() {

        Experimento experimento =
                new Experimento(
                        1,
                        dataset1,
                        modelo
                );

        gestor.registrar(experimento);

        assertEquals(
                1,
                gestor.listarExperimentos().getTamanio()
        );
    }

    @Test
    public void listarExperimentosTest() {

        Experimento e1 =
                new Experimento(
                        1,
                        dataset1,
                        modelo
                );

        Experimento e2 =
                new Experimento(
                        2,
                        dataset2,
                        modelo
                );

        gestor.registrar(e1);
        gestor.registrar(e2);

        assertEquals(
                2,
                gestor.listarExperimentos().getTamanio()
        );
    }

    @Test
    public void listarPorDatasetTest() {

        Experimento e1 =
                new Experimento(
                        1,
                        dataset1,
                        modelo
                );

        Experimento e2 =
                new Experimento(
                        2,
                        dataset1,
                        modelo
                );

        Experimento e3 =
                new Experimento(
                        3,
                        dataset2,
                        modelo
                );

        gestor.registrar(e1);
        gestor.registrar(e2);
        gestor.registrar(e3);

        assertEquals(
                2,
                gestor.listarPorDataset(dataset1).getTamanio()
        );
    }

    @Test
    public void listarPorDatasetSinResultadosTest() {

        Dataset datasetNuevo =
                new Dataset(
                        "D3",
                        "Dataset 3",
                        500,
                        "Clasificacion"
                );

        assertEquals(
                0,
                gestor.listarPorDataset(datasetNuevo).getTamanio()
        );
    }
}