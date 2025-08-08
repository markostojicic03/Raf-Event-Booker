package rs.raf.backend;

import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import rs.raf.backend.repository.user.MySqlUserRepository;
import rs.raf.backend.repository.user.UserRepository;
import rs.raf.backend.service.UserService;

import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
public class ApplicationConfig extends ResourceConfig {


    public ApplicationConfig() {
        packages("rs.raf.backend");

        // Dependency Injection
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(MySqlUserRepository.class).to(UserRepository.class).in(Singleton.class);
                bindAsContract(UserService.class);
            }
        });
    }
}