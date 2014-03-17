package com.uf.cise.sp14.cnt.project;

import com.uf.cise.sp14.cnt.project.constants.FileSharerConstants;
import com.uf.cise.sp14.cnt.project.exception.FileShareException;
import com.uf.cise.sp14.cnt.project.networkDataManager.Client;
import com.uf.cise.sp14.cnt.project.networkDataManager.Server;
import com.uf.cise.sp14.cnt.project.remotepeer.RemotePeerInfo;
import com.uf.cise.sp14.cnt.project.remotepeer.StartRemotePeers;
import com.uf.cise.sp14.cnt.project.util.ApplicationUtils;

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
			
			/* Test scenario: Transfer GDP.csv to all peers configured in PeerInfo. */
//			String testFileName = "GDP.csv";
//			File testFile = new File(testFileName);
			
			for (RemotePeerInfo peer : startedPeers.peerInfoVector) {
				// Set up the TCP communication link
				peer.client = new Client(peer.address, Integer.valueOf(peer.port));
				
				// Announce my ID to the peer
				peer.client.sendHandshake(myIDInt);
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
			myNode.server.waitForHandshake();
		}
	}
}
