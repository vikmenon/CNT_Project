package com.uf.cise.sp14.cnt.project.protocolManager.messages;

import java.nio.ByteBuffer;

/**
 * @author Rahul
 * 
 */
public class HandshakeMsg extends Message {
	private static final long serialVersionUID = 1L;
	/**
	 * this contains "HELLO"
	 */
	byte[] handshake_header = new byte[5];
	/**
	 * 
	 */
	byte[] zero_bits = new byte[23];
	/**
	 * 
	 */
	byte[] peerID = new byte[4];

	/**
	 * @param peerID
	 */
	public HandshakeMsg(int peerID) {
		super();
		this.handshake_header = "HELLO".getBytes();
		this.peerID = ByteBuffer.allocate(4).putInt(peerID).array();
	}

	public byte[] getHandshake_header() {
		return handshake_header;
	}

	/**
	 * @param handshake_header
	 */
	public void setHandshake_header(byte[] handshake_header) {
		this.handshake_header = handshake_header;
	}

	public byte[] getZero_bits() {
		return zero_bits;
	}

	/**
	 * @param zero_bits
	 */
	public void setZero_bits(byte[] zero_bits) {
		this.zero_bits = zero_bits;
	}

	public byte[] getPeerID() {
		return peerID;
	}

	/**
	 * @param peerID
	 */
	public void setPeerID(byte[] peerID) {
		this.peerID = peerID;
	}

}
