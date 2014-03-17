package com.uf.cise.sp14.cnt.project.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.uf.cise.sp14.cnt.project.constants.ConfigKeys;
import com.uf.cise.sp14.cnt.project.exception.FileShareException;
import com.uf.cise.sp14.cnt.project.protocolManager.messages.RegularMsg;

/**
 * @author vikmenon
 *
 * All methods for handling of files during send/receive with peers.
 */
public class FileUtils {
	public enum FileOperation {
		SEND,
		RECV
	}
	
	/**
	 * Given a file path create it's parts inside PartsDirectory.
	 * Each file part is of the configuration ConfigKeys.PartSize in bytes.
	 * 
	 * @param file
	 * @return int: N => Number of parts created (success), 0 => failure
	 * @throws FileShareException
	 */
	public static int splitFile(File file, FileOperation fOper)
			throws FileShareException {
		Integer outFileCounter = 0;
		try {
			int bufferSize = ConfigUtils.getInteger(ConfigKeys.FileReadBufferSize);
			byte[] dataBuffer = new byte[bufferSize];
			int readBytes = 0;
			
			String outFileNamePrefix = ConfigUtils.getString(ConfigKeys.PartsDirectory) + "/" 
				+ fOper + "/"+ file.getName() + ConfigUtils.getString(ConfigKeys.PartSuffix);
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
						// Increment the count of part files
						outFileCounter++;
						fOStream = new FileOutputStream(outFile);
					} catch (FileNotFoundException e) {
						fIStream.close();
						fOStream.close();
						throw new FileShareException("ERR: Could not create the part file: " + outFileName, e);
					}
				}
				
				// Write the bytes read from input file into the part file
				fOStream.write(dataBuffer, 0, readBytes);
				
				// If we cannot read into this part file again, open the next part file.
				if (outFile.length() + bufferSize > ConfigUtils.getInteger(ConfigKeys.PartSize)) {
					fOStream.close();
					fOStream = null;
					outFileName = outFileNamePrefix + outFileCounter;
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
		return outFileCounter;
	}
	
	/**
	 * Find out how much continuous data, starting from file part 0, is available 
	 * for processing. This method also updates the list of file parts that hold the data.
	 * 
	 * @param fileName
	 * @param partFileList
	 * @return
	 */
	private static long getDownloadedFilePartsLength(String fileName, List<File> partFileList, 
			FileOperation fOper) {
		long availableDataLength = 0;
		
		String partFileNamePrefix = ConfigUtils.getString(ConfigKeys.PartsDirectory) + "/" 
				+ fOper + "/" + fileName + ConfigUtils.getString(ConfigKeys.PartSuffix);
		Integer partIndex = 0;
		String partFileName = partFileNamePrefix + partIndex.toString();
		File partFile = new File(partFileName);
		
		// Find how much continguous data we have for our expected file, from the start
		while (partFile.exists() && partFile.isFile()) {
			ApplicationUtils.printLine("CONTD: " + partFileNamePrefix);
			availableDataLength += partFile.length();
			partFileList.add(partFile);
			
			// Move to next part file
			partIndex++;
			partFileName = partFileNamePrefix + partIndex.toString();
			partFile = new File(partFileName);
		}
		
		return availableDataLength;
	}
	
	/**
	 * Given a file name and the expected size, reconstruct it 
	 * from all it's parts in PartsDirectory and store in DownloadsDirectory.
	 * 
	 * @param fileName
	 * @param expectedFileLength
	 * @param deleteParts
	 * @return int: N => Number of parts merged (success), 0 => failure
	 * @throws FileShareException
	 */
	public static int mergeFile(String fileName, long expectedFileLength, FileOperation fOper, boolean deleteParts)
			throws FileShareException {
		List<File> partFileList = new ArrayList<File>();
		long availableDataLength = getDownloadedFilePartsLength(fileName, partFileList, fOper);
		
		if (availableDataLength != expectedFileLength) {
			// We do not yet have enough data to construct the file.
			return 0;
		}
		
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
			return partFileList.size();
		}
		catch (IOException e) {
			throw new FileShareException("ERR: Could not merge the parts for: " + fileName, e);
		}
	}
	
	/**
	 * If not explicitly specified, we delete file parts after merging them.
	 * 
	 * @param fileName
	 * @param expectedFileLength
	 * @return int: N => Number of parts merged (success), 0 => failure
	 * @throws FileShareException
	 */
	public static int mergeFile(String fileName, long expectedFileLength, FileOperation fOper)
			throws FileShareException {
		return mergeFile(fileName, expectedFileLength, fOper, true);
	}

	public static void writePartDataToFile(RegularMsg msg, String fileName) {
		byte[] data = msg.getMessagePayload();
		
		// Build the part file name
		String partFileNamePrefix = ConfigUtils.getString(ConfigKeys.PartsDirectory) + "/" 
			+ FileOperation.RECV + "/" + fileName + ConfigUtils.getString(ConfigKeys.PartSuffix);
		Integer partFileIndex = ByteBuffer.wrap(data, 0, 4).getInt();
		ApplicationUtils.logServerPortEvent("Writing msg data out to file part index: " + partFileIndex, 2211);
		String partFileName = partFileNamePrefix + partFileIndex.toString();
		
		try {
			// Create the part file if it does not exist
			File partFile = new File(partFileName);
			partFile.getParentFile().mkdirs();
			partFile.createNewFile();
			
			// Write the payload (after 4 bytes) to the file
			FileOutputStream fOStream = new FileOutputStream(partFile, true);
			fOStream.write(data, 4, data.length - 4);
			fOStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
