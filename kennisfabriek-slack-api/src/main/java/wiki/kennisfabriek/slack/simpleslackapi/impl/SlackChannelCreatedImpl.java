package wiki.kennisfabriek.slack.simpleslackapi.impl;

import wiki.kennisfabriek.slack.simpleslackapi.SlackChannel;
import wiki.kennisfabriek.slack.simpleslackapi.SlackUser;
import wiki.kennisfabriek.slack.simpleslackapi.events.SlackChannelCreated;
import wiki.kennisfabriek.slack.simpleslackapi.events.SlackEventType;

class SlackChannelCreatedImpl implements SlackChannelCreated
{
    private SlackChannel slackChannel;
    private SlackUser slackuser;
    
    SlackChannelCreatedImpl(SlackChannel slackChannel, SlackUser slackuser)
    {
        this.slackChannel = slackChannel;
        this.slackuser = slackuser;
    }

    @Override
    public SlackChannel getSlackChannel()
    {
        return this.slackChannel;
    }

    @Override
    public SlackUser getCreator()
    {
        return this.slackuser;
    }

    @Override
    public SlackEventType getEventType()
    {
        return SlackEventType.SLACK_CHANNEL_CREATED;
    }

}
