package wiki.kennisfabriek.slack.simpleslackapi.events;

import wiki.kennisfabriek.slack.simpleslackapi.SlackUser;

public interface SlackChannelArchived extends SlackChannelEvent
{
    SlackUser getUser();
}
