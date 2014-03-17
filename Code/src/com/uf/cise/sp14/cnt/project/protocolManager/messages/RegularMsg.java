package com.uf.cise.sp14.cnt.project.protocolManager.messages;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * @author Rahul
 * 
 */
public class RegularMsg extends Message {
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	byte[] messageLength = new byte[4];
	/**
	 * 
	 */
	byte[] messageType = new byte[1];
	/**
	 * 
	 */
	byte[] messagePayload;

	/**
	 * @param messageLength
	 * @param messageType
	 * @param messagePayload
	 */
	public RegularMsg(int messageLength, MessageType messageType,
			byte[] messagePayload) {
		super();
		this.messageLength = ByteBuffer.allocate(4).putInt(messageLength).array();
		this.messageType = ByteBuffer.allocate(4).putInt(messageType.ordinal()).array();
		this.messagePayload = messagePayload;
	}

	public byte[] getMessageLength() {
		return messageLength;
	}

	/**
	 * @param messageLength
	 */
	public void setMessageLength(byte[] messageLength) {
		this.messageLength = messageLength;
	}

	public byte[] getMessageType() {
		return messageType;
	}

	/**
	 * @param messageType
	 */
	public void setMessageType(byte[] messageType) {
		this.messageType = messageType;
	}

	public byte[] getMessagePayload() {
		return messagePayload;
	}

	/**
	 * @param messagePayload
	 */
	public void setMessagePayload(byte[] messagePayload) {
		this.messagePayload = messagePayload;
	}

	@Override
	public String toString() {
		return "RegularMsg [messageLength=" + Arrays.toString(messageLength)
				+ ", messageType=" + Arrays.toString(messageType)
				+ ", messagePayload=" + Arrays.toString(messagePayload) + "]";
	}

}
