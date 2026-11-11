package patrones.abstractfactory;

import java.util.List;
import java.util.Locale;

/** Familia concreta: tema de alto contraste (accesibilidad, WCAG AAA). */
public class FabricaAltoContraste implements FabricaComponentes {

    @Override
    public Boton crearBoton() {
        return new BotonAltoContraste();
    }

    @Override
    public CuadroTexto crearCuadroTexto() {
        return new CuadroTextoAltoContraste();
    }

    @Override
    public Menu crearMenu() {
        return new MenuAltoContraste();
    }

    static final class BotonAltoContraste implements Boton {
        @Override
        public String renderizar(String texto) {
            return "[BOTÓN NEGRO/AMARILLO | " + texto.toUpperCase(Locale.ROOT) + "]";
        }

        @Override
        public Tema tema() {
            return Tema.ALTO_CONTRASTE;
        }
    }

    static final class CuadroTextoAltoContraste implements CuadroTexto {
        @Override
        public String renderizar(String valor) {
            return "[TEXTO BORDE GRUESO | " + valor + "]";
        }

        @Override
        public Tema tema() {
            return Tema.ALTO_CONTRASTE;
        }
    }

    static final class MenuAltoContraste implements Menu {
        @Override
        public String renderizar(List<String> opciones) {
            return "[MENÚ ALTO CONTRASTE | " + String.join(" / ", opciones) + "]";
        }

        @Override
        public Tema tema() {
            return Tema.ALTO_CONTRASTE;
        }
    }
}
