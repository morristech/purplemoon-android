package ch.defiant.purplesky.api.internal;

import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ch.defiant.purplesky.constants.PurplemoonAPIConstantsV1;
import ch.defiant.purplesky.core.UserSearchOptions;
import ch.defiant.purplesky.enums.Gender;
import ch.defiant.purplesky.enums.Sexuality;
import ch.defiant.purplesky.exceptions.PurpleSkyException;

/**
 * @author Patrick Bänziger
 * @since 1.0.1
 */
public final class APIUtility {

    public static JSONObject getJSONUserSearchObject(UserSearchOptions options) throws PurpleSkyException {
        JSONObject object = new JSONObject();
        try {
            if (options.getGenderSexualities() != null && !options.getGenderSexualities().isEmpty()) { // TODO Remove
                JSONArray arr = new JSONArray();
                for (String pair : options.getGenderSexualities()) {
                    if (pair == null) {
                        continue;
                    }
                    arr.put(pair);
                }

                object.put(PurplemoonAPIConstantsV1.JSON_USERSEARCH_GENDER_SEXUALITY, arr);
            }

            if (options.getAttractions() != null) {
                object.put(PurplemoonAPIConstantsV1.JSON_USERSEARCH_GENDER_SEXUALITY, createGenderSexualityCombinations(options.getAttractions()));
            }

            if (options.getMinAge() != null) {
                object.put(PurplemoonAPIConstantsV1.JSON_USERSEARCH_AGEMIN, options.getMinAge());
            }
            if (options.getMaxAge() != null) {
                object.put(PurplemoonAPIConstantsV1.JSON_USERSEARCH_AGEMAX, options.getMaxAge());
            }
            if (options.getCountryId() != null) {
                object.put(PurplemoonAPIConstantsV1.JSON_USERSEARCH_COUNTRY, options.getCountryId());
            }
            if (options.isShowOnlyOnline()) {
                object.put(PurplemoonAPIConstantsV1.JSON_USERSEARCH_ONLINE_ONLY, options.isShowOnlyOnline());
            }
            if (options.getMaxDistance() != null) {
                object.put(PurplemoonAPIConstantsV1.JSON_USERSEARCH_DISTANCE_KM, options.getMaxDistance());
            }

            return object;
        } catch (JSONException e) {
            throw new PurpleSkyException("Internal error",e); // FIXME pbn error handling
        }
    }

    private static JSONArray createGenderSexualityCombinations(List<Pair<Gender, Sexuality>> attractions) {
        JSONArray array = new JSONArray();
        for(Pair<Gender, Sexuality> p: attractions){
            String genderString = translateGender(p.first);
            array.put(genderString +PurplemoonAPIConstantsV1.JSON_USERSEARCH_GENDER_SEXUALITY_SEPARATOR + translateSexuality(p.second));
        }
        return array;
    }

    public static String translateSexuality(Sexuality s) {
        switch (s) {
            case HETEROSEXUAL_MALE:
                return PurplemoonAPIConstantsV1.JSON_USER_SEXUALITY_HETEROSEXUAL_VALUE;
            case HETEROSEXUAL_FEMALE:
                return PurplemoonAPIConstantsV1.JSON_USER_SEXUALITY_HETEROSEXUAL_VALUE;
            case GAY:
                return PurplemoonAPIConstantsV1.JSON_USER_SEXUALITY_HOMOSEXUAL_VALUE;
            case LESBIAN:
                return PurplemoonAPIConstantsV1.JSON_USER_SEXUALITY_HOMOSEXUAL_VALUE;
            case BISEXUAL:
                return "bi"; // TODO pbn add to constants
            default:
                throw new IllegalArgumentException("No api value for "+s);
        }
    }

    public static String translateGender(Gender g){
        switch(g){
            case MALE:
                return "male";
            case FEMALE:
                return "female";
            default:
                throw new IllegalArgumentException("No api value for "+g);
        }
    }
}
