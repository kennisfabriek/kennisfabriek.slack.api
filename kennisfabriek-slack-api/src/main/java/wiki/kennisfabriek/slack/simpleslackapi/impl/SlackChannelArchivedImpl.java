package wiki.kennisfabriek.slack.simpleslackapi.impl;

import wiki.kennisfabriek.slack.simpleslackapi.SlackChannel;
import wiki.kennisfabriek.slack.simpleslackapi.SlackUser;
import wiki.kennisfabriek.slack.simpleslackapi.events.SlackChannelArchived;
import wiki.kennisfabriek.slack.simpleslackapi.events.SlackEventType;

class SlackChannelArchivedImpl implements SlackChannelArchived
{
    private SlackChannel slackChannel;
    private SlackUser slackuser;
    
    SlackChannelArchivedImpl(SlackChannel slackChannel, SlackUser slackuser)
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
    public SlackUser getUser()
    {
        return this.slackuser;
    }

    @Override
    public SlackEventType getEventType()
    {
        return SlackEventType.SLACK_CHANNEL_ARCHIVED;
    }

}
