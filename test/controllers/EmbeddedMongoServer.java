package controllers;

import org.junit.rules.ExternalResource;
import org.testcontainers.containers.MongoDBContainer;

import java.net.URI;

/**
 * Cette classe nous permet de ne démarrer qu'une seule fois un serveur Mongo
 * pour toutes les méthodes de test (marqué comme @Test) dans une seule class
 * de tests. Ici on utilise le mécanisme fourni par JUnit -- c.a.d ExternalResource
 *
 * Normalement il suffit d'ajouter un champ 'static' dans la classe de tests, en
 * utilisant l'annotation @ClassRule:
 *
 * <pre>
 * class MyTest {
 *     @ClassRule
 *     public static EmbeddedMongoServer mongoServer = EmbeddedMongoServer.startNewServer();
 *
 *     @Before
 *     public void setUp() {
 *         MongoClient mongo = new MongoClient(mongoServer.getHost(), mongoServer.getPort());
 *     }
 * }
 * </pre>
 */
public final class EmbeddedMongoServer extends ExternalResource {


    //https://www.testcontainers.org/modules/databases/mongodb/

    private int port;

    private EmbeddedMongoServer(final int port) {
        this.port = port;
    }

    public static EmbeddedMongoServer startNewServer() {
        return new EmbeddedMongoServer(0);
    }

    final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.2.9");


    public URI getURI() {
        return URI.create(mongoDBContainer.getReplicaSetUrl());
    }

    @Override
    protected void before() {


        if (port != 0) {
            mongoDBContainer.withExposedPorts(port);
        }

        mongoDBContainer.start();

        if (port == 0) {
            port = mongoDBContainer.getFirstMappedPort();
        }
    }

    @Override
    protected void after() {

        if (mongoDBContainer != null) {
            mongoDBContainer.stop();
        }
    }
}
