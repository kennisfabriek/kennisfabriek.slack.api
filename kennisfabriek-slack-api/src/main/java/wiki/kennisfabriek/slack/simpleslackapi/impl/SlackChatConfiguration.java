package wiki.kennisfabriek.slack.simpleslackapi.impl;

public class SlackChatConfiguration
{
    static enum Avatar
    {
        DEFAULT, EMOJI, ICON_URL;
    }

    protected boolean asUser;
    protected Avatar  avatar = Avatar.DEFAULT;
    protected String  userName;
    protected String  avatarDescription;

    private SlackChatConfiguration()
    {

    }

    public SlackChatConfiguration asUser()
    {
        this.asUser = true;
        this.avatar = Avatar.DEFAULT;
        this.avatarDescription = null;
        return this;
    }

    public SlackChatConfiguration withIcon(String iconURL)
    {
        this.asUser = false;
        this.avatar = Avatar.ICON_URL;
        this.avatarDescription = iconURL;
        return this;
    }

    public SlackChatConfiguration withName(String name)
    {
        this.asUser = false;
        this.userName = name;
        return this;
    }

    public SlackChatConfiguration withEmoji(String emoji)
    {
        this.asUser = false;
        this.avatar = Avatar.EMOJI;
        this.avatarDescription = emoji;
        return this;
    }

    public static SlackChatConfiguration getConfiguration()
    {
        return new SlackChatConfiguration();
    }

}
