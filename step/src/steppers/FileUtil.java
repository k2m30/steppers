package steppers;
import java.io.*;
import java.net.URL;
import java.util.*;

class FileUtil {
	// ~ Constructors
	// ***********************************************************************
	/**
	 * Creates a new FileUtil object.
	 */
	public FileUtil() {
	}

	// ~ Methods
	// ****************************************************************************
	/**
	 * Gets the content from a File as StringArray List.
	 * 
	 * @param fileName
	 *            A file to read from.
	 * @return List of individual line of the specified file. List may be empty
	 *         but not null.
	 * @throws IOException
	 */
	public static ArrayList <String> getFileContent(String fileName) throws IOException {
		ArrayList <String> result = new ArrayList<String>();
		File aFile = new File(fileName);
		if (!aFile.isFile()) {
			// throw new IOException( fileName + " is not a regular File" );
			return result; // None
		}
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(aFile));
		} catch (FileNotFoundException e1) {
			// TODO handle Exception
			e1.printStackTrace();
			return result;
		}
		String aLine = null;
		while ((aLine = reader.readLine()) != null) {
			result.add(aLine + "\n");
		}
		reader.close();
		return result;
	}

	/**
	 * Searches case sensitively, and returns true if the given SearchString
	 * occurs in the first File with the given Filename.
	 * 
	 * @param aFileName
	 *            A files name
	 * @param aSearchString
	 *            the string search for
	 * @return true if found in the file otherwise false
	 */
	public static boolean fileContains(String aFileName, String aSearchString) {
		return (fileContains(aFileName, aSearchString, false));
	}

	/**
	 * Tests if the given File contains the given Search String
	 * 
	 * @param aFileName
	 *            A files name
	 * @param aSearchString
	 *            the String to search for
	 * @param caseInSensitiveSearch
	 *            If false the Search is case sensitive
	 * @return true if found in the file otherwise false
	 */
	public static boolean fileContains(String aFileName, String aSearchString,
			boolean caseInSensitiveSearch) {
		boolean result = false;
		String searchString = caseInSensitiveSearch ? aSearchString
				.toLowerCase() : aSearchString;
		ArrayList <String> fileContent = new ArrayList<String>();
		try {
			fileContent = getFileContent(aFileName);
		} catch (IOException e) {
			// TODO handle Exception
			e.printStackTrace();
		}
		Iterator <String> linesIter = fileContent.iterator();
		while (linesIter.hasNext()) {
			String currentline = (String) linesIter.next();
			if (caseInSensitiveSearch) {
				currentline = currentline.toLowerCase();
			}
			if (currentline.indexOf(searchString) > -1) {
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * Gets file date and time.
	 * 
	 * @param url
	 *            The URL of the file for which date and time will be returned.
	 * @return Returns long value which is the date and time of the file. If any
	 *         error occurs returns -1 (=no file date and time available).
	 */
	public static long getFileDateTime(URL url) {
		if (url == null) {
			return -1;
		}
		String fileName = url.getFile();
		if (fileName.charAt(0) == '/' || fileName.charAt(0) == '\\') {
			fileName = fileName.substring(1, fileName.length());
		}
		try {
			File file = new File(fileName);
			// File name must be a file or a directory.
			if (!file.isDirectory() && !file.isFile()) {
				return -1;
			}
			return file.lastModified();
		} catch (java.lang.Exception e) { // Trap all Exception based exceptions
											// and return -1.
			return -1;
		}
	}

	public static String[] getFileNames(String dirPath) throws Exception {
		return getFileNames(dirPath, null);
	}

	public static String[] getFileNames(String dirPath,
			FilenameFilter fileNameFilter) throws Exception {
		String fileNames[] = null;
		File dir = new File(dirPath);
		if (dir.isDirectory()) {
			if (fileNameFilter != null) {
				fileNames = dir.list(fileNameFilter);
			} else {
				fileNames = dir.list();
			}
		}
		return fileNames;
	}

}
