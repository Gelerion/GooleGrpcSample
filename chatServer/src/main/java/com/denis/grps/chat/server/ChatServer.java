package com.denis.grps.chat.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class ChatServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(8080)
                                     .addService(new ChatServiceImpl())
                                     .build();

        server.start();
        server.awaitTermination();
    }
}
