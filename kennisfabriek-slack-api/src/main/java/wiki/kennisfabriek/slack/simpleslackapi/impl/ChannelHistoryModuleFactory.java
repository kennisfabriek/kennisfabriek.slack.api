package wiki.kennisfabriek.slack.simpleslackapi.impl;

import wiki.kennisfabriek.slack.simpleslackapi.ChannelHistoryModule;
import wiki.kennisfabriek.slack.simpleslackapi.SlackSession;

public class ChannelHistoryModuleFactory {
    
    public static ChannelHistoryModule createChannelHistoryModule(SlackSession session){
        return new ChannelHistoryModuleImpl(session);
    }
    
}
