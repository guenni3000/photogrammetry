package photogrammetry;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Main {
	
	public static int filterSize = 3, tolerance = 10;
	public static BufferedImage res; 
	
	public static void main(String[] args) throws IOException {
		
		BufferedImage img1 = ImageIO.read(new File("C:/Users/Julian/Desktop/pictures1/1_IMG.20180602_210752.jpg"));
		BufferedImage img2 = ImageIO.read(new File("C:/Users/Julian/Desktop/pictures1/1_IMG.20180602_210757.jpg"));
		
		res = reload(scale(img1));
		
		JFrame frame = new JFrame();
		frame.setVisible(true);
		frame.setSize(img1.getWidth(), img1.getHeight());
		
		Graphics g = frame.getGraphics();
		
		frame.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

				if(e.getKeyChar() == 'r') {
					try {
						res = reload(scale(img1));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else if(e.getKeyChar() == ' ') {
					
					g.drawImage(res, 0, 0, null);
					
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	public static BufferedImage extend(BufferedImage img) {
		
		BufferedImage res = new BufferedImage(img.getWidth()+filterSize-1, img.getHeight()+filterSize-1, BufferedImage.TYPE_INT_RGB);
		Graphics g = res.getGraphics();
		
		g.drawImage(img, 0, 0, null);
		
		return res;
		
	}
	
	public static BufferedImage reload(BufferedImage img) throws IOException {
		
		int width = img.getWidth();
		int height = img.getHeight();
		
		img = extend(img);
		
		BufferedImage filter = ImageIO.read(new File("C:/Users/Julian/Desktop/filter.png"));
		BufferedImage res = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		for(int x = 0; x < width; x++) {
			
			for(int y = 0; y < height; y++) {
				
				int r = 0;
				int g = 0;
				int b = 0;
				
				for(int x1 = 0; x1 < filterSize; x1++) {
					
					for(int y1 = 0; y1 < filterSize; y1++) {
						
						Color colFilter = new Color(filter.getRGB(x1, y1));
						Color colImg = new Color(img.getRGB(x+x1, y+y1));
						
						if(Math.abs(colFilter.getRed()-colImg.getRed()) < tolerance) r += 255/9;
						if(Math.abs(colFilter.getGreen()-colImg.getGreen()) < tolerance) g += 255/9;
						if(Math.abs(colFilter.getBlue()-colImg.getBlue()) < tolerance) b += 255/9;
						
					}
					
				}
				
				res.setRGB(x, y, (new Color(r, g, b)).getRGB());
				
			}
			
		}
		
		return res;
		
	}
	
	public static BufferedImage scale(BufferedImage source){
		
		double fact = 1080.0/source.getHeight();
		
		BufferedImage result = new BufferedImage((int)(fact*source.getWidth()), 1080, BufferedImage.TYPE_INT_RGB);
		Graphics g = result.getGraphics();
		
		for(int x = 0; x < result.getWidth(); x++){
			
			for(int y = 0; y < 1080; y++){
				
				Color c = new Color(source.getRGB((int)(x/fact), (int)(y/fact)));
				g.setColor(c);
				
				g.fillRect(x, y, 1, 1);
			}
			
		}
		
		return result;
		
	}

}
