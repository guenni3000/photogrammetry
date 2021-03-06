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
	
	public static int filterSize = 3, tolerance = 10, scale = 0, ind = 0;
	public static BufferedImage res, filter;
	public static BufferedImage[] imgs = new BufferedImage[2];
	
	public static void main(String[] args) throws IOException {
		
		BufferedImage img1 = scale(ImageIO.read(new File("C:/Users/Julian/Desktop/pic1.jpg")), 1080, false);
		BufferedImage img2 = scale(ImageIO.read(new File("C:/Users/Julian/Desktop/pic2.jpg")), 1080, false);
		
		imgs[0] = img1;
		imgs[1] = img2;
		
		res = imgs[ind];
		
		JFrame frame = new JFrame();
		frame.setVisible(true);
		frame.setSize(1920, 1080);
		
		Graphics g = frame.getGraphics();
		
		frame.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

				if(e.getKeyChar() == 'r') {
					try {
						res = reload(true);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else if(e.getKeyChar() == ' ') {
					
					g.drawImage(res, 0, 0, null);
					g.drawImage(scale(filter, 100, false), res.getWidth()+50, 50, null);
					
				} else if(e.getKeyChar() == '+') {
					
					if(scale < 0) scale++;
					res = scale(imgs[ind], (int)(imgs[ind].getHeight()*Math.pow(1.1, scale)), true);
					g.drawImage(res, 0, 0, null);
					
				} else if(e.getKeyChar() == '-') {
					
					scale--;
					res = scale(imgs[ind], (int)(imgs[ind].getHeight()*Math.pow(1.1, scale)), true);
					g.setColor(Color.WHITE);
					g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
					g.drawImage(res, 0, 0, null);
					
				} else if(e.getKeyChar() == '.') {
					
					ind++;
					ind %= 2;
					res = scale(imgs[ind], (int)(imgs[ind].getHeight()*Math.pow(1.1, scale)), true);
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
	
	public static BufferedImage getSubImage(BufferedImage img) {
		
		int x = (int)(Math.random()*(img.getWidth()-filterSize));
		int y = (int)(Math.random()*(img.getHeight()-filterSize));
		
		return img.getSubimage(x, y, filterSize, filterSize);
		
	}
	
	public static BufferedImage extend(BufferedImage img) {
		
		BufferedImage res = new BufferedImage(img.getWidth()+filterSize-1, img.getHeight()+filterSize-1, BufferedImage.TYPE_INT_RGB);
		Graphics g = res.getGraphics();
		
		g.drawImage(img, 0, 0, null);
		
		return res;
		
	}
	
	public static BufferedImage reload(boolean isSubFilter) throws IOException {
		
		BufferedImage img1 = scale(imgs[ind], (int)(imgs[ind].getHeight()*Math.pow(1.1, scale)), false);
		BufferedImage img2 = scale(imgs[ind==1?0:1], (int)(imgs[ind].getHeight()*Math.pow(1.1, scale)), false);
		
		int width = img1.getWidth();
		int height = img1.getHeight();
		
		img1 = extend(img1);
		
		if(!isSubFilter) filter = ImageIO.read(new File("C:/Users/Julian/Desktop/filter.png"));
		else filter = getSubImage(img2);
		BufferedImage res = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		for(int x = 0; x < width; x++) {
			
			for(int y = 0; y < height; y++) {
				
				int r = 0;
				int g = 0;
				int b = 0;
				
				for(int x1 = 0; x1 < filterSize; x1++) {
					
					for(int y1 = 0; y1 < filterSize; y1++) {
						
						Color colFilter = new Color(filter.getRGB(x1, y1));
						Color colImg = new Color(img1.getRGB(x+x1, y+y1));
						
						if(Math.abs(colFilter.getRed()-colImg.getRed()) < tolerance) r += 255/9;
						if(Math.abs(colFilter.getGreen()-colImg.getGreen()) < tolerance) g += 255/9;
						if(Math.abs(colFilter.getBlue()-colImg.getBlue()) < tolerance) b += 255/9;
						
					}
					
				}
				
				res.setRGB(x, y, (new Color(r, g, b)).getRGB());
				
			}
			
		}
		
		return scale(res, 1080, false);
		
	}
	
	public static BufferedImage scale(BufferedImage source, int height, boolean keepDisplaySize){
		
		if(height <= 0) height = 1;
		double fact = (1.0*height)/source.getHeight();
		int width = (int)(fact*source.getWidth());
		
		BufferedImage result;
		
		if(!keepDisplaySize) result = new BufferedImage(width<1?1:width, height, BufferedImage.TYPE_INT_RGB);
		else result = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		Graphics g = result.getGraphics();
		
		for(int x = 0; x < width; x++){
			
			for(int y = 0; y < height; y++){
				
				Color c = new Color(source.getRGB((int)(x/fact), (int)(y/fact)));
				g.setColor(c);
				
				if(!keepDisplaySize) g.fillRect(x, y, 1, 1);
				else g.fillRect((int)(x/fact), (int)(y/fact), (int)(1/fact)+1, (int)(1/fact)+1);
			}
			
		}
		
		return result;
		
	}

}
