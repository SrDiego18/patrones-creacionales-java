package patrones.abstractfactory;

/** Ejecución de ejemplo del patrón Abstract Factory. */
public final class DemoAbstractFactory {

    private DemoAbstractFactory() {
    }

    public static void main(String[] args) {
        for (Tema tema : Tema.values()) {
            FormularioLogin formulario = new FormularioLogin(FabricaComponentes.para(tema));
            System.out.println("--- Tema " + tema + " (consistente: " + formulario.esConsistente() + ") ---");
            System.out.println(formulario.dibujar());
        }
    }
}
