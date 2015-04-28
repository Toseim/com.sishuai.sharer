package com.sishuai.sharer.modules;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class ClientTableLabelProvider implements ITableLabelProvider {

	@Override
	public void addListener(ILabelProviderListener arg0) {

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLabelProperty(Object arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public Image getColumnImage(Object arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object arg0, int arg1) {
		if (!(arg0 instanceof ItemInfo)) return null;
		ItemInfo itemInfo = (ItemInfo) arg0;
		switch (arg1) {
		case 1:
			return itemInfo.getOne();
		case 2:
			return itemInfo.getTwo();
		case 3:
			return itemInfo.getThree();
		case 4:
			return itemInfo.getFour();
		case 5:
			return itemInfo.getFive();
		default:
			return null;
		}
	}

}
