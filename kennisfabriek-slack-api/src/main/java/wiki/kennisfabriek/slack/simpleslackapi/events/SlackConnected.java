package wiki.kennisfabriek.slack.simpleslackapi.events;

import wiki.kennisfabriek.slack.simpleslackapi.SlackPersona;

public interface SlackConnected extends SlackEvent
{
    SlackPersona getConnectedPersona();
}
