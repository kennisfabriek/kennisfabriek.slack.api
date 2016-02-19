package wiki.kennisfabriek.slack.simpleslackapi.impl;

import wiki.kennisfabriek.slack.simpleslackapi.SlackPersona;
import wiki.kennisfabriek.slack.simpleslackapi.events.SlackConnected;
import wiki.kennisfabriek.slack.simpleslackapi.events.SlackEventType;

class SlackConnectedImpl implements SlackConnected
{
    private SlackPersona slackConnectedPersona;

    SlackConnectedImpl(SlackPersona slackConnectedPersona)
    {
        this.slackConnectedPersona = slackConnectedPersona;
    }

    @Override
    public SlackPersona getConnectedPersona()
    {
        return this.slackConnectedPersona;
    }

    @Override
    public SlackEventType getEventType()
    {
        return SlackEventType.SLACK_CONNECTED;
    }
}
