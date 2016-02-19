package wiki.kennisfabriek.slack.simpleslackapi.impl;

import wiki.kennisfabriek.slack.simpleslackapi.SlackChannel;
import wiki.kennisfabriek.slack.simpleslackapi.SlackFile;
import wiki.kennisfabriek.slack.simpleslackapi.SlackUser;
import wiki.kennisfabriek.slack.simpleslackapi.events.PinRemoved;
import wiki.kennisfabriek.slack.simpleslackapi.events.SlackEventType;

public class PinRemovedImpl implements PinRemoved {

  private final SlackUser sender;
  private final SlackChannel channel;
  private final String timestamp;
  private final SlackFile file;
  private final String message;

  public PinRemovedImpl(SlackUser sender, SlackChannel channel, String timestamp, SlackFile file, String message) {
        this.sender = sender;
        this.channel = channel;
        this.timestamp = timestamp;
        this.file = file;
        this.message = message;
    }

    @Override
    public SlackUser getSender() {
        return this.sender;
    }

    @Override
    public SlackChannel getChannel() {
        return this.channel;
    }

    @Override
    public String getTimestamp() {
        return this.timestamp;
    }

    @Override
    public SlackFile getFile() {
        return this.file;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
  
    @Override
    public SlackEventType getEventType() {
        return SlackEventType.PIN_REMOVED;
    }

}
