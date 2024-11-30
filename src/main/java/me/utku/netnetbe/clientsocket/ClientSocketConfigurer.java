package me.utku.netnetbe.clientsocket;

import com.corundumstudio.socketio.SocketIOServer;
import me.utku.netnetbe.dto.AgentDto;
import org.springframework.stereotype.Component;

@Component
public class ClientSocketConfigurer {
    public ClientSocketConfigurer(SocketIOServer socketIOServer, ClientSocketService clientSocketService) {
        socketIOServer.addConnectListener(clientSocketService.onSocketConnection());
        socketIOServer.addDisconnectListener(clientSocketService.onSocketDisconnection());
        socketIOServer.addEventListener("REQUEST_REALTIME_DATA", AgentDto.class, clientSocketService.onRealtimeDataRequest());
    }
}
