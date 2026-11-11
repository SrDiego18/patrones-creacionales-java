package patrones.abstractfactory;

/**
 * Abstract Factory: crea una familia completa de componentes compatibles.
 * Cada implementación produce únicamente componentes de su propio tema.
 */
public interface FabricaComponentes {
    Boton crearBoton();

    CuadroTexto crearCuadroTexto();

    Menu crearMenu();

    /** Fábrica correspondiente a un tema (punto único de selección). */
    static FabricaComponentes para(Tema tema) {
        if (tema == null) {
            throw new IllegalArgumentException("El tema es obligatorio");
        }
        return switch (tema) {
            case INSTITUCIONAL -> new FabricaInstitucional();
            case ALTO_CONTRASTE -> new FabricaAltoContraste();
        };
    }
}
