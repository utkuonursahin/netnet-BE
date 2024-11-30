package me.utku.netnetbe.agentsocket;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.utku.netnetbe.service.AgentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AgentSocketService {
    private final AgentConnectionTracker agentConnections;
    private final ExecutorService executor = Executors.newCachedThreadPool(Thread.ofVirtual().factory());
    private final AgentService agentService;
    @Value("${spring.application.agent_socket.port}")
    private int port;

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("Agent server started on port: {}", port);
            while (true) {
                Socket agentSocket = serverSocket.accept();
                executor.execute(() -> handleAgentConnection(agentSocket));
            }
        } catch (IOException e) {
            log.error("Error starting the agent server: {}", e.getMessage());
        }
    }

    private void handleAgentConnection(Socket agentSocket) {
        try {
            InputStream input = agentSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));

            OutputStream output = agentSocket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            String response = processAgentConnectionData(reader.readLine(), agentSocket);
            writer.println(response);
        } catch (IOException e) {
            log.error("Error handling agent connection: {}", e.getMessage());
        }
    }

    public void handleRealtimeDataRequest(String hardwareId, SocketIOClient client) {
        Socket agentSocket = agentConnections.getAgentConnection(hardwareId);
        try {
            InputStream input = agentSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            OutputStream output = agentSocket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            writer.println("startStream");

            String line;
            while ((line = reader.readLine()) != null) {
                Map<String, String> response = processAgentRealtimeData(line);
                client.getNamespace().getBroadcastOperations().sendEvent("RECEIVE_REALTIME_DATA", response);
            }
        } catch (IOException e) {
            log.error("Error requesting realtime data: {}", e.getMessage());
        }
    }

    private String processAgentConnectionData(String data, Socket agentSocket) {
        Map<String, String> parsedData = parseIncomingData(data);
        if (parsedData.get("type").equals("init")) {
            log.info("Initial handshake received: {}", agentSocket.getInetAddress().getHostAddress());
            agentService.handleInitialHandshake(parsedData, agentSocket);
        }
        return "ACK";
    }

    private Map<String, String> processAgentRealtimeData(String data) {
        Map<String, String> parsedData = parseIncomingData(data);
        if (parsedData.get("type").equals("realTimeData")) {
            log.info("Realtime data received: {}", parsedData);
            return parsedData;
        } else {
            return new HashMap<>();
        }
    }

    private Map<String, String> parseIncomingData(String input) {
        Map<String, String> data = new HashMap<>();
        String[] parts = input.split("--");
        for (String part : parts) {
            if (part.contains(":")) {
                String[] keyValue = part.split(":");
                String key = keyValue[0];
                String value = keyValue[1].replaceAll("[\\[\\]]", ""); // Remove square brackets
                data.put(key, value);
            } else if (part.contains("[")) { // Handle cases without ":"
                String key = part.substring(0, part.indexOf("["));
                String value = part.substring(part.indexOf("[") + 1, part.indexOf("]"));
                data.put(key, value);
            }
        }
        return data;
    }

    private void closeConnection(Socket agentSocket) {
        try {
            String agentId = agentSocket.getInetAddress().getHostAddress();
            agentConnections.removeAgentConnection(agentId);
            agentSocket.close();
            log.info("Connection closed for agent: {}", agentId);
        } catch (IOException e) {
            log.error("Error closing agent connection: {}", e.getMessage());
        }
    }

    public void stop() {
        try {
            executor.shutdownNow();
            for (Socket socket : agentConnections.getAllAgentConnections()) {
                socket.close();
            }
            log.info("Agent server stopped.");
        } catch (IOException e) {
            log.error("Error stopping the agent server: {}", e.getMessage());
        }
    }
}
