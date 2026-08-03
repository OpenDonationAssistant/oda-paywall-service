package io.github.opendonationassistant.repository;

import jakarta.inject.Singleton;

@Singleton
public class PaywallRecipientRepository {

  private final PaywallRecipientDataRepository repository;

  public PaywallRecipientRepository(PaywallRecipientDataRepository repository) {
    this.repository = repository;
  }

  public PaywallRecipientData get(String id) {
    return repository
      .findById(id)
      .orElseGet(() -> new PaywallRecipientData(id, "", false));
  }

  public PaywallRecipientData create(
    String id,
    String recipientId,
    Boolean enabled
  ) {
    return repository.save(new PaywallRecipientData(id, recipientId, enabled));
  }
}
