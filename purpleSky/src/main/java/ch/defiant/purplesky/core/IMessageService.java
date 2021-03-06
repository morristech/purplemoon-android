package ch.defiant.purplesky.core;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import ch.defiant.purplesky.beans.MinimalUser;
import ch.defiant.purplesky.beans.PrivateMessage;
import ch.defiant.purplesky.beans.UserMessageHistoryBean;
import ch.defiant.purplesky.exceptions.PurpleSkyException;
import ch.defiant.purplesky.util.Holder;

/**
 * Created by Chakotay on 09.05.2014.
 */
public interface IMessageService {

    public final int BATCH = 50;

    Holder<List<PrivateMessage>> getPreviousMessagesOnline(String profileId, long messageId);

    List<PrivateMessage> getNewestCachedMessagesWithUser(String profileId);

    List<PrivateMessage> getPreviousCachedMessagesWithUser(String profileId, long messageId);

    Holder<List<PrivateMessage>> getNewMessagesFromUser(String profileId, Long lastMessageId);

    void insertMessage(PrivateMessage m);

    void insertMessages(Collection<PrivateMessage> list);

    Long getLatestReceivedMessageTimestamp(String profileId);

    Long getLatestMessageId(String profileId);

    void updateLastContact(Collection<UserMessageHistoryBean> conversations);

    void updateUserNameMapping(MinimalUser user);

    void updateUserNameMapping(Collection<MinimalUser> users);

    String getUserNameForId(String userId);

    String getProfilePictureUrlForId(String userId);

    void injectProfilePictureUrlForId(Collection<UserMessageHistoryBean> users);

    void cleanupDB();

    List<UserMessageHistoryBean> getCachedConversations();

    Date getNewestConversationTimestamp();

    List<UserMessageHistoryBean> getOnlineConversations() throws IOException, PurpleSkyException;
}
