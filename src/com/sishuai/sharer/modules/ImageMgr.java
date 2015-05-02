package com.sishuai.sharer.modules;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

public class ImageMgr {
	public static final String IMAGE_FACE = "icons/dialog/face.png";
	public static final String IMAGE_BLANK = "icons/dialog/blank.png";
	public static final String IMAGE_SHOOT = "icons/dialog/shoot.png";
	
	private final HashMap<ImageDescriptor, Image> imageMap = new HashMap<ImageDescriptor, Image>();
	private static ImageMgr instance;
	
	public static ImageMgr getInstance() {
		if (instance == null) 
			instance = new ImageMgr();
		return instance;
	}
	
	public Image getImage(ImageDescriptor descriptor) {
		if (descriptor == null)
			return null;
		Image image = imageMap.get(descriptor);
		if (image == null) {
			image = descriptor.createImage();
			imageMap.put(descriptor, image);
		}
		return image;
	}
	
	public void dispose() {
		Iterator<Image> iterator = imageMap.values().iterator();
		while (iterator.hasNext())
			iterator.next().dispose();
		imageMap.clear();
	}
	
	/*public static ImageDescriptor getImageDescriptor(String path)
	{
		return Activator.getImageDescriptor(path);
	}*/
}
