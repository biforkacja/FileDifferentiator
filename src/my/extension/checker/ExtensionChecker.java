package my.extension.checker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;

public class ExtensionChecker {

	private String filePath; // file absolute directory
	private String extFromFile; // file extenstion read from file
	private String extFromName; // file extenstion read from file name

	public String getExtFromFile() {
		return extFromFile;
	}

	public String getExtFromName() {
		return extFromName;
	}

	public String getFilePath() {
		return filePath;
	}

	public ExtensionChecker() {

		this.filePath = null;
		this.extFromFile = null;
		this.extFromName = null;
	}

	public ExtensionChecker(String filePath) throws IOException {

		if (new File(filePath).exists()) {
			this.filePath = filePath;
			this.extFromFile = checkExtn();
			this.extFromName = extFromPath();
		} else {
			System.out.println("File does not exist");
			this.filePath = null;
			this.extFromFile = null;
			this.extFromName = null;
		}
	}

	private String checkExtn() throws IOException {

		FileInputStream fis;
		BufferedReader bf = null;
		ArrayList<String> hex; // file data in hex format
		String fileExt = new String();

		try {

			fis = new FileInputStream(filePath);
			bf = new BufferedReader(new InputStreamReader(fis));

			if (checkIfText(bf))
				fileExt = "txt";
			else {

				fis.getChannel().position(0); // rewind the file
				bf = new BufferedReader(new InputStreamReader(fis));
				hex = stringToHex(bf);
				fileExt = getExtFromFile(hex);
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
		} finally {
			if (bf != null)
				bf.close();
		}

		return fileExt;
	}

	private boolean checkIfText(BufferedReader bf) throws IOException {

		String line = bf.readLine();

		while (line != null) {
			// if in file there is only ASCII code the file is txt
			for (int i = 0; i < line.length(); i++)
				if (((int) line.charAt(i)) > 128)
					return false;
			line = bf.readLine();
		}
		return true;
	}

	private ArrayList<String> stringToHex(BufferedReader bf) throws IOException {
		String line = bf.readLine();
		ArrayList<String> hexString = new ArrayList<String>();

		while (line != null) {
			for (int i = 0; i < line.length(); i += 2)
				if (i + 2 < line.length())
					// convert file data to hex format
					hexString.add(String.format("%4x", new BigInteger(1, line.substring(i, i + 2).getBytes())));
			line = bf.readLine();
		}

		return hexString;
	}

	private String getExtFromFile(ArrayList<String> hex) {
		String ext = new String();
		StringBuilder sb = new StringBuilder();

		sb.append(hex.get(0));
		sb.append(hex.get(1));

		// check if extension is jpg or gif
		if (sb.toString().equalsIgnoreCase("FFD8FFDB") || sb.toString().equalsIgnoreCase("FFD8FFE0")
				|| sb.toString().equalsIgnoreCase("FFD8FFE1"))
			ext = "jpg";
		else if (sb.toString().equalsIgnoreCase("47494638")) {
			if (hex.get(2).equalsIgnoreCase("3761") || hex.get(2).equalsIgnoreCase("3961"))
				ext = "gif";
		} else
			ext = "undefined";

		return ext;
	}

	private String extFromPath() {
		String ext = new String();
		StringBuilder sb = new StringBuilder();
		int i;
		if (filePath != null) {
			for (i = 0; i < filePath.length(); i++)
				if (filePath.charAt(i) == '.')
					break;
			if (i < filePath.length()) {
				i++;
				for (int j = i; j < filePath.length(); j++)
					sb.append(filePath.charAt(j));
				ext = sb.toString();
			}
		}
		return ext;
	}
}
