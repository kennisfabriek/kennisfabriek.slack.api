package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackUser;

class SlackUserImpl extends SlackPersonaImpl implements SlackUser
{
    //private static final Logger LOGGER   = LoggerFactory.getLogger(SlackUserImpl.class);

    @Override
    public String toString()
    {
        return "SlackUserImpl{" + "id='" + this.id + '\'' + ", userName='" + this.userName + '\'' + ", realName='" + this.realName + '\'' + ", userMail='" + this.userMail + '\'' + ", isDeleted=" + this.deleted + '\'' + ", isAdmin=" + this.admin + '\'' + ", isOwner="
            + this.owner + '\'' + ", isPrimaryOwner=" + this.primaryOwner + '\'' + ", isRestricted=" + this.restricted + '\'' + ", isUltraRestricted=" + this.ultraRestricted + ", timeZone=" + this.timeZone + ", timeZoneLabel=" + this.timeZoneLabel + ", timeZoneOffset="
            + this.timeZoneOffset + "}";
    }

    SlackUserImpl(String id, String userName, String realName, String userMail, boolean deleted, boolean admin, boolean owner, boolean primaryOwner, boolean restricted, boolean ultraRestricted, boolean bot, String timeZone,
        String timeZoneLabel, Integer timeZoneOffset)
    {
        super(id, userName, realName, userMail, deleted, admin, owner, primaryOwner, restricted, ultraRestricted, bot, timeZone, timeZoneLabel, timeZoneOffset);
    }
}
