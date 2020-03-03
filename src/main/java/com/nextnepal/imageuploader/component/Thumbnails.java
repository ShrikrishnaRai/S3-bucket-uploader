package com.nextnepal.imageuploader.component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Thumbnails {

    public File scaleImage(File image) throws IOException {
        int targetWidth = 150;
        int targetHeight = 150;
        BufferedImage img = ImageIO.read(image);
        BufferedImage newImage = new BufferedImage(
                img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = newImage.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = img;
        BufferedImage scratchImage = null;
        Graphics2D g2 = null;
        int w = img.getWidth();
        int h = img.getHeight();
        int prevW = w;
        int prevH = h;
        do {
            if (w > targetWidth) {
                w /= 2;
                w = Math.max(w, targetWidth);
            }

            if (h > targetHeight) {
                h /= 2;
                h = Math.max(h, targetHeight);
            }

            if (scratchImage == null) {
                scratchImage = new BufferedImage(w, h, type);
                g2 = scratchImage.createGraphics();
            }

            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(ret, 0, 0, w, h, 0, 0, prevW, prevH, null);

            prevW = w;
            prevH = h;
            ret = scratchImage;
        } while (w != targetWidth || h != targetHeight);

        g2.dispose();

        if (targetWidth != ret.getWidth() || targetHeight != ret.getHeight()) {
            scratchImage = new BufferedImage(targetWidth, targetHeight, type);
            g2 = scratchImage.createGraphics();
            g2.drawImage(ret, 0, 0, null);
            g2.dispose();
            ret = scratchImage;
        }
        File outputFile = new File("thumbnail" + image.getName());
        ImageIO.write(ret, "jpg", outputFile);
        return outputFile;

    }

}
