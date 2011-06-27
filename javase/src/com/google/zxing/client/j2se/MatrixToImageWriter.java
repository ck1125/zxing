/*
 * Copyright 2009 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.j2se;

import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.OutputStream;
import java.io.IOException;
import java.awt.image.BufferedImage;

/**
 * Writes a {@link BitMatrix} to {@link BufferedImage},
 * file or stream. Provided here instead of core since it depends on
 * Java SE libraries.
 *
 * @author Sean Owen
 */
public final class MatrixToImageWriter {

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    private MatrixToImageWriter() {
    }


    /**
     * Renders a {@link BitMatrix} as an image, where "false" bits are rendered
     * as white, and "true" bits are rendered as black.
     *
     */
    public static BufferedImage toBufferedImage(BitMatrix matrix, String foregroundColor, String backgroundColor) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? fgToHexString(foregroundColor) : bgToHexString(backgroundColor));
            }
        }
        return image;
    }

    private static int fgToHexString(String colorString) {
        int fgHex = BLACK;
        fgHex = colorStringToHex(colorString, fgHex);
        return fgHex;
    }

    private static int bgToHexString(String colorString) {
        int bgHex = WHITE;
        bgHex = colorStringToHex(colorString, bgHex);
        return bgHex;
    }

    private static int colorStringToHex(String colorString, int bgHex) {
        if (!colorString.isEmpty()) {
            if (colorString.length() == 6) {
                bgHex = Long.valueOf("FF" + colorString, 16).intValue();
            }
        }
        return bgHex;
    }

    /**
     * Writes a {@link BitMatrix} to a file.
     *
     * @see #toBufferedImage(BitMatrix, String,String)
     */
    public static void writeToFile(BitMatrix matrix, String format, File file, String foregroundColor, String backgroundColor)
            throws IOException {
        BufferedImage image = toBufferedImage(matrix,foregroundColor,  backgroundColor);
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of format " + format + " to " + file);
        }
    }

    /**
     * Writes a {@link BitMatrix} to a stream.
     *
     * @see #toBufferedImage(BitMatrix,String,String)
     */
    public static void writeToStream(BitMatrix matrix, String format, OutputStream stream,String foregroundColor, String backgroundColor)
            throws IOException {
        BufferedImage image = toBufferedImage(matrix,foregroundColor,  backgroundColor);
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format " + format);
        }
    }

}
