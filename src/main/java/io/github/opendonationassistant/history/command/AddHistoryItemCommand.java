package io.github.opendonationassistant.history.command;

import io.github.opendonationassistant.commons.Amount;
import io.micronaut.serde.annotation.Serdeable;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.Nullable;

@Serdeable
public record AddHistoryItemCommand(
  @Nullable String paymentId,
  String nickname,
  String recipientId,
  Amount amount,
  String message,
  Instant authorizationTimestamp,
  String system,
  @Nullable String externalId,
  List<Attachment> attachments,
  List<String> links,
  List<TargetGoal> goals,
  List<ReelResult> reelResults,
  List<ActionRequest> actions,
  @Nullable AlertMedia alertMedia,
  @Nullable Vote vote,
  String event,
  @Nullable Integer count,
  @Nullable Integer level,
  @Nullable String levelName,
  boolean triggerAlert,
  boolean triggerReel,
  boolean triggerDonaton,
  boolean addToGoal,
  boolean addToTop
) {

  @Serdeable
  public record Attachment(String id) {}

  @Serdeable
  public record ReelResult(String title) {}

  @Serdeable
  public record TargetGoal(String goalId, String goalTitle) {}

  @Serdeable
  public record ActionRequest(
    String id,
    String actionId,
    String name,
    Integer amount,
    Map<String, Object> payload
  ) {}

  @Serdeable
  public record AlertMedia(String url) {}

  @Serdeable
  public record Vote(@Nullable String id, String name, Boolean isNew) {}
}
