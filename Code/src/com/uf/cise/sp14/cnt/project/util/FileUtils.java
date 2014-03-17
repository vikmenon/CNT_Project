package com.uf.cise.sp14.cnt.project.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.uf.cise.sp14.cnt.project.constants.ConfigKeys;
import com.uf.cise.sp14.cnt.project.exception.FileShareException;

/**
 * @author vikmenon
 *
 * All methods for handling of files during send/receive with peers.
 */
public class FileUtils {

	/**
	 * Given a file path and the number of parts required, 
	 * create the file parts in PartsDirectory.
	 * 
	 * @param file
	 * @param parts
	 * @return boolean: 1 => success, 0 => failure
	 * @throws FileShareException
	 */
	static boolean splitFile(File file, int parts)
			throws FileShareException {
		try {
			int bufferSize = ConfigUtils.getInteger(ConfigKeys.FileReadBufferSize);
			byte[] dataBuffer = new byte[bufferSize];
			int readBytes = 0;
			
			String outFileNamePrefix = ConfigUtils.getString(ConfigKeys.PartsDirectory) + "/" + file.getName() + ConfigUtils.getString(ConfigKeys.PartSuffix);
			Integer outFileCounter = 0;
			String outFileName = outFileNamePrefix + outFileCounter.toString();
			
			FileInputStream fIStream = new FileInputStream(file);
			FileOutputStream fOStream = null;
			File outFile = null;
			
			// Start reading the input file
			while((readBytes = fIStream.read(dataBuffer)) != -1) {
				if (ConfigUtils.isDebug()) {
					ApplicationUtils.printLine("Read from file (bytes): " + readBytes);
				}
				
				if (null == fOStream) {
					// Create the output part file if it does not exist
					try {
						if (ConfigUtils.isDebug()) {
							ApplicationUtils.printLine("Creating output file: " + outFileName);
						}
						outFile = new File(outFileName);
						outFile.getParentFile().mkdirs();
						outFile.createNewFile();
						fOStream = new FileOutputStream(outFile);
					} catch (FileNotFoundException e) {
						throw new FileShareException("ERR: Could not create the part file: " + outFileName, e);
					}
				}
				
				// Write the bytes read from input file into the part file
				fOStream.write(dataBuffer, 0, readBytes);
				
				// If we cannot read into this part file again, open the next part file.
				if (outFile.length() + bufferSize > ConfigUtils.getInteger(ConfigKeys.PartSize)) {
					fOStream.close();
					fOStream = null;
					outFileCounter++;
					outFileName = outFileNamePrefix + outFileCounter.toString();
				}
			}
			
			// Close all streams
			fOStream.close();
			fIStream.close();
		}
		catch (IOException e) {
			throw new FileShareException("ERR: Could not split into parts: " + file.getName(), e);
		}
		
		// Indicate success
		return true;
	}
	
	/**
	 * Given a file name and the expected size, reconstruct it 
	 * from all it's parts in PartsDirectory and store in DownloadsDirectory.
	 * 
	 * @param fileName
	 * @param expectedFileLength
	 * @param deleteParts
	 * @return boolean: 1 => success, 0 => failure
	 * @throws FileShareException
	 */
	static boolean mergeFile(String fileName, long expectedFileLength, boolean deleteParts)
			throws FileShareException {
		String partFileNamePrefix = ConfigUtils.getString(ConfigKeys.PartsDirectory) + "/" + fileName + ConfigUtils.getString(ConfigKeys.PartSuffix);
		
		List<File> partFileList = new ArrayList<File>();
		Integer partIndex = 0;
		String partFileName = partFileNamePrefix + partIndex.toString();
		File partFile = new File(partFileName);
		
		// Find how much continguous data we have for our expected file, from the start
		long availableDataLength = 0;
		while (partFile.exists() && partFile.isFile()) {
			availableDataLength += partFile.length();
			partFileList.add(partFile);
			
			// Move to next part file
			partIndex++;
			partFileName = partFileNamePrefix + partIndex.toString();
			partFile = new File(partFileName);
		}
		
		if (availableDataLength != expectedFileLength) {
			// We do not yet have enough data to construct the file.
			return false;
		}
		else {
			/* Reconstruct the file from all it's parts.
			 */
			try {
				String outFileName = ConfigUtils.getString(ConfigKeys.DownloadsDirectory) + "/" + fileName;
				File outFile = new File(outFileName);
				outFile.getParentFile().mkdirs();
				outFile.createNewFile();
				FileOutputStream fOStream = new FileOutputStream(outFile);
				
				int bufferSize = ConfigUtils.getInteger(ConfigKeys.FileReadBufferSize);
				byte[] dataBuffer = new byte[bufferSize];
				int readBytes = 0;
				
				// Iterate over all the parts of the file
				for (File partFileElem : partFileList) {
					FileInputStream fIStream = new FileInputStream(partFileElem);
					if (ConfigUtils.isDebug()) {
						ApplicationUtils.printLine("Now reading from file part: " + partFileElem.getName());
					}
					
					// Start reading from the part file
					while((readBytes = fIStream.read(dataBuffer)) != -1) {
						if (ConfigUtils.isDebug()) {
							ApplicationUtils.printLine("Read from file part (bytes): " + readBytes);
						}
						
						// Write the bytes read from part file into the output file
						fOStream.write(dataBuffer, 0, readBytes);
					}
					
					fIStream.close();
				}
				
				fOStream.close();
				
				if (deleteParts) {
					for (File partFileElem : partFileList) {
						partFileElem.delete();
					}
				}
				
				// Indicate success
				return true;
			}
			catch (IOException e) {
				throw new FileShareException("ERR: Could not merge the parts for: " + fileName, e);
			}
		}
	}
	
	/**
	 * If not explicitly specified, we delete file parts after merging them.
	 * 
	 * @param fileName
	 * @param expectedFileLength
	 * @return
	 * @throws FileShareException
	 */
	static boolean mergeFile(String fileName, long expectedFileLength)
			throws FileShareException {
		return mergeFile(fileName, expectedFileLength, true);
	}
	
	/**
	 * @param args
	 * @throws FileShareException
	 */
	public static void main(String args[]) throws FileShareException {
		String testFileName = "GDP.csv";
		File testFile = new File(testFileName);
		
//		splitFile(testFile, 10);
		mergeFile(testFileName, testFile.length());
	}
}
