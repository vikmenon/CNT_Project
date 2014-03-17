package com.uf.cise.sp14.cnt.project.remotepeer;
/*
 *                     CEN5501C Project2
 * This is the program starting remote processes.
 * This program was only tested on CISE SunOS environment.
 * If you use another environment, for example, linux environment in CISE 
 * or other environments not in CISE, it is not guaranteed to work properly.
 * It is your responsibility to adapt this program to your running environment.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

import com.uf.cise.sp14.cnt.project.constants.FileSharerConstants;
import com.uf.cise.sp14.cnt.project.util.ApplicationUtils;

/*
 * The StartRemotePeers class begins remote peer processes. 
 * It reads configuration file PeerInfo.cfg and starts remote peer processes.
 * You must modify this program a little bit if your peer processes are written in C or C++.
 * Please look at the lines below the comment saying IMPORTANT.
 */
public class StartRemotePeers {
	private static final String PeerInfoFile = "PeerInfo.cfg";
	
	/* Hold the peer information read from the peer info file. */
	public Vector<RemotePeerInfo> peerInfoVector;
	
	public void getConfiguration()
	{
		String buf;
		peerInfoVector = new Vector<RemotePeerInfo>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(StartRemotePeers.PeerInfoFile));
			while((buf = in.readLine()) != null) {
				String[] tokens = buf.split("\\s+");
				peerInfoVector.addElement(new RemotePeerInfo(tokens[0], tokens[1], tokens[2]));
			}
			in.close();
		}
		catch (Exception ex) {
			ApplicationUtils.printLine(ex.toString());
		}
	}
	
	/**
	 * @param args
	 */
	public Vector<RemotePeerInfo> startConfiguredPeers() {
		try {
			getConfiguration();
			
			// get current path
			String path = System.getProperty("user.dir");
			
			// start clients at remote hosts
			for (int i = 0; i < this.peerInfoVector.size(); i++) {
				RemotePeerInfo pInfo = (RemotePeerInfo) this.peerInfoVector.elementAt(i);
				ApplicationUtils.printLine("Start remote peer " + pInfo.peerId +  " at " + pInfo.address );
				
				// Start this peer process
/* SERVER:
				Runtime.getRuntime().exec("ssh " + pInfo.address + " cd " + path + "; java " + FileSharerConstants.PeerExecutableName +  " " + pInfo.peerId);
 */
/* LOCAL: */
/* (LINUX)		String[] cmd = { "/bin/sh", "-c", "cd " + path }; */
				ApplicationUtils.printLine(path);
				String[] cmd = { "C:/Windows/System32/cmd", "/C", "cd " + path };
				Runtime applicationRuntime = Runtime.getRuntime();
				applicationRuntime.exec(cmd);
				applicationRuntime.exec("java " + FileSharerConstants.PeerExecutableName +  " " + pInfo.peerId + " " + pInfo.port);
			}
			
			ApplicationUtils.printLine("Starting all remote peers has done." );
		}
		catch (Exception ex) {
			ApplicationUtils.printLine(ex.toString());
		}
		
		return peerInfoVector;
	}
}
