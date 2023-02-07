package com.credit.oem.admin.common.utils;

import java.io.*;

import static com.credit.oem.admin.common.utils.SimpleImageInfo.ImageEnum.*;

/**
 * @author chenzj
 * @since 2018/6/6
 */
public final class SimpleImageInfo {

    public enum ImageEnum {
        GIF("image/gif", "gif"),
        JPG("image/jpeg", "jpg"),
        PNG("image/png", "png"),
        BMP("image/bmp", "bmp"),
        TIFF("image/tiff", "tiff");

        private final String mimeType;
        private final String type;


        ImageEnum(String mimeType, String type) {
            this.mimeType = mimeType;
            this.type = type;
        }


        public String getMimeType() {
            return mimeType;
        }


        public String getType() {
            return type;
        }
    }

    private int height;
    private int width;
    private ImageEnum imageType;


    public SimpleImageInfo(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        try {
            processStream(is);
        } finally {
            is.close();
        }
    }


    public SimpleImageInfo(InputStream is) throws IOException {
        processStream(is);
    }


    public SimpleImageInfo(byte[] bytes) throws IOException {
        InputStream is = new ByteArrayInputStream(bytes);
        try {
            processStream(is);
        } finally {
            is.close();
        }
    }


    private void processStream(InputStream is) throws IOException {
        int c1 = is.read();
        int c2 = is.read();
        int c3 = is.read();

        width = height = -1;

        if (c1 == 'G' && c2 == 'I' && c3 == 'F') { // GIF
            is.skip(3);
            width = readInt(is, 2, false);
            height = readInt(is, 2, false);
            imageType = GIF;
        } else if (c1 == 0xFF && c2 == 0xD8) { // JPG
            while (c3 == 255) {
                int marker = is.read();
                int len = readInt(is, 2, true);
                if (marker == 192 || marker == 193 || marker == 194) {
                    is.skip(1);
                    height = readInt(is, 2, true);
                    width = readInt(is, 2, true);
                    imageType = JPG;
                    break;
                }
                is.skip(len - 2);
                c3 = is.read();
            }
        } else if (c1 == 137 && c2 == 80 && c3 == 78) { // PNG
            is.skip(15);
            width = readInt(is, 2, true);
            is.skip(2);
            height = readInt(is, 2, true);
            imageType = PNG;
        } else if (c1 == 66 && c2 == 77) { // BMP
            is.skip(15);
            width = readInt(is, 2, false);
            is.skip(2);
            height = readInt(is, 2, false);
            imageType = BMP;
        } else {
            int c4 = is.read();
            if ((c1 == 'M' && c2 == 'M' && c3 == 0 && c4 == 42)
                    || (c1 == 'I' && c2 == 'I' && c3 == 42 && c4 == 0)) { //TIFF
                boolean bigEndian = c1 == 'M';
                int ifd = 0;
                int entries;
                ifd = readInt(is, 4, bigEndian);
                is.skip(ifd - 8);
                entries = readInt(is, 2, bigEndian);
                for (int i = 1; i <= entries; i++) {
                    int tag = readInt(is, 2, bigEndian);
                    int fieldType = readInt(is, 2, bigEndian);
                    int valOffset;
                    if ((fieldType == 3 || fieldType == 8)) {
                        valOffset = readInt(is, 2, bigEndian);
                        is.skip(2);
                    } else {
                        valOffset = readInt(is, 4, bigEndian);
                    }
                    if (tag == 256) {
                        width = valOffset;
                    } else if (tag == 257) {
                        height = valOffset;
                    }
                    if (width != -1 && height != -1) {
                        imageType = TIFF;
                        break;
                    }
                }
            }
        }
        if (imageType == null) {
            throw new IOException("Unsupported image type");
        }
    }


    private int readInt(InputStream is, int noOfBytes, boolean bigEndian) throws IOException {
        int ret = 0;
        int sv = bigEndian ? ((noOfBytes - 1) * 8) : 0;
        int cnt = bigEndian ? -8 : 8;
        for (int i = 0; i < noOfBytes; i++) {
            ret |= is.read() << sv;
            sv += cnt;
        }
        return ret;
    }


    public int getHeight() {
        return height;
    }


    public int getWidth() {
        return width;
    }


    public ImageEnum getImageType() {
        return imageType;
    }


    @Override
    public String toString() {
        return "MIME Type : " + imageType.getMimeType() + "\t Width : " + width + "\t Height : "
                + height;
    }
}
