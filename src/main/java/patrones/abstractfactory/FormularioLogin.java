package patrones.abstractfactory;

import java.util.List;
import java.util.Objects;

/**
 * Cliente del patrón: solo conoce la fábrica abstracta y los productos
 * abstractos. No sabe qué tema está usando.
 */
public class FormularioLogin {

    private final Menu menu;
    private final CuadroTexto usuario;
    private final Boton ingresar;

    public FormularioLogin(FabricaComponentes fabrica) {
        Objects.requireNonNull(fabrica, "La fábrica es obligatoria");
        this.menu = fabrica.crearMenu();
        this.usuario = fabrica.crearCuadroTexto();
        this.ingresar = fabrica.crearBoton();
    }

    public String dibujar() {
        return String.join(System.lineSeparator(),
                menu.renderizar(List.of("Inicio", "Ayuda")),
                usuario.renderizar("usuario@correo.com"),
                ingresar.renderizar("Ingresar"));
    }

    /** true si los tres componentes pertenecen al mismo tema. */
    public boolean esConsistente() {
        return menu.tema() == usuario.tema() && usuario.tema() == ingresar.tema();
    }

    public Tema tema() {
        return menu.tema();
    }
}
