package patrones.factorymethod;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.EnumSet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Factory Method: servicio de notificaciones (laboratorio)")
class FactoryMethodTest {

    @Test
    @DisplayName("6.3 Crear y enviar una notificación por email")
    void enviaPorEmail() {
        assertEquals("Email: Hola", new CreadorEmail().enviarNotificacion("Hola"));
    }

    @Test
    @DisplayName("6.3 Crear y enviar una notificación por SMS")
    void enviaPorSms() {
        assertEquals("SMS: Código 1234", new CreadorSMS().enviarNotificacion("Código 1234"));
    }

    @Test
    @DisplayName("6.3 WhatsApp se agrega sin modificar enviarNotificacion (método final)")
    void whatsappSinModificarLaBase() throws NoSuchMethodException {
        assertEquals("WhatsApp: Recibido", new CreadorWhatsApp().enviarNotificacion("Recibido"));

        Method plantilla = CreadorNotificacion.class.getMethod("enviarNotificacion", String.class);
        assertTrue(Modifier.isFinal(plantilla.getModifiers()),
                "enviarNotificacion debe ser final: ningún canal nuevo puede modificarla");
    }

    @Test
    @DisplayName("Extra: un canal nuevo (Push) solo requiere clases nuevas")
    void pushSoloConClasesNuevas() {
        assertEquals("Push: Nueva alerta", new CreadorPush().enviarNotificacion("Nueva alerta"));
    }

    @Test
    @DisplayName("Cada creador concreto produce el producto que le corresponde")
    void cadaCreadorProduceSuProducto() {
        assertInstanceOf(NotificacionEmail.class, new CreadorEmail().crearNotificacion());
        assertInstanceOf(NotificacionSMS.class, new CreadorSMS().crearNotificacion());
        assertInstanceOf(NotificacionWhatsApp.class, new CreadorWhatsApp().crearNotificacion());
        assertInstanceOf(NotificacionPush.class, new CreadorPush().crearNotificacion());
    }

    @ParameterizedTest(name = "Mensaje inválido \"{0}\" es rechazado")
    @ValueSource(strings = {"", "   ", "\t"})
    void rechazaMensajesVacios(String mensaje) {
        assertThrows(IllegalArgumentException.class, () -> new CreadorEmail().enviarNotificacion(mensaje));
    }

    @Test
    @DisplayName("Mensaje nulo es rechazado")
    void rechazaMensajeNulo() {
        assertThrows(IllegalArgumentException.class, () -> new CreadorSMS().enviarNotificacion(null));
    }

    @Test
    @DisplayName("Los espacios sobrantes del mensaje se eliminan")
    void limpiaEspacios() {
        assertEquals("Email: Hola", new CreadorEmail().enviarNotificacion("  Hola  "));
    }

    @ParameterizedTest(name = "El selector entrega un creador para {0}")
    @EnumSource(Canal.class)
    void selectorCubreTodosLosCanales(Canal canal) {
        String enviado = SelectorCreador.porDefecto().para(canal).enviarNotificacion("x");
        assertTrue(enviado.endsWith(": x"));
    }

    @Test
    @DisplayName("El selector entrega una instancia nueva en cada llamada")
    void selectorCreaInstanciasNuevas() {
        SelectorCreador selector = SelectorCreador.porDefecto();
        assertNotSame(selector.para(Canal.EMAIL), selector.para(Canal.EMAIL));
    }

    @Test
    @DisplayName("El selector rechaza canales sin creador registrado")
    void selectorSinRegistro() {
        SelectorCreador vacio = new SelectorCreador();
        assertThrows(IllegalArgumentException.class, () -> vacio.para(Canal.SMS));
        assertEquals(EnumSet.noneOf(Canal.class), vacio.canalesDisponibles());
    }

    @Test
    @DisplayName("Se puede registrar un canal con un creador propio (extensión sin modificar)")
    void registrarCreadorPersonalizado() {
        SelectorCreador selector = new SelectorCreador()
                .registrar(Canal.EMAIL, () -> new CreadorNotificacion() {
                    @Override
                    protected Notificacion crearNotificacion() {
                        return mensaje -> "Correo corporativo: " + mensaje;
                    }
                });
        assertEquals("Correo corporativo: Hola", selector.para(Canal.EMAIL).enviarNotificacion("Hola"));
    }

    @ParameterizedTest(name = "\"{0}\" se interpreta como canal válido")
    @ValueSource(strings = {"email", "SMS", " WhatsApp ", "push"})
    void canalDesdeTexto(String texto) {
        assertTrue(EnumSet.allOf(Canal.class).contains(Canal.desdeTexto(texto)));
    }

    @Test
    @DisplayName("Un canal desconocido produce un error claro")
    void canalDesconocido() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Canal.desdeTexto("fax"));
        assertEquals("Canal no soportado: fax", e.getMessage());
    }
}
