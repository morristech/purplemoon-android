package ch.defiant.purplesky.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * Bean representing a full message, storing all related information
 * 
 * @author Patrick Bänziger
 * 
 */
public class PrivateMessage implements Serializable {

    private static final long serialVersionUID = -7577615162687828508L;

    public static final String TYPEIDENTIFIER = "ch.defiant.purplesky.beans.PrivateMessage";

    private String m_messageText;
    private PrivateMessageHead m_messageHead;
    private boolean m_isDummy;

    public void setMessageText(String messageText) {
        m_messageText = messageText;
    }

    public String getMessageText() {
        return m_messageText;
    }

    /**
     * Convenience method. Delegates to {@link PrivateMessageHead#getAuthor()}
     */
    public MinimalUser getAuthor() {
        if (getMessageHead() != null) {
            return getMessageHead().getAuthor();
        } else {
            return null;
        }
    }

    /**
     * Convenience method. Delegates to {@link PrivateMessageHead#getTimeSent()}
     */
    public Date getTimeSent() {
        if (getMessageHead() != null) {
            return getMessageHead().getTimeSent();
        } else {
            return null;
        }
    }

    /**
     * Convenience method. Delegates to {@link PrivateMessageHead#getRecipient()}
     */
    public MinimalUser getRecipient() {
        if (getMessageHead() != null) {
            return getMessageHead().getRecipient();
        } else {
            return null;
        }
    }

    public void setMessageHead(PrivateMessageHead messageHead) {
        m_messageHead = messageHead;
    }

    public PrivateMessageHead getMessageHead() {
        return m_messageHead;
    }

    /**
     * Convenience method. Delegates to {@link PrivateMessageHead#getReplyToMessageId()}
     */
    public String getReplyToMessageId() {
        if (getMessageHead() != null) {
            return getMessageHead().getReplyToMessageId();
        } else {
            return null;
        }
    }

    /**
     * @return <tt>true</tt>:Indicates that this is a dummy message (created for display, not retrieved).
     */
    public boolean isDummy() {
        return m_isDummy;
    }

    public void setDummy(boolean isDummy) {
        m_isDummy = isDummy;
    }
}
