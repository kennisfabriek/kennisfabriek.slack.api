package wiki.kennisfabriek.slack.simpleslackapi.impl;

import wiki.kennisfabriek.slack.simpleslackapi.replies.ParsedSlackReply;

class SlackReplyImpl implements ParsedSlackReply
{

    @SuppressWarnings("unused")
	private boolean ok;

    SlackReplyImpl(boolean ok)
    {
        this.ok = ok;
    }

    @Override
    public boolean isOk()
    {
        return false;
    }
}
