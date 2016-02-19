package wiki.kennisfabriek.slack.simpleslackapi.events;

import java.util.Map;

import org.json.simple.JSONObject;

import wiki.kennisfabriek.slack.simpleslackapi.SlackBot;
import wiki.kennisfabriek.slack.simpleslackapi.SlackChannel;
import wiki.kennisfabriek.slack.simpleslackapi.SlackFile;
import wiki.kennisfabriek.slack.simpleslackapi.SlackUser;

@SuppressWarnings("deprecation")
public interface SlackMessagePosted extends SlackMessageEvent
{
    String getMessageContent();

    SlackUser getSender();

    @Deprecated
    SlackBot getBot();

    SlackChannel getChannel();
    
    SlackFile getSlackFile();
    
    JSONObject getJsonSource();
    
    String getTimestamp();
    
    Map<String, Integer> getReactions();
    
    int getTotalCountOfReactions();

}
