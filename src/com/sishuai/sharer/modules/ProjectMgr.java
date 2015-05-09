package com.sishuai.sharer.modules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.PreferenceConstants;

import com.sishuai.sharer.util.Logging;

public class ProjectMgr {
	private static String path = "";
	private static void createJavaProject(String projectName , String fileName  ) {
		Logging.getLogger().setFileName("");
		// 获取工作区
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		path = Platform.getInstanceLocation().getURL().getPath()+"/"+projectName+"/Share/src/";
		// 创建新项目
		final IProject project = root.getProject(projectName);

		// 设置工程的位置
		// 为项目指定存放路径,默认放在当前工作区
		IWorkspace workspace = root.getWorkspace();
		final IProjectDescription description = workspace
				.newProjectDescription(project.getName());
		description.setLocation(null);

		// 设置工程标记,即为java工程
		String[] javaNature = description.getNatureIds();
		String[] newJavaNature = new String[javaNature.length + 1];
		System.arraycopy(javaNature, 0, newJavaNature, 0, javaNature.length);
		newJavaNature[javaNature.length] = JavaCore.NATURE_ID; // 这个标记证明本工程是Java工程
		description.setNatureIds(newJavaNature);

		try {
			NullProgressMonitor monitor = new NullProgressMonitor();
			project.create(description, monitor);
			project.open(IResource.BACKGROUND_REFRESH, new SubProgressMonitor(
					monitor, 1000));
		} catch (CoreException e) {
			System.out.println("Error int Core . ");
		} finally {
			// 转化成java工程
			IJavaProject javaProject = JavaCore.create(project);
			// 添加JRE库
			System.out.println("Test 1 ");
			try {
				// 获取默认的JRE库
				IClasspathEntry[] jreLibrary = PreferenceConstants
						.getDefaultJRELibrary();
				// 获取原来的build path
				IClasspathEntry[] oldClasspathEntries = javaProject
						.getRawClasspath();
				List<IClasspathEntry> list = new ArrayList<IClasspathEntry>();
				list.addAll(Arrays.asList(jreLibrary));
				list.addAll(Arrays.asList(oldClasspathEntries));
				javaProject.setRawClasspath((IClasspathEntry[]) list
						.toArray(new IClasspathEntry[list.size()]), null);
			} catch (JavaModelException e) {
				System.out.println("Error in JavaModelException . ");
			} finally {
				// 创建输出路径
				IFolder binFolder = javaProject.getProject().getFolder("bin");
				try {
					binFolder.create(true, true, null);
					javaProject
							.setOutputLocation(binFolder.getFullPath(), null);
				} catch (CoreException e) {
				} finally {
					// 设置Java生成器
					try {
						IProjectDescription description2 = javaProject
								.getProject().getDescription();
						ICommand command = description2.newCommand();
						command.setBuilderName("org.eclipse.jdt.core.javabuilder");
						description2.setBuildSpec(new ICommand[] { command });
						description2
								.setNatureIds(new String[] { "org.eclipse.jdt.core.javanature" });
						javaProject.getProject().setDescription(description2,
								null);
					} catch (CoreException e) {
						System.out.println("Error in the create the Description . ");
					} finally {
						// 创建源代码文件夹
						// 源文件夹和文件夹相似,只是使用PackageFragmentRoot进行了封装
						IFolder srcFolder = javaProject.getProject().getFolder(
								"src");
						try {
							srcFolder.create(true, true, null);
							// 创建SourceLibrary
							IClasspathEntry srcClasspathEntry = JavaCore
									.newSourceEntry(srcFolder.getFullPath());

							// 得到旧的build path
							IClasspathEntry[] oldClasspathEntries = javaProject
									.readRawClasspath();

							// 添加新的
							List<IClasspathEntry> list = new ArrayList<IClasspathEntry>();
							list.addAll(Arrays.asList(oldClasspathEntries));
							list.add(srcClasspathEntry);

							// 原来存在一个与工程名相同的源文件夹,必须先删除
							IClasspathEntry temp = JavaCore
									.newSourceEntry(new Path("/" + "Share"));
							if (list.contains(temp)) {
								list.remove(temp);
							}

							javaProject.setRawClasspath(
									(IClasspathEntry[]) list
											.toArray(new IClasspathEntry[list
													.size()]), null);
						} catch (CoreException e) {
							System.out.println("Project has been exit . ");
						} finally {
							// ///////////////////////////////创建包//////////////////////////
							// IPackageFragmentRoot packageFragmentRoot =
							// javaProject.getPackageFragmentRoot(javaProject.getResource());
							// 此处得到的src目录只读
							try {
								// 先找指定的源文件夹所在的IPackageFragmentRoot
								IPackageFragmentRoot packageFragmentRoot = javaProject
										.findPackageFragmentRoot(new Path("/"+ "Share" + "/src"));
								// 根据IPackageFragmentRoot创建IPackageFragment,IPackageFragment就是包了
								IPackageFragment packageFragment = packageFragmentRoot
										.createPackageFragment("Share", true, null);
								// //////////////////////////////////创建Java文件////////////////////////
								packageFragment.createCompilationUnit(
										fileName, "", true, new NullProgressMonitor());
							} catch (Exception e) {
								System.out.println("Something has been already exit . ");
							}
						}
					}
				}
			}
		}
	}
	
	public static String getFilePath(String filename) {
		ProjectMgr.createJavaProject("SharedFiles" , filename);
		return path + filename;
	}
	
//	private static String getName(String path) {
//		String str = path;
//		File F = new File(str);
//		return F.getPath().substring(F.getPath().lastIndexOf("/")+1);
//	}
//	private static String getCode(String path) {
//		try {	
//			String str = path;
//			Scanner s = new Scanner(new File(str));
//			str = "";
//			while(s.hasNextLine()){
//				str += s.nextLine();
//			}
//			return str;
//		} catch (FileNotFoundException e) {
//			System.out.println("Error . ");
//			return null;
//		}
//	
//	}
}