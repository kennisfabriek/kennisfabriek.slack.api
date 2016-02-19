package wiki.kennisfabriek.slack.simpleslackapi.events;

import wiki.kennisfabriek.slack.simpleslackapi.SlackUser;

public interface SlackChannelUnarchived extends SlackChannelEvent
{
    SlackUser getUser();
}
