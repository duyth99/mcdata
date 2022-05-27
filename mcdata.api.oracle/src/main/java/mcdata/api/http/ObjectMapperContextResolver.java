package mcdata.api.http;

import java.text.SimpleDateFormat;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Provider
public final class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {
    private final ObjectMapper mapper = getMapper();

    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }

    private ObjectMapper getMapper() {
        ObjectMapper m = new ObjectMapper();
        m.setSerializationInclusion(Include.NON_NULL);
//        m.setSerializationInclusion(Include.NON_NULL);
        m.registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
                .configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true)
                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm a z"));
        return m;
    }
}
