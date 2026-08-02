package io.github.opendonationassistant.paywall;

import io.github.opendonationassistant.commons.Amount;
import io.github.opendonationassistant.commons.logging.ODALogger;
import io.github.opendonationassistant.history.command.AddHistoryItemCommand;
import io.github.opendonationassistant.rabbit.RabbitClient;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.Nullable;

@Controller
public class PaywallWebhook {

  private final ODALogger log = new ODALogger(this);
  private final RabbitClient commandsClient;

  @Inject
  public PaywallWebhook(@Named("commands") RabbitClient commandsClient) {
    this.commandsClient = commandsClient;
  }

  @Post("/paywall/webhook/{recipientId}")
  public HttpResponse<String> handleWebhook(
    String recipientId, @Body Map<String, String> params
  ) {
    log.info(
      "Received Paywall webhook",
      Map.of(
        "recipientId", recipientId,
        "type", params.get("type"),
        "paymentId", params.get("paymentId")
      )
    );
    commandsClient.sendCommand(buildCommand(recipientId, params));
    return HttpResponse.ok("OK");
  }

  private AddHistoryItemCommand buildCommand(
    String recipientId,
    Map<String, String> params
  ) {
    var type = params.get("type");
    var safeType = type != null ? type : "";
    return new AddHistoryItemCommand(
      params.get("paymentId"),
      resolveNickname(params),
      recipientId,
      parseAmount(params.get("planPrice"), params.get("planCurrency")),
      buildMessage(safeType, params),
      Instant.now(),
      "paywall",
      params.get("txid"),
      List.of(),
      List.of(),
      List.of(),
      List.of(),
      List.of(),
      null,
      null,
      safeType,
      null,
      null,
      null,
      false,
      false,
      isPaymentEvent(safeType),
      false,
      false
    );
  }

  private String resolveNickname(Map<String, String> params) {
    var email = params.get("consumerEmail");
    if (email != null && !email.isBlank()) {
      return email;
    }
    var telegram = params.get("consumerTelegramId");
    if (telegram != null && !telegram.isBlank()) {
      return telegram;
    }
    return "paywall-user";
  }

  private Amount parseAmount(@Nullable String price, @Nullable String currency) {
    var cur = currency != null ? currency : "RUB";
    if (price == null || price.isBlank()) {
      return new Amount(0, 0, cur);
    }
    var kopecks = Integer.parseInt(price);
    return new Amount(kopecks / 100, kopecks % 100, cur);
  }

  private String buildMessage(String type, Map<String, String> params) {
    var planName = params.getOrDefault("planName", "");
    var planPeriod = params.getOrDefault("planPeriod", "");
    if (type.isBlank()) return planName;
    return switch (type) {
      case "trial" -> "Trial started: " + planName;
      case "firstbill" ->
        "Subscription started: " + planName + " (" + planPeriod + ")";
      case "rebill" ->
        "Subscription renewed: " + planName + " (" + planPeriod + ")";
      case "refund" -> "Refund: " + planName;
      case "cancel" -> "Subscription cancelled: " + planName;
      case "update" -> "Subscription updated: " + planName;
      default -> planName;
    };
  }

  private boolean isPaymentEvent(String type) {
    return "firstbill".equals(type) || "rebill".equals(type);
  }
}
