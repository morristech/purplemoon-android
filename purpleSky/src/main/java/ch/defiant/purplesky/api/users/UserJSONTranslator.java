package ch.defiant.purplesky.api.users;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ch.defiant.purplesky.api.internal.APIUtility;
import ch.defiant.purplesky.api.internal.PurplemoonAPIConstantsV1;
import ch.defiant.purplesky.beans.BasicUser;
import ch.defiant.purplesky.beans.DetailedUser;
import ch.defiant.purplesky.beans.LocationBean;
import ch.defiant.purplesky.beans.MinimalUser;
import ch.defiant.purplesky.beans.PreviewUser;
import ch.defiant.purplesky.beans.ProfileTriplet;
import ch.defiant.purplesky.enums.Gender;
import ch.defiant.purplesky.enums.OnlineStatus;
import ch.defiant.purplesky.enums.ProfileStatus;
import ch.defiant.purplesky.translators.ProfileTranslator;
import ch.defiant.purplesky.util.DateUtility;
import ch.defiant.purplesky.util.StringUtility;
import ch.defiant.purplesky.util.UserUtility;

/**
 * @author Patrick Bänziger
 * @since v.1.1.0
 */
public class UserJSONTranslator {

    private static String TAG = UserJSONTranslator.class.getSimpleName();

    public static <T extends MinimalUser> Map<String, T> translateToUsers(JSONArray array, Class<T> clazz) {
        // Translate users
        HashMap<String, T> userMap = new HashMap<String, T>();
        if (array == null) {
            return userMap;
        }

        for (int i = 0, size = array.length(); i < size; i++) {
            T translatedUser = translateToUser(array.optJSONObject(i), clazz);
            if (translatedUser != null && StringUtility.isNotNullOrEmpty(translatedUser.getUserId())) {
                userMap.put(translatedUser.getUserId(), translatedUser);
            }
        }

        return userMap;
    }

