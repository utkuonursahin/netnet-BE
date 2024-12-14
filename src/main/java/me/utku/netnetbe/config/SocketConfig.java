package me.utku.netnetbe.config;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.RequiredArgsConstructor;
import me.utku.netnetbe.agentsocket.AgentSocketService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SocketConfig implements CommandLineRunner {
    private final AgentSocketService agentSocketService;

    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setPort(9092);
        return new SocketIOServer(config);
    }

    @Override
    public void run(String... args) throws Exception {
        Thread.ofVirtual().start(agentSocketService::start);
        socketIOServer().start();
        //Runtime.getRuntime().addShutdownHook(Thread.startVirtualThread(agentSocketService::stop));
    }
}
