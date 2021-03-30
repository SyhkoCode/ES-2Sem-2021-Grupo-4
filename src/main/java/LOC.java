
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LOC {
	private static String scrPath;

	private static ArrayList<String> list;

	public static ArrayList<String> getList() {
		return list;
	}

	public static void setList(ArrayList<String> list) {
		LOC.list = list;
	}

	public static String getScrPath() {
		return scrPath;
	}

	public static void setScrPath(String scrPath) {
		LOC.scrPath = scrPath;
	}

	public static void getTotalLines(File root, ArrayList<String> lis) throws IOException {
		String file = root.getAbsolutePath();
		String[] files = root.list();
		for (String fi : files) {
			File temp = new File(file + "\\" + fi);
			if (temp.isDirectory()) {
				int packold = Integer.parseInt(lis.get(0));
				getTotalLines(temp, lis);
				int packnew = Integer.parseInt(lis.get(0));
				int count = packnew - packold;
				String packpath = temp.getAbsolutePath();
				if (count > 0) {
					String packagename = packpath.substring(scrPath.length() + 1, packpath.length());
					packagename = packagename.replace('\\', '.');
					lis.add(packagename + "=" + count);
				}
			} else if ((file + "\\" + fi).endsWith(".java")) {
				int x = getLines(temp);
				int a = Integer.parseInt(lis.get(0));
				a += x;
				lis.set(0, a + "");
				String packpath = temp.getAbsolutePath();
				String packagename = packpath.substring(scrPath.length() + 1, packpath.length());
				packagename = packagename.replace('\\', '.');
				lis.add(packagename + "=" + x);
			}
		}
	}

	public static int getLines(File f) throws IOException {
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		int i = 0;
		try {
			while (true) {
				if (br.readLine().indexOf("{") != -1) {
					while (br.readLine() != null)
						i++;
					break;
				}
			}
		} catch (NullPointerException e) {
		}
		br.close();
		fr.close();
		return i;
	}	
}