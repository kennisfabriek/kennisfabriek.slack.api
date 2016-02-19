package wiki.kennisfabriek.slack.simpleslackapi.impl;

import wiki.kennisfabriek.slack.simpleslackapi.SlackChannel;
import wiki.kennisfabriek.slack.simpleslackapi.events.SlackEventType;
import wiki.kennisfabriek.slack.simpleslackapi.events.SlackMessageUpdated;

class SlackMessageUpdatedImpl implements SlackMessageUpdated
{
    private final SlackChannel channel;
    private final String       messageTimestamp;
    private final String       editionTimestamp;
    private final String       newMessage;

    SlackMessageUpdatedImpl(SlackChannel channel, String messageTimestamp, String editionTimestamp, String newMessage)
    {
        this.channel = channel;
        this.messageTimestamp = messageTimestamp;
        this.editionTimestamp = editionTimestamp;
        this.newMessage = newMessage;
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
        return this.editionTimestamp;
    }

    @Override
    public String getNewMessage()
    {
        return this.newMessage;
    }

    @Override
    public SlackEventType getEventType()
    {
        return SlackEventType.SLACK_MESSAGE_UPDATED;
    }


}
