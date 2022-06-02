import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Resizer implements Runnable{

    public static final String TO = "reformedArts";
    public static final int IMAGE_SCALE = 2; // для примера просто уменьшим изображения вдвое
    private File[] files;

    public Resizer(File[] files) {
        this.files = files;
    }

    @Override
    public void run() {
        try {
            for (File file : files) {
                BufferedImage image = ImageIO.read(file);
                if (image == null) {
                    continue;
                }
                int newWidth = image.getWidth() / IMAGE_SCALE;
                int newHeight = image.getHeight() / IMAGE_SCALE;

                BufferedImage reformedImage = Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_EXACT, newWidth, newHeight);
                ImageIO.write(reformedImage, "jpg", new File(TO + "\\" + file.getName()));
                image.flush();
                reformedImage.flush();
            }
        } catch (Exception ex) {

        }
    }
}
