package model;

import java.util.LinkedList;

public class Pixel {

	private int _pixelEnergy;
	private int _pathEnergy;
	private int _rowPosition;
	private int _columnPosition;
	private LinkedList<Pixel> _pixelPath;
	
	public Pixel(int pixelEnergy, int rowPosition, int columnPosition) {
		_pixelEnergy = pixelEnergy;
		_rowPosition = rowPosition;
		_columnPosition = columnPosition;
		_pathEnergy = pixelEnergy;
		_pixelPath = new LinkedList<Pixel>();
	}
	
	public void addToPathEnergy(int energyToAdd) {
		_pathEnergy += energyToAdd;
	}
	
	public int getPathEnergy() {
		return _pathEnergy;
	}
	
	public int getRowPosition() {
		return _rowPosition;
	}
	
	public int getColumnPosition() {
		return _columnPosition;
	}
	
	public void setPixelPath(LinkedList<Pixel> pixelPath) {
		_pixelPath = pixelPath;
	}
	
	public LinkedList<Pixel> getPixelPath() {
		return _pixelPath;
	}
	
	public int getPixelEnergy() {
		return _pixelEnergy;
	}
}
