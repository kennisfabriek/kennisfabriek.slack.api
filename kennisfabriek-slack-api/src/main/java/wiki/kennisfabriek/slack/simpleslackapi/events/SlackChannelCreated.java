package wiki.kennisfabriek.slack.simpleslackapi.events;

import wiki.kennisfabriek.slack.simpleslackapi.SlackUser;

public interface SlackChannelCreated extends SlackChannelEvent
{
    SlackUser getCreator();
}
