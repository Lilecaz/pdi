package utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageLoader {
    private static Map<String, BufferedImage> cache = new HashMap<>();

    public static BufferedImage getImage(String path) {
        if (!cache.containsKey(path)) {
            try {
                File f = new File(path);
                if (f.exists()) {
                    cache.put(path, ImageIO.read(f));
                } else {
                    // Fallback si le chemin est différent
                    cache.put(path, ImageIO.read(ImageLoader.class.getResourceAsStream("/" + path)));
                }
            } catch (Exception e) {
                System.err.println("Erreur chargement image : " + path);
                return null;
            }
        }
        return cache.get(path);
    }
}