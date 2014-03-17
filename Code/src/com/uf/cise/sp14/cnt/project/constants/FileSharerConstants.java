package com.uf.cise.sp14.cnt.project.constants;

import com.uf.cise.sp14.cnt.project.FileSharer;

/**
 * @author vikmenon
 *
 * This class holds constants used by code across FileSharer.
 */
public class FileSharerConstants {
	public static final String localhost = "127.0.0.1";
	public static final Integer OriginalPeer = 0;
	public static final String PeerExecutableName = "-cp bin " + FileSharer.class.getCanonicalName();
}
