import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ReadJavaProject {
	public static List<String[]> readJavaProject(String pathProject) {
		List<String[]> result = new ArrayList<>();

		Stack<File> folders = new Stack<>();
		folders.push(new File(pathProject + "\\src"));
		do {
			File current = folders.pop();

			for (File packageFile : current.listFiles()) {
				if (packageFile.isDirectory())
					folders.push(packageFile);
				else {
					if (packageFile.getName().endsWith(".java")) {
						try {
							List<String> methods = NOM.countMethods(packageFile);
							for (String s : methods) {
								String[] lines = new String[5];
								lines[0] = current.getName();
								lines[1] = packageFile.getName().substring(0, packageFile.getName().lastIndexOf('.'));
								lines[2] = s;
								lines[3] = methods.size() + "";
								lines[4] = LOC.getLines(packageFile) + "";
								result.add(lines);
							}
						} catch (IOException e) {
							System.out.println("sou estupido!");
						}
					}

				}

			}

		} while (!folders.isEmpty());

		return result;
	}

}
