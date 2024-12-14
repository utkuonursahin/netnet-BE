package me.utku.netnetbe.controller;

import lombok.RequiredArgsConstructor;
import me.utku.netnetbe.dto.AgentDto;
import me.utku.netnetbe.service.AgentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/agent")
@RequiredArgsConstructor
public class AgentController {
    private final AgentService agentService;

    @GetMapping
    public List<AgentDto> getAgents() {
        return agentService.getAgents();
    }
}
