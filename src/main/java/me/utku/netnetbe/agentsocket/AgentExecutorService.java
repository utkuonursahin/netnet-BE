package me.utku.netnetbe.agentsocket;

import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class AgentExecutorService {
    private final ExecutorService executor = Executors.newCachedThreadPool(Thread.ofVirtual().factory());

    public void execute(Runnable runnable) {
        executor.execute(runnable);
    }
}
