package module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.typesafe.config.Config;
import play.Environment;

public class MongodbModule extends AbstractModule {

    private final Environment environment;
    private final Config configuration;


    public MongodbModule(Environment environment, Config configuration) {
        this.environment = environment;
        this.configuration = configuration;
    }

    @Override
    protected void configure() {
    }

    @Provides
    @Named("Connexion_mongo" )
    public MongoDatabase getConnexionPfeAgira() {
        final String uri = configuration.getString("mongo.metier.uri");
        final String dbName = configuration.getString("mongo.metier.dbname");

        final MongoClient mongoClient = MongoClients.create(uri);
        return  mongoClient.getDatabase(dbName);
    }

}