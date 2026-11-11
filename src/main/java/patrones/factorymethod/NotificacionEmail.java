package patrones.factorymethod;

/** Producto concreto: notificación por Email. */
public class NotificacionEmail implements Notificacion {

    @Override
    public String enviar(String mensaje) {
        String salida = "Email: " + mensaje;
        System.out.println(salida);
        return salida;
    }
}
