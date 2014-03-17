package com.uf.cise.sp14.cnt.project.protocolManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.uf.cise.sp14.cnt.project.constants.ConfigKeys;
import com.uf.cise.sp14.cnt.project.protocolManager.messages.HandshakeMsg;
import com.uf.cise.sp14.cnt.project.protocolManager.messages.MessageType;
import com.uf.cise.sp14.cnt.project.protocolManager.messages.RegularMsg;
import com.uf.cise.sp14.cnt.project.util.ConfigUtils;
import com.uf.cise.sp14.cnt.project.util.FileUtils.FileOperation;

/**
 * @author Rahul
 *
 * This class generates messages to send/receive file or control data with peers.
 */
public class MessageGenerator {
	/**
	 * @param peerID
	 * @return
	 */
	public static HandshakeMsg getHandshakeMessage(int peerID) {
		HandshakeMsg handshakeMsg = new HandshakeMsg(peerID);
		return handshakeMsg;
	}

	/**
	 * @param messageLength
	 * @param messageType
	 * @return
	 */
	public static RegularMsg getRegularMsg(int messageLength, MessageType messageType,
			int index) {
		byte[] payload = null;
		switch (messageType.ordinal())
		{
		case 0:
		case 1:
		case 2:
		case 3:
			return new RegularMsg(messageLength, messageType, null);

		case 4:
			/*
			 * have: Message has a payload that contains a
			 * 4-byte index of a file piece. It indicates that the sender has
			 * the piece referred to by the index. Payload = filesystem.getIsIndex();
			 */

			return new RegularMsg(messageLength, messageType, payload);

		case 5:
			/*
			 * bitfield: Find all the bits that correspond to the preset
			 * file chunks with current peer. Payload = filesystem.getPiecesInfo(index);
			 */
			return new RegularMsg(messageLength, messageType, payload);
		case 6:
			/*
			 * request: Message has a 4-byte payload, which contains the 
			 * index of the piece being requested. Payload = filesystem.getPiecesInfo(index);
			 */
			return new RegularMsg(messageLength, messageType, payload);
		case 7:
			/*
			 * piece: Message has a payload consisting of a 4-byte 
			 * piece index and the content of the piece. It is used to
			 * send a piece from the sender to a receiving peer.
			 * Payload = filesystem.getData(index);
			 */
			return new RegularMsg(messageLength, messageType, payload);
		default:
			break;
		}
		return null;
	}

	/**
	 * @param dataBuffer
	 * @param dataLength
	 * @return
	 */
	public static List<RegularMsg> getPieceMsgs(byte[] dataBuffer, int dataLength, int indx) {
		List<RegularMsg> retList = new ArrayList<RegularMsg>();
		
		// First 4 bytes of the payload are the index of the file part
		int maxDataLen = ConfigUtils.getInteger(ConfigKeys.MaxPayloadSize) - 4;
		
		// We can only send MaxPayloadSize at a time, so split the given data
		do {
			int sendLen = (dataLength < maxDataLen) ? dataLength : maxDataLen;
			byte[] messagePayload = ByteBuffer.allocate(sendLen + 4)
					.putInt(indx).put(dataBuffer, 0, sendLen).array();
			retList.add(
					new RegularMsg(sendLen, MessageType.piece, messagePayload)
				);
			
			// Update the databuffer after creating a message
			dataBuffer = Arrays.copyOfRange(dataBuffer, sendLen, dataLength);
			dataLength -= sendLen;
		} while(dataLength > 0);
		
		return retList;
	}

	/**
	 * @param testFileName
	 * @param indx
	 * @return
	 */
	public static List<RegularMsg> getPieceMsgsFromPartFile(String fileName, int indx) {
		// Build the part file name
		List<RegularMsg> retList = new ArrayList<RegularMsg>();
		String partFileNamePrefix = ConfigUtils.getString(ConfigKeys.PartsDirectory) + "/" 
			+ FileOperation.SEND + "/" + fileName + ConfigUtils.getString(ConfigKeys.PartSuffix);
		String partFileName = partFileNamePrefix + String.valueOf(indx);
		
		int bufferSize = ConfigUtils.getInteger(ConfigKeys.FileReadBufferSize);
		byte[] dataBuffer = new byte[bufferSize];
		int readBytes = 0;
		
		try {
			FileInputStream fIStream = new FileInputStream(partFileName);
			
			/* Start reading the part file.
			 * We can only send MaxPayloadSize at a time, so wesplit the part file data.
			 */
			while((readBytes = fIStream.read(dataBuffer)) != -1) {
				retList.addAll(MessageGenerator.getPieceMsgs(dataBuffer, readBytes, indx));
			}
			
			fIStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return retList;
	}
}
