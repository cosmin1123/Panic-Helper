package ninja.PanicHelper.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import ninja.PanicHelper.R;
import ninja.PanicHelper.configurations.Configurations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * The class for generating the Facebook account fragment view and checking if the user
 * is authenticated on facebook.
 **/
public class FacebookAccountFragment extends Fragment {
    private TextView loggedInName;
    public FacebookAccountFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView  = inflater.inflate(R.layout.fragment_facebook, container, false);
        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();
        loggedInName = (TextView) getView().findViewById(R.id.loggedin_name);

        /* Check if Facebook session is still valid */
        if (Configurations.isLoggedIn()){
            Session session = Session.getActiveSession();
            if (session != null && session.isOpened()){
                Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
                    // callback after Graph API response with user object
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        if (user != null) {
                            loggedInName.setText("Logged in as: " + user.getName());
                            Configurations.setFacebookName(user.getName());
                        }

                    }
                });
            }
            else{
                Configurations.setLoggedIn(false);
                loggedInName.setText("You are currently not logged in on Facebook");
            }
        }
        else{
            loggedInName.setText("You are currently not logged in on Facebook");
        }

        /* Facebook Login Button */
        LoginButton authButton = (LoginButton) getView().findViewById(R.id.activity_login_facebook_btn_login);
        authButton.setReadPermissions(Arrays.asList("basic_info","xmpp_login"));
        authButton.setSessionStatusCallback(new Session.StatusCallback() {
            @Override
            public void call(final Session session, SessionState state, Exception exception) {
                if (session.isOpened()) {
                    // make request to the /me API
                    /* Check for publish permissions */
                    List<String> permissions = session.getPermissions();
                    List<String> PERMISSIONS = Arrays.asList("publish_actions");
                    if (!isSubsetOf(PERMISSIONS, permissions)) {
                        boolean pendingPublishReauthorization = true;
                        Session.NewPermissionsRequest newPermissionsRequest = new Session
                                .NewPermissionsRequest(getActivity(), PERMISSIONS);
                        session.requestNewPublishPermissions(newPermissionsRequest);
                        return;
                    }
                    Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
                        // callback after Graph API response with user object
                        @Override
                        public void onCompleted(GraphUser user, Response response) {
                            if (user != null) {
                                Configurations.setLoggedIn(true);
                                loggedInName.setText("Logged in as: " + user.getName());
                                Configurations.setFacebookName(user.getName());
                                Configurations.setAccessToken(session.getAccessToken());
                            }

                        }
                    });
                } else {
                    Configurations.setLoggedIn(false);
                    loggedInName.setText("You are currently not logged in on Facebook");
                }
            }
        });
    }

    private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
        for (String string : subset) {
            if (!superset.contains(string)) {
                return false;
            }
        }
        return true;
    }
    /* Save configurations on closing the page*/
    @Override
    public void onStop() {
        super.onStop();
        Configurations.save();
    }
}
