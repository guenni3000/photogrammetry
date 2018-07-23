package photogrammetry;

public class Vector {

	public int x, y, z;
	
	public Vector(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double getYrot() {
		
		return Math.atan((z*1.0)/x);
		
	}
	
	public double getZrot() {
		
		return Math.atan((y*1.0)/x);
		
	}

}
