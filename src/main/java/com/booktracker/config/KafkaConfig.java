package com.booktracker.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration
@EnableKafka
public class KafkaConfig {
    // Дополнительная конфигурация Kafka (если требуется) настраивается через application.properties или application.docker-compose.yml
}
