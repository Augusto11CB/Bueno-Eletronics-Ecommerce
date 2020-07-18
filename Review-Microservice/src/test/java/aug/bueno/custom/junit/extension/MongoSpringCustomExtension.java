package aug.bueno.custom.junit.extension;

import aug.bueno.custom.MongoDataFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class MongoSpringCustomExtension implements BeforeEachCallback, AfterEachCallback {

    /**
     * Path to where our test JSON files are stored
     */
    private static Path JSON_PATH = Paths.get("src", "test", "resources", "data");

    private final ObjectMapper mapper = new ObjectMapper();


    /**
     * Called before each test executes. This callback is reponsible for importing the JSON document,
     * defined by the MongoDataFile annotation, into the embedded MongoDB, through the provided MongoTemplate.
     *
     * @param context The ExtensionContext which provides access to the test method
     */
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        context.getTestMethod().ifPresent(method -> {

            // Load test file from annotation
            MongoDataFile mongoDataFile = method.getAnnotation(MongoDataFile.class);

            // Load the MongoTemplate that can be used to import the data
            getMongoTemplate(context).ifPresent(mongoTemplate -> {
                try {
                    List objects = mapper.readValue(
                            JSON_PATH.resolve(mongoDataFile.value()).toFile(),
                            mapper.getTypeFactory().constructCollectionType(List.class, mongoDataFile.classType())
                    );

                    //Load each review into MongoDB
                    objects.forEach(mongoTemplate::save);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        });
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        context.getTestMethod().ifPresent(method -> {

            // Load test file from annotation
            MongoDataFile mongoDataFile = method.getAnnotation(MongoDataFile.class);

            // Load the MongoTemplate that can be used to import the data
            getMongoTemplate(context).ifPresent(mongoTemplate -> mongoTemplate.dropCollection(mongoDataFile.collectionName()));

        });
    }

    /**
     * Helper method that uses reflection to invoke the getMongoTemplate() method on the test instance
     *
     * @param context The ExtensionContext which provides access to the test method
     */
    private Optional<MongoTemplate> getMongoTemplate(ExtensionContext context) {

        //get class -> get class's method -> get instance -> invoke the retrieved method in the retrieved instance

        Optional<Class<?>> testClass = context.getTestClass();
        if (testClass.isPresent()) {
            // get class ->
            Class<?> clazz = testClass.get();
            try {
                // get class's method
                Method method = clazz.getMethod("getMongoTemplate", null);
                // get instance ->
                Optional<Object> testInstance = context.getTestInstance();
                if (testInstance.isPresent()) {
                    //  invoke the retrieved method in the retrieved instance
                    return Optional.of((MongoTemplate) method.invoke(testInstance.get(), null));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return Optional.empty();
    }

}
