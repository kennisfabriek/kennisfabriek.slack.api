package wiki.kennisfabriek.slack.simpleslackapi.events;

import wiki.kennisfabriek.slack.simpleslackapi.SlackUser;

public interface SlackUserChange extends SlackEvent {

    SlackUser getUser();
}
