package wiki.kennisfabriek.slack.simpleslackapi.replies;

import wiki.kennisfabriek.slack.simpleslackapi.SlackChannel;

public interface SlackChannelReply extends SlackReply
{
    SlackChannel getSlackChannel();
}
