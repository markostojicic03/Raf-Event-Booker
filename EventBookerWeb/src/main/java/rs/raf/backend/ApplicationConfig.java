package rs.raf.backend;

import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import rs.raf.backend.repository.event.EventRepository;
import rs.raf.backend.repository.event.MySqlEventRepository;
import rs.raf.backend.repository.tag.MySqlTagRepository;
import rs.raf.backend.repository.tag.TagRepository;
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
        register(rs.raf.backend.filth.Cors.class);


        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(MySqlUserRepository.class).to(UserRepository.class).in(Singleton.class);
                bindAsContract(UserService.class);
                bind(MySqlEventRepository.class).to(EventRepository.class).in(Singleton.class);
                bind(MySqlTagRepository.class).to(TagRepository.class).in(Singleton.class);

            }
        });
    }
}