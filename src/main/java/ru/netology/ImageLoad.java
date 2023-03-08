package ru.netology;

import java.awt.*;
import java.awt.event.*;
import javax.imageio.*;
import java.io.*;

public class ImageLoad extends Frame {
	Image img;

	public ImageLoad() {
		try {
			File imageFile = new File("fileName.jpg");
			img = ImageIO.read(imageFile);
		} catch (IOException e) {
			System.out.println("Cannot load image file.");
			System.exit(0);
		}
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
	}
}
