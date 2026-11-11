package patrones.abstractfactory;

import java.util.List;

/** Familia concreta: tema institucional (colores de marca). */
public class FabricaInstitucional implements FabricaComponentes {

    @Override
    public Boton crearBoton() {
        return new BotonInstitucional();
    }

    @Override
    public CuadroTexto crearCuadroTexto() {
        return new CuadroTextoInstitucional();
    }

    @Override
    public Menu crearMenu() {
        return new MenuInstitucional();
    }

    static final class BotonInstitucional implements Boton {
        @Override
        public String renderizar(String texto) {
            return "[Botón azul #003A70 | " + texto + "]";
        }

        @Override
        public Tema tema() {
            return Tema.INSTITUCIONAL;
        }
    }

    static final class CuadroTextoInstitucional implements CuadroTexto {
        @Override
        public String renderizar(String valor) {
            return "[Texto borde gris | " + valor + "]";
        }

        @Override
        public Tema tema() {
            return Tema.INSTITUCIONAL;
        }
    }

    static final class MenuInstitucional implements Menu {
        @Override
        public String renderizar(List<String> opciones) {
            return "[Menú marca | " + String.join(" · ", opciones) + "]";
        }

        @Override
        public Tema tema() {
            return Tema.INSTITUCIONAL;
        }
    }
}
