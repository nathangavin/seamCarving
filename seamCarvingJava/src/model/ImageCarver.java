package model;

import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class ImageCarver {
	
	private BufferedImage _image;
	private BufferedImage _imageEnergy;
	private BufferedImage _colouredSeamImage;
	private BufferedImage _imageWithSeamRemoved;
	private SeamDirection _direction;
	private int[] _seam;
	
	private Pixel[][] _imageEnergyArray;
	
	public ImageCarver(Image image, SeamDirection direction) {
		_image = SwingFXUtils.fromFXImage(image, null);
		_direction = direction;
		carveSeam();
	}
	
	public Image getImageEnergy() {
		return SwingFXUtils.toFXImage(_imageEnergy, null);
	}
	
	public Image getColouredSeam() {
		return SwingFXUtils.toFXImage(_colouredSeamImage, null);
	}
	
	public Image getImageWithSeamRemoved() {
		return SwingFXUtils.toFXImage(_imageWithSeamRemoved, null);
	}
	
	private void carveSeam() {
		// TODO: finish algorithm
		_imageEnergy = calculateImageEnergy(_image);
		_seam = findSeam(_imageEnergyArray, _direction);
		
	}
	
	public int[] getSeam() {
		return _seam;
	}
	

	private BufferedImage calculateImageEnergy(BufferedImage image) {
		int height = image.getHeight();
		int width = image.getWidth();
		
		BufferedImage calculatedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Pixel[][] pixelArray = new Pixel[height][];
		
		for (int i = 0; i < height; i++) {
			Pixel[] row = new Pixel[width];
			for (int j = 0; j < width; j++) {
				int rawPixelEnergy = calculatePixelEnergy(image, i, j);
				row[j] = new Pixel(rawPixelEnergy, i, j);
				int pixelEnergy = formatEnergy(rawPixelEnergy); //row[j] == energyArray[i][j]
				calculatedImage.setRGB(j, i, pixelEnergy);
			}
			pixelArray[i] = row;
		}
		
		_imageEnergyArray = pixelArray;
		
		return calculatedImage;
	}
	
	private int formatEnergy(int rawEnergyValue) {
		// energy lower bound = 0
		// energy upper bound = (255^4)*6 = 25369503750
		double a = (double) rawEnergyValue;
		
		double upperBound = (Math.pow(255.0, 2)) * 8;
		// convert to value between 0 and 255
		double argb_d = (a/upperBound)*255;
		
		int argb = (int) argb_d;
		// convert to pixel representative int
		int p = (255<<24) | (argb<<16) | (argb<<8) | argb;
		
		return p;
	}

	private int calculatePixelEnergy(BufferedImage image, int rowPosition, int columnPosition) {
		int upPixel;
		int downPixel;
		int leftPixel;
		int rightPixel;
		
		int width = image.getWidth();
		int height = image.getHeight();
		
		if (rowPosition <= 0) {
			
			upPixel = image.getRGB(columnPosition, height-1);
			downPixel = image.getRGB(columnPosition, rowPosition+1);
		
		} else if (rowPosition >= height-1) {
			
			upPixel = image.getRGB(columnPosition, rowPosition-1);
			downPixel = image.getRGB(columnPosition, 0);
		
		} else {
			
			upPixel = image.getRGB(columnPosition, rowPosition-1);
			downPixel = image.getRGB(columnPosition, rowPosition+1);
		}
		
		
		if (columnPosition <= 0) {
			
			leftPixel = image.getRGB(width-1, rowPosition);
			rightPixel = image.getRGB(columnPosition+1, rowPosition);
		
		} else if (columnPosition >= width-1) {
			
			leftPixel = image.getRGB(columnPosition-1, rowPosition);
			rightPixel = image.getRGB(0, rowPosition);
		
		} else {
			
			leftPixel = image.getRGB(columnPosition-1, rowPosition);
			rightPixel = image.getRGB(columnPosition+1, rowPosition);
		
		}
		
		int upDownGradient;
		int leftRightGradient;
		
		if (upPixel == downPixel) {
			upDownGradient = 0;
		} else {
			upDownGradient = calculatePixelGradient(upPixel, downPixel);
		}
		
		if (leftPixel == rightPixel) {
			leftRightGradient = 0;
		} else {
			leftRightGradient = calculatePixelGradient(leftPixel, rightPixel);
		}
		
		int pixelEnergy = upDownGradient + leftRightGradient;
		
		return pixelEnergy;
		
	}

	private int calculatePixelGradient(int pixelOne, int pixelTwo) {
		int a1,r1,g1,b1;
		int a2,r2,g2,b2;
		
		a1 = (pixelOne >> 24) & 0xff;
		r1 = (pixelOne >> 16) & 0xff;
		g1 = (pixelOne >> 8) & 0xff;
		b1 = pixelOne & 0xff;
		
		a2 = (pixelTwo >> 24) & 0xff;
		r2 = (pixelTwo >> 16) & 0xff;
		g2 = (pixelTwo >> 8) & 0xff;
		b2 = pixelTwo & 0xff;
		
		double aDiff = a2-a1;
		double rDiff = r2-r1;
		double gDiff = g2-g1;
		double bDiff = b2-b1;
		
		double power = 2;
		
		int aSquare = (int) Math.pow(aDiff, power);
		int rSquare = (int) Math.pow(rDiff, power);
		int gSquare = (int) Math.pow(gDiff, power);
		int bSquare = (int) Math.pow(bDiff, power);
		
		int gradient = aSquare + rSquare + gSquare + bSquare;
		
		return gradient;
	}
	
	private int[] findSeam(Pixel[][] imageEnergyArray, SeamDirection direction) {
		
		int height = imageEnergyArray.length;
		int width = imageEnergyArray[0].length;
		
		int[][] energyArray = new int[height][width];
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				energyArray[i][j] = imageEnergyArray[i][j].getPixelEnergy();
			}
		}
		
		ImageData image = new ImageData(energyArray, direction);
		image.processImage();
		int[] seam = image.findSeam();
		
		return seam;
	}
	
}
