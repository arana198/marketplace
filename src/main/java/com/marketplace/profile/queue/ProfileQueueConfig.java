package com.marketplace.profile.queue;

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
public class ProfileQueueConfig {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Value("${exchange.name}")
    private String exchangeName;

    @Value("profile-${environment.name}")
    private String queueName;

    @Bean("profileQueue")
    public Queue queue() {
        return new Queue(queueName, true);
    }

    @Bean("profileExchange")
    public FanoutExchange exchange() {
        return new FanoutExchange(exchangeName);
    }

    @Bean("profileMessageChannel")
    public MessageChannel messageChannel() {
        return new PublishSubscribeChannel();
    }

    @Bean(value = "profileBinding")
    public Binding binding(@Qualifier("profileQueue") final Queue queue,
                           @Qualifier("profileExchange") final FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean("profileAmqpOutboundFlow")
    public IntegrationFlow amqpOutboundFlow(@Qualifier("profileMessageChannel") final MessageChannel publishSubscribeChannel) {
        return IntegrationFlows.from(publishSubscribeChannel)
                .handleWithAdapter(h -> h.amqp(amqpTemplate).exchangeName(exchangeName))
                .get();
    }

    @Bean("profileAmqpInboundFlow")
    public IntegrationFlow amqpInboundFlow(final ConnectionFactory rabbitConnectionFactory,
                                           @Qualifier("profileQueueFilterImpl") final MessageSelector messageSelector,
                                           @Qualifier("profileMessageHandlerImpl") final MessageHandler messageHandler) {
        return IntegrationFlows
                .from(Amqp.inboundAdapter(rabbitConnectionFactory, this.queue()))
                .filter(messageSelector)
                .handle(messageHandler)
                .get();
    }
}