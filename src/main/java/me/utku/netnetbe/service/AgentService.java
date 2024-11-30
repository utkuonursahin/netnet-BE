package me.utku.netnetbe.service;

import lombok.RequiredArgsConstructor;
import me.utku.netnetbe.agentsocket.AgentConnectionTracker;
import me.utku.netnetbe.model.Agent;
import me.utku.netnetbe.repository.AgentRepository;
import org.springframework.stereotype.Service;

import java.net.Socket;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AgentService {
    private final AgentRepository agentRepository;
    private final AgentConnectionTracker agentConnections;

    public Agent handleInitialHandshake(Map<String, String> data, Socket agentSocket) {
        Agent agent = agentRepository.findByHardwareId(data.get("hardwareId")).orElse(new Agent());
        agent.setHardwareId(data.get("hardwareId"));
        agent.setName(data.get("hostName"));
        agentRepository.save(agent);
        agentConnections.addAgentConnection(agent.getHardwareId(), agentSocket);
        return agent;
    }


}
