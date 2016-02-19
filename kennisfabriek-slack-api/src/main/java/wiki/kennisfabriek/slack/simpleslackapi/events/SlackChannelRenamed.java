package wiki.kennisfabriek.slack.simpleslackapi.events;

public interface SlackChannelRenamed extends SlackChannelEvent
{
    String getNewName();
}
