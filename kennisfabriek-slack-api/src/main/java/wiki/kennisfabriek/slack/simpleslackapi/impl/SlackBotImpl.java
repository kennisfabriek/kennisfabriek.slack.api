package wiki.kennisfabriek.slack.simpleslackapi.impl;

import wiki.kennisfabriek.slack.simpleslackapi.SlackBot;

@Deprecated
class SlackBotImpl extends SlackPersonaImpl implements SlackBot
{
    SlackBotImpl(String id, String userName, String realName, String userMail, boolean deleted, boolean admin, boolean owner, boolean primaryOwner, boolean restricted, boolean ultraRestricted)
    {
        super(id, userName, realName, userMail, deleted, admin, owner, primaryOwner, restricted, ultraRestricted, true,null,null,0);
    }

}
