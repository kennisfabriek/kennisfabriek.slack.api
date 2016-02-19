package wiki.kennisfabriek.slack.simpleslackapi.impl;

import wiki.kennisfabriek.slack.simpleslackapi.SlackChannel;
import wiki.kennisfabriek.slack.simpleslackapi.events.SlackChannelDeleted;
import wiki.kennisfabriek.slack.simpleslackapi.events.SlackEventType;

class SlackChannelDeletedImpl implements SlackChannelDeleted
{
    private SlackChannel slackChannel;

    SlackChannelDeletedImpl(SlackChannel slackChannel)
    {
        this.slackChannel = slackChannel;
    }

    @Override
    public SlackChannel getSlackChannel()
    {
        return this.slackChannel;
    }

    @Override
    public SlackEventType getEventType()
    {
        return SlackEventType.SLACK_CHANNEL_DELETED;
    }
}
