package wiki.kennisfabriek.slack.simpleslackapi.impl;

import wiki.kennisfabriek.slack.simpleslackapi.replies.SlackReply;
import wiki.kennisfabriek.slack.simpleslackapi.replies.SlackUserPresenceReply;

public class SlackUserPresenceReplyImpl extends SlackReplyImpl implements SlackUserPresenceReply, SlackReply
{
    private boolean active;

    SlackUserPresenceReplyImpl(boolean ok, boolean active)
    {
        super(ok);
        this.active = active;
    }

    @Override
    public boolean isActive()
    {
        return this.active;
    }
}
