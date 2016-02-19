package wiki.kennisfabriek.slack.simpleslackapi.events;

import wiki.kennisfabriek.slack.simpleslackapi.SlackChannel;

public interface SlackMessageUpdated extends SlackMessageEvent
{
    SlackChannel getChannel();
    String getMessageTimestamp();
    String getNewMessage();
}
