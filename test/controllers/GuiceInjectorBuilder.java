package controllers;

import com.google.inject.Injector;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import module.AmazonS3Module;
import module.MongodbModule;
import play.ApplicationLoader.Context;
import play.Environment;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceApplicationLoader;

import java.util.*;
import java.util.function.BiFunction;

public class GuiceInjectorBuilder {
    private final Environment environment;
    private Config config;
    private final GuiceApplicationBuilder underlying;
    private final List<BiFunction<Environment, Config, List<? extends com.google.inject.Module>>> modulesFnList;

    public GuiceInjectorBuilder(
            final Environment environment,
            final Config config
    ) {
        this.environment = environment;
        this.config = config;
        this.underlying = new GuiceApplicationLoader().builder(new Context(environment));
        this.modulesFnList = new ArrayList<>();
    }

    public GuiceInjectorBuilder withAdditionalConfiguration(final Config additionalConfiguration) {
        config = additionalConfiguration.withFallback(config);
        return this;
    }

    public GuiceInjectorBuilder withAdditionalModules(
            BiFunction<Environment, Config, List<? extends com.google.inject.Module>> additionalModulesFn
    ) {
        modulesFnList.add(additionalModulesFn);
        return this;
    }

    public GuiceInjectorBuilder withWebappModules() {
        return withAdditionalModules((env, conf) ->
            Arrays.asList(
                new AmazonS3Module(env, conf),
                new MongodbModule(env, conf)
            )
        );
    }

    

    public Injector build() {

        return underlying
                .overrides(modulesFnList
                        .stream()
                        .flatMap(modulesFn -> modulesFn.apply(environment, config).stream()).toArray(com.google.inject.Module[]::new))
                .injector().instanceOf(Injector.class);
    }

    public static GuiceInjectorBuilder newBuilder(final ExternalResources agiraExternalResources) {
        // Pour Alpakka S3 le seul moyen de changer la configuration est d'utiliser System.setProperty
        System.setProperty("alpakka.s3.proxy.port", String.valueOf(agiraExternalResources.getS3MockRule().getHttpPort()));

        // Pour les autres on peut utiliser 'config.withValue' mais pour l'uniformité on utilise aussi System.setProperty
        System.setProperty("service.notification.mockroot", agiraExternalResources.getNotificationClientMockFolder().getPath());
        System.setProperty("s3.pj.uri", agiraExternalResources.getS3MockRule().getServiceEndpoint());
        System.setProperty("mongo.metier.uri", agiraExternalResources.getMongoServer().getURI().toString());

        // Invalidate Config cache au cas où
        ConfigFactory.invalidateCaches();

        // On va fusionner les System Properties ci-dessus avec les clés dans le fichier de conf
        final Config config = ConfigFactory.load("application.conf");

        Environment environment = Environment.simple();

        return new GuiceInjectorBuilder(environment, config);
    }
}
