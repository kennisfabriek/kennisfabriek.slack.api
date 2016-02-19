package wiki.kennisfabriek.slack.simpleslackapi.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import wiki.kennisfabriek.slack.simpleslackapi.SlackChannel;
import wiki.kennisfabriek.slack.simpleslackapi.SlackUser;

class SlackChannelImpl implements SlackChannel
{
    private final boolean direct;
    private String         id;
    private String         name;
    private Set<SlackUser> members = new HashSet<>();
    private String         topic;
    private String         purpose;

    SlackChannelImpl(String id, String name, String topic, String purpose, boolean direct)
    {
        this.id = id;
        this.name = name;
        this.topic = topic;
        this.purpose = purpose;
        this.direct = direct;
    }

    void addUser(SlackUser user)
    {
        this.members.add(user);
    }

    void removeUser(SlackUser user)
    {
        this.members.remove(user);
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
    public Collection<SlackUser> getMembers()
    {
        return new ArrayList<>(this.members);
    }

    @Override
    public String getTopic()
    {
        return this.topic;
    }

    @Override
    public String toString() {
        return "SlackChannelImpl{" +
                "topic='" + this.topic + '\'' +
                ", purpose='" + this.purpose + '\'' +
                ", id='" + this.id + '\'' +
                ", name='" + this.name + '\'' +
                '}';
    }

    @Override
    public String getPurpose()
    {
        return this.purpose;
    }

    @Override
    public boolean isDirect() {
        return this.direct;
    }
}
