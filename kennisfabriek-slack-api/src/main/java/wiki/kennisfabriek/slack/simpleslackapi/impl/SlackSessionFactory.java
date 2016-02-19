package wiki.kennisfabriek.slack.simpleslackapi.impl;

import java.net.Proxy;

import wiki.kennisfabriek.slack.simpleslackapi.SlackSession;

public class SlackSessionFactory
{
    public static SlackSession createWebSocketSlackSession(String authToken)
    {
        return new SlackWebSocketSessionImpl(authToken, true);
    }

    public static SlackSession createWebSocketSlackSession(final String authToken, Proxy.Type proxyType, String proxyAddress, int proxyPort)
    {
        return new SlackWebSocketSessionImpl(authToken, proxyType, proxyAddress, proxyPort, true);
    }

}
