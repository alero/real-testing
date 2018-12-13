package org.hrodberaht.injection.plugin.junit.service;

import com.salesforce.kafka.test.KafkaProvider;
import org.hrodberaht.injection.core.Module;
import org.hrodberaht.injection.core.register.InjectionFactory;
import org.hrodberaht.injection.core.stream.InjectionRegistryBuilder;
import org.hrodberaht.injection.plugin.junit.ContainerContext;
import org.hrodberaht.injection.plugin.junit.ContainerContextConfigBase;
import org.hrodberaht.injection.plugin.junit.JUnit5Extension;
import org.hrodberaht.injection.plugin.junit.plugins.KafkaPlugin;
import org.hrodberaht.injection.plugin.junit.service.sample.Article;
import org.hrodberaht.injection.plugin.junit.service.sample.ArticlesService;
import org.hrodberaht.injection.plugin.junit.service.sample.ArticlesStore;
import org.hrodberaht.injection.plugin.junit.service.sample.KafkaListenerRunner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContainerContext(TestJUnitKafkaService.InnerConfig.class)
@ExtendWith(JUnit5Extension.class)
public class TestJUnitKafkaService {

    private static final Logger LOG = LoggerFactory.getLogger(TestJUnitKafkaService.class);

    @Inject
    private KafkaPlugin kafkaPlugin;

    @Inject
    private ArticlesStore articlesStore;

    @Inject
    private ArticlesService articlesService;

    @Inject
    private KafkaListenerRunner kafkaListenerRunner;

    @Test
    public void testForBasicSetup(){

        assertEquals(0, articlesStore.getSize());

        String id = articlesService.save(new Article( "shoe", new BigDecimal(12)));
        articlesService.flush();

        kafkaListenerRunner.runOnce("article_store_1");

        kafkaListenerRunner.runOnce("article_store_1");

        assertEquals(1, articlesStore.getSize());
        assertEquals("shoe", articlesStore.get(id).getName());
        assertEquals(new BigDecimal(12), articlesStore.get(id).getPrice());
    }

    @Test
    public void testForMultipleConsume() throws InterruptedException {

        assertEquals(0, articlesStore.getSize());

        new Thread(){
            @Override
            public void run() {
                kafkaListenerRunner.runOnceWithSubscribe("article_store_1");
            }
        }.start();

        Thread.sleep(1000);

        String id = articlesService.save(new Article( "shoe", new BigDecimal(12)));
        articlesService.flush();

        Thread.sleep(10000);

        assertEquals(1, articlesStore.getSize());
        assertEquals("shoe", articlesStore.get(id).getName());
        assertEquals(new BigDecimal(12), articlesStore.get(id).getPrice());
    }

    public static class InnerConfig extends ContainerContextConfigBase {
        @Override
        public void register(InjectionRegistryBuilder registryBuilder) {

            KafkaPlugin kafkaPlugin = activatePlugin(new KafkaPlugin());

            registryBuilder.module(() -> {
                Module module = new Module();
                module.register(ArticlesService.class);
                module.register(KafkaListenerRunner.class);
                module.register(ArticlesStore.class);
                module.register(KafkaProvider.class).withFactory(new InjectionFactory<KafkaProvider>() {
                    @Override
                    public KafkaProvider getInstance() {
                        return kafkaPlugin.getEmbedded();
                    }

                    @Override
                    public Class getInstanceType() {
                        return KafkaProvider.class;
                    }

                    @Override
                    public boolean newObjectOnInstance() {
                        return false;
                    }
                });
                return module;
            });


        }
    }
}
