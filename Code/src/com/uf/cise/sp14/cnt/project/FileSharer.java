/* TODO:
 * 
 * HACK: 4 bytes used for messageType enum
 * logEvents
 * Delete part files after complete send?
 */

package com.uf.cise.sp14.cnt.project;

import java.io.File;
import java.util.List;

import com.uf.cise.sp14.cnt.project.constants.FileSharerConstants;
import com.uf.cise.sp14.cnt.project.exception.FileShareException;
import com.uf.cise.sp14.cnt.project.networkDataManager.Client;
import com.uf.cise.sp14.cnt.project.networkDataManager.Server;
import com.uf.cise.sp14.cnt.project.protocolManager.MessageGenerator;
import com.uf.cise.sp14.cnt.project.protocolManager.messages.HandshakeMsg;
import com.uf.cise.sp14.cnt.project.protocolManager.messages.Message;
import com.uf.cise.sp14.cnt.project.protocolManager.messages.RegularMsg;
import com.uf.cise.sp14.cnt.project.remotepeer.RemotePeerInfo;
import com.uf.cise.sp14.cnt.project.remotepeer.StartRemotePeers;
import com.uf.cise.sp14.cnt.project.util.ApplicationUtils;
import com.uf.cise.sp14.cnt.project.util.FileUtils;
import com.uf.cise.sp14.cnt.project.util.FileUtils.FileOperation;

public class FileSharer {
	/**
	 * @param args
	 * @throws FileShareException
	 */
	public static void main(String args[]) throws FileShareException {
		/* Peer ID and Port for this node is assumed to be 0 unless 
		 * otherwise specified at startup.
		 * 
		 * Port = 0 instructs the program to use any available port.
		 */
		String myPeerID = "0";
		String myPort = "0";
		
		if (args.length >= 1) {
			myPeerID = args[0];
		}
		if (args.length >= 2) {
			myPort = args[1];
		}
		Integer myIDInt = Integer.valueOf(myPeerID);
		Integer myPortInt = Integer.valueOf(myPort);
		
		// Fetch the ID of this peer provided at startup.
		RemotePeerInfo myNode = new RemotePeerInfo(myPeerID, FileSharerConstants.localhost, myPort);
		StartRemotePeers startedPeers = null;
		
		// Test scenario: Transfer GDP.csv to all peers configured in PeerInfo
		String testFileName = "GDP.csv";
		File testFile = new File(testFileName);
		
		if (0 == myIDInt.compareTo(FileSharerConstants.OriginalPeer)) {
			/* This peer is an original instance, and must set up the peers 
			 * configured in PeerInfo.
			 * 
			 * NOTE that the original instance also acts as the *TRACKER*, 
			 * since we have details of all the peers in .
			 */
			ApplicationUtils.printLine("Original peer was started.");
			startedPeers = new StartRemotePeers();
			startedPeers.startConfiguredPeers();
			
			for (RemotePeerInfo peer : startedPeers.peerInfoVector) {
				// Set up the TCP communication link
				peer.client = new Client(peer.address, Integer.valueOf(peer.port));
				
				// Announce my ID to the peer via Handshake
				HandshakeMsg handshake = MessageGenerator.getHandshakeMessage(myIDInt);
				peer.client.sendMessage(handshake);
				
				// Split and send the data to our peers
				int parts = FileUtils.splitFile(testFile, FileOperation.SEND);
				
				for (int i = 0; i < parts; i++) {
					List<RegularMsg> pieces = MessageGenerator.getPieceMsgsFromPartFile(testFileName, i);
					
					for(RegularMsg piece : pieces) {
						peer.client.sendMessage(piece);
					}
				}
				
				// Tear down the TCP communication link
				peer.client.close();
			}
		}
		else {
			/* This peer was created by an original instance.
			 * 
			 * Test scenario: Check that GDP.csv is NOT downloaded yet, 
			 * then proceed to download it from the original peer/tracker.
			 */
			myNode.server = new Server(myPortInt);
			
			// Wait for the Handshake message announcing any client's peerID
			Message msg = myNode.server.waitForMessage();
			
			if (msg instanceof HandshakeMsg) {
				// Reassemble the file from chunks received in messages
				do {
					msg = myNode.server.waitForMessage();
					if (msg instanceof RegularMsg) {
						/* Each file part may consist of multiple messages.
						 * Thus data needs to be appended to the part file.
						 */
						FileUtils.writePartDataToFile((RegularMsg) msg, testFileName);
					}
				} while (0 == FileUtils.mergeFile(testFileName, testFile.length(), FileOperation.RECV));
			}
			
			// Tear down the TCP communication link
			myNode.server.close();
		}
	}
}
