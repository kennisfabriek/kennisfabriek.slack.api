package wiki.kennisfabriek.slack.simpleslackapi.events;

import wiki.kennisfabriek.slack.simpleslackapi.SlackChannel;
import wiki.kennisfabriek.slack.simpleslackapi.SlackFile;
import wiki.kennisfabriek.slack.simpleslackapi.SlackUser;

public interface PinRemoved extends SlackEvent {

    public SlackUser getSender();

    public SlackChannel getChannel();

    public String getTimestamp();

    public SlackFile getFile();

    public String getMessage();

}
