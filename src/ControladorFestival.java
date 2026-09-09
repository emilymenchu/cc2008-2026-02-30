import java.util.ArrayList;
import java.util.InputMismatchException;

public class ControladorFestival {
    private Festival festival;
    private Escenario[] escenarios;
    private ArrayList<Artista> artistas;
    private VistaFestival vista;

    // Constructor de la clase que inicializa atributos
    public ControladorFestival() {
        festival = null;
        escenarios = new Escenario[5];
        artistas = new ArrayList<Artista>();
        vista = new VistaFestival();
    }

    // Mètodo que inicia la ejecución del programa
    public void iniciar() {
        nuevoFestival();
        int opcion;

        do {
            opcion = mostrarMenu();

            switch (opcion) {
                case 1:
                    nuevoFestival();
                    break;
                case 2:
                    configurarEscenario();
                    break;
                case 3:
                    consultarEscenarios();
                    break;
                case 4:
                    consultarEscenario();
                    break;
                case 5:
                    modificarEscenario();
                    break;
                case 6:
                    retirarEscenario();
                    break;
                case 7:
                    registrarArtista();
                    break;
                case 8:
                    consultarArtistas();
                    break;
                case 9:
                    buscarArtista();
                    break;
                case 10:
                    modificarArtista();
                    break;
                case 11:
                    cancelarParticipacion();
                    break;
                case 12:
                    mostrarReporteFestival();
                    break;
                case 0:
                    vista.mostrarMensaje("\nAdióoooooos ;)");
                    break;
                default:
                    vista.mostrarMensaje("Opción inválida. Ingrese la opción otra vez :(");
                    break;
            }

        } while (opcion != 0);
        
    }

    private void nuevoFestival() {
        try {
            String nombre = vista.pedirString("Ingrese el nombre del festival: ");
            String codigo = vista.pedirString("Ingrese el código del festival: ");
            String coordinador = vista.pedirString("Ingrese el nombre del coordinador: ");

            // Para que cada que se crea un nuevo festival también se reseteen los valores de escenarios y artistas.
            Festival nuevoFestival = new Festival(nombre, codigo, coordinador);
            this.festival = nuevoFestival;
            escenarios = new Escenario[5];
            artistas = new ArrayList<Artista>();

            vista.mostrarMensaje("Festival creado exitosamente :D");
        } catch (IllegalArgumentException e) {
            vista.mostrarMensaje("Error: " + e.getMessage());
        }
    }

    private int mostrarMenu() {
        return vista.mostrarMenu(festival != null ? festival.toString() : "");
    }

    private void configurarEscenario() {
        int posicion = vista.pedirInt("Ingrese la posición del escenario (1-5): ");
        int index = posicion - 1; //Se le resta 1 poque los indices de la lista van de 0 a 4 pero el usuario igresa de 1 a 5
        try {
            if (escenarios[index] != null) {
                vista.mostrarMensaje("Error: ya existe un escenario en esta posición");
                return;
            }

            String codigo = vista.pedirString("Ingrese el código del escenario: ");
            String nombre = vista.pedirString("Ingrese el nombre del escenario: ");
            String ubicacion = vista.pedirString("Ingrese la ubicación del escenario: ");
            int capacidad = vista.pedirInt("Ingrese la capacidad máxima: ");
            while (capacidad <= 0) {
                vista.mostrarMensaje("La capacidad debe ser mayor a 0");
                capacidad = vista.pedirInt("Ingrese la capacidad máxima: ");
            }

            Escenario nuevoEscenario = new Escenario(codigo, nombre, ubicacion, capacidad, false);
            escenarios[index] = nuevoEscenario;

            vista.mostrarMensaje("Escenario configurado exitosamente :D");
        } catch (IndexOutOfBoundsException e) {
            vista.mostrarMensaje("Error: la posición ingresada debe de estar entre 1 y 5 -_-");
        } catch (IllegalArgumentException e) {
            vista.mostrarMensaje("Error: " + e.getMessage());
        }
    }

