package agents.util;

import com.google.genai.types.Blob;
import com.google.genai.types.Part;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class TerminalImageRenderer {

    private static final int DEFAULT_DOWNSAMPLE_FACTOR = 10;

    public static void printImagePart(Part part) {
        printImage(partToImage(part));
    }

    /**
     * Renders a downsized version of the given image to the terminal.
     * @param image The BufferedImage to print.
     */
    public static void printImage(BufferedImage image) {
        if (image != null) {
            renderHalfCell(downsize(image));
        } else {
            System.err.println("No inline image data found.");
        }
    }

    public static void saveImageToFile(BufferedImage image) {
        try {
            boolean written = ImageIO.write(image, "png", new File("output.png"));
            if (!written) {
                System.err.println("Failed to write image to file.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage partToImage(Part part) {
        return part.inlineData()
            .flatMap(Blob::data)
            .map(bytes -> {
                try {
                    return ImageIO.read(new ByteArrayInputStream(bytes));
                } catch (IOException e) {
                    throw new RuntimeException("Failed to read image from part data", e);
                }
            })
            .orElse(null);
    }

    @NotNull
    private static BufferedImage downsize(BufferedImage image) {
        return downsize(image, DEFAULT_DOWNSAMPLE_FACTOR);
    }

    /**
     * Downsizes an image by averaging pixel blocks.
     *
     * @param originalImage The image to downsize.
     * @param factor        The factor by which to downsize (e.g., 2 for 2x2 blocks).
     * @return A new, downsized BufferedImage.
     */
    public static BufferedImage downsize(BufferedImage originalImage, int factor) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        int newWidth = originalWidth / factor;
        int newHeight = originalHeight / factor;

        BufferedImage newImage = new BufferedImage(newWidth, newHeight, originalImage.getType());

        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < newWidth; x++) {
                long totalAlpha = 0;
                long totalRed = 0;
                long totalGreen = 0;
                long totalBlue = 0;
                int pixelCount = 0;

                // Loop over the block in the original image
                for (int blockY = 0; blockY < factor; blockY++) {
                    for (int blockX = 0; blockX < factor; blockX++) {
                        int currentX = x * factor + blockX;
                        int currentY = y * factor + blockY;

                        if (currentX < originalWidth && currentY < originalHeight) {
                            int rgb = originalImage.getRGB(currentX, currentY);
                            totalAlpha += (rgb >> 24) & 0xff;
                            totalRed += (rgb >> 16) & 0xff;
                            totalGreen += (rgb >> 8) & 0xff;
                            totalBlue += rgb & 0xff;
                            pixelCount++;
                        }
                    }
                }

                if (pixelCount > 0) {
                    int avgAlpha = (int) (totalAlpha / pixelCount);
                    int avgRed = (int) (totalRed / pixelCount);
                    int avgGreen = (int) (totalGreen / pixelCount);
                    int avgBlue = (int) (totalBlue / pixelCount);

                    int avgRgb = (avgAlpha << 24) | (avgRed << 16) | (avgGreen << 8) | avgBlue;
                    newImage.setRGB(x, y, avgRgb);
                }
            }
        }

        return newImage;
    }

    /**
     * Renders an image using half-height cells for better vertical resolution.
     * It uses the '▄' character, setting the foreground for the lower pixel
     * and the background for the upper pixel.
     *
     * @param image The BufferedImage to render.
     */
    private static void renderHalfCell(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        // Iterate through the image height by steps of 2
        for (int y = 0; y < height; y += 2) {
            StringBuilder lineBuilder = new StringBuilder();
            for (int x = 0; x < width; x++) {
                // Upper pixel
                int upperRgb = image.getRGB(x, y);
                int upperAlpha = (upperRgb >> 24) & 0xff;
                int upperRed = (upperRgb >> 16) & 0xff;
                int upperGreen = (upperRgb >> 8) & 0xff;
                int upperBlue = upperRgb & 0xff;

                // Lower pixel (if it exists)
                int lowerRed = 0, lowerGreen = 0, lowerBlue = 0;
                boolean lowerPixelExists = (y + 1) < height;
                if (lowerPixelExists) {
                    int lowerRgb = image.getRGB(x, y + 1);
                    lowerRed = (lowerRgb >> 16) & 0xff;
                    lowerGreen = (lowerRgb >> 8) & 0xff;
                    lowerBlue = lowerRgb & 0xff;
                }

                if (upperAlpha > 0) {
                    // Set background for the upper pixel
                    lineBuilder.append(String.format("\u001b[48;2;%d;%d;%dm", upperRed, upperGreen, upperBlue));
                } else {
                    lineBuilder.append("\u001b[0m"); // Reset if transparent
                }

                if (lowerPixelExists) {
                    // Set foreground for the lower pixel
                    lineBuilder.append(String.format("\u001b[38;2;%d;%d;%dm", lowerRed, lowerGreen, lowerBlue));
                }

                lineBuilder.append("▄"); // Lower half block character
            }
            lineBuilder.append("\u001b[0m"); // Reset style at the end of the line
            System.out.println(lineBuilder.toString());
        }
    }
}
