package patrones.factorymethod;

/** Ejecución de ejemplo del laboratorio (sección 6 de la guía). */
public final class DemoFactoryMethod {

    private DemoFactoryMethod() {
    }

    public static void main(String[] args) {
        System.out.println("--- Ejemplo de la guía ---");
        CreadorNotificacion creador = new CreadorWhatsApp();
        creador.enviarNotificacion("Su solicitud fue recibida");

        System.out.println("--- Todos los canales a través de la abstracción ---");
        SelectorCreador selector = SelectorCreador.porDefecto();
        for (Canal canal : selector.canalesDisponibles()) {
            selector.para(canal).enviarNotificacion("Pago aprobado");
        }

        System.out.println("--- Canal elegido desde configuración ---");
        Canal deConfiguracion = Canal.desdeTexto("sms");
        selector.para(deConfiguracion).enviarNotificacion("Su código es 4821");
    }
}
