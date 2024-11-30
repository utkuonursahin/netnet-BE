package me.utku.netnetbe.agentsocket;

import org.springframework.stereotype.Component;

import java.net.Socket;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AgentConnectionTracker {
    private final ConcurrentHashMap<String, Socket> agentConnections = new ConcurrentHashMap<>();

    public void addAgentConnection(String agentId, Socket agentSocket) {
        agentConnections.put(agentId, agentSocket);
    }

    public Socket getAgentConnection(String agentId) {
        return agentConnections.get(agentId);
    }

    public void removeAgentConnection(String agentId) {
        agentConnections.remove(agentId);
    }

    public List<Socket> getAllAgentConnections() {
        return agentConnections.values().stream().toList();
    }
}
