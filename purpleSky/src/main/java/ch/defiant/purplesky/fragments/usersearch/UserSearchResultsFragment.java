package ch.defiant.purplesky.fragments.usersearch;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.commonsware.cwac.endless.EndlessAdapter;

import java.util.ArrayList;
import java.util.List;

import ch.defiant.purplesky.R;
import ch.defiant.purplesky.adapters.UserSearchResultListAdapter;
import ch.defiant.purplesky.beans.LocationBean;
import ch.defiant.purplesky.beans.MinimalUser;
import ch.defiant.purplesky.beans.PreviewUser;
import ch.defiant.purplesky.beans.util.Pair;
import ch.defiant.purplesky.core.UserSearchOptions;
import ch.defiant.purplesky.fragments.BaseFragment;
import ch.defiant.purplesky.listeners.OpenUserProfileListener;

public class UserSearchResultsFragment extends BaseFragment {

    public static final String EXTRA_SEARCHOBJ = "searchobj";
    public static final String EXTRA_SEARCHNAME = "searchname";
    public static final String TAG = UserSearchResultsFragment.class.getSimpleName();

    private static final String SAVEINSTANCE_DATA = "data";
    private static final int RESULT_SIZE = 100;
    private String m_usernameSearch;
    private UserSearchOptions m_options;
    private UserSearchResultListAdapter m_adapter;

    private EndlessResultAdapter m_endlessAdapter;
    private ListView m_listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_adapter = new UserSearchResultListAdapter(getActivity());

        if(savedInstanceState != null){
            @SuppressWarnings("unchecked")
            List<MinimalUser> data = (List<MinimalUser>) savedInstanceState.getSerializable(SAVEINSTANCE_DATA);
            if(data != null){
                for (MinimalUser m : data) {
                    m_adapter.add(m);
                }
            }
        }

    }

    public void startUsernameSearch(String searchString) {
        m_options = null;
        m_usernameSearch = searchString;
        // Reset

        resetAdapter();
    }

    public void startSearch(UserSearchOptions searchOptions) {
        m_options = searchOptions;
        m_usernameSearch = null;

        // Reset
        if (m_options != null && m_options.getLocation() != null) {
            // To show distance, need the preview user for location
            m_options.setUserClass(PreviewUser.class);
            LocationBean locationBean = new LocationBean();
            Pair<Double, Double> loc = m_options.getLocation();
            locationBean.setLatitude(loc.getFirst());
            locationBean.setLongitude(loc.getSecond());
            m_adapter.setOwnLocation(locationBean);
        }

        resetAdapter();
    }

    private void resetAdapter() {
        m_endlessAdapter = new EndlessResultAdapter(getActivity(), m_adapter, R.layout.loading_listitem);
        m_listView.setAdapter(m_endlessAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        m_listView  = (ListView) inflater.inflate(R.layout.list_plain, container);
        m_listView.setOnItemClickListener(new OpenUserProfileListener(getActivity()));
        return m_listView;
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        final int size = m_adapter.getCount();
        ArrayList<MinimalUser> storedData = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            storedData.add(m_adapter.getItem(i));
        }
        
        outState.putSerializable(SAVEINSTANCE_DATA, storedData);
    }


    private boolean hasOptions() {
        return getArguments() != null && (getArguments().containsKey(EXTRA_SEARCHNAME) || getArguments().containsKey(EXTRA_SEARCHOBJ));
    }

    private boolean isSearchByName() {
        return m_usernameSearch != null;
    }
    
    private class EndlessResultAdapter extends EndlessAdapter {

        public EndlessResultAdapter(Context context, UserSearchResultListAdapter wrapped, int pendingResource) {
            super(context, wrapped, pendingResource);
        }

        private List<MinimalUser> m_data;

        @Override
        protected boolean cacheInBackground() throws Exception {
            if(m_options == null){
                UserSearchOptions opts = new UserSearchOptions();
                opts.setNumber(RESULT_SIZE);
                m_options = opts;
            }
            
            List<MinimalUser> result;
            if(isSearchByName()){
                result = apiAdapter.searchUserByName(m_usernameSearch, m_options);
            } else {
                result = apiAdapter.searchUser(m_options);
            }
            m_data = result;
            
            return false;
        }

        @Override
        protected void appendCachedData() {
            if (m_data != null) {
                for (MinimalUser user : m_data) {
                    m_adapter.add(user);
                }
            }
        }
    }
}
