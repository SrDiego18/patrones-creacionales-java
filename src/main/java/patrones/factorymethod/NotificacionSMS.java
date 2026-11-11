package patrones.factorymethod;

/** Producto concreto: notificación por SMS. */
public class NotificacionSMS implements Notificacion {

    @Override
    public String enviar(String mensaje) {
        String salida = "SMS: " + mensaje;
        System.out.println(salida);
        return salida;
    }
}
