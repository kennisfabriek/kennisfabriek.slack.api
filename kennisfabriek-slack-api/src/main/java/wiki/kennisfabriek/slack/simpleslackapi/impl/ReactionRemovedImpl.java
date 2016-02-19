package wiki.kennisfabriek.slack.simpleslackapi.impl;

import wiki.kennisfabriek.slack.simpleslackapi.SlackChannel;
import wiki.kennisfabriek.slack.simpleslackapi.events.ReactionRemoved;
import wiki.kennisfabriek.slack.simpleslackapi.events.SlackEventType;

public class ReactionRemovedImpl implements ReactionRemoved{
    private final String emojiName;
    private final String messageID;
    private final SlackChannel channel;

    public ReactionRemovedImpl(String emojiName, String messageID, SlackChannel channel) {
        this.emojiName = emojiName;
        this.messageID = messageID;
        this.channel = channel;
    }

    @Override
    public String getEmojiName() {
        return this.emojiName;
    }

    @Override
    public SlackChannel getChannel() {
        return this.channel;
    }

    @Override
    public String getMessageID() {
        return this.messageID;
    }

    @Override
    public SlackEventType getEventType() {
        return SlackEventType.REACTION_REMOVED;
    }
}
