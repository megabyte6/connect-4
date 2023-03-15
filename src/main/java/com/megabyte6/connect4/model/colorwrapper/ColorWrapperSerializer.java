package com.megabyte6.connect4.model.colorwrapper;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import javafx.scene.paint.Color;

public class ColorWrapperSerializer extends JsonSerializer<Color> {

    @Override
    public void serialize(Color value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeObject(ColorWrapper.of(value));
    }

}
