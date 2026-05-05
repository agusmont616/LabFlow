package ucu.edu.labflow.ui;
 
import ucu.edu.labflow.modelo.*;
import ucu.edu.labflow.servicio.*;
import ucu.edu.labflow.tda.implementaciones.lineales.lista.ListaEnlazada;
import ucu.edu.labflow.tda.implementaciones.lineales.conjunto.ConjuntoSobreEnlazada;
import ucu.edu.labflow.tda.implementaciones.lineales.cola.ColaEnlazada;
 
import java.util.Scanner;

public class MenuConsola {
    private final Facade facade;
    private final Scanner scanner;
    private int contadorExperimentos;

    public MenuConsola(Facade fachada) {
        this.facade = fachada;
        this.scanner = new Scanner(System.in);
        this.contadorExperimentos = 1;
    }

    public void iniciar() {
        System.out.println("--- BIENVENIDO A LABFLOW ---");
        boolean corriendo = true;
        
        while (corriendo) {
            mostrarMenuPrincipal();
            int opcion = leerInt("Opción: ");
            switch (opcion) {
                case 1 -> menuDatasets();
                case 2 -> menuModelos();
                case 3 -> menuExperimentos();
                case 4 -> menuSimulador();
                case 0 -> corriendo = false;
                default -> System.out.println("[!] Opción inválida");
            }
        }
        System.out.println("Programa finalizado.");
        scanner.close();
    }

    // menú principal

    private void mostrarMenuPrincipal() {
        System.out.println();
        System.out.println("--- MENU PRINCIPAL ---");
        System.out.println("1. Gestionar Datasets");
        System.out.println("2. Gestionar Modelos");
        System.out.println("3. Gestionar Experimentos");
        System.out.println("4. Simulador de Ejecucion");
        System.out.println("0. Salir");
    }

    // menú datasets

    private void menuDatasets() {
        boolean volver = false;
        while (!volver) {
            System.out.println();
            System.out.println("--- DATASETS ---");
            System.out.println("1. Registrar dataset");
            System.out.println("2. Buscar dataset por ID");
            System.out.println("3. Eliminar dataset por ID");
            System.out.println("4. Listar todos los datasets");
            System.out.println("0. Volver");

            int op = leerInt("Opcion: ");
            switch (op) {
                case 1 -> registrarDataset();
                case 2 -> buscarDataset();
                case 3 -> eliminarDataset();
                case 4 -> listarDatasets();

                case 0 -> volver = true;
                default -> System.out.println("[!] Opción inválida");
            }
        }
    }

    private void registrarDataset() {

        System.out.print("ID: ");   //registro id
        String id = scanner.nextLine().trim();

        System.out.print("Nomrbe: ");   // registro nomrbe
        String nombre = scanner.nextLine().trim();

        int tamanio = leerInt("Tamaño: ");  // registro tamañp

        System.out.print("Tipo: "); // registro tipo
        String tipo = scanner.nextLine().trim();

        if (facade.registrarDataset(id, nombre, tamanio, tipo)) {
            System.out.println("[OK] Dataset registrado");
        } else {
            System.out.println("[!] Error de registro");
        }
        
    }

    private void buscarDataset() {

        System.out.print("ID: "); 
        String id = scanner.nextLine().trim();

        Dataset d = facade.buscarDataset(id);
        if (d != null) {
            imprimirDataset(d);
        } else {
            System.out.println("[!] No encontrado.");
        }
    }

    private void eliminarDataset() {
        System.out.print("ID: ");
        String id = scanner.nextLine().trim();
        
        if (facade.eliminarDataset(id)) {
            System.out.println("[OK] Eliminado.");
        } else {
            System.out.println("[~] Dataset inexistente.");
        }
        
    }

    private void listarDatasets() {
        ConjuntoSobreEnlazada<Dataset> lista = facade.listarDatasets();

        if (lista.esVacio()) {
            System.out.println("[~] Sin datasets");
            return;
        }

        for (int i = 0; i < lista.getTamanio(); i++) {
            imprimirDataset(lista.obtener(i));
        }
    }

    private void imprimirDataset(Dataset d) {
        System.out.println("[" + d.getId() + "]" + d.getNombre() + "|" + d.getTamanio() + "|" + d.getTipoProblema() + ".");
    }


    // modelos

    private void menuModelos(){
        boolean volver = false;
        while (!volver) {
            System.out.println();
            System.out.println("--- MODELOS ---");
            System.out.println("1. Registrar modelo");
            System.out.println("2. Buscar modelo por ID");
            System.out.println("3. Eliminar modelo por ID");
            System.out.println("4. Listar todos los modelos");
            System.out.println("0. Volver");

            int op = leerInt("Opcion: ");
            switch (op) {
                case 1 -> registrarModelo();
                case 2 -> buscarModelo();
                case 3 -> eliminarModelo();
                case 4 -> listarModelos();

                case 0 -> volver = true;
                default -> System.out.println("[!] Opción inválida");
            }
        }
    }

    private void registrarModelo() {
        System.out.print("ID: ");   //registro id
        String id = scanner.nextLine().trim();

        System.out.print("Nomrbe: ");   // registro nomrbe
        String nombre = scanner.nextLine().trim();

        System.out.print("Tipo: "); // registro tipo
        String tipo = scanner.nextLine().trim();

        System.out.print("Parámetros: "); // registro parámetros
        String params = scanner.nextLine().trim();

        if (facade.registrarModelo(id, nombre, tipo, params)) {
            System.out.println("[OK] Dataset registrado");
        } else {
            System.out.println("[!] Error de registro");
        }
    }

