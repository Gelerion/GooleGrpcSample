package com.denis.grps.chat.server;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ChatClient extends Application {
    private ObservableList<String> messages = FXCollections.observableArrayList();
    private ListView<String> messagesView = new ListView<>();
    private TextField name = new TextField("name");
    private TextField message = new TextField("message");
    private Button send = new Button("Send");

    public static void main(String[] args) {
        launch(args);
    }

    private void init(Stage primaryStage) {
        messagesView.setItems(messages);

        BorderPane pane = new BorderPane();
        pane.setLeft(name);
        pane.setCenter(message);
        pane.setRight(send);

        BorderPane root = new BorderPane();
        root.setCenter(messagesView);
        root.setBottom(pane);

        primaryStage.setTitle("gRPC Chat");
        primaryStage.setScene(new Scene(root, 480, 320));

        primaryStage.show();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        init(primaryStage);

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                                                        .usePlaintext(true)
                                                        .build();
        ChatServiceGrpc.ChatServiceStub chatService = ChatServiceGrpc.newStub(channel);

        //listener for messages coming from server to the client
        StreamObserver<ChatMessage> toServer = chatService.chat(new StreamObserver<ChatMessageFromServer>() {
            @Override
            public void onNext(ChatMessageFromServer value) {
                Platform.runLater(() -> messages.add(
                        String.format("%s: %s",
                                      value.getMessage().getFrom(),
                                      value.getMessage().getMessage())));
            }

            @Override
            public void onError(Throwable t) {
                //do nothing
                t.printStackTrace();
            }

            @Override
            public void onCompleted() { //do nothing
            }
        });

        send.setOnAction(e -> toServer.onNext(ChatMessage.newBuilder()
                             .setFrom(name.getText())
                             .setMessage(message.getText())
                             .build()));

    }
}
