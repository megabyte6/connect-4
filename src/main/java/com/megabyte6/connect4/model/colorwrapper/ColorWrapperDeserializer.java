package com.megabyte6.connect4.model.colorwrapper;

import java.io.IOException;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import javafx.scene.paint.Color;

public class ColorWrapperDeserializer extends JsonDeserializer<Color> {

    @Override
    public Color deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        ColorWrapper colorWrapper = p.readValueAs(ColorWrapper.class);

        Color color = null;
        if (colorWrapper != null) {
            color = new Color(colorWrapper.getRed(), colorWrapper.getGreen(), colorWrapper.getBlue(),
                    colorWrapper.getOpacity());
        }

        return color;
    }

}
