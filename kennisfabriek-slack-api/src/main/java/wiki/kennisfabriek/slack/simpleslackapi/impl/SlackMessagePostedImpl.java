package wiki.kennisfabriek.slack.simpleslackapi.impl;

import java.util.Map;

import org.json.simple.JSONObject;

import wiki.kennisfabriek.slack.simpleslackapi.SlackBot;
import wiki.kennisfabriek.slack.simpleslackapi.SlackChannel;
import wiki.kennisfabriek.slack.simpleslackapi.SlackFile;
import wiki.kennisfabriek.slack.simpleslackapi.SlackUser;
import wiki.kennisfabriek.slack.simpleslackapi.events.SlackEventType;
import wiki.kennisfabriek.slack.simpleslackapi.events.SlackMessagePosted;

@SuppressWarnings("deprecation")
class SlackMessagePostedImpl implements SlackMessagePosted
{
    private String       messageContent;
    private SlackUser    user;
	private SlackBot     bot;
    private SlackChannel channel;
    private String       timestamp;
    private SlackFile    slackFile;
    private JSONObject   jsonSource;
    private Map<String, Integer> reactions;
    
    SlackMessagePostedImpl(String messageContent, SlackBot bot, SlackUser user, SlackChannel channel, String timestamp)
    {
        this.channel = channel;
        this.messageContent = messageContent;
        this.user = user;
        this.bot = bot;
        this.timestamp = timestamp;
    }
    
    SlackMessagePostedImpl(String messageContent, SlackBot bot, SlackUser user, SlackChannel channel, String timestamp, JSONObject jsonSource)
    {
        this.channel = channel;
        this.messageContent = messageContent;
        this.user = user;
        this.bot = bot;
        this.timestamp = timestamp;
        this.jsonSource = jsonSource;
    }
    
    SlackMessagePostedImpl(String messageContent, SlackBot bot, SlackUser user, SlackChannel channel, String timestamp, SlackFile slackFile, JSONObject jsonSource)
    {
        this.channel = channel;
        this.messageContent = messageContent;
        this.user = user;
        this.bot = bot;
        this.timestamp = timestamp;
        this.jsonSource = jsonSource;
        this.slackFile = slackFile;
    }

    @Override
    public String toString() {
        return "SlackMessagePostedImpl{" + "messageContent=" + this.messageContent + ", user=" + this.user + ", bot=" + this.bot + ", channel=" + this.channel + ", timestamp=" + this.timestamp + ", reactions=" + this.reactions + '}';
    }

    @Override
    public JSONObject getJsonSource() {
        return this.jsonSource;
    }

    @Override
    public SlackFile getSlackFile() {
        return this.slackFile;
    }
    

    @Override
    public String getMessageContent() {
        return this.messageContent;
    }

    @Override
    public SlackUser getSender() {
        return this.user;
    }

    @Override
    public SlackBot getBot() {
        return this.bot;
    }

    @Override
    public SlackChannel getChannel() {
        return this.channel;
    }

    @Override
    public String getTimeStamp() {
        return this.timestamp;
    }

    @Override
    public SlackEventType getEventType() {
        return SlackEventType.SLACK_MESSAGE_POSTED;
    }

    @Override
    public Map<String, Integer> getReactions() {
        return this.reactions;
    }

    public void setReactions(Map<String, Integer> reactions) {
        this.reactions = reactions;
    }

    @Override
    public int getTotalCountOfReactions() {
        int count = 0;
        for (Integer tmpCount : this.reactions.values()) {
            count += tmpCount;
        }
        return count;
    }

    @Override
    public String getTimestamp() {
        return this.timestamp;
    }

}

