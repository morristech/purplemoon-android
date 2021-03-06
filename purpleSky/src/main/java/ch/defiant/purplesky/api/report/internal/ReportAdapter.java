package ch.defiant.purplesky.api.report.internal;

import android.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import ch.defiant.purplesky.api.common.APINetworkUtility;
import ch.defiant.purplesky.api.common.ErrorJSONTranslator;
import ch.defiant.purplesky.api.internal.PurplemoonAPIConstantsV1;
import ch.defiant.purplesky.api.report.IReportAdapter;
import ch.defiant.purplesky.api.report.ReportResponse;
import ch.defiant.purplesky.enums.UserReportReason;
import ch.defiant.purplesky.exceptions.PurpleSkyException;
import ch.defiant.purplesky.util.HTTPURLResponseHolder;
import ch.defiant.purplesky.util.StringUtility;

/**
 * @author Patrick Bänziger
 * @since v.1.1.0
 */
class ReportAdapter implements IReportAdapter {

    public static final ReportAdapter INSTANCE = new ReportAdapter();

    @Override
    public ReportResponse reportUser(String profileId, UserReportReason reason, String description) throws IOException, PurpleSkyException {
        StringBuilder sb = new StringBuilder();
        sb.append(PurplemoonAPIConstantsV1.BASE_URL);
        sb.append(ReportAPIConstants.REPORT_URL);
        sb.append(profileId);

        Pair<String,String> param1 = new Pair<>(ReportAPIConstants.REPORT_REASON_PARAM, new ReportReasonTranslator().translate(reason));
        Pair<String,String> param2 = new Pair<>(ReportAPIConstants.REPORT_DESCRIPTION_PARAM, description);

        List<Pair<String,String>> args = Arrays.asList(param1, param2);

        HTTPURLResponseHolder response = APINetworkUtility.postForResponseHolderNoThrow(new URL(sb.toString()), args, null);
        if (response.isSuccessful()) {
            return ReportResponse.OK;
        } else {
            String errorString = new ErrorJSONTranslator().translate(response.getError());
            if(StringUtility.isNullOrEmpty(errorString)){
                return ReportResponse.ERROR;
            } else {
                return new ReportResponseTranslator().translate(errorString);
            }
        }
    }
}
