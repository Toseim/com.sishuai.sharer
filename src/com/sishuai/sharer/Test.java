package com.sishuai.sharer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
public class Test {
	public static void main(String args){
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		// 创建新项目
		final IProject project = root.getProject("Share");
		// 设置工程的位置
		// 为项目指定存放路径,默认放在当前工作区
		IWorkspace workspace = root.getWorkspace();
		final IProjectDescription description = workspace
				.newProjectDescription(project.getName());
		description.setLocation(null);
		System.out.println(workspace.toString());
	}
}
