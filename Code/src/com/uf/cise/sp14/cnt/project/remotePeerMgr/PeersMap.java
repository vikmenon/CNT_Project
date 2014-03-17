package com.uf.cise.sp14.cnt.project.remotePeerMgr;

import java.util.HashMap;

/**
 * @author Rahul
 *
 */
public class PeersMap {
	/**
	 * this is a mapping of peer ID and the peer info of all the peers in the connection
	 */
	HashMap<Integer,RemotePeerInfo> peersMap = new HashMap<Integer,RemotePeerInfo>();

	public HashMap<Integer, RemotePeerInfo> getPeersMap() {
		return peersMap;
	}

	public void setPeersMap(HashMap<Integer, RemotePeerInfo> peersMap) {
		this.peersMap = peersMap;
	}

	@Override
	public String toString() {
		return "PeersMap [peersMap=" + peersMap + "]";
	} 
}
