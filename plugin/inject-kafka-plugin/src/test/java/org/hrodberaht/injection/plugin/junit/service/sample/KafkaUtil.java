package org.hrodberaht.injection.plugin.junit.service.sample;

import kafka.server.KafkaConfig;

public class KafkaUtil {
    public static String getUrl(KafkaConfig kafkaConfig) {
        String url = kafkaConfig.getString("host.name")+":"+kafkaConfig.getInt("advertised.port");
        return url;
    }
}