    public void buscarModelo(){
        System.out.print("ID: "); 
        String id = scanner.nextLine().trim();

        Modelo m = facade.buscarModelo(id);
        if (m != null) {
            imprimirModelo(m);
        } else {
            System.out.println("[!] No encontrado.");
        }
    }

    public void eliminarModelo() {
        System.out.print("ID: ");
        String id = scanner.nextLine().trim();

        if (facade.eliminarModelo(id)) {
            System.out.println("[OK] Eliminado.");
        } else {
            System.out.println("[~] Modelo inexistente.");
        }        
    }

    public void listarModelos() {
        ConjuntoSobreEnlazada<Modelo> lista = facade.listarModelos();

        if (lista.esVacio()) {
            System.out.println("[~] Sin modelos");
            return;
        }

        for (int i = 0; i < lista.getTamanio(); i++) {
            imprimirModelo(lista.obtener(i));
        }
    }

    private void imprimirModelo(Modelo m) {
        System.out.println("[" + m.getId() + "]" + m.getNombre() + "|" + m.getTipoModelo() + ".");
    }


    // experimentos

    private void menuExperimentos() {
        boolean volver = false;
        while (!volver) {
            System.out.println();
            System.out.println("--- EXPERIMENTOS ---");
            System.out.println("1. Crear");
            System.out.println("2. Listar todos");
            System.out.println("3. Listar por dataset");
            System.out.println("4. Listar por modelo");
            System.out.println("0. Volver");

            int op = leerInt("Opcion: ");
            switch (op) {
                case 1 -> crearExperimento();
                case 2 -> listarTodosLosExperimentos();
                case 3 -> listarExperimentosPorDataset();
                case 4 -> listarExperimentosPorModelo();

                case 0 -> volver = true;
                default -> System.out.println("[!] Opción inválida");
            }
        }
    }

    private void crearExperimento() {
        System.out.print("ID Dataset: ");   //registro id Dataset
        String idD = scanner.nextLine().trim();
        Dataset d = facade.buscarDataset(idD);

        if (d == null) {
            System.out.println("[!] Dataset no encontrado");
        }

        System.out.print("ID Modelo: ");   // registro id Modelo
        String idM = scanner.nextLine().trim();
        Modelo m = facade.buscarModelo(idM);

        if (m == null) {
            System.out.println("[!] Modelo no encontrado");
        }

        Experimento e = facade.crearExperimento(contadorExperimentos++, d, m);
        System.out.println("[OK] Experimento creado #" + e.getId());
    }

    private void listarTodosLosExperimentos() {

        ListaEnlazada<Experimento> lista = facade.listarTodosLosExperimentos();

        if (lista.esVacio()) {
            System.out.println("[~] No hay experimentos");
            return;
        }

        for (int i = 0; i < lista.getTamanio(); i++) {
            imprimirExperimento(lista.obtener(i));
        }
    }

    private void listarExperimentosPorDataset() {
        System.out.println("ID dataset: ");
        String id = scanner.nextLine().trim();

        Dataset d = facade.buscarDataset(id);

        if (d == null) {
            System.out.println("[!] Dataset no encontrado");
            return;
        }

        ListaEnlazada<Experimento> lista = facade.listarPorDataset(d);

        if (lista.esVacio()) {
            System.out.println("[~] Sin resultados");
            return;
        }

        for (int i = 0; i < lista.getTamanio(); i++) {
            imprimirExperimento(lista.obtener(i));
        }
    }

    private void listarExperimentosPorModelo() {
        System.out.println("ID modelo: ");
        String id = scanner.nextLine().trim();

        Modelo m = facade.buscarModelo(id);

        if (m == null) {
            System.out.println("[!] Modelo no encontrado");
            return;
        }

        ListaEnlazada<Experimento> lista = facade.listarPorModelo(m);

        if (lista.esVacio()) {
            System.out.println("[~] Sin resultados");
            return;
        }

        for (int i = 0; i < lista.getTamanio(); i++) {
            imprimirExperimento(lista.obtener(i));
        }
    }

    private void imprimirExperimento(Experimento e) {
        System.out.println("[" + e.getId() + "]" + e.getDataset().getNombre() + "|" + e.getModelo().getNombre() + "|" + e.getEstado());
        
        if (e.getResultado() != null) {
            System.out.println("| Acc:" + e.getResultado().getAccuracy() + "| Prec: " + e.getResultado().getPrecision());
        }

        System.out.println();
    }

    
    // simulador

    private void menuSimulador() {
        boolean volver = false;
        while (!volver) {
            System.out.println();
            System.out.println("--- SIMULADOR ---");
            System.out.println("1. Ejecutar siguiente experimento");
            System.out.println("2. Ver historial");
            System.out.println("0. Volver");

            int op = leerInt("Opcion: ");
            switch (op) {
                case 1 -> { 
                    facade.ejecutarSiguiente();
                    System.out.println("[OK] Ejecutado");
                }
                case 2 -> mostrarHistorial();

                case 0 -> volver = true;
                default -> System.out.println("[!] Opción inválida");
            }
        }
    }

    private void mostrarHistorial() {
        ListaEnlazada<Experimento> lista = facade.getHistorial();

        if (lista.esVacio()) {
            System.out.println("[~] No hay experimentos en el historial");
            return;
        }

        for (int i = 0; i < lista.getTamanio(); i++) {
            imprimirExperimento(lista.obtener(i));
        }
    }

    
    // leer int

    private int leerInt(String msg) {
        while (true) {
            System.out.print(msg);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                System.out.println("[!] Número inválido.");
            }
        }
    }
}