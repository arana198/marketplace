package com.marketplace.notification.email.queue;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSelector;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.amqp.Amqp;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
@EnableIntegration
@IntegrationComponentScan
public class NotificationQueueConfig {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Value("${exchange.name}")
    private String exchangeName;

    @Value("notification-${environment.name}")
    private String queueName;

    @Bean("notificationQueue")
    public Queue queue() {
        return new Queue(queueName, true);
    }

    @Bean("notificationExchange")
    public FanoutExchange exchange() {
        return new FanoutExchange(exchangeName);
    }

    @Bean("notificationMessageChannel")
    public MessageChannel messageChannel() {
        return new PublishSubscribeChannel();
    }

    @Bean(value = "notificationBinding")
    public Binding binding(@Qualifier("notificationQueue") final Queue queue,
                           @Qualifier("notificationExchange") final FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean("notificationAmqpOutboundFlow")
    public IntegrationFlow amqpOutboundFlow(@Qualifier("notificationMessageChannel") final MessageChannel publishSubscribeChannel) {
        return IntegrationFlows.from(publishSubscribeChannel)
                .handleWithAdapter(h -> h.amqp(amqpTemplate).exchangeName(exchangeName))
                .get();
    }

    @Bean("notificationAmqpInboundFlow")
    public IntegrationFlow amqpInboundFlow(final ConnectionFactory rabbitConnectionFactory,
                                           @Qualifier("notificationQueueFilterImpl") final MessageSelector messageSelector,
                                           @Qualifier("notificationMessageHandlerImpl") final MessageHandler messageHandler) {
        return IntegrationFlows
                .from(Amqp.inboundAdapter(rabbitConnectionFactory, this.queue()))
                .filter(messageSelector)
                .handle(messageHandler)
                .get();
    }
}