package org.weight.jmeter.extractor;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import com.github.jaiimageio.plugins.tiff.TIFFImageWriteParam;

public class ImageIOHelper {
	
	public static int isFilter(int colorInt) {  
        Color color = new Color(colorInt);
        if ((color.getRed() > 85 && color.getRed() < 255) 
        		&& (color.getGreen() > 85 && color.getGreen() < 255) 
        		&& (color.getBlue() > 85 && color.getBlue() < 255)) {  
            return 1;  
        }  
        return 0;
    }  
  
    public static BufferedImage removeBackgroud(BufferedImage img)  
            throws Exception {  
        int width = img.getWidth();  
        int height = img.getHeight();  
        for (int x = 0; x < width; ++x) {  
            for (int y = 0; y < height; ++y) {  
                if (isFilter(img.getRGB(x, y)) == 1) {  
                    img.setRGB(x, y, Color.WHITE.getRGB());  
                }
            }  
        }  
        return img;
    }  
	
	public static File createImage(File imageFile, String imageFormat) {
		
		File tempFile = null;
		ImageInputStream iis = null;
		ImageOutputStream ios = null;
		ImageReader reader = null;
		ImageWriter writer = null;
		
		try {
			Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(imageFormat);
			reader = readers.next();
			
		    iis = ImageIO.createImageInputStream(imageFile);
			reader.setInput(iis);
			
			IIOMetadata streamMetadata = reader.getStreamMetadata();
						TIFFImageWriteParam tiffWriteParam = new TIFFImageWriteParam(Locale.CHINESE);
			tiffWriteParam.setCompressionMode(ImageWriteParam.MODE_DISABLED);
			Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("tiff");
		    writer = writers.next();
			
			BufferedImage bi = removeBackgroud(reader.read(0));
			IIOImage image = new IIOImage(bi,null,reader.getImageMetadata(0));
			tempFile = tempImageFile(imageFile);
			
		    ios = ImageIO.createImageOutputStream(tempFile);
			writer.setOutput(ios);
			writer.write(streamMetadata, image, tiffWriteParam);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(iis != null){
				try {
					iis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(ios != null){
				try {
					ios.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(writer != null){
				writer.dispose();
			}
			if(reader != null){
				reader.dispose();
			}
			
		}
		return tempFile;
	}

	private static File tempImageFile(File imageFile) {
		String path = imageFile.getPath();
		StringBuffer strB = new StringBuffer(path);
		return new File(strB.toString().replaceFirst("jpg", "tif"));
	}

}