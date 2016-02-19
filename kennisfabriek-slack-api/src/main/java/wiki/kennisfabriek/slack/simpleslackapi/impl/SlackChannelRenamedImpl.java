package wiki.kennisfabriek.slack.simpleslackapi.impl;

import wiki.kennisfabriek.slack.simpleslackapi.SlackChannel;
import wiki.kennisfabriek.slack.simpleslackapi.events.SlackChannelRenamed;
import wiki.kennisfabriek.slack.simpleslackapi.events.SlackEventType;

class SlackChannelRenamedImpl implements SlackChannelRenamed
{
    private SlackChannel slackChannel;
    private String       newName;

    SlackChannelRenamedImpl(SlackChannel slackChannel, String newName)
    {
        this.slackChannel = slackChannel;
        this.newName = newName;
    }

    @Override
    public SlackChannel getSlackChannel()
    {
        return this.slackChannel;
    }

    @Override
    public String getNewName()
    {
        return this.newName;
    }

    @Override
    public SlackEventType getEventType()
    {
        return SlackEventType.SLACK_CHANNEL_RENAMED;
    }

}
