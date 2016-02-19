package wiki.kennisfabriek.slack.simpleslackapi.events;

import wiki.kennisfabriek.slack.simpleslackapi.SlackChannel;

public interface SlackChannelEvent extends SlackEvent
{
    SlackChannel getSlackChannel();
}
