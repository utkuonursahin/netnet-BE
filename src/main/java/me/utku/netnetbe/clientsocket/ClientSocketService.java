package me.utku.netnetbe.clientsocket;

import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.utku.netnetbe.agentsocket.AgentSocketService;
import me.utku.netnetbe.dto.AgentDto;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientSocketService {
    private final AgentSocketService agentSocketService;

    public ConnectListener onSocketConnection() {
        return client -> log.info(String.format("SockedID: %s connected", client.getSessionId().toString()));
    }

    public DisconnectListener onSocketDisconnection() {
        return client -> {
            log.info(String.format("SockedID: %s disconnected", client.getSessionId().toString()));
            client.disconnect();
        };
    }

    public DataListener<AgentDto> onRealtimeDataRequest() {
        return (client, data, ackSender) -> agentSocketService.handleRealtimeDataRequest(data.hardwareId(), client);
    }

    public DataListener<AgentDto> onRealtimeDataStopRequest() {
        return (client, data, ackSender) -> agentSocketService.handleRealtimeDataStopRequest(data.hardwareId());
    }
}
