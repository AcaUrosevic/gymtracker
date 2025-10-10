package rs.ac.fon.gymtracker.infrastructure.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.nio.file.Path;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * Jednostavan JSON eksport (pretty-print) za bilo koji Java objekat.
 */
public final class JsonExporter {
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .findAndRegisterModules()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private JsonExporter() {}

    /** Upisuje dati objekat u JSON fajl na prosleđenu putanju. */
    public static void write(Path file, Object value) throws IOException {
        MAPPER.writeValue(file.toFile(), value);
    }
}
