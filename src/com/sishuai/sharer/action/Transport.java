package com.sishuai.sharer.action;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.nio.channels.FileChannel;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Scanner;

//import org.eclipse.core.resources.ICommand;
//import org.eclipse.core.resources.IFolder;
//import org.eclipse.core.resources.IProject;
//import org.eclipse.core.resources.IProjectDescription;
//import org.eclipse.core.resources.IResource;
//import org.eclipse.core.resources.IWorkspace;
//import org.eclipse.core.resources.IWorkspaceRoot;
//import org.eclipse.core.resources.ResourcesPlugin;
//import org.eclipse.core.runtime.CoreException;
//import org.eclipse.core.runtime.IPath;
//import org.eclipse.core.runtime.NullProgressMonitor;
//import org.eclipse.core.runtime.Path;
//import org.eclipse.core.runtime.Platform;
//import org.eclipse.core.runtime.SubProgressMonitor;
//import org.eclipse.jdt.core.IClasspathEntry;
//import org.eclipse.jdt.core.IJavaProject;
//import org.eclipse.jdt.core.IPackageFragment;
//import org.eclipse.jdt.core.IPackageFragmentRoot;
//import org.eclipse.jdt.core.JavaCore;
//import org.eclipse.jdt.ui.PreferenceConstants;
//import org.eclipse.jface.action.Action;
//import org.eclipse.jface.action.ActionContributionItem;
//import org.eclipse.jface.action.IAction;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Control;
//import org.eclipse.swt.widgets.Display;
//import org.eclipse.swt.widgets.Shell;
//
public class Transport {
//	public static boolean flag = false;
//	public static Display display = Display.getDefault();
//	public static final Shell shell = new Shell(display);
//	public static ServerSocket ss;
//	public static ArrayList<Socket> sub = new ArrayList<Socket>();
//	public static Thread only;
//
//    public void run() {
//        createProject();
//    }
//	public boolean createProject() {
//		IProject Test = ResourcesPlugin.getWorkspace().getRoot()
//				.getProject("Share");
//			if (Test.exists()) {
//			System.out.println("Project  exits .");
//			File temp = new File(Platform.getInstanceLocation().getURL()
//					.getPath()+"Share/Share/src/"+this.getFileName().substring(this.getFileName().lastIndexOf("\\")+1, this.getFileName().length()));
//System.out.println(temp.toString());
//			if(temp.exists()){
//				return false;
//			}else{
//				copy(this.getFileName() , Platform.getInstanceLocation().getURL().getPath());	
//			}
//			return true;
//		} else {
//			// create the project with name
//			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
//			IProject project = root.getProject();
//			IWorkspace workspace = root.getWorkspace();
//			IPath path = new Path(Platform.getInstanceLocation().getURL()
//					.getPath());
//			IProjectDescription description = workspace
//					.newProjectDescription("Share");
//			description.setLocation(path);
//
////			String[] javaNature = description.getNatureIds();
////			String[] newNature = new String[javaNature.length + 1];
////			System.arraycopy(javaNature, 0, newNature, 0, javaNature.length);
////			newNature[newNature.length - 1] = "org.eclipse.jdt.core.java.core.javanature";
//
//			// create the project
//			try {
//				NullProgressMonitor monitor = new NullProgressMonitor();
//				project.create(description, monitor);
//				project.open(IResource.BACKGROUND_REFRESH,
//						new SubProgressMonitor(monitor, 1000));
//
//			} catch (Exception e) {
//				System.out.println("Create the project failed , return false");
//				e.printStackTrace();
//				return false;
//			}
//
//			IJavaProject javaProject = JavaCore.create(project);
//
//			try {
////				IClasspathEntry[] jre = PreferenceConstants
////						.getDefaultdJRELibrary();
//				IClasspathEntry[] old = javaProject.getRawClasspath();
//				List list = new ArrayList();
//				list.addAll(Arrays.asList(jre));
//				list.addAll(Arrays.asList(old));
//				javaProject.setRawClasspath((IClasspathEntry[]) list
//						.toArray(new IClasspathEntry[list.size()]), null);
//			} catch (Exception e) {
//				System.out.println("Error in the IClasspathEntry[] , return false");
//				return false;
//			}
//
//			// create the floder
//			try {
//				IFolder bin = javaProject.getProject().getFolder("bin");
//				javaProject.setOutputLocation(bin.getFullPath(), null);
//				bin.create(true, true, null);
//			} catch (Exception e) {
//				System.out.println("Error int the create the folder bin . ");
//				return false;
//			}
//			try {
//				IProjectDescription des = javaProject.getProject()
//						.getDescription();
//				ICommand command = des.newCommand();
//				command.setBuilderName("Share");
////				des.setBuildSpec(new ICommand[] { command });
////				des.setNatureIds(new String[] { "org.eclipse.jdt.core.javanature" });
//			} catch (CoreException e) {
//				System.out
//						.println("Error int the description . Return false . ");
//				e.printStackTrace();
//				return false;
//			}
//
//			// create the floder bin .
//			try {
//				IFolder src = javaProject.getProject().getFolder("src");
//				src.create(true, true, null);
//				IClasspathEntry srcClasspath = JavaCore.newSourceEntry(src
//						.getFullPath());
//				IClasspathEntry[] oldsrcpath = javaProject.readRawClasspath();
//
//				List list = new ArrayList();
//				list.addAll(Arrays.asList(oldsrcpath));
//				list.add(oldsrcpath);
//
//				IClasspathEntry temp = JavaCore
//						.newSourceEntry(new Path("Share"));
//				if (list.contains(temp)) {
//					list.remove(temp);
//				}
//
//			} catch (Exception e) {
//				System.out.println("Error in creating src . Retrurn false");
//				e.printStackTrace();
//				return false;
//			}
//			// create the package .
//			IPackageFragmentRoot pack = null;
//			try {
//				pack = javaProject.findPackageFragmentRoot(new Path(
//						"/Share/src"));
//
//			} catch (Exception e) {
//				System.out.println("Error in creating package . Retrurn false");
//				e.printStackTrace();
//				return false;
//			}
//			IPackageFragment packwithoutroot = null;
//			try {
//				packwithoutroot = pack.createPackageFragment(getPackageName(),
//						true, null);
//				packwithoutroot.createCompilationUnit("Default", getCode(),
//						true, new NullProgressMonitor());
//			} catch (Exception e) {
//				System.out.println("Error in IPacageFragment , return false");
//				e.printStackTrace();
//				return false;
//			}
//			return true;
//		}
//	}
//
//	private void copy(String sourcePath , String destinationPath) {
//		File source = new File(sourcePath);
//		File destination = new File(destinationPath);
//		FileChannel in_c= null;
//		FileChannel out_c = null;
//		try{
//			in_c = new FileInputStream(source).getChannel();
//			out_c = new FileOutputStream(destination).getChannel();
//			in_c.transferTo(0, in_c.size(), out_c);
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}finally{
//			try {
//				in_c.close();	
//				out_c.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	@SuppressWarnings("resource")
//	private String getPackageName() throws FileNotFoundException {
////		File file = new File(getFileName());
////		String result = new String("");
////		Scanner scan = new Scanner(file);
////		Scanner s;
////		while (scan.hasNextLine()) {
////			String temp = scan.nextLine();
////			s = new Scanner(temp);
////			while (s.hasNext()) {
////				if (s.next().equals("package")) {
////					result = s.next();
////					result = s.next();
////					return result.replace(";"	, "");
////				}
////			}
////
////		}
//		return "Default";
//	}
//
//	private String getCode() throws FileNotFoundException {
//		File file = new File(getFileName());
//		String result = new String("");
//		@SuppressWarnings("resource")
//		Scanner scan = new Scanner(file);
//		while (scan.hasNextLine()) {
//				result += scan.nextLine();
//			}
//		scan.close();
//		return result;
//	}
//
//
//	private String getFileName() {
//		return null;
//	}
//
//	public void run(IAction arg0) {
////		if (!flag) {
////			Openit();
////			MessageBox message = new MessageBox(Display.getDefault()
////					.getActiveShell(), SWT.ICON_INFORMATION);
////			message.setMessage("You have open it .");
////			message.setText("Attention . ");
////			message.open();
////		} else {
////			MessageBox message = new MessageBox(Display.getDefault()
////					.getActiveShell(), SWT.ICON_INFORMATION);
////			message.setMessage("You have open it before .");
////			message.setText("Attention . ");
////			message.open();
////		}
//	}
//
//	private void Openit() {
////		flag = true;
////		Other other;
////		try {
////			ss = new ServerSocket(8888);
////			other = new Other();
////			only = new Thread(other);
////			only.start();
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
//
//	}
//
//	private Control createContents(Composite parent) {
//		Transport action = new Transport();
//		ActionContributionItem aci = new ActionContributionItem(
//				(IAction) action);
//		aci.fill(parent);
//		return parent;
//	}
//
}