    public static <T extends MinimalUser> T translateToUser(JSONObject jsonUserObject, Class<T> clazz) {
        if (jsonUserObject == null)
            return null;
        T user = UserUtility.instantiateUser(clazz);

        try {
            // 'status': string,
            if (jsonUserObject.has(PurplemoonAPIConstantsV1.JSON_USER_PROFILESTATUS)) {
                String string = jsonUserObject.getString(PurplemoonAPIConstantsV1.JSON_USER_PROFILESTATUS);
                ProfileStatus status = ProfileStatus.getStatusByAPIValue(string);
                if (status == ProfileStatus.NOTFOUND) {
                    // No valid user.
                    return null;
                }
                user.setProfileStatus(status);
            }

            // 'profile_id': integer,
            if (jsonUserObject.has(PurplemoonAPIConstantsV1.JSON_USER_PROFILE_ID)) {
                String string = jsonUserObject.getString(PurplemoonAPIConstantsV1.JSON_USER_PROFILE_ID);
                user.setUserId(string);
            }
            // 'name': string,
            if (jsonUserObject.has(PurplemoonAPIConstantsV1.JSON_USER_NAME)) {
                String name = jsonUserObject.getString(PurplemoonAPIConstantsV1.JSON_USER_NAME);
                user.setUsername(name);
            }

            // 'gender': string,
            Gender gender = null;
            if (jsonUserObject.has(PurplemoonAPIConstantsV1.JSON_USER_GENDER)) {
                String string = jsonUserObject.getString(PurplemoonAPIConstantsV1.JSON_USER_GENDER);
                gender = APIUtility.toGender(string);
                user.setGender(gender);
            }

            // 'sexuality': string,
            if (jsonUserObject.has(PurplemoonAPIConstantsV1.JSON_USER_SEXUALITY)) {
                String string = jsonUserObject.getString(PurplemoonAPIConstantsV1.JSON_USER_SEXUALITY);
                user.setSexuality(APIUtility.toSexuality(string));
            }

            // 'age': integer,
            if (jsonUserObject.has(PurplemoonAPIConstantsV1.JSON_USER_AGE_INT)) {
                int age = jsonUserObject.getInt(PurplemoonAPIConstantsV1.JSON_USER_AGE_INT);
                user.setAge(age);
            }
            // 'verified': boolean,
            if (jsonUserObject.has(PurplemoonAPIConstantsV1.JSON_USER_VERIFIED_BOOL)) {
                boolean verified = jsonUserObject.getBoolean(PurplemoonAPIConstantsV1.JSON_USER_VERIFIED_BOOL);
                user.setAgeVerified(verified);
            }
            // 'picture': url,
            if (jsonUserObject.has(PurplemoonAPIConstantsV1.JSON_USER_PICTUREDIR_URL)) {
                String string = jsonUserObject.getString(PurplemoonAPIConstantsV1.JSON_USER_PICTUREDIR_URL);
                try {
                    user.setProfilePictureURLDirectory(new URL(string));
                } catch (MalformedURLException e) {
                    Log.d(TAG, "Invalid picture base URL: '" + string + "'");
                }
            }

            if (BasicUser.class.isAssignableFrom(clazz)) {
                BasicUser basicUser = (BasicUser) user;

                // 'noted': boolean,
                if (jsonUserObject.has(PurplemoonAPIConstantsV1.JSON_USER_HASNOTES_BOOL)) {
                    boolean hasNotes = jsonUserObject.getBoolean(PurplemoonAPIConstantsV1.JSON_USER_HASNOTES_BOOL);
                    basicUser.setHasNotes(hasNotes);
                }
                // 'friend': boolean,
                if (jsonUserObject.has(PurplemoonAPIConstantsV1.JSON_USER_ISFRIEND_BOOL)) {
                    boolean isFriend = jsonUserObject.getBoolean(PurplemoonAPIConstantsV1.JSON_USER_ISFRIEND_BOOL);
                    basicUser.setFriend(isFriend);
                }
                // 'known': boolean,
                if (jsonUserObject.has(PurplemoonAPIConstantsV1.JSON_USER_ISKNOWN)) {
                    boolean isKnown = jsonUserObject.getBoolean(PurplemoonAPIConstantsV1.JSON_USER_ISKNOWN);
                    basicUser.setKnown(isKnown);
                }
                // 'notes': string,
                if (jsonUserObject.has(PurplemoonAPIConstantsV1.JSON_USER_NOTES)) {
                    String notes = jsonUserObject.getString(PurplemoonAPIConstantsV1.JSON_USER_NOTES);
                    basicUser.setNotes(notes);
                }
                // 'online_status': string,
                if (jsonUserObject.has(PurplemoonAPIConstantsV1.JSON_USER_ONLINESTATUS)) {
                    String string = jsonUserObject.getString(PurplemoonAPIConstantsV1.JSON_USER_ONLINESTATUS);
                    OnlineStatus status =  APIUtility.toOnlineStatus(string);
                    basicUser.setOnlineStatus(status);
                } else {
                    basicUser.setOnlineStatus(OnlineStatus.OFFLINE);
                }

                // 'online_status_text': string,
                if (jsonUserObject.has(PurplemoonAPIConstantsV1.JSON_USER_ONLINESTATUSTEXT)) {
                    String text = jsonUserObject.getString(PurplemoonAPIConstantsV1.JSON_USER_ONLINESTATUSTEXT);
                    basicUser.setOnlineStatusText(text);
                }
                // 'online_since': timestamp
                if (jsonUserObject.has(PurplemoonAPIConstantsV1.JSON_USER_ONLINESINCE_TIMESTAMP)) {
                    long timestamp = jsonUserObject.getLong(PurplemoonAPIConstantsV1.JSON_USER_ONLINESINCE_TIMESTAMP);
                    basicUser.setOnlineSince(DateUtility.getFromUnixTime(timestamp));
                }
            }

            if (PreviewUser.class.isAssignableFrom(clazz)) {
                PreviewUser previewUser = (PreviewUser) user;

                // TODO Preview user needs to be implemented here (Occupation!)
                Map<String, ProfileTriplet> details = translateToUserDetails(jsonUserObject);
                addUserLocation(jsonUserObject, previewUser);
                previewUser.setProfileDetails(details);
            }

            if(DetailedUser.class.isAssignableFrom(clazz)){
                DetailedUser detailedUser = (DetailedUser) user;


                JSONObject partnerInformation = jsonUserObject.optJSONObject(PurplemoonAPIConstantsV1.ProfileDetails.TARGET_PARTNER);
                if(partnerInformation != null){
                    DetailedUser.RelationshipStatus status = ch.defiant.purplesky.api.users.APIUtility.translateToRelationshipStatus(partnerInformation.optString(PurplemoonAPIConstantsV1.JSON_USER_RELATIONSHIP_STATUS, null));
                    detailedUser.setRelationshipStatus(status);
                }

                if(jsonUserObject.has(PurplemoonAPIConstantsV1.ProfileDetails.EVENTS_TMP)){
                    Map<Integer, String> map = new HashMap<>();
                    JSONArray array = jsonUserObject.getJSONArray(PurplemoonAPIConstantsV1.ProfileDetails.EVENTS_TMP);
                    for(int i=0; i<array.length(); i++){
                        JSONObject obj = array.getJSONObject(i);
                        int eventId = obj.getInt(PurplemoonAPIConstantsV1.ProfileDetails.EVENT_ID);
                        String eventText = obj.getString(PurplemoonAPIConstantsV1.ProfileDetails.EVENT_TEXT);
                        map.put(eventId, eventText);
                    }
                    detailedUser.setEventTmp(map);
                }
            }

        } catch (JSONException e) {
            Log.w(TAG, "Translation from JSON to user failed", e);
            return null;
        }
        return user;
    }

