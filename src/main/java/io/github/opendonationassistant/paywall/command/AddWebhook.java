package io.github.opendonationassistant.paywall.command;

import io.github.opendonationassistant.commons.micronaut.BaseController;
import io.github.opendonationassistant.repository.PaywallRecipientRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Inject;

@Controller
public class AddWebhook extends BaseController {

  private PaywallRecipientRepository repository;

  @Inject
  public AddWebhook(PaywallRecipientRepository repository) {
    this.repository = repository;
  }

  @Post("/paywall/commands/add-webhook")
  public HttpResponse<Void> addWebhook(
    Authentication auth,
    @Body AddWebhookRequest request
  ) {
    var owner = getOwnerId(auth);
    if (owner == null) {
      return HttpResponse.unauthorized();
    }
    repository.create(request.id(), owner.get(), true);
    return HttpResponse.ok();
  }

  @Serdeable
  public static record AddWebhookRequest(String id) {}
}
