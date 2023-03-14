package com.megabyte6.connect4.model.colorwrapper;

import javafx.scene.paint.Color;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColorWrapper {

    private double red;
    private double green;
    private double blue;
    private double opacity;

    public static ColorWrapper of(Color color) {
        return new ColorWrapper(color.getRed(), color.getGreen(), color.getBlue(), color.getOpacity());
    }

    public Color toColor() {
        return new Color(red, green, blue, opacity);
    }

}
