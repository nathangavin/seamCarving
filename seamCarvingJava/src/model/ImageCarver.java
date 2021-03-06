package model;

import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class ImageCarver {
	
	private BufferedImage _image;
	private SeamDirection _direction;
	private int[] _seam;
	
	private Pixel[][] _imageEnergyArray;
	
	public ImageCarver(Image image, SeamDirection direction) {
		_image = SwingFXUtils.fromFXImage(image, null);
		_direction = direction;
	}
	
	public Image getImageEnergy() {
		BufferedImage imageEnergy = calculateImageEnergy(_image);
		return SwingFXUtils.toFXImage(imageEnergy, null);
	}
	
	public void calculateSeam() {
		_seam = findSeam(_imageEnergyArray, _direction);
	}
	
	public Image getColouredSeam() throws EmptySeamException {
		if (_seam == null) {
			throw new EmptySeamException();
		}
		BufferedImage colouredSeam = createColouredSeamImage(_image, _seam, _direction);
		return SwingFXUtils.toFXImage(colouredSeam, null);
	}
	
	public Image getImageWithSeamRemoved() throws EmptySeamException {
		if (_seam == null) {
			throw new EmptySeamException();
		}
		BufferedImage removedSeam = removeSeamFromImage(_image, _seam, _direction);
		return SwingFXUtils.toFXImage(removedSeam, null);
	}
	
	private BufferedImage removeSeamFromImage(BufferedImage image, int[] seam, SeamDirection direction) {
		
		if (direction == SeamDirection.VERTICAL) {
			
			int height = image.getHeight();
			int width = image.getWidth();
			
			BufferedImage removedSeam = new BufferedImage(width-1, height, BufferedImage.TYPE_INT_RGB);
			
			for (int row = 0; row < height; row++) {
				int a = 0;
				for (int column = 0; column < width; column++) {
					if (seam[row] != column) {
						removedSeam.setRGB(a, row, image.getRGB(column, row));
						a++;
					}
				}
			}
			
			return removedSeam;
			
		} else {
			
			int height = image.getHeight();
			int width = image.getWidth();
			
			BufferedImage removedSeam = new BufferedImage(width, height-1, BufferedImage.TYPE_INT_RGB);
			
			for (int column = 0; column < width; column++) {
				int a = 0;
				for (int row = 0; row < height; row++) {
					if (seam[column] != row) {
						removedSeam.setRGB(column, a, image.getRGB(column, row));
						a++;
					}
				}
			}
			
			return removedSeam;
		}
	}

	
	private BufferedImage createColouredSeamImage(BufferedImage image, int[] seam, SeamDirection direction) {
		
		BufferedImage colouredSeam = image;
		int red = (255<<24) | (255<<16) | (0<<8) | (0);
		
		if (direction == SeamDirection.VERTICAL) {
			
			for (int row = 0; row < seam.length; row++) {
				int column = seam[row];
				colouredSeam.setRGB(column, row, red);
			}
			
		} else {
			
			for (int column = 0; column < seam.length; column++) {
				int row = seam[column];
				colouredSeam.setRGB(column, row, red);
			}
		}
		
		return colouredSeam;
	}
	

	public int[] getSeam() {
		return _seam;
	}
	

	private BufferedImage calculateImageEnergy(BufferedImage image) {
		int height = image.getHeight();
		int width = image.getWidth();
		
		BufferedImage calculatedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
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
		int p = (argb<<16) | (argb<<8) | argb;
		
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
		int r1,g1,b1;
		int r2,g2,b2;
		
		r1 = (pixelOne >> 16) & 0xff;
		g1 = (pixelOne >> 8) & 0xff;
		b1 = pixelOne & 0xff;
		
		r2 = (pixelTwo >> 16) & 0xff;
		g2 = (pixelTwo >> 8) & 0xff;
		b2 = pixelTwo & 0xff;
		
		double rDiff = r2-r1;
		double gDiff = g2-g1;
		double bDiff = b2-b1;
		
		double power = 2;
		
		int rSquare = (int) Math.pow(rDiff, power);
		int gSquare = (int) Math.pow(gDiff, power);
		int bSquare = (int) Math.pow(bDiff, power);
		
		int gradient = rSquare + gSquare + bSquare;
		
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
