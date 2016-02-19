package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.SlackChannelCreated;
import com.ullink.slack.simpleslackapi.events.SlackEventType;

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
