package wiki.kennisfabriek.slack.simpleslackapi.listeners;

import wiki.kennisfabriek.slack.simpleslackapi.SlackSession;
import wiki.kennisfabriek.slack.simpleslackapi.events.SlackEvent;

public interface SlackEventListener<T extends SlackEvent>
{
    void onEvent(T event, SlackSession session);
}
