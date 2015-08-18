package hmm.build.sockets;

import hmm.build.ftp.FtpInfo;
import hmm.build.settings.Settings;
import hmm.build.util.SettingsUtil;
import hmm.build.util.SocketThreadUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;
import java.util.Set;

public class SocketClient {
	
	private SocketChannel clientSocketChannel;
	private ByteBuffer buffer = ByteBuffer.allocate(8192);
	private CharsetDecoder decoder;
	private CharsetEncoder encoder;
	private Selector selector;
	private String ip;
	private int port;
	
	public SocketClient() {
		ip = Settings.getInstance().getServerIp();
		port = Settings.getInstance().getServerPort();
		decoder = new SocketThreadUtil().createCharsetDecoder();
		encoder = new SocketThreadUtil().createCharsetEncoder();
	}
	
	public void setServerIp(String ip) {
		this.ip = ip;
	}
	
	public FtpInfo getFtpInformation() throws IOException {
		boolean finished = false;
		FtpInfo ftpInfo = null;
		connect();
		while(!finished) {
			selector.select();
		    Set<SelectionKey> keys = selector.selectedKeys();
		    for (Iterator<SelectionKey> i = keys.iterator(); i.hasNext();) {
		        SelectionKey key = (SelectionKey) i.next();
		        i.remove();
		        if(key.isConnectable()) {
		        	SocketChannel client = (SocketChannel) key.channel();
		        	boolean ret = client.finishConnect();
		        	if(ret == false)
		        		throw new IOException("Connecting to server failed by network problem.");
	        		send("ftp_conf", client);
	        		client.register(selector, SelectionKey.OP_READ);
		        } else if(key.isReadable()) {
		        	int bytesread = ((SocketChannel) key.channel()).read(buffer);
		            if (bytesread > 0) {
		            	String response = receive();
			            if(response.equals("no_ftp")) {
			            	finished = true;
			            	break;
			            }
			            ftpInfo = SettingsUtil.getInstance().parseReceivedXml(response);
			            if(ftpInfo != null)
			            	finished = true;
		            }
		        }
			}
		}
		disconnect();
		return ftpInfo;
	}
	
	public void releaseUsingFtp() throws IOException {
		connect();
		boolean finished = false;
		while(!finished) {
			selector.select();
		    Set<SelectionKey> keys = selector.selectedKeys();
		    for (Iterator<SelectionKey> i = keys.iterator(); i.hasNext();) {
		        SelectionKey key = (SelectionKey) i.next();
		        i.remove();
		        if(key.isConnectable()) {
		        	SocketChannel client = (SocketChannel) key.channel();
		        	boolean ret = client.finishConnect();
		        	if(ret == false)
		        		throw new IOException("Connecting to server failed by network problem.");
	        		send("ftp_fin", client);
	        		client.register(selector, SelectionKey.OP_READ);
		        } else if(key.isReadable()) {
		        	int bytesread = ((SocketChannel) key.channel()).read(buffer);
		            if (bytesread > 0) {
		            	String response = receive();
			            if(response.equals("ftp_release"))
			            	finished = true;
		            }
		        }
			}
		}
		disconnect();
	}
	
	private void connect() throws IOException {
		clientSocketChannel = SocketChannel.open();
		clientSocketChannel.configureBlocking(false);
		selector = Selector.open();
		clientSocketChannel.register(selector, SelectionKey.OP_CONNECT);
		SocketAddress address = new InetSocketAddress(ip, port);
		clientSocketChannel.connect(address);
	}
	
	private void disconnect() throws IOException {
		selector.close();
		clientSocketChannel.close();
	}
	
	private void send(String content, SocketChannel channel) throws IOException {
		ByteBuffer buf = encoder.encode(CharBuffer.wrap(content));
		channel.write(buf);
	}
	
	private String receive() throws CharacterCodingException {
		buffer.flip();
    	CharBuffer charBuffer = decoder.decode(buffer);
    	String received = charBuffer.toString().trim();
        buffer.clear();
        return received;
	}

}
