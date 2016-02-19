package wiki.kennisfabriek.slack.simpleslackapi.events;

import wiki.kennisfabriek.slack.simpleslackapi.SlackChannel;

public interface ReactionRemoved extends SlackEvent {

    public String getEmojiName();

    public SlackChannel getChannel();

    public String getMessageID();

}
