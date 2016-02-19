package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.SlackChannelArchived;
import com.ullink.slack.simpleslackapi.events.SlackEventType;

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
