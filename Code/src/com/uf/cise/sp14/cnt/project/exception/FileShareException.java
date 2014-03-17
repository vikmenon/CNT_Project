package com.uf.cise.sp14.cnt.project.exception;

/**
 * @author vikmenon
 *
 * Custom exception thrown by FileSharer in case of unrecoverable exception.
 */
public class FileShareException extends Exception {
	private static final long serialVersionUID = 1L;

	/** Pass the exception and the message to the superclass.
	 * 
	 * @param msg
	 * @param t
	 */
	public FileShareException(String msg, Throwable t) {
		super(msg, t);
	}
}
