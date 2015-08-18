package hmm.build.sockets;

import hmm.build.settings.FtpSetting;
import hmm.build.settings.Settings;
import hmm.build.util.SettingsUtil;
import hmm.build.util.ShowMessageUtil;
import hmm.build.util.SocketThreadUtil;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.swt.widgets.Display;

public class SocketServer extends Thread {
	
	private ServerSocketChannel serverSocketChannel;
	private ByteBuffer buffer = ByteBuffer.allocate(8192);
	private Selector selector;
	private CharsetDecoder decoder;
	private CharsetEncoder encoder;
	private boolean exit = false;
	private Map<InetAddress, FtpSetting> map = new HashMap<InetAddress, FtpSetting>();
	private static Lock lock = new ReentrantLock();
	
	@Override
	public void run() {
		lock.lock();
		try {
			decoder = new SocketThreadUtil().createCharsetDecoder();
			encoder = new SocketThreadUtil().createCharsetEncoder();
			serverSocketChannel = ServerSocketChannel.open();
			selector = Selector.open();
			serverSocketChannel.socket().bind(new InetSocketAddress(Settings.getInstance().getServerPort()));
			serverSocketChannel.configureBlocking(false);
		    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		    while(!exit) {
				selector.select();
			    Set<SelectionKey> keys = selector.selectedKeys();
			    for (Iterator<SelectionKey> i = keys.iterator(); i.hasNext();) {
			        SelectionKey key = (SelectionKey) i.next();
			        i.remove();
		            if (key.isAcceptable()) {
		            	SocketChannel client = ((ServerSocketChannel) key.channel()).accept();
		            	client.configureBlocking(false);
		            	client.register(selector, SelectionKey.OP_READ);
			        } else if(key.isReadable()) {
			        	SocketChannel client = (SocketChannel) key.channel();
			            int bytesread = client.read(buffer);
			            if (bytesread > 0) {
			            	String request = receive();
				            if (request.equals("ftp_conf")) {
				            	List<FtpSetting> models = Settings.getInstance().getFtpSettings();
				            	FtpSetting fm = null;
				            	for (FtpSetting ftpModel : models) {
				        			if(!ftpModel.isUsing()) {
				        				fm = setUsingFtp(ftpModel, client);
				        				break;
				        			}
				        		}
				            	if(fm != null) {
				            		String send = SettingsUtil.getInstance().getSendXml(fm);
				            		send(send, client);
				            	} else {
				            		send("no_ftp", client);
				            	}
				            } else if(request.equals("ftp_fin")) {
				            	releaseUsingFtp(client);
				            	send("ftp_release", client);
				            }
			            }
			        }
			    }
			}
			selector.close();
			serverSocketChannel.close();
			lock.unlock();
		} catch (BindException e) {
			e.printStackTrace();
			lock.unlock();
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					new ShowMessageUtil().showFatalError("Another HanMeiMei Server is running, this one will be forced to exit.");
				}
			});
		} catch (final IOException e) {
			e.printStackTrace();
			lock.unlock();
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					new ShowMessageUtil().showNormalError("Socket server captured exception: " + e.getMessage());
				}
			});
		}
	}
	
	public void quit() {
		exit = true;
		if(selector != null && selector.isOpen()) {
			selector.wakeup();
		}
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

	private FtpSetting setUsingFtp(FtpSetting ftpModel, SocketChannel client) {
		ftpModel.setUsing(true);
		map.put(client.socket().getInetAddress(), ftpModel);
		return ftpModel;
	}

	private void releaseUsingFtp(SocketChannel client) {
		FtpSetting model = map.get(client.socket().getInetAddress());
		model.setUsing(false);
	}
	
}
