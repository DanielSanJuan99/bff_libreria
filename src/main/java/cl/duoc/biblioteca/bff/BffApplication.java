package cl.duoc.biblioteca.bff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class BffApplication {

    /**
     * Punto de entrada de la aplicación BFF.
     *
     * @param args argumentos de arranque en {@code String[]}.
     */
    public static void main(String[] args) {
        SpringApplication.run(BffApplication.class, args);
    }
}
