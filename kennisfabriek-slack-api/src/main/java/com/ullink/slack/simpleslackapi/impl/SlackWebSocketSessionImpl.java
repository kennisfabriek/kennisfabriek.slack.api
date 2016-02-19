package com.ullink.slack.simpleslackapi.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Proxy;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

import com.google.common.io.CharStreams;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.message.BasicNameValuePair;
import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.client.ClientProperties;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ullink.slack.simpleslackapi.SlackAttachment;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackMessageHandle;
import com.ullink.slack.simpleslackapi.SlackPersona;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.PinAdded;
import com.ullink.slack.simpleslackapi.events.PinRemoved;
import com.ullink.slack.simpleslackapi.events.ReactionAdded;
import com.ullink.slack.simpleslackapi.events.ReactionRemoved;
import com.ullink.slack.simpleslackapi.events.SlackChannelArchived;
import com.ullink.slack.simpleslackapi.events.SlackChannelCreated;
import com.ullink.slack.simpleslackapi.events.SlackChannelDeleted;
import com.ullink.slack.simpleslackapi.events.SlackChannelRenamed;
import com.ullink.slack.simpleslackapi.events.SlackChannelUnarchived;
import com.ullink.slack.simpleslackapi.events.SlackConnected;
import com.ullink.slack.simpleslackapi.events.SlackEvent;
import com.ullink.slack.simpleslackapi.events.SlackGroupJoined;
import com.ullink.slack.simpleslackapi.events.SlackMessageDeleted;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.events.SlackMessageUpdated;
import com.ullink.slack.simpleslackapi.events.SlackUserChange;
import com.ullink.slack.simpleslackapi.impl.SlackChatConfiguration.Avatar;
import com.ullink.slack.simpleslackapi.listeners.SlackEventListener;
import com.ullink.slack.simpleslackapi.replies.GenericSlackReply;
import com.ullink.slack.simpleslackapi.replies.SlackChannelReply;
import com.ullink.slack.simpleslackapi.replies.SlackMessageReply;
import com.ullink.slack.simpleslackapi.replies.SlackReply;
import com.ullink.slack.simpleslackapi.replies.SlackUserPresenceReply;

class SlackWebSocketSessionImpl extends AbstractSlackSessionImpl implements SlackSession, MessageHandler.Whole<String>
{
    private static final String SLACK_API_HTTPS_ROOT      = "https://slack.com/api/";

    private static final String DIRECT_MESSAGE_OPEN_CHANNEL_COMMAND = "im.open";

    private static final String MULTIPARTY_DIRECT_MESSAGE_OPEN_CHANNEL_COMMAND = "mpim.open";

    private static final String CHANNELS_LEAVE_COMMAND    = "channels.leave";

    private static final String CHANNELS_JOIN_COMMAND     = "channels.join";
    
    private static final String CHANNELS_INVITE_COMMAND     = "channels.invite";
    
    private static final String CHANNELS_ARCHIVE_COMMAND     = "channels.archive";

    private static final String CHAT_POST_MESSAGE_COMMAND = "chat.postMessage";

    private static final String CHAT_DELETE_COMMAND       = "chat.delete";

    private static final String CHAT_UPDATE_COMMAND       = "chat.update";

    private static final String REACTIONS_ADD_COMMAND     = "reactions.add";
    
    private static final String INVITE_USER_COMMAND     = "users.admin.invite";


