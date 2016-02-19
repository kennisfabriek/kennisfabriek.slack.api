package wiki.kennisfabriek.slack.simpleslackapi.impl;

import wiki.kennisfabriek.slack.simpleslackapi.SlackChannel;
import wiki.kennisfabriek.slack.simpleslackapi.events.SlackEventType;
import wiki.kennisfabriek.slack.simpleslackapi.events.SlackMessageDeleted;

class SlackMessageDeletedImpl implements SlackMessageDeleted
{
    private final SlackChannel channel;
    private final String       messageTimestamp;
    private final String       deleteTimestamp;

    SlackMessageDeletedImpl(SlackChannel channel, String messageTimestamp, String deleteTimestamp)
    {
        this.channel = channel;
        this.messageTimestamp = messageTimestamp;
        this.deleteTimestamp = deleteTimestamp;
    }

    @Override
    public SlackChannel getChannel()
    {
        return this.channel;
    }

    @Override
    public String getMessageTimestamp()
    {
        return this.messageTimestamp;
    }

    @Override
    public String getTimeStamp()
    {
        return this.deleteTimestamp;
    }

    @Override
    public SlackEventType getEventType()
    {
        return SlackEventType.SLACK_MESSAGE_DELETED;
    }

}
