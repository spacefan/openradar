package de.knewcleus.fgfs.multiplayer;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.logging.Logger;

import de.knewcleus.fgfs.IUpdateable;
import de.knewcleus.fgfs.location.Position;
import de.knewcleus.fgfs.multiplayer.protocol.MultiplayerPacket;
import de.knewcleus.fgfs.multiplayer.protocol.PositionMessage;

public abstract class MultiplayerClient<T extends Player> extends AbstractMultiplayerEndpoint<T> implements IUpdateable {
	protected static Logger logger=Logger.getLogger("de.knewcleus.fgfs.multiplayer");
	protected final InetAddress serverAddress;
	protected final int serverPort;
	protected final Queue<String> chatQueue=new ArrayDeque<String>();
	protected String lastChatMessage="";
	
	public MultiplayerClient(IPlayerRegistry<T> playerRegistry) throws IOException {
		super(playerRegistry, getStandardPort());
		String serverName=System.getProperty("de.knewcleus.fgfs.multiplayer.server.host", "localhost");
		serverAddress=InetAddress.getByName(serverName);
		serverPort=Integer.getInteger("de.knewcleus.fgfs.multiplayer.server.port", MultiplayerServer.STANDARD_PORT);
	}
	
	protected static int getStandardPort() {
		return Integer.getInteger("de.knewcleus.fgfs.multiplayer.client.port", 0);
	}

	@Override
	protected void processPacket(T player, MultiplayerPacket mppacket) throws MultiplayerException {
		if (mppacket.getMessage() instanceof PositionMessage) {
			PositionMessage positionMessage=(PositionMessage)mppacket.getMessage();
			player.updatePosition(System.currentTimeMillis(),positionMessage);
		}
	}
	
	public void sendChatMessage(String message) {
		chatQueue.add(message);
	}
	
	public void sendPacket(MultiplayerPacket mppacket) throws MultiplayerException {
		sendPacket(serverAddress, serverPort, mppacket);
	}
	
	public void update(double dt) {
		PositionMessage positionMessage=new PositionMessage();
		positionMessage.setModel(getModel());
		positionMessage.setPosition(getPosition());
		positionMessage.setOrientation(getOrientation());
		positionMessage.setLinearVelocity(getLinearVelocity());
		// TODO: update the rest
		
		if (chatQueue.size()>0) {
			lastChatMessage=chatQueue.remove();
		}
		positionMessage.putProperty("sim/multiplay/chat", lastChatMessage);
		
		MultiplayerPacket mppacket=new MultiplayerPacket(getCallsign(), positionMessage);
		try {
			sendPacket(serverAddress, serverPort, mppacket);
		} catch (MultiplayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public abstract String getModel();
	
	public abstract String getCallsign();
	
	public abstract Position getPosition();
	
	public abstract Position getOrientation();
	
	public abstract Position getLinearVelocity();
}
