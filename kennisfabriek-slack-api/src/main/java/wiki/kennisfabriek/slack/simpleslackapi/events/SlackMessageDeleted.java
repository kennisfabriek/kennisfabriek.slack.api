package wiki.kennisfabriek.slack.simpleslackapi.events;

import wiki.kennisfabriek.slack.simpleslackapi.SlackChannel;

public interface SlackMessageDeleted extends SlackMessageEvent
{
    SlackChannel getChannel();
    String getMessageTimestamp();
}
