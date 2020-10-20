package controllers;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import modele.People;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import play.Application;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.GET;
import static play.test.Helpers.route;

public class HomeControllerShinyTest extends WithApplication {

    /**
     * Un seul serveur Mongo et server S3 pour tous les tests dans cette class
     */
    @ClassRule
    public static ExternalResources externalResources = new ExternalResources();

    private static Injector injector;

    @Override
    protected Application provideApplication() {
        injector = GuiceInjectorBuilder.newBuilder(externalResources)
                .withWebappModules()
                .build();

        return injector.getInstance(Application.class);
    }
    
    @Before
    public void beforeEach() {
        initData();
    }

    @After
    public void afterEach() {
        DatabaseCleaner dbCleaner = DatabaseCleaner.newInstance(externalResources.getMongoServer());
        dbCleaner.clean();
    }

    private void initData() {
        MongoDatabase mongoDatabase = injector.getInstance(Key.get(MongoDatabase.class, Names.named("Connexion_mongo")));
        Document jane = new Document();
        jane.put(People.Schema.lastName, "Doe");
        jane.put(People.Schema.firstName, "Jane");
        jane.put(People.CLASS_NAME, "modele.People");
        mongoDatabase.getCollection(People.COLLECTION_NAME).insertOne(jane);

        Document john = new Document();
        john.put(People.Schema.lastName, "Doe");
        john.put(People.Schema.firstName, "John");
        john.put(People.CLASS_NAME, "modele.People");
        mongoDatabase.getCollection(People.COLLECTION_NAME).insertOne(john);
    }

    @Test
    public void testExplore() {
        Http.RequestBuilder requestWelcome = new Http.RequestBuilder()
                .method(GET)
                .uri("/explore");
        Result requestWelcomeResult = route(app, requestWelcome);
        
        assertThat(requestWelcomeResult.status()).isEqualTo(Http.Status.OK);

        MongoDatabase mongoDatabase = injector.getInstance(Key.get(MongoDatabase.class, Names.named("Connexion_mongo")));
        FindIterable<Document> documents = mongoDatabase.getCollection(People.COLLECTION_NAME).find(Filters.eq(People.Schema.lastName, "Doe"));
        assertThat(documents).hasSize(2);
    }

    @Test
    public void testIndex() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/");

        Result result = route(app, request);
        assertEquals(OK, result.status());
    }
}
