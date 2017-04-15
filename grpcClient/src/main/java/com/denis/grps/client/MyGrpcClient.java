package com.denis.grps.client;

import com.denis.grps.GreetingServiceGrpc;
import com.denis.grps.HelloRequest;
import com.denis.grps.HelloResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.io.IOException;

public class MyGrpcClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        // 1. Channel, ManagedChannel, usePlainText?
        // 2. Load Balancing, Name Resolver
        // 3. Blocking vs Non-blocking Stubs, and Futures
        // 4. Builders

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 8080)
                .usePlaintext(true)
                .build();

        GreetingServiceGrpc.GreetingServiceBlockingStub stub = GreetingServiceGrpc
                .newBlockingStub(channel);

        HelloResponse response = stub.greeting(
                HelloRequest
                        .newBuilder()
                        .setFirstName("Denis")
                        .setAge(30)
                        .addHobbies("programming")
                        .addHobbies("travel")
                        .putBagOfTricks("live coding", "not very good")
                        .build()
        );

        System.out.println(response);
    }
}
