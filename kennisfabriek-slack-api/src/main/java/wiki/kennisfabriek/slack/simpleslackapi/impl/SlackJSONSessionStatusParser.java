package wiki.kennisfabriek.slack.simpleslackapi.impl;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wiki.kennisfabriek.slack.simpleslackapi.SlackChannel;
import wiki.kennisfabriek.slack.simpleslackapi.SlackPersona;
import wiki.kennisfabriek.slack.simpleslackapi.SlackTeam;
import wiki.kennisfabriek.slack.simpleslackapi.SlackUser;

class SlackJSONSessionStatusParser
{
    private static final Logger       LOGGER   = LoggerFactory.getLogger(SlackJSONSessionStatusParser.class);

    private Map<String, SlackChannel> channels = new HashMap<>();
    private Map<String, SlackUser>    users    = new HashMap<>();

    private SlackPersona              sessionPersona;

    private SlackTeam                 team;

    private String                    webSocketURL;

    private String                    toParse;

    private String                    error;

    SlackJSONSessionStatusParser(String toParse)
    {
        this.toParse = toParse;
    }

    Map<String, SlackChannel> getChannels()
    {
        return this.channels;
    }

    Map<String, SlackUser> getUsers()
    {
        return this.users;
    }

    public String getWebSocketURL()
    {
        return this.webSocketURL;
    }

    public String getError()
    {
        return this.error;
    }
    
    void parse() throws ParseException
    {
        LOGGER.debug("parsing session status : " + this.toParse);
        JSONParser parser = new JSONParser();
        JSONObject jsonResponse = (JSONObject) parser.parse(this.toParse);
        Boolean ok = (Boolean)jsonResponse.get("ok");
        if (Boolean.FALSE.equals(ok)) {
            this.error = (String)jsonResponse.get("error");
            return;
        }
        JSONArray usersJson = (JSONArray) jsonResponse.get("users");

        for (Object jsonObject : usersJson)
        {
            JSONObject jsonUser = (JSONObject) jsonObject;
            SlackUser slackUser = SlackJSONParsingUtils.buildSlackUser(jsonUser);
            LOGGER.debug("slack user found : " + slackUser.getId());
            this.users.put(slackUser.getId(), slackUser);
        }

        JSONArray botsJson = (JSONArray) jsonResponse.get("bots");
        if (botsJson != null) {
            for (Object jsonObject : botsJson)
            {
                JSONObject jsonBot = (JSONObject) jsonObject;
                SlackUser slackUser = SlackJSONParsingUtils.buildSlackUser(jsonBot);
                LOGGER.debug("slack bot found : " + slackUser.getId());
                this.users.put(slackUser.getId(), slackUser);
            }
        }

        JSONArray channelsJson = (JSONArray) jsonResponse.get("channels");

        for (Object jsonObject : channelsJson)
        {
            JSONObject jsonChannel = (JSONObject) jsonObject;
            SlackChannelImpl channel = SlackJSONParsingUtils.buildSlackChannel(jsonChannel, this.users);
            LOGGER.debug("slack public channel found : " + channel.getId());
            this.channels.put(channel.getId(), channel);
        }

        JSONArray groupsJson = (JSONArray) jsonResponse.get("groups");

        for (Object jsonObject : groupsJson)
        {
            JSONObject jsonChannel = (JSONObject) jsonObject;
            SlackChannelImpl channel = SlackJSONParsingUtils.buildSlackChannel(jsonChannel, this.users);
            LOGGER.debug("slack private group found : " + channel.getId());
            this.channels.put(channel.getId(), channel);
        }

        JSONArray imsJson = (JSONArray) jsonResponse.get("ims");

        for (Object jsonObject : imsJson)
        {
            JSONObject jsonChannel = (JSONObject) jsonObject;
            SlackChannelImpl channel = SlackJSONParsingUtils.buildSlackImChannel(jsonChannel, this.users);
            LOGGER.debug("slack im channel found : " + channel.getId());
            this.channels.put(channel.getId(), channel);
        }

        JSONObject selfJson = (JSONObject) jsonResponse.get("self");
        this.sessionPersona = SlackJSONParsingUtils.buildSlackUser(selfJson);

        JSONObject teamJson = (JSONObject) jsonResponse.get("team");
        this.team = SlackJSONParsingUtils.buildSlackTeam(teamJson);

        this.webSocketURL = (String) jsonResponse.get("url");

    }

    public SlackPersona getSessionPersona()
    {
        return this.sessionPersona;
    }

    public SlackTeam getTeam()
    {
        return this.team;
    }
}
