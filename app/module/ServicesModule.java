package module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import play.Environment;
import play.libs.akka.AkkaGuiceSupport;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;

/**
 * Module de creation des clients des services spécifiques Darva
 */
public class ServicesModule extends AbstractModule implements AkkaGuiceSupport {

    private final Config configuration;
    private final Environment environment;

    @Inject
    public ServicesModule(Environment environment, Config configuration) {
        this.environment = environment;
        this.configuration = configuration;
    }

    @Override
    public void configure() {
    }
}
