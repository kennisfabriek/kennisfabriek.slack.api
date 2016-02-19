package wiki.kennisfabriek.slack.simpleslackapi.impl;

import org.json.simple.JSONObject;

import wiki.kennisfabriek.slack.simpleslackapi.SlackChannel;
import wiki.kennisfabriek.slack.simpleslackapi.replies.SlackChannelReply;

public class SlackChannelReplyImpl extends SlackReplyImpl implements SlackChannelReply
{
    private SlackChannel slackChannel;

    SlackChannelReplyImpl(boolean ok, JSONObject plain, SlackChannel slackChannel)
    {
        super(ok);
        this.slackChannel = slackChannel;
    }

    @Override
    public SlackChannel getSlackChannel()
    {
        return this.slackChannel;
    }
}
