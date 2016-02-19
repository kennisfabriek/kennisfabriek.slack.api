package wiki.kennisfabriek.slack.simpleslackapi.impl;

import wiki.kennisfabriek.slack.simpleslackapi.SlackTeam;

public class SlackTeamImpl implements SlackTeam
{
    final String id;
    final String name;
    final String domain;

    public SlackTeamImpl(String id, String name, String domain)
    {
        this.id = id;
        this.name = name;
        this.domain = domain;
    }

    @Override
    public String getId()
    {
        return this.id;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public String getDomain()
    {
        return this.domain;
    }
}
