package com.marketplace.storage.queue;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSelector;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
@EnableIntegration
@IntegrationComponentScan
public class StorageQueueConfig {

  @Value("storage-${environment.name}")
  private String queueName;

  @Bean("storageQueue")
  public Queue queue() {
    return new Queue(queueName, true);
  }

  @Bean("storageMessageChannel")
  public MessageChannel messageChannel() {
    return new PublishSubscribeChannel();
  }

  @Bean(value = "storageBinding")
  public Binding binding(@Qualifier("storageQueue") final Queue queue,
                         final FanoutExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange);
  }

  @Bean("storageAmqpOutboundFlow")
  public IntegrationFlow amqpOutboundFlow(final AmqpTemplate amqpTemplate,
                                          @Qualifier("storageMessageChannel") final MessageChannel publishSubscribeChannel,
                                          final FanoutExchange exchange) {
    return IntegrationFlows.from(publishSubscribeChannel)
        .handle(Amqp.outboundGateway(amqpTemplate).exchangeName(exchange.getName()))
        .get();
  }

  @Bean("storageAmqpInboundFlow")
  public IntegrationFlow amqpInboundFlow(final ConnectionFactory rabbitConnectionFactory,
                                         @Qualifier("storageQueueFilterImpl") final MessageSelector messageSelector,
                                         @Qualifier("storageMessageHandlerImpl") final MessageHandler messageHandler) {
    return IntegrationFlows
        .from(Amqp.inboundAdapter(rabbitConnectionFactory, this.queue()))
        .filter(messageSelector)
        .handle(messageHandler)
        .get();
  }
}