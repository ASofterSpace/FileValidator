/**
 * Unlicensed code created by A Softer Space, 2025
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.fileValidator;

import com.asofterspace.toolbox.io.File;
import com.asofterspace.toolbox.io.JSON;
import com.asofterspace.toolbox.io.JsonFile;
import com.asofterspace.toolbox.io.JsonParseException;
import com.asofterspace.toolbox.io.XmlElement;
import com.asofterspace.toolbox.io.XmlFile;
import com.asofterspace.toolbox.Utils;

import java.util.List;
import java.util.Set;


public class FileValidator {

	public final static String PROGRAM_TITLE = "FileValidator";
	public final static String VERSION_NUMBER = "0.0.0.1(" + Utils.TOOLBOX_VERSION_NUMBER + ")";
	public final static String VERSION_DATE = "5th of February 2025 - 5th of February 2025";

	public static void main(String[] args) {

		// let the Utils know in what program it is being used
		Utils.setProgramTitle(PROGRAM_TITLE);
		Utils.setVersionNumber(VERSION_NUMBER);
		Utils.setVersionDate(VERSION_DATE);

		if (args.length > 0) {
			if (args[0].equals("--version")) {
				System.out.println(Utils.getFullProgramIdentifierWithDate());
				return;
			}

			if (args[0].equals("--version_for_zip")) {
				System.out.println("version " + Utils.getVersionNumber());
				return;
			}
		}

		boolean checkForJson = false;
		boolean checkForXml = false;
		String fileName = null;

		int argPos = 0;

		if (args.length > argPos) {
			if (args[argPos].equals("--json")) {
				checkForJson = true;
				argPos++;
			}
		}

		if (args.length > argPos) {
			if (args[argPos].equals("--xml")) {
				checkForXml = true;
				argPos++;
			}
		}

		if (args.length > argPos) {
			fileName = args[argPos];
			argPos++;
		}

		if (fileName == null) {
			System.out.println("Cannot validate if you don't indicate what to validate. Please enter a valid file path as argument!");
			return;
		}

		File file = new File(fileName);
		if (!file.exists()) {
			System.out.println(fileName + " does not seem to exist. Please enter a valid file path as argument!");
			return;
		}

		// if several or none are selected, determine based on file ending...
		if ((checkForJson && checkForXml) || (!checkForJson && !checkForXml)) {
			String lowerName = fileName.toLowerCase();
			if (lowerName.endsWith(".js") || lowerName.endsWith(".json")) {
				checkForJson = true;
				checkForXml = false;
			} else {
				checkForJson = false;
				checkForXml = true;
			}
		}

		if (checkForJson) {
			JsonFile parseFile = new JsonFile(file);
			try {
				JSON json = parseFile.getAllContents();
				Set<String> keys = json.getKeys();
				StringBuilder resBuilder = new StringBuilder();
				String sep = "";
				for (String key : keys) {
					resBuilder.append(sep);
					sep = ", ";
					resBuilder.append(key);
				}
				System.out.println("Input appears to be valid JSON (containing top-level keys: " + resBuilder.toString() + ")");
			} catch (JsonParseException e) {
				System.out.println("Input appears NOT to be valid JSON! " + e);
			}
			return;
		}

		if (checkForXml) {
			XmlFile parseFile = new XmlFile(file);
			XmlElement root = parseFile.getRoot();
			if (root == null) {
				System.out.println("Input appears NOT to be valid XML!");
			} else {
				List<XmlElement> children = root.getChildNodes();
				StringBuilder resBuilder = new StringBuilder();
				String sep = "";
				for (XmlElement child : children) {
					resBuilder.append(sep);
					sep = ", ";
					resBuilder.append(child.getTagName());
				}
				System.out.println("Input appears to be valid XML (containing top-level tag names: " + resBuilder.toString() + ")");
			}
			return;
		}
	}

}
