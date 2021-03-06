package wiki.kennisfabriek.slack.simpleslackapi.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.temporal.ChronoUnit;

import wiki.kennisfabriek.slack.simpleslackapi.ChannelHistoryModule;
import wiki.kennisfabriek.slack.simpleslackapi.SlackMessageHandle;
import wiki.kennisfabriek.slack.simpleslackapi.SlackSession;
import wiki.kennisfabriek.slack.simpleslackapi.events.ReactionAdded;
import wiki.kennisfabriek.slack.simpleslackapi.events.ReactionRemoved;
import wiki.kennisfabriek.slack.simpleslackapi.events.SlackMessagePosted;
import wiki.kennisfabriek.slack.simpleslackapi.listeners.ReactionAddedListener;
import wiki.kennisfabriek.slack.simpleslackapi.listeners.ReactionRemovedListener;
import wiki.kennisfabriek.slack.simpleslackapi.listeners.SlackMessagePostedListener;
import wiki.kennisfabriek.slack.simpleslackapi.replies.GenericSlackReply;

public class ChannelHistoryModuleImpl implements ChannelHistoryModule {

    private final SlackSession session;
    private static final String FETCH_CHANNEL_HISTORY_COMMAND = "channels.history";

    public ChannelHistoryModuleImpl(SlackSession session) {
        this.session = session;
    }

    @Override
    public List<SlackMessagePosted> fetchHistoryOfChannel(String channelId) {
        return fetchHistoryOfChannel(channelId, null, -1);
    }

    @Override
    public List<SlackMessagePosted> fetchHistoryOfChannel(String channelId, LocalDate day) {
        return fetchHistoryOfChannel(channelId, day, -1);
    }

    @Override
    public List<SlackMessagePosted> fetchHistoryOfChannel(String channelId, int numberOfMessages) {
        return fetchHistoryOfChannel(channelId, null, numberOfMessages);
    }

    @Override
    public List<SlackMessagePosted> fetchHistoryOfChannel(String channelId, LocalDate day, int numberOfMessages) {
        Map<String, String> params = new HashMap<>();
        params.put("channel", channelId);
        if (day != null) {
            ZonedDateTime start = ZonedDateTime.of(day.atStartOfDay(), ZoneId.of("UTC"));
            ZonedDateTime end = ZonedDateTime.of(day.atStartOfDay().plusDays(1).minus(1, ChronoUnit.MILLIS), ZoneId.of("UTC"));
            params.put("oldest", convertDateToSlackTimestamp(start));
            params.put("latest", convertDateToSlackTimestamp(end));
        }
        if (numberOfMessages > -1) {
            params.put("count", String.valueOf(numberOfMessages));
        } else {
            params.put("count", String.valueOf(1000));
        }
        return fetchHistoryOfChannel(params);
    }

    private List<SlackMessagePosted> fetchHistoryOfChannel(Map<String, String> params) {
        SlackMessageHandle<GenericSlackReply> handle = this.session.postGenericSlackCommand(params, FETCH_CHANNEL_HISTORY_COMMAND);
        GenericSlackReply replyEv = handle.getReply();
        JSONObject answer = replyEv.getPlainAnswer();
        JSONArray events = (JSONArray) answer.get("messages");
        List<SlackMessagePosted> messages = new ArrayList<>();
        if (events != null) {
            for (Object event : events) {
                if ((((JSONObject) event).get("subtype") == null)) {
                    messages.add((SlackMessagePosted) SlackJSONMessageParser.decode(this.session, (JSONObject) event));
                }
            }
        }
        return messages;
    }

    @Override
    public List<SlackMessagePosted> fetchUpdatingHistoryOfChannel(String channelId) {
        return fetchUpdatingHistoryOfChannel(channelId, null, -1);
    }

    @Override
    public List<SlackMessagePosted> fetchUpdatingHistoryOfChannel(String channelId, LocalDate day) {
        return fetchUpdatingHistoryOfChannel(channelId, day, -1);
    }

    @Override
    public List<SlackMessagePosted> fetchUpdatingHistoryOfChannel(String channelId, int numberOfMessages) {
        return fetchUpdatingHistoryOfChannel(channelId, null, numberOfMessages);
    }

    @Override
    public List<SlackMessagePosted> fetchUpdatingHistoryOfChannel(String channelId, LocalDate day, int numberOfMessages) {
        List<SlackMessagePosted> messages = fetchHistoryOfChannel(channelId, day, numberOfMessages);
        this.session.addReactionAddedListener(new ChannelHistoryReactionAddedListener(messages));
        this.session.addReactionRemovedListener(new ChannelHistoryReactionRemovedListener(messages));
        this.session.addMessagePostedListener(new ChannelHistoryMessagePostedListener(messages));
        return messages;
    }

    public class ChannelHistoryReactionAddedListener implements ReactionAddedListener {

        List<SlackMessagePosted> messages = new ArrayList<>();

        public ChannelHistoryReactionAddedListener(List<SlackMessagePosted> initialMessages) {
            this.messages = initialMessages;
        }

        @Override
        public void onEvent(ReactionAdded event, SlackSession sessionParam) {
            String emojiName = event.getEmojiName();
            for (SlackMessagePosted message : this.messages) {
                for (String reaction : message.getReactions().keySet()) {
                    if (emojiName.equals(reaction)) {
                        int count = message.getReactions().get(emojiName);
                        message.getReactions().put(emojiName, count++);
                        return;
                    }
                }
                message.getReactions().put(emojiName, 1);
            }
        }
    }

    public class ChannelHistoryReactionRemovedListener implements ReactionRemovedListener {

        List<SlackMessagePosted> messages = new ArrayList<>();

        public ChannelHistoryReactionRemovedListener(List<SlackMessagePosted> initialMessages) {
            this.messages = initialMessages;
        }

        @Override
        public void onEvent(ReactionRemoved event, SlackSession sessionParam) {
            String emojiName = event.getEmojiName();
            for (SlackMessagePosted message : this.messages) {
                for (String reaction : message.getReactions().keySet()) {
                    if (emojiName.equals(reaction)) {
                        int count = message.getReactions().get(emojiName);
                        if (count == 1) {
                            message.getReactions().remove(emojiName);
                        } else {
                            message.getReactions().put(emojiName, --count);
                        }
                        return;
                    }
                }
            }
        }
    }

    public class ChannelHistoryMessagePostedListener implements SlackMessagePostedListener {

        List<SlackMessagePosted> messages = new ArrayList<>();

        public ChannelHistoryMessagePostedListener(List<SlackMessagePosted> initialMessages) {
            this.messages = initialMessages;
        }

        @Override
        public void onEvent(SlackMessagePosted event, SlackSession sessionParam) {
            this.messages.add(event);
        }
    }

    private static String convertDateToSlackTimestamp(ZonedDateTime date) {
        return (date.toInstant().toEpochMilli() / 1000) + ".123456";
    }

}
