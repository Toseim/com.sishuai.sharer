package com.sishuai.sharer;

import java.io.InputStream;
import java.net.URI;

import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.provider.FileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * 
 * 用来临时测试的地方
 *
 */
public class Test extends FileStore{

	@Override
	public String[] childNames(int arg0, IProgressMonitor arg1)
			throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFileInfo fetchInfo(int arg0, IProgressMonitor arg1)
			throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFileStore getChild(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFileStore getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream openInputStream(int arg0, IProgressMonitor arg1)
			throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URI toURI() {
		// TODO Auto-generated method stub
		return null;
	}
}
