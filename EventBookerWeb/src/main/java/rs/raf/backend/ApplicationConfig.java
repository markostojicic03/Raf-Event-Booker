package rs.raf.backend;

import org.glassfish.jersey.server.ServerProperties;
import org.hibernate.boot.jaxb.internal.AbstractBinder;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
public class ApplicationConfig extends Application {

    public ApplicationConfig() {
//// Ukljucujemo validaciju (npr. @NotNull anotacije)
//        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
//
//        // Dependency Injection povezivanje
//        register(new AbstractBinder() {
//            @Override
//            protected void configure() {
//                // Repo -> Interface mapping
//                bind(MySqlEventRepository.class).to(EventRepository.class).in(Singleton.class);
//                bind(MySqlUserRepository.class).to(UserRepository.class).in(Singleton.class);
//
//                // Servisi kao contract
//                bindAsContract(EventService.class);
//                bindAsContract(UserService.class);
//            }
//        });
//
//        // Registruje sve resurse u ovom paketu
//        packages("rs.raf.eventbooker");

    }
}