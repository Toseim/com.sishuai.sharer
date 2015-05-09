package com.sishuai.sharer.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;

public class CodeDiff {
	private static ArrayList<String> dict = new ArrayList<String>();
	private static ArrayList<Integer> str1 = new ArrayList<Integer>();
	private static LinkedList<Integer> str2 = new LinkedList<Integer>();
	private static State[][] map;
	private static int len1;
	private static int len2;

	public static void diff(String originFile, String modifyedFile) {
//	public static void main(String[] args) {
		BufferedReader br1 = null;
		BufferedReader br2 = null;
		try {
//			File in = new File("D:/Works/Github/Test_Code_Diff/sample.in");
//			File out = new File("D:/Works/Github/Test_Code_Diff/sample.out");
//			File in = new File("/home/tose/workspace/Test/a.txt");
//			File out = new File("/home/tose/workspace/Test/b.txt");
			File ori = new File(originFile);
			File mod = new File(modifyedFile);
			br1 = new BufferedReader(new InputStreamReader(new FileInputStream(ori)));
			br2 = new BufferedReader(new InputStreamReader(new FileInputStream(mod)));

			String line = null;
			int index = 0;
			while ((line = br1.readLine()) != null)
				if ((index = dict.indexOf(line)) == -1) {
					dict.add(line);
					str1.add(dict.size() - 1);
				} else
					str1.add(index);

			while ((line = br2.readLine()) != null)
				if ((index = dict.indexOf(line)) == -1) {
					dict.add(line);
					str2.add(dict.size() - 1);
				} else
					str2.add(index);

			len1 = str1.size();
			len2 = str2.size();
			
			System.out.println(dict);
			
			System.out.println(str1);
			System.out.println(str2);

			System.out.println(handle());
			
			for (int i = str1.size(); i >= 0; i--) {
				for (int j = 0; j < len2+2; j++) {
					System.out.print(map[i][j].value+" "+map[i][j].dir+"\t");
				}
				System.out.println();
			}

			writeFile(mod);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (br1 != null)
					br1.close();
				if (br2 != null)
					br2.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static int handle() {
		map = new State[len1 + 1][len2 + 2];
		for (int i = 0; i < len1 + 1; i++)
			for (int j = 0; j < len2 + 2; j++) {
				map[i][j] = new State(i, j);
				if (i == len1) map[i][j].value = len2-j;
				if (j == len2+1) map[i][j].value = len1-1-i;
			}
		map[len1][len2+1].value = 0;
		for (int i = len1 - 1; i >= 0; i--) {
			for (int j = len2; j >= 0; j--) {

				if (j < len2 && i < len1 && str1.get(i) == str2.get(j)) {
					map[i][j].value = map[i+1][j + 1].value;
					map[i][j].dir = 3;
					continue;
				}
				if (map[i][j+1].dir != 0)
					map[i][j].setDirVal(2, map[i][j+1].value+1);
				if (map[i+1][j].dir != 0)
					map[i][j].setDirVal(1, map[i+1][j].value+1);
				map[i][j].check();
			}
		}
		return map[0][0].value;
	}
	
	public static void writeFile(File mod) {
//		BufferedWriter bw = new BufferedWriter(
//				new OutputStreamWriter(new FileOutputStream(mod)));
		State whi = map[0][0];
		while (whi.dir != 0) {
			switch (whi.dir) {
			case 1:
				System.out.println("-" + dict.get(str1.get(whi.x)));
//				String line = dict.get(str1.get(whi.x));
//				if (line.charAt(0) == '\t') {
//					bw.write("//- ");
//					bw.write(line, 1, line.length());
//					bw.flush();
//				}
				whi = map[whi.x+1][whi.y];
				break;
			case 2:
				System.out.println("+" + dict.get(str2.get(whi.y)));
				whi = map[whi.x][whi.y+1];
				break;
			case 3:
				System.out.println(dict.get(str1.get(whi.x)));
				if (whi.x+1 == len1) {
					whi = map[whi.x][whi.y+1];
					map[len1-1][len2].dir = 0;
				} else whi = map[whi.x+1][whi.y+1];
				break;
			default:
				break;
			}
		}
	}
}

class State {
	public int dir;
	public int value = 0;
	public int x;
	public int y;
	private boolean flag = false;
	
	public State(int x, int y) {
		this.x = x;
		this.y = y;
	}

    public void setDirVal(int dir, int value) {
    	if (value <= this.value || this.value == 0) {
    		this.dir = dir;
    		this.value = value;
    	}
    	flag = true;
    }
    public void check() {
    	if (!flag) {
    		value = 1;
    		dir = 1;
    	}
    }
}
