/**
 * 
 */
package com.uf.cise.sp14.cnt.project.protocolManager.messages;

/**
 * @author Rahul
 *
 */
public enum MessageType {
	choke,
	unchoke,
	interested, 
	notInterested, 
	have, 
	bitfield, 
	request, 
	piece 
}
