package wiki.kennisfabriek.slack.simpleslackapi.impl;

import org.json.simple.JSONObject;

import wiki.kennisfabriek.slack.simpleslackapi.replies.GenericSlackReply;

class GenericSlackReplyImpl implements GenericSlackReply
{
    private JSONObject obj;

    public GenericSlackReplyImpl(JSONObject obj)
    {
        this.obj = obj;
    }

    @Override
    public JSONObject getPlainAnswer()
    {
        return this.obj;
    }

}
