package module;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.inject.AbstractModule;
import com.typesafe.config.Config;
import play.Environment;

public class AmazonS3Module extends AbstractModule {

    private final Config configuration;
    private final Environment environment;

    public AmazonS3Module(Environment environment, Config configuration) {
        this.environment = environment;
        this.configuration = configuration;
    }

    @Override
    protected void configure() {
        final String uri = configuration.getString("s3.pj.uri");
        final String accessKey = configuration.getString("s3.pj.accessKey");
        final String secretKey = configuration.getString("s3.pj.secretKey");

        AmazonS3 client = AmazonS3Client.builder()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(uri, ""))
                .withClientConfiguration(new ClientConfiguration().withProtocol(Protocol.HTTPS).withSignerOverride("S3SignerType"))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .withPathStyleAccessEnabled(true)
                .build();

        bind(AmazonS3.class).toInstance(client);
    }
}



