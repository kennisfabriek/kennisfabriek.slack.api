package wiki.kennisfabriek.slack.simpleslackapi.events;

public interface SlackMessageEvent extends SlackEvent
{
    String getTimeStamp();
}
