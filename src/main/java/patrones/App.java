package patrones;

import patrones.abstractfactory.DemoAbstractFactory;
import patrones.builder.DemoBuilder;
import patrones.factorymethod.DemoFactoryMethod;
import patrones.prototype.DemoPrototype;
import patrones.singleton.DemoSingleton;

/** Ejecuta las demostraciones de los cinco patrones creacionales. */
public final class App {

    private App() {
    }

    public static void main(String[] args) {
        seccion("1. Factory Method (laboratorio)");
        DemoFactoryMethod.main(args);
        seccion("2. Builder (Caso A)");
        DemoBuilder.main(args);
        seccion("3. Abstract Factory (Caso B)");
        DemoAbstractFactory.main(args);
        seccion("4. Prototype (Caso C)");
        DemoPrototype.main(args);
        seccion("5. Singleton vs. inyección de dependencias (Caso D)");
        DemoSingleton.main(args);
    }

    private static void seccion(String titulo) {
        System.out.println();
        System.out.println("=== " + titulo + " ===");
    }
}
