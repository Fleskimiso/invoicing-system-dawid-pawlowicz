package pl.futurecollars.invoicing.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

@Service
public class JsonService {

  private final ObjectMapper objectMapper = new ObjectMapper();

  {
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  public String toJson(Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException exception) {
      throw new RuntimeException("Failed to convert to JSON", exception);
    }
  }

  public <T> T toObject(String json, Class<T> theClass) {
    try {
      return objectMapper.readValue(json, theClass);
    } catch (JsonProcessingException exception) {
      throw new RuntimeException("Failed to parse JSON", exception);
    }
  }

}
