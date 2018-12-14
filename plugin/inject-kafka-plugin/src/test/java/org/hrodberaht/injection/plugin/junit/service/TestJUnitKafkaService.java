package org.hrodberaht.injection.plugin.junit.service;

import com.salesforce.kafka.test.KafkaTestUtils;
import org.hrodberaht.injection.core.Module;
import org.hrodberaht.injection.core.register.InjectionFactory;
import org.hrodberaht.injection.core.stream.InjectionRegistryBuilder;
import org.hrodberaht.injection.plugin.junit.ContainerContext;
import org.hrodberaht.injection.plugin.junit.ContainerContextConfigBase;
import org.hrodberaht.injection.plugin.junit.JUnit5Extension;
import org.hrodberaht.injection.plugin.junit.api.resource.ResourceProvider;
import org.hrodberaht.injection.plugin.junit.plugins.KafkaPlugin;
import org.hrodberaht.injection.plugin.junit.service.sample.Article;
import org.hrodberaht.injection.plugin.junit.service.sample.ArticlesService;
import org.hrodberaht.injection.plugin.junit.service.sample.ArticlesStore;
import org.hrodberaht.injection.plugin.junit.service.sample.KafkaListenerManager;
import org.hrodberaht.injection.plugin.junit.service.sample.KafkaListenerRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

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


    @BeforeEach
    public void init(){
        articlesService.clean();
    }

    @Test
    public void testBasicSetup(){

        final String topicName = "articles_1";

        assertEquals(0, articlesStore.getSize());
        articlesService.topicName(topicName);
        String id = articlesService.save(new Article( "shoe", new BigDecimal(12)));
        articlesService.flush();

        kafkaListenerRunner.runOnce("article_store_1", topicName);

        assertEquals(1, articlesStore.getSize());
        assertEquals("shoe", articlesStore.get(id).getName());
        assertEquals(new BigDecimal(12), articlesStore.get(id).getPrice());
    }

    @Test
    public void testForSingleConsumer() throws InterruptedException {

        testWithConsumersAndWait("articles_2", 1);

    }

    private void testWithConsumersAndWait(String topicName, int consumers) {
        final KafkaTestUtils kafkaTestUtils = new KafkaTestUtils(kafkaPlugin.getEmbedded());
        kafkaTestUtils.createTopic(topicName, consumers, (short) 1);
        assertEquals(0, articlesStore.getSize());
        articlesService.topicName(topicName);
        KafkaListenerManager kafkaListenerManager = new KafkaListenerManager(kafkaListenerRunner, "article_store_2", topicName, consumers);
        int messagesToProduceWarmUp = 10;
        int messagesToProduce = 100;

        // Most time is spent waiting for the message consumer to rebalance to find the partitions (aprox 5-6 seconds)

        // Warm up
        runAMessageRound(kafkaListenerManager, messagesToProduceWarmUp, 10000);
        runAMessageRound(kafkaListenerManager, messagesToProduce, 10000);

        kafkaListenerManager.stop();
        assertEquals(messagesToProduce + messagesToProduceWarmUp, articlesStore.getSize());
    }

    @Test
    public void testForMultipleConsumers() throws InterruptedException {
        testWithConsumersAndWait("articles_3", 2);
    }


    private int runAMessageRound(KafkaListenerManager kafkaListenerManager, int messagesToProduce, int timeout) {

        kafkaListenerManager.resetCounter();
        ExecutorService executor = Executors.newFixedThreadPool(4);
        final AtomicInteger counter = new AtomicInteger(0);
        while(counter.incrementAndGet() <= messagesToProduce) {

            executor.execute( () -> {
                int count = counter.get();
                articlesService.save(new Article("shoe_"+count, new BigDecimal(12+count)));
                if(count%100 == 0){
                    articlesService.flush();
                }
            } );

        }

        kafkaListenerManager.waitForConsume(messagesToProduce, timeout);


        return messagesToProduce;
    }

    public static class InnerConfig extends ContainerContextConfigBase {
        @Override
        public void register(InjectionRegistryBuilder registryBuilder) {

            KafkaPlugin kafkaPlugin = activatePlugin(new KafkaPlugin().kafkaConfigWriteMessages(10));

            registryBuilder.module(() -> {
                Module module = new Module();
                module.register(ArticlesService.class);
                module.register(KafkaListenerRunner.class);
                module.register(ArticlesStore.class);
                module.register(KafkaListenerManager.class);

                addResourcesToModule(module, kafkaPlugin.resources());
                return module;
            });


        }

        private void addResourcesToModule(Module module, Set<ResourceProvider> resources) {
            for(ResourceProvider resourceProvider:resources){

                module.register(resourceProvider.getType()).withFactory(new InjectionFactory() {
                    @Override
                    public Object getInstance() {
                        return resourceProvider.getInstance();
                    }

                    @Override
                    public Class getInstanceType() {
                        return resourceProvider.getType();
                    }

                    @Override
                    public String name() {
                        return resourceProvider.getName();
                    }

                    @Override
                    public boolean newObjectOnInstance() {
                        return false;
                    }
                });
            }
        }
    }
}
