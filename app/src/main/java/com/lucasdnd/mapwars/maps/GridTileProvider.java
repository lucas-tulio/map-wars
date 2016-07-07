package com.lucasdnd.mapwars.maps;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileProvider;

/**
 * Provides the Grid on the Map so the player can measure distances with relative accuracy.
 * 
 * @author lucasdnd
 *
 */
public class GridTileProvider implements TileProvider {

	// Size of the Tile Image (should always be 256, except on high end devices, where it should be 512)
	private final int tileSize = 256;
	
	// Minimum zoom level to show the Tile
	private final int minZoomLevel = 11;
	
	// Current Latitude and Longitude. We need these values because the Height of each Grid in the Tiles
	// vary according to the position on Earth.
	private LatLng currentLatLng;
	
	// The Paint used to draw the Bitmap
	private Paint paint;
	
	/**
	 * Creates the Grid Tile Provider with basic properties
	 */
	public GridTileProvider(int strokeColor, float strokeWidth) {
		
		// Create the Paint used to draw the Bitmap
		paint = new Paint();
		paint.setColor(strokeColor);
		paint.setStrokeWidth(strokeWidth);
	}
	
	/**
	 * In this method, we're going to draw the Grid lines into a Bitmap and return it.
	 */
	@Override
	public Tile getTile(int x, int y, int zoom) {
		
		// Check if we're at the min required zoom level
		if(zoom < minZoomLevel) {
			return NO_TILE;
		}
		
		// Create our Bitmap and Canvas, where the Tile will be written on
		Bitmap bitmap = Bitmap.createBitmap(tileSize, tileSize, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas (bitmap);
		
		// Calculate the Width and Height of each Grid. Step by step:
		
		// 1. Calculate the number of Tiles at this zoom level
		int numTiles = 1 << zoom; // (just a fancy way of saying Math.pow(2, zoom), which is a fancy way of saying 2^zoom)
		
		// 2. Calculate the relationship between degrees of lat/lng and Pixels on the Tile
		double degreesPerPixel = 360.0 / (numTiles * tileSize);
		
		// 3. Calculate the grid Width and Height in Pixels.
		// In the Mercator projection, the closer you get to the poles, the larger the Grid Height will be.
		// In order to account for this variation, we need to calculate the gridHeightInThisLat every time we provide a Tile
		double gridWidthInPixels = GeometryUtil.GRID_SIZE_IN_DEGREES / degreesPerPixel;
		double gridHeightInPixels = this.getGridHeightInThisLat() / degreesPerPixel;

		// We can draw the Vertical lines at this point.
		this.drawVerticalLines(canvas, x, (float)gridWidthInPixels);
		
		// To draw the Horizontal lines, we need more information
		// First, get the bounds (in LatLng) of the Tile we're trying to draw
		LatLngBounds boundsOfTile = this.getBoundsOfTile(x, y, zoom);
		
		// Now we get the offset, in Pixels, from the Tile we're trying to draw to the Equator Line.
		// This is needed because of the difference between the Tiles' bounds (which are squares) and our Grid (whose Height
		// is distorted because of the Mercator Projection, making them rectangles)
		float offsetInPixels = this.getEquatorOffsetInPixels(boundsOfTile, gridHeightInPixels);
		
		// Finally, draw the Horizontal lines
		this.drawHorizontalLines(canvas, offsetInPixels, (float)gridHeightInPixels, boundsOfTile);
		
		// Compress the Bitmap into an Output Stream
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); // Quality is set to 100, but PNG ignores this value

		return new Tile(tileSize, tileSize, stream.toByteArray());
	}
	
	/**
	 * Draw HORIZONTAL lines
	 * 
	 * The space between each horizontal line will vary. Because of that, the initial offset of the grid will also vary.
	 * We need to calculate its exact value before we start drawing the lines.
	 * 
	 * We can do that by calculating the amount of kilometers from the Equator line until the Tile location.
	 * The remaining meters will be our offset.
	 * 
	 * @param canvas
	 * @param offsetInPixels
	 * @param gridHeightInPixels
	 * @param boundsOfTile
	 */
	private void drawHorizontalLines(Canvas canvas, float offsetInPixels, float gridHeightInPixels, LatLngBounds boundsOfTile) {
		
		float posY = 0f;
		if (boundsOfTile.southwest.latitude >= 0f) {	// Above the Equator line?
			posY = tileSize;
			posY += offsetInPixels;	// Add the offset

			while (posY >= 0) {
				if (posY <= tileSize) {
					canvas.drawLine (0, posY, (float)tileSize, posY, paint);
				}
				posY -= gridHeightInPixels;
			}

		} else {	// Below the Equator Line?
			posY -= offsetInPixels;	 // Cuts the offset out

			while (posY <= tileSize) {
				if (posY >= 0) {
					canvas.drawLine (0, posY, (float)tileSize, posY, paint);
				}
				posY += gridHeightInPixels;
			}
		}

	}
	
