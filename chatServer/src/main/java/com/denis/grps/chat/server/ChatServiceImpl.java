package com.denis.grps.chat.server;

import io.grpc.stub.StreamObserver;

import java.util.LinkedHashSet;

public class ChatServiceImpl extends ChatServiceGrpc.ChatServiceImplBase {
    private static LinkedHashSet<StreamObserver<ChatMessageFromServer>> observers
            = new LinkedHashSet<>();

    @Override
    public StreamObserver<ChatMessage> chat(StreamObserver<ChatMessageFromServer> responseObserver) {
        observers.add(responseObserver);

        return new StreamObserver<ChatMessage>() {

            @Override
            public void onNext(ChatMessage value) {
                //receiving the data from client
                ChatMessageFromServer msg = ChatMessageFromServer
                        .newBuilder()
                        .setMessage(value)
                        .build();

                observers.forEach(o -> o.onNext(msg));
            }

            @Override
            public void onError(Throwable t) {
                observers.remove(responseObserver);
            }

            @Override
            public void onCompleted() {
                observers.remove(responseObserver);
            }
        };
    }
}
