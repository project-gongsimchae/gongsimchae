package techit.gongsimchae.global.config.kafka;

import java.util.List;

public class KafkaConstants {
    public static final String KAFKA_AI_TOPIC = "chat-ai-topic";
    public static final String KAFKA_TOPIC = "chat-topic";
    public static final String GROUP_ID = "chat-group";
    public static final String KAFKA_BROKER = "localhost:9092";
    public static List<Integer> partitionList;
}
