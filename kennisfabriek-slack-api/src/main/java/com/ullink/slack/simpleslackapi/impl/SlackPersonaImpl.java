package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackPersona;

class SlackPersonaImpl implements SlackPersona
{
    final String  id;
    final String  userName;
    final String  realName;
    final String  userMail;
    final boolean deleted;
    final boolean admin;
    final boolean owner;
    final boolean primaryOwner;
    final boolean restricted;
    final boolean ultraRestricted;
    final boolean bot;
    final String timeZone;
    final String timeZoneLabel;
    final Integer timeZoneOffset;

    SlackPersonaImpl(String id, String userName, String realName, String userMail, boolean deleted, boolean admin, boolean owner, boolean primaryOwner, boolean restricted, boolean ultraRestricted, boolean bot, String timeZone, String timeZoneLabel, Integer timeZoneOffset)
    {
        this.id = id;
        this.userName = userName;
        this.realName = realName;
        this.userMail = userMail;
        this.deleted = deleted;
        this.admin = admin;
        this.owner = owner;
        this.primaryOwner = primaryOwner;
        this.restricted = restricted;
        this.ultraRestricted = ultraRestricted;
        this.bot = bot;
        this.timeZone = timeZone;
        this.timeZoneLabel = timeZoneLabel;
        this.timeZoneOffset = timeZoneOffset;
    }

    @Override
    public String getId()
    {
        return this.id;
    }

    @Override
    public String getUserName()
    {
        return this.userName;
    }

    @Override
    public boolean isDeleted()
    {
        return this.deleted;
    }

    @Override
    public boolean isAdmin()
    {
        return this.admin;
    }

    @Override
    public boolean isOwner()
    {
        return this.owner;
    }

    @Override
    public boolean isPrimaryOwner()
    {
        return this.primaryOwner;
    }

    @Override
    public boolean isRestricted()
    {
        return this.restricted;
    }

    @Override
    public boolean isUltraRestricted()
    {
        return this.ultraRestricted;
    }

    @Override
    public boolean isBot()
    {
        return this.bot;
    }

    @Override
    public String getUserMail()
    {
        return this.userMail;
    }

    @Override
    public String getRealName()
    {
        return this.realName;
    }

    @Override
    public String getTimeZone()
    {
        return this.timeZone;
    }

    @Override
    public String getTimeZoneLabel()
    {
        return this.timeZoneLabel;
    }

    @Override
    public Integer getTimeZoneOffset()
    {
        return this.timeZoneOffset;
    }

}
