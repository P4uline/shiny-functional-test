package controllers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.junit.rules.ExternalResource;

/**
 * Cette classe nous permet de vider toutes les BD dans un serveur embedded-mongo.
 *
 * On utilisant le mécanisme de TestRule de JUnit, cette classe va renouveller pour
 * chaque test (marqué avec @Test) toutes les BD.
 *
 * L'Utilisation:
 *
 * <pre>
 * class MyTest {
 *     @ClassRule
 *     public static EmbeddedMongoServer mongoServer = EmbeddedMongoServer.startNewServer();
 *
 *     @Rule
 *     public DatabaseCleaner dbCleaner = DatabaseCleaner.newInstance(mongoServer);
 * }
 * </pre>
 */
public class DatabaseCleaner extends ExternalResource {
    private final MongoClient mongoClient;

    private DatabaseCleaner(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public static DatabaseCleaner newInstance(EmbeddedMongoServer mongoServer) {
        MongoClient mongoClient = MongoClients.create(mongoServer.getURI().toString());
        return new DatabaseCleaner(mongoClient);
    }

    @Override
    protected void after() {
        clean();
    }

    public void clean() {
        for (String dbName : mongoClient.listDatabaseNames()) {
            if (dbName.equals("admin") || dbName.equals("config") || dbName.equals("local")) {
                // Sinon on aura l'erreur
                // Command failed with error 20 (IllegalOperation): 'Dropping the 'admin' database is prohibited.'
                continue;
            }
            mongoClient.getDatabase(dbName).drop();
        }
    }
}
