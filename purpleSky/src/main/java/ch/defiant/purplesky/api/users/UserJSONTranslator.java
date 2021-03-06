package ch.defiant.purplesky.api.users;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.defiant.purplesky.api.common.JSONUtility;
import ch.defiant.purplesky.api.internal.APIUtility;
import ch.defiant.purplesky.api.internal.PurplemoonAPIConstantsV1;
import ch.defiant.purplesky.beans.BasicUser;
import ch.defiant.purplesky.beans.DetailedUser;
import ch.defiant.purplesky.beans.LocationBean;
import ch.defiant.purplesky.beans.MinimalUser;
import ch.defiant.purplesky.beans.PreviewUser;
import ch.defiant.purplesky.enums.OnlineStatus;
import ch.defiant.purplesky.enums.profile.Gender;
import ch.defiant.purplesky.enums.profile.MessengerType;
import ch.defiant.purplesky.enums.profile.OccupationType;
import ch.defiant.purplesky.enums.profile.ProfileStatus;
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
        HashMap<String, T> userMap = new HashMap<>();
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
            // Check status first
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

            translateMinimalUserProperties(jsonUserObject, user);

            if (BasicUser.class.isAssignableFrom(clazz)) {
                BasicUser basicUser = (BasicUser) user;
                translateBasicUserProperties(jsonUserObject, basicUser);
            }

            if (PreviewUser.class.isAssignableFrom(clazz)) {
                PreviewUser previewUser = (PreviewUser) user;
                translatePreviewUserProperties(jsonUserObject, previewUser);
            }

            if(DetailedUser.class.isAssignableFrom(clazz)){
                translateDetailedUserProperties(jsonUserObject, (DetailedUser) user);
            }

        } catch (JSONException e) {
            Log.w(TAG, "Translation from JSON to user failed", e);
            return null;
        }
        return user;
    }

    private static void translateMinimalUserProperties(JSONObject jsonUserObject, MinimalUser user) throws JSONException {
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
    }

    private static void translateBasicUserProperties(JSONObject jsonUserObject, BasicUser basicUser) throws JSONException {
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

    private static void translatePreviewUserProperties(JSONObject jsonUserObject, PreviewUser previewUser) {
        previewUser.setFirstName(jsonUserObject.optString(PurplemoonAPIConstantsV1.ProfileDetails.FIRST_NAME, null));
        previewUser.setLastName(jsonUserObject.optString(PurplemoonAPIConstantsV1.ProfileDetails.LAST_NAME, null));
        previewUser.setHeight(JSONUtility.optInt(jsonUserObject, PurplemoonAPIConstantsV1.ProfileDetails.HEIGHT, null));
        previewUser.setWeight(JSONUtility.optInt(jsonUserObject, PurplemoonAPIConstantsV1.ProfileDetails.WEIGHT, null));
        previewUser.setPhysique(APIUtility.translateToPhysique(jsonUserObject.optString(PurplemoonAPIConstantsV1.ProfileDetails.PHYSIQUE, null)));
        previewUser.setHairColor(APIUtility.translateToHairColor(jsonUserObject.optString(PurplemoonAPIConstantsV1.ProfileDetails.HAIR_COLOR, null)));
        previewUser.setHairLength(APIUtility.translateToHairLength(jsonUserObject.optString(PurplemoonAPIConstantsV1.ProfileDetails.HAIR_LENGTH, null)));
        previewUser.setEyeColor(APIUtility.translateToEyeColor(jsonUserObject.optString(PurplemoonAPIConstantsV1.ProfileDetails.EYE_COLOR, null)));
        previewUser.setFacialHair(APIUtility.translateToFacialHair(jsonUserObject.optString(PurplemoonAPIConstantsV1.ProfileDetails.FACIAL_HAIR, null)));
        previewUser.setDrinkerFrequency(APIUtility.translateToDrinkerFrequency(jsonUserObject.optString(PurplemoonAPIConstantsV1.ProfileDetails.DRINKER, null)));
        previewUser.setSmokerFrequency(APIUtility.translateToSmoker(jsonUserObject.optString(PurplemoonAPIConstantsV1.ProfileDetails.SMOKER, null)));
        previewUser.setVegetarian(APIUtility.translateToVegetarian(jsonUserObject.optString(PurplemoonAPIConstantsV1.ProfileDetails.VEGETARIAN, null)));
        previewUser.setWantsKids(APIUtility.translateToWantsKids(jsonUserObject.optString(PurplemoonAPIConstantsV1.ProfileDetails.WANTS_KIDS, null)));
        previewUser.setHasKids(APIUtility.translateToHasKids(jsonUserObject.optString(PurplemoonAPIConstantsV1.ProfileDetails.HAS_KIDS, null)));
        previewUser.setReligion(APIUtility.translateToReligion(jsonUserObject.optString(PurplemoonAPIConstantsV1.ProfileDetails.RELIGION, null)));
        previewUser.setPolitics(APIUtility.translateToPolitics(jsonUserObject.optString(PurplemoonAPIConstantsV1.ProfileDetails.POLITICS, null)));

        // TODO Preview user needs to be implemented here (Occupation!)
        addUserLocation(jsonUserObject, previewUser);
    }

    private static <T extends MinimalUser> void translateDetailedUserProperties(JSONObject jsonUserObject, DetailedUser user) throws JSONException {
        user.setNicknames(jsonUserObject.optString(PurplemoonAPIConstantsV1.ProfileDetails.NICKNAMES, null));
        user.setEmailAddress(jsonUserObject.optString(PurplemoonAPIConstantsV1.ProfileDetails.EMAIL_ADDRESS, null));
        user.setChatFrequency(APIUtility.translateToChatFrequency(jsonUserObject.optString(PurplemoonAPIConstantsV1.ProfileDetails.CHATS_FREQUENCY, null)));
        user.setChatNames(jsonUserObject.optString(PurplemoonAPIConstantsV1.ProfileDetails.CHATS_NAMES, null));
        user.setWhichChats(jsonUserObject.optString(PurplemoonAPIConstantsV1.ProfileDetails.CHATS_WHICH, null));
        user.setHomepage(jsonUserObject.optString(PurplemoonAPIConstantsV1.ProfileDetails.HOMEPAGE, null));

        JSONArray messengers = jsonUserObject.optJSONArray(PurplemoonAPIConstantsV1.ProfileDetails.CHATS_MESSENGERS);
        List<DetailedUser.MessengerBean> messengerBeans = new ArrayList<>();
        if(messengers != null){
            for(int i=0, size = messengers.length(); i<size; i++){
               JSONObject obj = messengers.optJSONObject(i);
               if(obj != null){
                   MessengerType type = APIUtility.translateToMessengerType(obj.optString(PurplemoonAPIConstantsV1.ProfileDetails.CHATS_MESSENGERS_TYPE, null));
                   String id = obj.optString(PurplemoonAPIConstantsV1.ProfileDetails.CHATS_MESSENGERS_ID, null);
                   if(type != null && StringUtility.isNotNullOrEmpty(id)){
                       messengerBeans.add(new DetailedUser.MessengerBean(type, id));
                   }
               }
           }
        }
        user.setMessengers(messengerBeans);

        JSONArray occupations = jsonUserObject.optJSONArray(PurplemoonAPIConstantsV1.ProfileDetails.OCCUPATIONS);
        List<DetailedUser.Occupation> occupationBeans = new ArrayList<>();
        if(occupations != null){
            for(int i=0, size = occupations.length(); i<size; i++){
                JSONObject obj = occupations.optJSONObject(i);
                if(obj != null){
                    OccupationType type = APIUtility.translateToOccupationType(obj.optString(PurplemoonAPIConstantsV1.ProfileDetails.OCCUPATION_TYPE, null));
                    if(type != null){
                        DetailedUser.Occupation occupation = new DetailedUser.Occupation(type);
                        occupation.setOccupationName(obj.optString(PurplemoonAPIConstantsV1.ProfileDetails.OCCUPATION_NAME, null));
                        occupation.setSchoolName(obj.optString(PurplemoonAPIConstantsV1.ProfileDetails.OCCUPATION_SCHOOL_NAME, null));
                        occupation.setSchoolDirection(obj.optString(PurplemoonAPIConstantsV1.ProfileDetails.OCCUPATION_SCHOOL_DIRECTION, null));
                        occupation.setCompanyName(obj.optString(PurplemoonAPIConstantsV1.ProfileDetails.OCCUPATION_COMPANY, null));
                        occupationBeans.add(occupation);
                    }
                }
            }
        }
        user.setOccupations(occupationBeans);

        if(jsonUserObject.has(PurplemoonAPIConstantsV1.ProfileDetails.BIRTHDATE)) {
            user.setBirthDate(DateUtility.parseJSONDate(jsonUserObject.optString(PurplemoonAPIConstantsV1.ProfileDetails.BIRTHDATE, null)));
        }
        JSONObject partnerInformation = jsonUserObject.optJSONObject(PurplemoonAPIConstantsV1.ProfileDetails.TARGET_PARTNER);
        if(partnerInformation != null){
            DetailedUser.RelationshipInformation info = new DetailedUser.RelationshipInformation();
            info.setRelationshipStatus(APIUtility.translateToRelationshipStatus(partnerInformation.optString(PurplemoonAPIConstantsV1.JSON_USER_RELATIONSHIP_STATUS, null))).
                setMaximumDistance(JSONUtility.optInt(partnerInformation, PurplemoonAPIConstantsV1.JSON_USER_RELATIONSHIP_MAXDISTANCE, null)).
                    setDesiredAgeFrom(JSONUtility.optInt(partnerInformation, PurplemoonAPIConstantsV1.JSON_USER_RELATIONSHIP_AGEFROM, null)).
                    setDesiredAgeTill(JSONUtility.optInt(partnerInformation, PurplemoonAPIConstantsV1.JSON_USER_RELATIONSHIP_AGETO, null)).
                setText(partnerInformation.optString(PurplemoonAPIConstantsV1.JSON_USER_RELATIONSHIP_TEXT));
            user.setRelationShipInformation(info);
        }

        JSONObject friendshipInformation = jsonUserObject.optJSONObject(PurplemoonAPIConstantsV1.ProfileDetails.TARGET_FRIENDS);
        if(friendshipInformation != null){
            DetailedUser.FriendshipInformation info = new DetailedUser.FriendshipInformation();
            info.setTargetGender(APIUtility.translateToTargetGender(friendshipInformation.optString(PurplemoonAPIConstantsV1.ProfileDetails.TARGET_FRIENDS_GENDER, null))).
                    setMaximumDistance(JSONUtility.optInt(friendshipInformation, PurplemoonAPIConstantsV1.JSON_USER_RELATIONSHIP_MAXDISTANCE, null)).
                    setDesiredAgeFrom(JSONUtility.optInt(friendshipInformation, PurplemoonAPIConstantsV1.JSON_USER_RELATIONSHIP_AGEFROM, null)).
                    setDesiredAgeTill(JSONUtility.optInt(friendshipInformation, PurplemoonAPIConstantsV1.JSON_USER_RELATIONSHIP_AGETO, null)).
                    setText(friendshipInformation.optString(PurplemoonAPIConstantsV1.JSON_USER_RELATIONSHIP_TEXT));
            user.setFriendshipInformation(info);
        }

        if(jsonUserObject.has(PurplemoonAPIConstantsV1.ProfileDetails.PROFILE_CREATION_DATE)){
            String date = jsonUserObject.optString(PurplemoonAPIConstantsV1.ProfileDetails.PROFILE_CREATION_DATE, null);
            Date creationDate;
            if(PurplemoonAPIConstantsV1.ProfileDetails.PROFILE_DATE_LAST24h.equals(date)){
                creationDate = new Date();
            } else {
                creationDate = DateUtility.parseJSONDate(date);
            }
            user.setCreateDate(creationDate);
        }

        if(jsonUserObject.has(PurplemoonAPIConstantsV1.ProfileDetails.PROFILE_LASTUPDATE_DATE)){
            String date = jsonUserObject.optString(PurplemoonAPIConstantsV1.ProfileDetails.PROFILE_LASTUPDATE_DATE, null);
            Date lastUpdate;
            if(PurplemoonAPIConstantsV1.ProfileDetails.PROFILE_DATE_LAST24h.equals(date)){
                lastUpdate = new Date();
            } else {
                lastUpdate = DateUtility.parseJSONDate(date);
            }
            user.setUpdateDate(lastUpdate);
        }

        if(jsonUserObject.has(PurplemoonAPIConstantsV1.ProfileDetails.PROFILE_LASTONLINE_DATE)){
            String date = jsonUserObject.optString(PurplemoonAPIConstantsV1.ProfileDetails.PROFILE_LASTONLINE_DATE, null);
            Date onlineDate;
            if(PurplemoonAPIConstantsV1.ProfileDetails.PROFILE_DATE_LAST24h.equals(date)){
                onlineDate = new Date();
            } else {
                onlineDate = DateUtility.parseJSONDate(date);
            }
            user.setLastOnlineDate(onlineDate);
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
            user.setEventTmp(map);
        }
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

}
