package io.github.opendonationassistant.repository;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
@MappedEntity("paywall_recipients")
public record PaywallRecipientData(
  @Id String id,
  String recipientId,
  Boolean enabled
) {}
