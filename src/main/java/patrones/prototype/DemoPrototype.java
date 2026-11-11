package patrones.prototype;

/** Ejecución de ejemplo del patrón Prototype (Caso C de la guía). */
public final class DemoPrototype {

    private DemoPrototype() {
    }

    public static void main(String[] args) {
        CargadorPlantillas cargador = new CargadorPlantillas();
        RegistroPlantillas registro = new RegistroPlantillas();
        registro.registrar("trimestral", cargador.cargarInformeTrimestral());

        String[] gerencias = {"Gerencia Bogotá", "Gerencia Medellín", "Gerencia Cali"};
        for (String gerencia : gerencias) {
            Informe informe = registro.crear("trimestral");
            informe.setDestinatario(gerencia);
            informe.seccion(2).setContenido("Recomendaciones para " + gerencia);
            System.out.println(informe + " -> " + informe.seccion(2));
        }
        Informe original = registro.crear("trimestral");
        System.out.println("Prototipo intacto: " + original.seccion(2));
        System.out.println("Inicializaciones costosas: " + cargador.getInicializaciones()
                + " (para " + gerencias.length + " informes)");
    }
}
