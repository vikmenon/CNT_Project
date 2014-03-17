package com.uf.cise.sp14.cnt.project.remotePeerMgr;

import com.uf.cise.sp14.cnt.project.networkDataManager.Client;
import com.uf.cise.sp14.cnt.project.networkDataManager.Server;

/*
 *                     CEN5501C Project2
 * This is the program starting remote processes.
 * This program was only tested on CISE SunOS environment.
 * If you use another environment, for example, linux environment in CISE 
 * or other environments not in CISE, it is not guaranteed to work properly.
 * It is your responsibility to adapt this program to your running environment.
 */

public class RemotePeerInfo {
	public String peerId;
	public String address;
	public String port;
	
	/*NOTE:
	 * Server listens for connections and receives messages.
	 * Client initiates connections and sends messages.
	 * 
	 * Between any 2 peers, only one of these two will be active and the other is dropped, 
	 * and this contention is resolved via the Handshake mechanism.
	 */
	public Server server;
	public Client client;
	
	public RemotePeerInfo(String pId, String pAddress, String pPort) {
		peerId = pId;
		address = pAddress;
		port = pPort;
	}
}
