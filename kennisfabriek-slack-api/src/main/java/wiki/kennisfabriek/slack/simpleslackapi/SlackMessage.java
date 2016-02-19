package wiki.kennisfabriek.slack.simpleslackapi;

import wiki.kennisfabriek.slack.simpleslackapi.events.SlackMessageEvent;
import wiki.kennisfabriek.slack.simpleslackapi.events.SlackMessagePosted;

/**
 * 
 * @deprecated use {@link SlackMessagePosted}
 *
 */
@Deprecated
public interface SlackMessage extends SlackMessageEvent
{

    String getMessageContent();

    SlackUser getSender();

    SlackBot getBot();

    SlackChannel getChannel();

}
