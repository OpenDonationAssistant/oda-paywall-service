package io.github.opendonationassistant;

import io.github.opendonationassistant.rabbit.RabbitClient;
import io.micronaut.context.ApplicationContextBuilder;
import io.micronaut.context.ApplicationContextConfigurer;
import io.micronaut.context.annotation.ContextConfigurer;
import io.micronaut.context.annotation.Factory;
import io.micronaut.rabbitmq.connect.ChannelPool;
import io.micronaut.runtime.Micronaut;
import io.micronaut.serde.ObjectMapper;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.jspecify.annotations.NonNull;

@Factory
public class Application {

  public static void main(String[] args) {
    Micronaut.build(args).banner(false).start();
  }

  @ContextConfigurer
  public static class Configurer implements ApplicationContextConfigurer {

    @Override
    public void configure(@NonNull ApplicationContextBuilder builder) {
      builder.defaultEnvironments("allinone");
    }
  }

  @Singleton
  @Named("commands")
  public RabbitClient commandsFacade(ChannelPool pool, ObjectMapper mapper) {
    return new RabbitClient(pool, mapper, "commands");
  }
}

