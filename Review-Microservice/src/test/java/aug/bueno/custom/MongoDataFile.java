package aug.bueno.custom;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that interacts with the MongoSpringExtesion that provides information about the test MongoDB
 * JSON file for this method as well as the collection name and type of objects stored in the test file
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MongoDataFile {

    // the name of the mongodb JSON test file.
    String value();

    // the class of objects stored in the mongodb test file
    Class classType();

    // the name of the mongodb collection hosting the test objects
    String collectionName();
}
