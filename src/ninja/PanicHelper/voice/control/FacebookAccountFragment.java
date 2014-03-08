package ninja.PanicHelper.voice.control;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
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

import java.util.Arrays;


/**
 * Created by Cataaa on 3/8/14.
 */
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
        LoginButton authButton = (LoginButton) getView().findViewById(R.id.activity_login_facebook_btn_login);
        authButton.setReadPermissions(Arrays.asList("basic_info"));
        authButton.setSessionStatusCallback(new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                if (session.isOpened()) {
                    // make request to the /me API
                    Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
                        // callback after Graph API response with user object
                        @Override
                        public void onCompleted(GraphUser user, Response response) {
                            if (user != null) {
                                loggedInName.setText("Logged in as: " + user.getName());
                            }

                        }
                    });
                }
                else{
                    loggedInName.setText("You are currently not logged in on Facebook");
                }
            }
        });

    }
}
