package patrones.factorymethod;

/** Producto concreto: notificación por Push. */
public class NotificacionPush implements Notificacion {

    @Override
    public String enviar(String mensaje) {
        String salida = "Push: " + mensaje;
        System.out.println(salida);
        return salida;
    }
}
