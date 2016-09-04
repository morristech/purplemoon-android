package ch.defiant.purplesky.constants;

public class DatabaseConstants {

    public static class MessageTable {
        public static final String TABLE_MESSAGES = "messages";
        public static final String MESSAGES_PK = "id"; // FIXME pbn DB Migration, new pk
        public static final String MESSAGES_MESSAGEID = "message_id"; // FIXME pbn Rename/Migrate?, old pk
        public static final String MESSAGES_FROMUSERID = "from_user_id";
        public static final String MESSAGES_TOUSERID = "to_user_id";
        public static final String MESSAGES_TIMESENT = "time_sent";
        public static final String MESSAGES_TEXT = "message_text";
        public static final String MESSAGES_STATUS = "status";

        public static final String MESSAGES_LASTATTEMPT = "last_attempt"; // FIXME DB Migration
        public static final String MESSAGES_NEXTATTEMPT = "next_attempt";
        public static final String MESSAGES_ATTEMPT_COUNT = "attempt_count";

        public static final String MESSAGES_INDEX_FROMUSERID = "messages_from_idx";
        public static final String MESSAGES_INDEX_TOUSERID = "messages_to_idx";
        public static final String MESSAGES_INDEX_SENT = "messages_time_sent_idx";
    }


    /********
     * Conversation table
     ********/
    public static class ConversationTable {
        public static final String TABLE_CONVERSATIONS = "conversations";
        public static final String CONVERSATIONS_OTHERUSERID = "otheruser_id";
        public static final String CONVERSATIONS_LASTCONTACT = "last_contact";
        public static final String CONVERSATIONS_UNREADCOUNT = "unread_count";
        public static final String CONVERSATIONS_EXCERPT = "excerpt";

        public static final String CONVERSATIONS_INDEX_LASTCONTACT = "conversations_last_contact_idx";
    }

    /*********
     * User mapping table
     *********/
    public static class UserMappingTable {
        public static final String TABLE_USERMAPPING = "usermapping";
        public static final String USERMAPPING_USERID = "user_id";
        public static final String USERMAPPING_USERNAME = "user_name";
        public static final String USERMAPPING_PROFILEPICTURE_URL = "profilepicture_url";
        public static final String USERMAPPING_INSERTED = "inserted";
    }

    /********
     * Bundle store table
     ********/
    public static class BundleStoreTable {
        public static final String TABLE_BUNDLESTORE = "bundlestore";
        public static final String BUNDLESTORE_OWNER = "bundleId";
        public static final String BUNDLESTORE_KEY = "key";
        public static final String BUNDLESTORE_TYPE = "type";

        /**
         * number value
         */
        public static final String BUNDLESTORE_NVALUE = "nval1";

        /**
         * char value
         */
        public static final String BUNDLESTORE_CVALUE = "cval1";

        /**
         * float value (real)
         */
        public static final String BUNDLESTORE_FVALUE = "fval1";
    }
}