    private static void addUserLocation(JSONObject obj, PreviewUser user) {
        if (obj.has(PurplemoonAPIConstantsV1.ProfileDetails.LOCATION_CURRENT)) {
            user.setCurrentLocation(translateToLocationBean(obj.optJSONObject(PurplemoonAPIConstantsV1.ProfileDetails.LOCATION_CURRENT)));
        }
        if (obj.has(PurplemoonAPIConstantsV1.ProfileDetails.LOCATION_HOME)) {
            user.setHomeLocation(translateToLocationBean(obj.optJSONObject(PurplemoonAPIConstantsV1.ProfileDetails.LOCATION_HOME)));
        }
        if (obj.has(PurplemoonAPIConstantsV1.ProfileDetails.LOCATION_HOME2)) {
            user.setHome2Location(translateToLocationBean(obj.optJSONObject(PurplemoonAPIConstantsV1.ProfileDetails.LOCATION_HOME2)));
        }
    }

    private static LocationBean translateToLocationBean(JSONObject obj) {
        if (obj == null) {
            return null;
        }
        double latid = obj.optDouble(PurplemoonAPIConstantsV1.JSON_LOCATION_LAT);
        double longit = obj.optDouble(PurplemoonAPIConstantsV1.JSON_LOCATION_LONG);
        String country = obj.optString(PurplemoonAPIConstantsV1.JSON_LOCATION_COUNTRYID, null);
        String locationDesc = obj.optString(PurplemoonAPIConstantsV1.JSON_LOCATION_NAME, null);
        return new LocationBean(longit, latid, country, locationDesc);
    }

    /**
     * Translates (recursively) all the properties into profile triplets.
     *
     * @param jsonUserObject
     *            The JSON object to translate
     * @return List of profile triplets, localized.
     */
    private static Map<String, ProfileTriplet> translateToUserDetails(JSONObject jsonUserObject) {
        if (jsonUserObject == null)
            return Collections.emptyMap();

        HashMap<String, ProfileTriplet> list = new HashMap<String, ProfileTriplet>();

        Iterator<?> keys = jsonUserObject.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();

            String translatedKey = ProfileTranslator.translateAPIKey(key);
            if (translatedKey == null) {
                // Cannot display this, skip it (If not listed as translatable, either unknown or handled otherwise).
                continue;
            }

            Object object = jsonUserObject.opt(key);
            if (object == null || JSONObject.NULL.equals(object)) {
                // Don't translate
            } else if (object instanceof JSONObject) {
                Map<String, ProfileTriplet> objectDetails = translateToUserDetails((JSONObject) object);
                list.put(key, new ProfileTriplet(key, translatedKey, objectDetails));
            } else if (object instanceof JSONArray) {
                ArrayList<Map<String, ProfileTriplet>> objectDetails = translateToUserDetails((JSONArray) object);
                list.put(key, new ProfileTriplet(key, objectDetails));
            } else {
                // Must be a simple one (String, Integer, Double, Long, Boolean)
                // We try to translate string values, but may fail (user specified). But that won't worry us.
                String translatedValue = null;
                Object rawValue = null;
                if (object instanceof String) {
                    translatedValue = ProfileTranslator.translateAPIValue(key, (String) object);
                } else {
                    rawValue = object;
                    assert (rawValue instanceof Serializable) : "Non-serializable object! Class was: " + rawValue.getClass();
                }

                list.put(key, new ProfileTriplet(key, translatedKey, translatedValue, (Serializable) rawValue));
            }
        }

        return list;
    }

    private static ArrayList<Map<String, ProfileTriplet>> translateToUserDetails(JSONArray jsonArray) {
        // These must be ordered
        ArrayList<Map<String, ProfileTriplet>> list = new ArrayList<>();
        for (int i = 0, count = jsonArray.length(); i < count; i++) {
            JSONObject obj = jsonArray.optJSONObject(i);
            if (obj == null) {
                continue;
            }

            Map<String, ProfileTriplet> details = translateToUserDetails(obj);
            list.add(details);
        }

        return list;
    }

}