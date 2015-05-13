package com.sishuai.sharer.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;

import com.sishuai.sharer.util.Logging;
/**
 * 打开插件的主视图
 * @author 四帅
 *
 */
public class OpenView implements IWorkbenchWindowActionDelegate {
	//用来打开视图的action
	private IWorkbenchWindow window;
	public static boolean isOpen = false;

	@Override
	public void run(IAction arg0) {
		if (isOpen) return;
		isOpen = true;
		// TODO Auto-generated method stub
		if (window == null) return;
		IWorkbenchPage page = window.getActivePage();
		if (page == null) return;
		
		try {
			Logging.getLogger().setFileName("OpenView");
			Logging.info("打开主视图");
			page.showView("com.sishuai.sharer.views.ClientView");
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			MessageDialog.openError(window.getShell(), "Error", "There is Something Wrong!");
		}
//		IPath path = new Path("/run/media/tose/GMW/workspace/Test/src/DialogBox2.java");
//		try {
//			IDE.openEditorOnFileStore(page,new Test().getFileStore(path));
//		} catch (PartInitException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("Test");
//		IFile file = project.getFile(new Path("/FindIP.java"));
//		try {
//			IDE.openEditor(page, file);
//		} catch (PartInitException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		}
	}
	
//	public void showMethod(IMember member) {
//		try {
//			ICompilationUnit icu = member.getCompilationUnit();
//			IEditorPart javaEditor = JavaUI.openInEditor(icu);
//			JavaUI.revealInEditor(javaEditor, (IJavaElement)member);
//		} catch (PartInitException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JavaModelException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		System.out.println("openview is dispose");
	}

	@Override
	public void init(IWorkbenchWindow arg0) {
		// TODO Auto-generated method stub
		this.window = arg0;
	}
	
}
