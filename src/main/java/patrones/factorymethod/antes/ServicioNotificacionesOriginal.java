package patrones.factorymethod.antes;

/**
 * Código inicial de la guía (sección 6), conservado solo como referencia
 * para comparar con la versión refactorizada.
 *
 * <p>Problemas: el servicio decide qué clase instanciar con {@code if/else},
 * conoce todas las clases concretas y hay que modificarlo cada vez que se
 * agrega un canal (viola el principio abierto/cerrado).</p>
 */
public class ServicioNotificacionesOriginal {

    static class NotificacionEmail {
        public void enviar(String mensaje) {
            System.out.println("Email: " + mensaje);
        }
    }

    static class NotificacionSMS {
        public void enviar(String mensaje) {
            System.out.println("SMS: " + mensaje);
        }
    }

    public void enviar(String canal, String mensaje) {
        if (canal.equals("EMAIL")) {
            NotificacionEmail email = new NotificacionEmail();
            email.enviar(mensaje);
        } else if (canal.equals("SMS")) {
            NotificacionSMS sms = new NotificacionSMS();
            sms.enviar(mensaje);
        } else {
            System.out.println("Canal no soportado");
        }
    }
}
