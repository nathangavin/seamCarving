package model;

public class ImageData {
	
	private int[][] _pixelEnergies;
	private int[][] _pathEnergies;
	private int[][][] _pathsForPixels;
	private SeamDirection _direction;
	private int _height;
	private int _width;
	
	
	public ImageData(int[][] pixelEnergies, SeamDirection direction) {
		_pixelEnergies = pixelEnergies;
		_direction = direction;
		
		_height = _pixelEnergies.length;
		_width = _pixelEnergies[0].length;
		
		_pathEnergies = new int[_height][_width];
		_pathsForPixels = new int[_height][_width][];
		
	}
	
	public void processImage() {
		
		for (int row = 0; row < _height; row++) {
			for (int column = 0; column < _width; column++) {
				processPixel(row, column);
			}
		}
	}

	private void processPixel(int row, int column) {
		
		if (_direction == SeamDirection.VERTICAL) {
			
			if (row == 0) {
				for (int i = 0; i < _width; i++) {
					_pathEnergies[0][i] = _pixelEnergies[0][i];
					int[] a = {i};
					_pathsForPixels[0][i] = a;
				}
			} else {
				
				int min = 0;
				
				if (column == 0) {
					min = getMin(_pathEnergies[row-1][column], _pathEnergies[row-1][column+1]);
					min++;
				} else if (column == _width - 1) {
					min = getMin(_pathEnergies[row-1][column-1], _pathEnergies[row-1][column]);
				} else {
					min = getMin(_pathEnergies[row-1][column-1], _pathEnergies[row-1][column], _pathEnergies[row-1][column+1]);
				}
				
				_pathEnergies[row][column] = _pixelEnergies[row][column] + _pathEnergies[row-1][column-1+min];
				int[] pathForChosenPixel = _pathsForPixels[row-1][column-1+min];
				int[] newPath = new int[pathForChosenPixel.length + 1];
				
				for (int i = 0; i < pathForChosenPixel.length; i++) {
					newPath[i] = pathForChosenPixel[i];
				
				}
				newPath[newPath.length - 1] = column;
				_pathsForPixels[row][column] = newPath;
			}
			
		} else {
			
			if (column == 0) {
				for (int i = 0; i < _height; i++) {
					_pathEnergies[i][0] = _pixelEnergies[i][0];
					_pathsForPixels[i][0][0] = i;
				}
			} else {
				
				int min = 0;
				
				if (row == 0) {
					min = getMin(_pathEnergies[row][column], _pathEnergies[row+1][column+1]);
					min++;
				} else if (row == _height - 1) {
					min = getMin(_pathEnergies[row-1][column-1], _pathEnergies[row][column]);
				} else {
					min = getMin(_pathEnergies[row-1][column-1], _pathEnergies[row][column], _pathEnergies[row+1][column+1]);
				}
				
				_pathEnergies[row][column] = _pixelEnergies[row][column] + _pathEnergies[row-1+min][column-1];
				int[] pathForChosenPixel = _pathsForPixels[row-1+min][column-1];
				int[] newPath = new int[pathForChosenPixel.length + 1];
				
				for (int i = 0; i < pathForChosenPixel.length; i++) {
					newPath[i] = pathForChosenPixel[i];
				}
				newPath[newPath.length - 1] = row;
				_pathsForPixels[row][column] = newPath;
			}
			
		}
		
	}
	
	private int getMin(int a, int b, int c) {
		if (a <= b && a <= c) {
			return 0;
		} else if (b <= a && b <= c) {
			return 1;
		} else {
			return 2;
		}
	}
	
	private int getMin(int a, int b) {
		if (a <= b) {
			return 0;
		} else {
			return 1;
		}
	}
	
	private int getMin(int[] array) {
		
		int min = 0;
		
		for (int i = 1; i < array.length; i++) {
			if (array[i] < array[min]) {
				min = i;
			}
		}
		
		return min;
	}
	
	public int[] findSeam() {
		
		if (_direction == SeamDirection.VERTICAL) {
			
			int finalRow = _height-1;
			
			int[] bottomRow = _pathEnergies[finalRow];
			
			int finalColumn = getMin(bottomRow);
			
			return _pathsForPixels[finalRow][finalColumn];
			
		} else {
			
			int finalColumn = _width-1;
			
			int[] rightColumn = new int[_height];
			
			for (int i = 0; i < _height; i++) {
				rightColumn[i] = _pathEnergies[i][finalColumn];
			}
			
			int finalRow = getMin(rightColumn);
			
			return _pathsForPixels[finalRow][finalColumn];
			
		}
	}
}
