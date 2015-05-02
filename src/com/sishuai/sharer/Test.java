package com.sishuai.sharer;



/**
 * 
 * 用来临时测试的地方
 *
 */
public class Test {
	public static void main(String[] args){
//		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
//		final IProject project = root.getProject("xyz");
//		IWorkspace workspace = root.getWorkspace();
//		IPath path = new Path("g:/myplugIn");
//		final IProjectDescription description = workspace
//				.newProjectDescription(project.getName());
//		description.setLocation(path);
//
//		String[] javaNature = description.getNatureIds();
//		String[] newJavaNature = new String[javaNature.length + 1];
//		System.arraycopy(javaNature, 0, newJavaNature, 0, javaNature.length);
//		newJavaNature[javaNature.length] = "org.eclipse.jdt.core.java.core.jacanature";
//		description.setNatureIds(newJavaNature);
//
//		try {
//			NullProgressMonitor monitor = new NullProgressMonitor();
//			project.create(description, monitor);
//			project.open(IResource.BACKGROUND_REFRESH, new SubProgressMonitor(
//					monitor, 1000));
//		} catch (CoreException e) {
//			System.out.println("Error in the NULLPROGRESSMONITTOR . ");
//		}
//		IJavaProject javaProject = JavaCore.create(project);
//
//		try {
//			IClasspathEntry[] jreLibrary = PreferenceConstants
//					.getDefaultJRELibrary();
//			IClasspathEntry[] oldCladdpathEntries = javaProject
//					.getRawClasspath();
//			List list = new ArrayList();
//			list.addAll(Arrays.asList(jreLibrary));
//			list.addAll(Arrays.asList(oldCladdpathEntries));
//
//			javaProject.setRawClasspath((IClasspathEntry[]) list
//					.toArray(new IClasspathEntry[list.size()]), null);
//		} catch (Exception e) {
//			System.out.println("Error in the IClasspathEntry[]");
//		}
//		IFolder binFolder = javaProject.getProject().getFolder("bin");
//		try {
//			binFolder.create(true, true, null);
//			javaProject.setOutputLocation(binFolder.getFullPath(), null);
//		} catch (Exception e) {
//			System.out.println("Error in the binFolder . ");
//		}
//
//		try {
//			IProjectDescription description2 = javaProject.getProject()
//					.getDescription();
//			ICommand command = description2.newCommand();
//			command.setBuilderName("Share");
//			description2.setBuildSpec(new ICommand[] { command });
//			description2
//					.setNatureIds(new String[] { "org.eclipse.jdt.core.javanatu" });
//			description2.setNatureIds(new String[] {
//
//			});
//		} catch (Exception e) {
//			System.out.println("Error in the description2");
//		}
//		// /////////////////////////////创建源代码文件夹//////////////////////////
//		// ///////////源文件夹和文件夹相似,只是使用PackageFragmentRoot进行了封装////////
//		IFolder srcFolder = javaProject.getProject().getFolder("src");
//		try {
//			srcFolder.create(true, true, null);
//			IClasspathEntry srcClasspathEntry = JavaCore
//					.newSourceEntry(srcFolder.getFullPath());
//			IClasspathEntry[] oldClasspathEntries = javaProject
//					.readRawClasspath();
//
//			List list = new ArrayList();
//			list.addAll(Arrays.asList(oldClasspathEntries));
//			list.add(srcClasspathEntry);
//
//			IClasspathEntry temp = JavaCore.newSourceEntry(new Path("/xyz"));
//			if (list.contains(temp)) {
//				list.remove(temp);
//			}
//			System.out.println(list.size());
//		} catch (Exception e) {
//			System.out.println("Error in the remove the xyz . ");
//		}
//
//		// creat the package
//		// 先找指定的源文件夹所在的IPackageFragmentRoot
//		IPackageFragmentRoot packageFragmentRoot = null;
//		try {
//			packageFragmentRoot = javaProject.findPackageFragmentRoot(new Path(
//					"/xyz/src"));
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		// 根据IPackageFragmentRoot创建IPackageFragment,IPackageFragment就是包了
//		IPackageFragment packageFragment = null;
//		try {
//			packageFragment = packageFragmentRoot.createPackageFragment(
//					"com.aptech.plugin", true, null);
//			String javaCode = "package com.aptech.plugin;public class HelloWorld{public static void main(String[] args){System.out.println(\"中华人民共和国\");}}";
//			packageFragment.createCompilationUnit("HelloWorld.java", javaCode,
//					true, new NullProgressMonitor());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
	}
}