    @Override
    public SlackMessageHandle<SlackMessageReply> sendMessageToUser(SlackUser user, String message, SlackAttachment attachment) {
        SlackChannel iMChannel = getIMChannelForUser(user);
        return sendMessage(iMChannel, message, attachment, DEFAULT_CONFIGURATION);
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> sendMessageToUser(String userName, String message, SlackAttachment attachment) {
        return sendMessageToUser(findUserByUserName(userName), message, attachment);
    }

    private List<SlackChannel> getAllIMChannels() {
        Collection<SlackChannel> allChannels = getChannels();
        List<SlackChannel> iMChannels = new ArrayList<>();
        for (SlackChannel channel : allChannels) {
            if (channel.isDirect()) {
                iMChannels.add(channel);
            }
        }
        return iMChannels;
    }

    private SlackChannel getIMChannelForUser(SlackUser user) {
        List<SlackChannel> imcs = getAllIMChannels();
        for (SlackChannel channel : imcs) {
            if (channel.getMembers().contains(user)) {
                return channel;
            }
        }
        SlackMessageHandle<SlackChannelReply> reply = openDirectMessageChannel(user);
        return reply.getReply().getSlackChannel();
    }

    public class EventDispatcher
    {

        void dispatch(SlackEvent event)
        {
            switch (event.getEventType())
            {
                case SLACK_CHANNEL_ARCHIVED:
                    dispatchImpl((SlackChannelArchived) event, SlackWebSocketSessionImpl.this.channelArchiveListener);
                    break;
                case SLACK_CHANNEL_CREATED:
                    dispatchImpl((SlackChannelCreated) event, SlackWebSocketSessionImpl.this.channelCreateListener);
                    break;
                case SLACK_CHANNEL_DELETED:
                    dispatchImpl((SlackChannelDeleted) event, SlackWebSocketSessionImpl.this.channelDeleteListener);
                    break;
                case SLACK_CHANNEL_RENAMED:
                    dispatchImpl((SlackChannelRenamed) event, SlackWebSocketSessionImpl.this.channelRenamedListener);
                    break;
                case SLACK_CHANNEL_UNARCHIVED:
                    dispatchImpl((SlackChannelUnarchived) event, SlackWebSocketSessionImpl.this.channelUnarchiveListener);
                    break;
                case SLACK_GROUP_JOINED:
                    dispatchImpl((SlackGroupJoined) event, SlackWebSocketSessionImpl.this.groupJoinedListener);
                    break;
                case SLACK_MESSAGE_DELETED:
                    dispatchImpl((SlackMessageDeleted) event, SlackWebSocketSessionImpl.this.messageDeletedListener);
                    break;
                case SLACK_MESSAGE_POSTED:
                    dispatchImpl((SlackMessagePosted) event, SlackWebSocketSessionImpl.this.messagePostedListener);
                    break;
                case SLACK_MESSAGE_UPDATED:
                    dispatchImpl((SlackMessageUpdated) event, SlackWebSocketSessionImpl.this.messageUpdatedListener);
                    break;
                case SLACK_CONNECTED:
                    dispatchImpl((SlackConnected) event, SlackWebSocketSessionImpl.this.slackConnectedListener);
                    break;
                case REACTION_ADDED:
                    dispatchImpl((ReactionAdded) event, SlackWebSocketSessionImpl.this.reactionAddedListener);
                    break;
                case REACTION_REMOVED:
                    dispatchImpl((ReactionRemoved) event, SlackWebSocketSessionImpl.this.reactionRemovedListener);
                    break;
                case SLACK_USER_CHANGE:
                    dispatchImpl((SlackUserChange) event, SlackWebSocketSessionImpl.this.slackUserChangeListener);
                    break;
                case PIN_ADDED:
                    dispatchImpl((PinAdded) event, SlackWebSocketSessionImpl.this.pinAddedListener);
                    break;
                case PIN_REMOVED:
                    dispatchImpl((PinRemoved) event, SlackWebSocketSessionImpl.this.pinRemovedListener);
                    break;
                case UNKNOWN:
                    throw new IllegalArgumentException("event not handled " + event);
			default:
				break;
            }
        }

        private <E extends SlackEvent, L extends SlackEventListener<E>> void dispatchImpl(E event, List<L> listeners)
        {
            for (L listener : listeners)
            {
                listener.onEvent(event, SlackWebSocketSessionImpl.this);
            }
        }
    }

    private static final Logger               LOGGER                     = LoggerFactory.getLogger(SlackWebSocketSessionImpl.class);

    private static final String               SLACK_HTTPS_AUTH_URL       = "https://slack.com/api/rtm.start?token=";

    private Session                           websocketSession;
    private String                            authToken;
    private String                            proxyAddress;
    private int                               proxyPort                  = -1;
    HttpHost                                  proxyHost;
    private long                              lastPingSent               = 0;
    private volatile long                     lastPingAck                = 0;

    private long                              messageId                  = 0;

    private boolean                           reconnectOnDisconnection;
    private boolean                           wantDisconnect             = false;

    private Thread                            connectionMonitoringThread = null;
    private EventDispatcher                   dispatcher                 = new EventDispatcher();

    SlackWebSocketSessionImpl(String authToken, Proxy.Type proxyType, String proxyAddress, int proxyPort, boolean reconnectOnDisconnection) {
        this.authToken = authToken;
        this.proxyAddress = proxyAddress;
        this.proxyPort = proxyPort;
        this.proxyHost = new HttpHost(proxyAddress, proxyPort);
        this.reconnectOnDisconnection = reconnectOnDisconnection;
    }

    SlackWebSocketSessionImpl(String authToken, boolean reconnectOnDisconnection)
    {
        this.authToken = authToken;
        this.reconnectOnDisconnection = reconnectOnDisconnection;
    }

    @Override
    public void connect() throws IOException
    {
        this.wantDisconnect = false;
        connectImpl();
        LOGGER.debug("starting connection monitoring");
        startConnectionMonitoring();
    }

    @Override
    public void disconnect()
    {
        this.wantDisconnect = true;
        LOGGER.debug("Disconnecting from the Slack server");
        disconnectImpl();
        stopConnectionMonitoring();
    }
    @Override
    public boolean isConnected()
    {
        return this.websocketSession!=null?this.websocketSession.isOpen():false;
    }

    private void connectImpl() throws IOException, ClientProtocolException, ConnectException
    {
        LOGGER.info("connecting to slack");
        this.lastPingSent = 0;
        this.lastPingAck = 0;
        HttpClient httpClient = getHttpClient();
        HttpGet request = new HttpGet(SLACK_HTTPS_AUTH_URL + this.authToken);
        HttpResponse response;
        response = httpClient.execute(request);
        LOGGER.debug(response.getStatusLine().toString());
        String jsonResponse = CharStreams.toString(new InputStreamReader(response.getEntity().getContent()));
        SlackJSONSessionStatusParser sessionParser = new SlackJSONSessionStatusParser(jsonResponse);
        try
        {
            sessionParser.parse();
        }
        catch (ParseException e1)
        {
            LOGGER.error(e1.toString());
        }
        if (sessionParser.getError() != null)
        {
            LOGGER.error("Error during authentication : " + sessionParser.getError());
            throw new ConnectException(sessionParser.getError());
        }
        this.users = sessionParser.getUsers();
        this.channels = sessionParser.getChannels();
        this.sessionPersona = sessionParser.getSessionPersona();
        this.team = sessionParser.getTeam();
        LOGGER.info("Team " + this.team.getId() + " : " + this.team.getName());
        LOGGER.info("Self " + this.sessionPersona.getId() + " : " + this.sessionPersona.getUserName());
        LOGGER.info(this.users.size() + " users found on this session");
        LOGGER.info(this.channels.size() + " channels found on this session");
        String wssurl = sessionParser.getWebSocketURL();

        LOGGER.debug("retrieved websocket URL : " + wssurl);
        ClientManager client = ClientManager.createClient();
        if (this.proxyAddress != null)
        {
            client.getProperties().put(ClientProperties.PROXY_URI, "http://" + this.proxyAddress + ":" + this.proxyPort);
        }
        final MessageHandler handler = this;
        LOGGER.debug("initiating connection to websocket");
        try
        {
            this.websocketSession = client.connectToServer(new Endpoint()
            {
                @Override
                public void onOpen(Session session, EndpointConfig config)
                {
                    session.addMessageHandler(handler);
                }

            }, URI.create(wssurl));
        }
        catch (DeploymentException e)
        {
            LOGGER.error(e.toString());
        }
        if (this.websocketSession != null)
        {
            SlackConnectedImpl slackConnectedImpl = new SlackConnectedImpl(this.sessionPersona);
            this.dispatcher.dispatch(slackConnectedImpl);
            LOGGER.debug("websocket connection established");
            LOGGER.info("slack session ready");
        }
    }

    private void disconnectImpl()
    {
        if (this.websocketSession != null)
        {
            try
            {
                this.websocketSession.close();
            }
            catch (IOException ex)
            {
                // ignored.
            }
            finally
            {
                this.websocketSession = null;
            }
        }
    }

    private void startConnectionMonitoring()
    {
        this.connectionMonitoringThread = new Thread()
        {
            @SuppressWarnings("synthetic-access")
			@Override
            public void run()
            {
                LOGGER.debug("monitoring thread started");
                while (true)
                {
                    try
                    {
                        // heart beat of 30s (should be configurable in the future)
                        Thread.sleep(30000);

                        // disconnect() was called.
                        if (SlackWebSocketSessionImpl.this.wantDisconnect)
                            this.interrupt();

                        if (SlackWebSocketSessionImpl.this.lastPingSent != SlackWebSocketSessionImpl.this.lastPingAck || SlackWebSocketSessionImpl.this.websocketSession == null)
                        {
                            // disconnection happened
                            LOGGER.warn("Connection lost...");
                            try
                            {
                                if (SlackWebSocketSessionImpl.this.websocketSession != null)
                                {
                                    SlackWebSocketSessionImpl.this.websocketSession.close();
                                }
                            }
                            catch (IOException e)
                            {
                                LOGGER.error("exception while trying to close the websocket ", e);
                            }
                            SlackWebSocketSessionImpl.this.websocketSession = null;
                            if (SlackWebSocketSessionImpl.this.reconnectOnDisconnection)
                            {
                                connectImpl();
                                continue;
                            }
							this.interrupt();
                        }
                        else
                        {
                            SlackWebSocketSessionImpl.this.lastPingSent = getNextMessageId();
                            LOGGER.debug("sending ping " + SlackWebSocketSessionImpl.this.lastPingSent);
                            try
                            {
                                if (SlackWebSocketSessionImpl.this.websocketSession.isOpen())
                                {
                                    SlackWebSocketSessionImpl.this.websocketSession.getBasicRemote().sendText("{\"type\":\"ping\",\"id\":" + SlackWebSocketSessionImpl.this.lastPingSent + "}");
                                }
                                else if (SlackWebSocketSessionImpl.this.reconnectOnDisconnection)
                                {
                                    connectImpl();
                                }
                            }
                            catch (IllegalStateException e)
                            {
                                // websocketSession might be closed in this case
                                if (SlackWebSocketSessionImpl.this.reconnectOnDisconnection)
                                {
                                    connectImpl();
                                }
                            }
                        }
                    }
                    catch (InterruptedException e)
                    {
                        break;
                    }
                    catch (IOException e)
                    {
                        LOGGER.error("unexpected exception on monitoring thread ", e);
                    }
                }
                LOGGER.debug("monitoring thread stopped");
            }
        };

        if (!this.wantDisconnect)
            this.connectionMonitoringThread.start();
    }

    private void stopConnectionMonitoring()
    {
        if (this.connectionMonitoringThread != null)
        {
            while (true)
            {
                try
                {
                    this.connectionMonitoringThread.interrupt();
                    this.connectionMonitoringThread.join();
                    break;
                }
                catch (InterruptedException ex)
                {
                    // ouch - let's try again!
                }
            }
        }
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration, boolean unfurl)
    {
        SlackMessageHandleImpl<SlackMessageReply> handle = new SlackMessageHandleImpl<SlackMessageReply>(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", this.authToken);
        arguments.put("channel", channel.getId());
        arguments.put("text", message);
        if (chatConfiguration.asUser)
        {
            arguments.put("as_user", "true");
        }
        if (chatConfiguration.avatar == Avatar.ICON_URL)
        {
            arguments.put("icon_url", chatConfiguration.avatarDescription);
        }
        if (chatConfiguration.avatar == Avatar.EMOJI)
        {
            arguments.put("icon_emoji", chatConfiguration.avatarDescription);
        }
        if (chatConfiguration.userName != null)
        {
            arguments.put("username", chatConfiguration.userName);
        }
        if (attachment != null)
        {
            arguments.put("attachments", SlackJSONAttachmentFormatter.encodeAttachments(attachment).toString());
        }
        if (!unfurl)
        {
            arguments.put("unfurl_links", "false");
            arguments.put("unfurl_media", "false");
        }

        postSlackCommand(arguments, CHAT_POST_MESSAGE_COMMAND, handle);
        return handle;
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> deleteMessage(String timeStamp, SlackChannel channel)
    {
        SlackMessageHandleImpl<SlackMessageReply> handle = new SlackMessageHandleImpl<SlackMessageReply>(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", this.authToken);
        arguments.put("channel", channel.getId());
        arguments.put("ts", timeStamp);
        postSlackCommand(arguments, CHAT_DELETE_COMMAND, handle);
        return handle;
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> updateMessage(String timeStamp, SlackChannel channel, String message)
    {
        SlackMessageHandleImpl<SlackMessageReply> handle = new SlackMessageHandleImpl<SlackMessageReply>(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", this.authToken);
        arguments.put("ts", timeStamp);
        arguments.put("channel", channel.getId());
        arguments.put("text", message);
        postSlackCommand(arguments, CHAT_UPDATE_COMMAND, handle);
        return handle;
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> addReactionToMessage(SlackChannel channel, String messageTimeStamp, String emojiCode)
    {
        SlackMessageHandleImpl<SlackMessageReply> handle = new SlackMessageHandleImpl<SlackMessageReply>(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", this.authToken);
        arguments.put("channel", channel.getId());
        arguments.put("timestamp", messageTimeStamp);
        arguments.put("name", emojiCode);
        postSlackCommand(arguments, REACTIONS_ADD_COMMAND, handle);
        return handle;
    }

    @Override
    public SlackMessageHandle<SlackChannelReply> joinChannel(String channelName)
    {
        SlackMessageHandleImpl<SlackChannelReply> handle = new SlackMessageHandleImpl<SlackChannelReply>(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", this.authToken);
        arguments.put("name", channelName);
        postSlackCommand(arguments, CHANNELS_JOIN_COMMAND, handle);
        return handle;
    }

    @Override
    public SlackMessageHandle<SlackChannelReply> leaveChannel(SlackChannel channel)
    {
        SlackMessageHandleImpl<SlackChannelReply> handle = new SlackMessageHandleImpl<SlackChannelReply>(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", this.authToken);
        arguments.put("channel", channel.getId());
        postSlackCommand(arguments, CHANNELS_LEAVE_COMMAND, handle);
        return handle;
    }

    @Override
    public SlackMessageHandle<SlackChannelReply> inviteToChannel(SlackChannel channel, SlackUser user) {
      SlackMessageHandleImpl<SlackChannelReply> handle = new SlackMessageHandleImpl<SlackChannelReply>(getNextMessageId());
      Map<String, String> arguments = new HashMap<>();
      arguments.put("token", this.authToken);
      arguments.put("channel", channel.getId());
      arguments.put("user", user.getId());
      postSlackCommand(arguments, CHANNELS_INVITE_COMMAND, handle);
      return handle;
    }
    
    @Override
    public SlackMessageHandle<SlackReply> archiveChannel(SlackChannel channel) 
    {
      SlackMessageHandleImpl<SlackReply> handle = new SlackMessageHandleImpl<SlackReply>(getNextMessageId());
      Map<String, String> arguments = new HashMap<>();
      arguments.put("token", this.authToken);
      arguments.put("channel", channel.getId());
      postSlackCommand(arguments, CHANNELS_ARCHIVE_COMMAND, handle);
      return handle;
    }
    
    @Override
    public SlackMessageHandle<SlackChannelReply> openDirectMessageChannel(SlackUser user)
    {
        SlackMessageHandleImpl<SlackChannelReply> handle = new SlackMessageHandleImpl<SlackChannelReply>(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", this.authToken);
        arguments.put("user", user.getId());
        postSlackCommand(arguments, DIRECT_MESSAGE_OPEN_CHANNEL_COMMAND, handle);
        return handle;
    }

    @Override
    public SlackMessageHandle<SlackChannelReply> openMultipartyDirectMessageChannel(SlackUser... usersParam)
    {
        SlackMessageHandleImpl<SlackChannelReply> handle = new SlackMessageHandleImpl<SlackChannelReply>(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", this.authToken);
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0 ; i < usersParam.length ; i++) {
            if (i != 0) {
                strBuilder.append(',');
            }
            strBuilder.append(usersParam[i].getId());
        }
        arguments.put("users", strBuilder.toString());
        postSlackCommand(arguments, MULTIPARTY_DIRECT_MESSAGE_OPEN_CHANNEL_COMMAND, handle);

        SlackMessageHandleImpl<?> check = handle;
        SlackReply reply = check.getReply();
        if (reply instanceof GenericSlackReply) {
            JSONObject obj = ((GenericSlackReply) reply).getPlainAnswer();

            Object ok = obj.get("ok");
            if (ok != null && ok instanceof Boolean && !((Boolean) ok)) {
                LOGGER.debug("Error occurred while performing command: '" + obj.get("error") + "'");
                return null;
            }
        }

        return handle;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	private void postSlackCommand(Map<String, String> params, String command, SlackMessageHandleImpl handle)
    {
        HttpClient client = getHttpClient();
        HttpPost request = new HttpPost(SLACK_API_HTTPS_ROOT + command);
        List<NameValuePair> nameValuePairList = new ArrayList<>();
        for (Map.Entry<String, String> arg : params.entrySet())
        {
            nameValuePairList.add(new BasicNameValuePair(arg.getKey(), arg.getValue()));
        }
        try
        {
            request.setEntity(new UrlEncodedFormEntity(nameValuePairList, "UTF-8"));
            HttpResponse response = client.execute(request);
            String jsonResponse = CharStreams.toString(new InputStreamReader(response.getEntity().getContent()));
            LOGGER.debug("PostMessage return: " + jsonResponse);
            SlackReply reply = SlackJSONReplyParser.decode(parseObject(jsonResponse),this);
            handle.setReply(reply);
        }
        catch (Exception e)
        {
            // TODO : improve exception handling
            e.printStackTrace();
        }
    }
    
    @Override
    public SlackMessageHandle<GenericSlackReply> postGenericSlackCommand(Map<String, String> params, String command)
    {
        HttpClient client = getHttpClient();
        HttpPost request = new HttpPost(SLACK_API_HTTPS_ROOT + command);
        List<NameValuePair> nameValuePairList = new ArrayList<>();
        for (Map.Entry<String, String> arg : params.entrySet())
        {
            if (!"token".equals(arg.getKey())) {
                nameValuePairList.add(new BasicNameValuePair(arg.getKey(), arg.getValue()));
            }
        }
        nameValuePairList.add(new BasicNameValuePair("token", this.authToken));
        try
        {
            SlackMessageHandleImpl<GenericSlackReply> handle = new SlackMessageHandleImpl<>(getNextMessageId());
            request.setEntity(new UrlEncodedFormEntity(nameValuePairList, "UTF-8"));
            HttpResponse response = client.execute(request);
            String jsonResponse = CharStreams.toString(new InputStreamReader(response.getEntity().getContent()));
            LOGGER.debug("PostMessage return: " + jsonResponse);
            GenericSlackReplyImpl reply = new GenericSlackReplyImpl(parseObject(jsonResponse));
            handle.setReply(reply);
            return handle;
        }
        catch (Exception e)
        {
            // TODO : improve exception handling
            e.printStackTrace();
        }
        return null;
    }

    private HttpClient getHttpClient()
    {
        HttpClient client = null;
        if (this.proxyHost != null)
        {
            client = HttpClientBuilder.create().setRoutePlanner(new DefaultProxyRoutePlanner(this.proxyHost)).build();
        }
        else
        {
            client = HttpClientBuilder.create().build();
        }
        return client;
    }

    @SuppressWarnings("unchecked")
	@Override
    public SlackMessageHandle<SlackMessageReply> sendMessageOverWebSocket(SlackChannel channel, String message, SlackAttachment attachment)
    {
        SlackMessageHandleImpl<SlackMessageReply> handle = new SlackMessageHandleImpl<SlackMessageReply>(getNextMessageId());
        try
        {
            JSONObject messageJSON = new JSONObject();
            messageJSON.put("type", "message");
            messageJSON.put("channel", channel.getId());
            messageJSON.put("text", message);
            if (attachment != null)
            {
                messageJSON.put("attachments", SlackJSONAttachmentFormatter.encodeAttachments(attachment));
            }
            this.websocketSession.getBasicRemote().sendText(messageJSON.toJSONString());
        }
        catch (Exception e)
        {
            // TODO : improve exception handling
            e.printStackTrace();
        }
        return handle;
    }

    @Override
    public SlackPersona.SlackPresence getPresence(SlackPersona persona)
    {
        HttpClient client = getHttpClient();
        HttpPost request = new HttpPost("https://slack.com/api/users.getPresence");
        List<NameValuePair> nameValuePairList = new ArrayList<>();
        nameValuePairList.add(new BasicNameValuePair("token", this.authToken));
        nameValuePairList.add(new BasicNameValuePair("user", persona.getId()));
        try
        {
            request.setEntity(new UrlEncodedFormEntity(nameValuePairList, "UTF-8"));
            HttpResponse response = client.execute(request);
            String jsonResponse = CharStreams.toString(new InputStreamReader(response.getEntity().getContent()));
            LOGGER.debug("PostMessage return: " + jsonResponse);
            JSONObject resultObject = parseObject(jsonResponse);
            //quite hacky need to refactor this
            SlackUserPresenceReply reply = (SlackUserPresenceReply)SlackJSONReplyParser.decode(resultObject,this);
            if (!reply.isOk())
            {
                return SlackPersona.SlackPresence.UNKNOWN;
            }
            String presence = (String) resultObject.get("presence");

            if ("active".equals(presence))
            {
                return SlackPersona.SlackPresence.ACTIVE;
            }
            if ("away".equals(presence))
            {
                return SlackPersona.SlackPresence.AWAY;
            }
        }
        catch (Exception e)
        {
            // TODO : improve exception handling
            e.printStackTrace();
        }
        return SlackPersona.SlackPresence.UNKNOWN;
    }

    private synchronized long getNextMessageId()
    {
        return this.messageId++;
    }

    @Override
    public void onMessage(String message)
    {
        LOGGER.debug("receiving from websocket " + message);
        if (message.contains("{\"type\":\"pong\",\"reply_to\""))
        {
            int rightBracketIdx = message.indexOf('}');
            String toParse = message.substring(26, rightBracketIdx);
            this.lastPingAck = Integer.parseInt(toParse);
            LOGGER.debug("pong received " + this.lastPingAck);
        }
        else
        {
            JSONObject object = parseObject(message);
            SlackEvent slackEvent = SlackJSONMessageParser.decode(this, object);
            if (slackEvent instanceof SlackChannelCreated)
            {
                SlackChannelCreated slackChannelCreated = (SlackChannelCreated) slackEvent;
                this.channels.put(slackChannelCreated.getSlackChannel().getId(), slackChannelCreated.getSlackChannel());
            }
            if (slackEvent instanceof SlackGroupJoined)
            {
                SlackGroupJoined slackGroupJoined = (SlackGroupJoined) slackEvent;
                this.channels.put(slackGroupJoined.getSlackChannel().getId(), slackGroupJoined.getSlackChannel());
            }
            if (slackEvent instanceof SlackUserChange)
            {
                SlackUserChange slackUserChange = (SlackUserChange) slackEvent;
                this.users.put(slackUserChange.getUser().getId(), slackUserChange.getUser());
            }
            this.dispatcher.dispatch(slackEvent);
        }
    }

    private static JSONObject parseObject(String json)
    {
        JSONParser parser = new JSONParser();
        try
        {
            JSONObject object = (JSONObject) parser.parse(json);
            return object;
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public SlackMessageHandle<GenericSlackReply> inviteUser(String email, String firstName, boolean setActive) {

        SlackMessageHandleImpl<GenericSlackReply> handle = new SlackMessageHandleImpl<GenericSlackReply>(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", this.authToken);
        arguments.put("email", email);
        arguments.put("first_name", firstName);
        arguments.put("set_active", ""+setActive);
        postSlackCommand(arguments, INVITE_USER_COMMAND, handle);
        return handle;
    }

}
