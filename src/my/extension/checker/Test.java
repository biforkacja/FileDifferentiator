package my.extension.checker;

import java.io.IOException;
import java.util.Scanner;



public class Test {

	public static void main(String[] args) throws IOException {
		
		String path;
		Scanner in = new Scanner(System.in);
		System.out.println("Please write absolute directory");
		path = in.nextLine();
		ExtensionChecker ext = new ExtensionChecker(path);
		
		//check if object was created
		if (ext.getFilePath() != null)
		{
			if (ext.getExtFromName().equalsIgnoreCase(ext.getExtFromFile()))
				System.out.println("A " + ext.getExtFromName() + " file is a " + ext.getExtFromFile() + " file");
			else if (ext.getExtFromFile().equals("undefined"))
				System.out.println("Extension does not match gif, jpg or txt");
			else
				System.out.println(
						"Extension is " + ext.getExtFromName() + ", while actually it's a " + ext.getExtFromFile());
		}
		in.close();

	}

}
