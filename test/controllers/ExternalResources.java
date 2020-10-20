package controllers;

import com.adobe.testing.s3mock.junit4.S3MockRule;
import org.junit.contrib.java.lang.system.RestoreSystemProperties;
import org.junit.rules.RunRules;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ExternalResources implements TestRule {
    private static final String PAS_ENCORE_INIT_MESSAGE = "AgiraExternalResources TestRule n'est pas encore initialisé.";

    private S3MockRule s3MockRule;
    private EmbeddedMongoServer mongoServer;
    private TemporaryFolder notificationClientMockFolder;
    private RestoreSystemProperties restoreSystemProperties;

    @Override
    public Statement apply(Statement base, Description description) {
        s3MockRule = S3MockRule.builder().withSecureConnection(false).build();
        mongoServer = EmbeddedMongoServer.startNewServer();
        notificationClientMockFolder = new TemporaryFolder();
        restoreSystemProperties = new RestoreSystemProperties();

        final List<TestRule> rules = Arrays.asList(s3MockRule, mongoServer, notificationClientMockFolder,
                restoreSystemProperties);

        return new RunRules(base, rules, description);
    }

    public S3MockRule getS3MockRule() {
        Objects.requireNonNull(s3MockRule, PAS_ENCORE_INIT_MESSAGE);
        return s3MockRule;
    }

    public EmbeddedMongoServer getMongoServer() {
        Objects.requireNonNull(mongoServer, PAS_ENCORE_INIT_MESSAGE);
        return mongoServer;
    }

    public File getNotificationClientMockFolder() {
        Objects.requireNonNull(notificationClientMockFolder, PAS_ENCORE_INIT_MESSAGE);
        return notificationClientMockFolder.getRoot();
    }
}