    private void consultarEscenarios() {
        boolean hayEscenarios = false;
        for (int i = 0; i < escenarios.length; i++) {
             if (escenarios[i] != null) {
                vista.mostrarMensaje(String.format("""
                        Posición: %d
                        %s
                        """,
                        i + 1,
                        escenarios[i]));
                hayEscenarios = true;
             }
        }
        if (!hayEscenarios) {
            vista.mostrarMensaje("No hay escenarios configurados.");
        }
    }

    private void consultarEscenario() {
        int posicion = vista.pedirInt("Ingrese la posición del escenario (1-5): ");

        int indice = posicion - 1;

        try {
            if (escenarios[indice] == null) {
                vista.mostrarMensaje("No hay escenario en esa posición.");
                return;
            }
            vista.mostrarMensaje(escenarios[indice].toString());
        } catch (IndexOutOfBoundsException e) {
            vista.mostrarMensaje("Error: la posición debe ser entre 1 y 5.");
        }
    }

    private void modificarEscenario() {
        int posicion = vista.pedirInt("Ingrese la posición del escenario que desea modificar (1-5): ");
        int indice = posicion - 1;

        try {
            if (escenarios[indice] == null) {
                vista.mostrarMensaje("No hay ningún escenario en esa posición.");
                return;
            }

            int capacidad = vista.pedirInt("Ingrese la nueva capacidad máxima: ");
            while (capacidad <= 0) {
                vista.mostrarMensaje("La capacidad debe ser mayor a 0");
                capacidad = vista.pedirInt("Ingrese la nueva capacidad máxima: ");
            }

            int estado = vista.pedirInt("""
                    Ingrese el nuevo estado:
                    1. Ocupado
                    2. Libre
                    Opción: 
                    """);

            while (estado != 1 && estado != 2) {
                vista.mostrarMensaje("Error: el estado ingresado no es válido.");
                estado = vista.pedirInt("""
                    Ingrese el nuevo estado:
                    1. Ocupado
                    2. Libre
                    Opción: 
                    """);
            }

            escenarios[indice].setCapacidadMax(capacidad);
            escenarios[indice].setEstado(estado == 1);

            vista.mostrarMensaje("Escenario modificado exitosamente.");

        } catch (IndexOutOfBoundsException e) {
            vista.mostrarMensaje("Error: la posición debe estar entre 1 y 5 -_-");
        } catch (IllegalArgumentException e) {
            vista.mostrarMensaje("Error: " + e.getMessage());
        }
    }

    private void retirarEscenario() {
        int posicion = vista.pedirInt("Ingrese la posición del escenario que desea retirar (1-5): ");
        int indice = posicion - 1;

        try {
            if (escenarios[indice] == null) {
                vista.mostrarMensaje("No hay ningún escenario configurado en esa posición.");
                return;
            }
            escenarios[indice] = null;
            vista.mostrarMensaje("Escenario retirado correctamente.");
        } catch (IndexOutOfBoundsException e) {
            vista.mostrarMensaje("Error: la posición debe estar entre 1 y 5.");
        }
    }

    private void registrarArtista() {

    }

    private void consultarArtistas() {

    }

    private void buscarArtista() {

    }

    private void modificarArtista() {

    }

    private void cancelarParticipacion() {

    }

    private void mostrarReporteFestival() {

    }

    private int calcularTotalEscenarios() {
        int contador = 0;
        for (int i = 0; i < escenarios.length; i++) {
            if (escenarios[i] != null) {
                contador++;
            }
        }
        return contador;
    }

    private Escenario calcularEscenarioMayorCapacidad() {
        Escenario escenarioMayor = null;

        for (int i = 0; i < escenarios.length; i++) {
            if (escenarios[i] != null) {
                if (escenarioMayor == null || escenarios[i].getCapacidadMax() > escenarioMayor.getCapacidadMax()) {
                    escenarioMayor = escenarios[i];
                }
            }
        }
        return escenarioMayor;
    }

    private int calcularTotalArtistas() {

        return 0;
    }

    private Artista getArtistaMayorDuracion() {

        return null;
    }

    private Artista getArtistaMayorAsistentes() {

        return null;
    }

    private float calcularPromedioDuracion() {

        return 0;
    }
}
