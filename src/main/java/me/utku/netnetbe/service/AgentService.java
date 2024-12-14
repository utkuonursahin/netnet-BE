package me.utku.netnetbe.service;

import lombok.RequiredArgsConstructor;
import me.utku.netnetbe.agentsocket.AgentConnectionTracker;
import me.utku.netnetbe.dto.AgentDto;
import me.utku.netnetbe.model.Agent;
import me.utku.netnetbe.repository.AgentRepository;
import org.springframework.stereotype.Service;

import java.net.Socket;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AgentService {
    private final AgentRepository agentRepository;
    private final AgentConnectionTracker agentConnections;

    public void handleInitialHandshake(Map<String, String> data, Socket agentSocket) {
        Agent agent = agentRepository.findByHardwareId(data.get("hardwareId")).orElse(new Agent());
        agent.setHardwareId(data.get("hardwareId"));
        agent.setName(data.get("hostName"));
        agent.setCpuCores(Float.parseFloat(data.get("cpu")));
        agent.setRam(Float.parseFloat(data.get("ram")));
        agentRepository.save(agent);
        agentConnections.addAgentConnection(agent.getHardwareId(), agentSocket);
    }

    public List<AgentDto> getAgents() {
        return agentRepository.findAll().stream()
                .map(agent -> new AgentDto(agent.getId(), agent.getName(), agent.getHardwareId(), agent.getCpuCores(), agent.getRam()))
                .toList();
    }
}
