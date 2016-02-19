package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.SlackChannelUnarchived;
import com.ullink.slack.simpleslackapi.events.SlackEventType;

class SlackChannelUnarchivedImpl implements SlackChannelUnarchived
{
    private SlackChannel slackChannel;
    private SlackUser slackuser;
    
    SlackChannelUnarchivedImpl(SlackChannel slackChannel, SlackUser slackuser)
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
        return SlackEventType.SLACK_CHANNEL_UNARCHIVED;
    }

}
