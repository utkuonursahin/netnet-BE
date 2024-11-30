package me.utku.netnetbe.clientsocket;

import com.corundumstudio.socketio.SocketIOServer;
import me.utku.netnetbe.dto.AgentDto;
import me.utku.netnetbe.enums.ClientEvent;
import org.springframework.stereotype.Component;

@Component
public class ClientSocketConfigurer {
    public ClientSocketConfigurer(SocketIOServer socketIOServer, ClientSocketService clientSocketService) {
        socketIOServer.addConnectListener(clientSocketService.onSocketConnection());
        socketIOServer.addDisconnectListener(clientSocketService.onSocketDisconnection());
        socketIOServer.addEventListener(ClientEvent.REQUEST_REALTIME_DATA.name(), AgentDto.class, clientSocketService.onRealtimeDataRequest());
        socketIOServer.addEventListener(ClientEvent.STOP_REALTIME_DATA.name(), AgentDto.class, clientSocketService.onRealtimeDataStopRequest());
    }
}
