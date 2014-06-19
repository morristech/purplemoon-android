package ch.defiant.purplesky.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Qualifier;

import ch.defiant.purplesky.activities.LoginActivity;
import ch.defiant.purplesky.activities.SettingActivity;
import ch.defiant.purplesky.activities.main.MainActivity;
import ch.defiant.purplesky.api.internal.APIModule;
import ch.defiant.purplesky.core.internal.CoreModule;
import ch.defiant.purplesky.db.internal.DatabaseModule;
import ch.defiant.purplesky.dialogs.CreatePostitDialogFragment;
import ch.defiant.purplesky.dialogs.OnlineStatusDialogFragment;
import ch.defiant.purplesky.dialogs.RadarOptionsDialogFragment;
import ch.defiant.purplesky.dialogs.UploadPhotoDialogFragment;
import ch.defiant.purplesky.fragments.BaseFragment;
import ch.defiant.purplesky.fragments.BaseListFragment;
import ch.defiant.purplesky.fragments.ChatListFragment;
import ch.defiant.purplesky.fragments.DisplayProfileFragment;
import ch.defiant.purplesky.fragments.FavoritesFragment;
import ch.defiant.purplesky.fragments.PostitFragment;
import ch.defiant.purplesky.fragments.RadarGridFragment;
import ch.defiant.purplesky.fragments.UserStatsFragment;
import ch.defiant.purplesky.fragments.VisitorFragment;
import ch.defiant.purplesky.fragments.conversation.ConversationFragment;
import ch.defiant.purplesky.fragments.gallery.PictureFolderGridViewFragment;
import ch.defiant.purplesky.fragments.photovote.PhotoVoteFragment;
import ch.defiant.purplesky.fragments.photovote.PhotoVoteListFragment;
import ch.defiant.purplesky.fragments.usersearch.UserSearchResultsFragment;
import ch.defiant.purplesky.fragments.usersearch.UsernameSearchFragment;
import dagger.Module;
import dagger.Provides;

/**
 * Main module for injections.
 * @author Patrick Bänziger
 */
@Module(
    includes = {
            APIModule.class,
            CoreModule.class,
            DatabaseModule.class
    },
    injects = {
            PurpleSkyApplication.class,
            MainActivity.class,
            LoginActivity.class,
            BaseListFragment.class,
            BaseFragment.class,
            SettingActivity.class,

            UserService.class,

            VisitorFragment.class,
            PostitFragment.class,
            FavoritesFragment.class,
            ConversationFragment.class,
            UsernameSearchFragment.class,
            UserSearchResultsFragment.class,
            PhotoVoteFragment.class,
            PhotoVoteListFragment.class,
            PictureFolderGridViewFragment.class,
            UserStatsFragment.class,
            ChatListFragment.class,
            OnlineStatusDialogFragment.class,
            RadarGridFragment.class,
            RadarOptionsDialogFragment.class,
            CreatePostitDialogFragment.class,
            UploadPhotoDialogFragment.class

    }
)
public class PurpleSkyModule {

    private final PurpleSkyApplication app;

    public PurpleSkyModule(PurpleSkyApplication app) {
        this.app = app;
    }

}