	/**
	 * Draw VERTICAL lines. The space between them is constant (latitude does not vary in the Mercator projection)
	 * 
	 * @param canvas
	 * @param x
	 * @param gridWidthInPixels
	 */
	private void drawVerticalLines(Canvas canvas, int x, float gridWidthInPixels) {
		
		// This first line will provide the initial offset
		float posX = (float)-((tileSize * x) % gridWidthInPixels);

		// Loop through the Tile Canvas and draw a line at every gridWidthInPixels pixels.
		while (posX <= tileSize) {
			if (posX >= 0) {
				canvas.drawLine (posX, 0, posX, (float)tileSize, paint);
			}
			posX += (float)gridWidthInPixels;
		}
	}
	
	
	/**
	 * Given the current Latitude, returns the Height of the Grid
	 * 
	 * @param latitude
	 * @return
	 */
	private double getGridHeightInThisLat() {
		return GeometryUtil.GRID_SIZE_IN_DEGREES / Math.cos(currentLatLng.latitude * Math.PI / 180.0);
	}
	
	/**
	 * Calculates the starting and ending Latitude and Longitude of a Tile
	 * 
	 * @param x
	 * @param y
	 * @param zoom
	 * @return
	 */
	private LatLngBounds getBoundsOfTile(int x, int y, int zoom) {
		
		int numTiles = 1 << zoom;
		double longitudeSpan = 360.0 / numTiles;
		double longitudeMin = -180.0 + x * longitudeSpan;

		double mercatorMax = 180.0 - (((double) y) / numTiles) * 360.0;
		double mercatorMin = 180.0 - (((double) y + 1.0) / numTiles) * 360.0;
		double latitudeMax = toLatitude(mercatorMax);
		double latitudeMin = toLatitude(mercatorMin);

		LatLngBounds bounds = new LatLngBounds(new LatLng(latitudeMin, longitudeMin), new LatLng(latitudeMax, longitudeMin + longitudeSpan));
		
		return bounds;
	}
	
	private double toLatitude(double mercator) {
		double radians = Math.atan(Math.exp(GeometryUtil.degreesToRadians(mercator)));
		return GeometryUtil.radiansToDegrees(2.0 * radians) - 90.0;
	}
	
	/**
	 * Returns how many pixels the current Tile is away from the Equator line
	 * 
	 * @return
	 */
	private float getEquatorOffsetInPixels(LatLngBounds boundsOfTile, double gridHeightInPixels) {
		
		// Calculate the distance form the Equator Line (latitude 0º) to the top left point of the grid
		// (southwest long, northeast lat). This result is given in Meters
		double distanceFromEquatorToTile = 0f;

		// Above or Below the Equator Line?
		if (boundsOfTile.southwest.latitude >= 0f) {
			
			// Above!
			distanceFromEquatorToTile = GeometryUtil.getDistanceInMeters(
				0.0,								boundsOfTile.northeast.longitude,
				boundsOfTile.southwest.latitude,	boundsOfTile.northeast.longitude);
			
		} else {
			
			// Below!
			distanceFromEquatorToTile = GeometryUtil.getDistanceInMeters(
				0.0,								boundsOfTile.southwest.longitude,
				boundsOfTile.northeast.latitude,	boundsOfTile.southwest.longitude);
		}
		
		// Get the remaining Meters and convert them into pixels
		float offsetInMeters = (float)(distanceFromEquatorToTile % 1000f) / 1000f;
		
		return (float)(gridHeightInPixels * offsetInMeters);
	}

	public LatLng getCurrentLatLng() {
		return currentLatLng;
	}

	public void setCurrentLatLng(LatLng newLatLng) {
		this.currentLatLng = newLatLng;
	}
}