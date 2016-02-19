package wiki.kennisfabriek.slack.simpleslackapi.impl;

import wiki.kennisfabriek.slack.simpleslackapi.SlackUser;
import wiki.kennisfabriek.slack.simpleslackapi.events.SlackEventType;
import wiki.kennisfabriek.slack.simpleslackapi.events.SlackUserChange;

public class SlackUserChangeImpl implements SlackUserChange {

    private SlackUser slackUser;

    SlackUserChangeImpl(SlackUser slackUser) {
        this.slackUser = slackUser;
    }

    @Override
    public SlackUser getUser() {
        return this.slackUser;
    }

    @Override
    public SlackEventType getEventType() {
        return SlackEventType.SLACK_USER_CHANGE;
    }
}
