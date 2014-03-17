package com.uf.cise.sp14.cnt.project.protocolManager;

import com.uf.cise.sp14.cnt.project.protocolManager.messages.HandshakeMsg;
import com.uf.cise.sp14.cnt.project.protocolManager.messages.MessageType;
import com.uf.cise.sp14.cnt.project.protocolManager.messages.RegularMsg;

/**
 * @author Rahul
 *
 * This class generates messages to send/receive file or control data with peers.
 */
public class MessageGenerator {
	public HandshakeMsg getHandshakeMessage(int peerID) {
		HandshakeMsg handshakeMsg = new HandshakeMsg(peerID);
		return handshakeMsg;
	}

	/**
	 * @param messageLength
	 * @param messageType
	 * @return
	 */
	public RegularMsg getRegularMsg(int messageLength, MessageType messageType,
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
			 * have TODO Each ‘have’ message has a payload that contains a
			 * 4-byte index of a file piece. It indicates that the sender has
			 * the piece referred to by the index. payload =
			 * filesystem.getIsIndex();
			 */

			return new RegularMsg(messageLength, messageType, payload);

		case 5:
			/*
			 * bitfield TODO find all the bits that correspond to the preset
			 * file chunks with current peer payload =
			 * filesystem.getPiecesInfo(index);
			 */
			return new RegularMsg(messageLength, messageType, payload);
		case 6:
			/*
			 * request TODO Each ‘request’ message has a 4-byte payload, which
			 * contains the index of the piece being requested payload =
			 * filesystem.getPiecesInfo(index);
			 */
			return new RegularMsg(messageLength, messageType, payload);
		case 7:
			/*
			 * piece TODO Each ‘piece’ message has a payload consisting of a
			 * 4-byte piece index and the contentof the piece. It is used to
			 * send a piece from the sender to a receiving peer payload =
			 * filesystem.getData(index);
			 */
			return new RegularMsg(messageLength, messageType, payload);
		default:
			break;
		}
		return null;
	}
}
