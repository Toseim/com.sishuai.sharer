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
	private static int direct = 0;

	public static void main(String[] args) {
		BufferedReader br1 = null;
		BufferedReader br2 = null;
		try {
//			File in = new File("D:/Works/Github/Test_Code_Diff/sample.in");
//			File out = new File("D:/Works/Github/Test_Code_Diff/sample.out");
			File in = new File("~/workspace/Test/a.txt");
			File out = new File("~/workspace/Test/b.txt");
			br1 = new BufferedReader(new InputStreamReader(new FileInputStream(in)));
			br2 = new BufferedReader(new InputStreamReader(new FileInputStream(out)));

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

			System.out.println(dict);
			
			System.out.println(str1);
			System.out.println(str2);

			System.out.println(handle());
			
			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < 4; j++) {
					System.out.print(map[i][j].value + "\t");
				}
				System.out.println();
			}
			
			
			rollBack();
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
		int len1 = str1.size();
		int len2 = str2.size();
		int range = 0;
		map = new State[len1 + 1][len2 + 2];
		for (int i = 0; i < len1 + 1; i++)
			for (int j = 0; j < len2 + 2; j++)
				map[i][j] = new State(i, j);

		for (int i = len1 - 1; i >= 0; i--) {
			if (i != 0)
				range = str2.indexOf(str1.get(i - 1));
			int j = 0;
			for (j = len2; j >= 0; j--) {

				if (j < len2 && i < len1 && str1.get(i) == str2.get(j)) {
					map[i][j].value = map[i+1][j + 1].value;
					map[i][j].dir = 3;
					continue;
				}
				
		        map[i][j].setDirVal(1, map[i][j+1].value+1);
                map[i][j].setDirVal(2, map[i+1][j].value+1);
			}
		}
		return map[0][0].value;
	}

	public static int min(LinkedList<Integer> list) {
		if (list.isEmpty())
			return 1;
		int ans = 2147483647;
		for (int i = 0; i < list.size(); i++)
			if ((i == 2 || list.get(i) != 1) && ans > list.get(i)) {
				direct = i+1;
				ans = list.get(i);
			}
		if (direct == 0) {
			ans = 1;
			direct = 1;
		}
		return ans;
	}
	
	public static void rollBack() {
		State whi = map[0][0];
		while (whi != null) {
			switch (whi.dir) {
			case 1:
				System.out.println("-" + dict.get(str1.get(whi.x)));
				whi = map[whi.x+1][whi.y];
				break;
			case 2:
				System.out.println("+" + dict.get(str2.get(whi.y)));
				whi = map[whi.x][whi.y+1];
				break;
			case 3:
				System.out.println(dict.get(str1.get(whi.x)));
				whi = map[whi.x+1][whi.y+1];
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
    	if (dir == 2) {
    		if (value > 1)
	    		if (!flag || (flag && value < this.value)) {
	    			this.dir = 2;
	    			this.value = value;
	    		}
    		flag = false;
    		return;
    	}
    	
    	this.dir = 1;
    	
    	if (dir == 1 && value > 1) {
    		this.value = value;
    		flag = true;
    		return;
    	}
    	
    	this.value = 1;
    }
}
