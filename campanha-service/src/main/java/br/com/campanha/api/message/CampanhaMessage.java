package br.com.campanha.api.message;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface CampanhaMessage {

    @Output
    MessageChannel output();
}