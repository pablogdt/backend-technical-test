package com.tui.proof.ws.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class LocalTimeSerializer extends JsonSerializer<LocalTime> {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public void serialize(LocalTime localTime, JsonGenerator gen, SerializerProvider provider) throws IOException {
        String formattedDate = dateTimeFormatter.format(localTime);
        gen.writeString(formattedDate);
    }
}
