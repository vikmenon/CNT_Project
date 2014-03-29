CNT_Project
===========

CNT Project, Spring 2014

For remote peer management, please see: [README-RemotePeers.md](README-RemotePeers.md)

PROGRESS MADE:
--------------

1. Run the class FileSharer as a Java application

2. It automatically sets up N peers using the modified StartRemotePeers class, which reads from PeerInfo.cfg in the present format:
<br/>peerID peerHost peerPort

3. The original peer sends a Handshake message to each newly created peer.

4. Then, the sample file is split into pieces, and the pieces are sent to each peer using the protocol message format.

5. When all peers have received all the pieces, original peer shuts down.

6. Each peer shuts down after reassembling the file.
