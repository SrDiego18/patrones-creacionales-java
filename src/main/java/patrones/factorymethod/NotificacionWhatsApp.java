package patrones.factorymethod;

/** Producto concreto: notificación por WhatsApp. */
public class NotificacionWhatsApp implements Notificacion {

    @Override
    public String enviar(String mensaje) {
        String salida = "WhatsApp: " + mensaje;
        System.out.println(salida);
        return salida;
    }
}
