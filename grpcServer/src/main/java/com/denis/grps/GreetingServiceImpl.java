package com.denis.grps;

import io.grpc.stub.StreamObserver;

public class GreetingServiceImpl extends GreetingServiceGrpc.GreetingServiceImplBase {

    @Override
    public void greeting(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        System.out.println(request);
        responseObserver.onNext(HelloResponse.newBuilder()
           .setGreeting("Hello " + request.getFirstName())
        .build());

        responseObserver.onCompleted();
    }
}
