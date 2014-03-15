package ninja.PanicHelper.facebook;

import android.os.AsyncTask;
import android.util.Log;
import de.measite.smack.Sasl;
import org.apache.harmony.javax.security.auth.callback.CallbackHandler;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.sasl.SASLMechanism;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class FacebookChatAPI {
    private static String accessToken;
    private final String TAG = "Facebook Chat API";
    private final String applicationKey = "582917665107934";

    public FacebookChatAPI(String accessToken){
        this.accessToken = accessToken;
    }

    public void sendMessage(final String[] contactUsernames, final String message){
        (new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                XMPPConnection connection = createXMPPConnection();
                try
                {
                    connection.connect();
                    connection.login(applicationKey, accessToken );
                    int counter = 0;
                    for (String username : contactUsernames){
                        System.out.println("Sending to " + username + "message #" + (++counter));
                        String contactUsernameId = getIdByUsername(username);
                        String to = String.format( "-%s@chat.facebook.com",contactUsernameId);
                        Chat chat = connection.getChatManager().createChat( to, null );
                        chat.sendMessage(message);
                    }
                }
                catch( XMPPException e )
                {
                    e.printStackTrace();
                }
                finally
                {
                    connection.disconnect();
                }
                return null;
            }
        }).execute();
    }

    private static synchronized XMPPConnection createXMPPConnection()
    {
        SASLAuthentication.registerSASLMechanism(
                SASLXFacebookPlatformMechanism.NAME,
                SASLXFacebookPlatformMechanism.class );
        SASLAuthentication.supportSASLMechanism(
                SASLXFacebookPlatformMechanism.NAME, 0 );

        ConnectionConfiguration configuration = new ConnectionConfiguration(
                "chat.facebook.com", 5222 );
        configuration.setSASLAuthenticationEnabled( true );

        return new XMPPConnection( configuration );
    }

    public static class SASLXFacebookPlatformMechanism extends SASLMechanism
    {
        public static final String NAME = "X-FACEBOOK-PLATFORM";

        public SASLXFacebookPlatformMechanism(
                SASLAuthentication saslAuthentication )
        {
            super( saslAuthentication );
        }

        private String apiKey = "";

        private String accessToken = "";

        @Override
        protected void authenticate() throws IOException, XMPPException
        {
            AuthMechanism stanza = new AuthMechanism( getName(), null );
            getSASLAuthentication().send( stanza );
        }

        @SuppressWarnings( "hiding" )
        @Override
        public void authenticate( String apiKey, String host, String accessToken )
                throws IOException, XMPPException
        {
            if( apiKey == null || accessToken == null )
            {
                throw new IllegalStateException( "Invalid parameters!" );
            }

            this.apiKey = apiKey;
            this.accessToken = accessToken;
            this.hostname = host;

            String[] mechanisms = { "DIGEST-MD5" };
            Map<String, String> props = new HashMap<String, String>();
            this.sc = Sasl.createSaslClient( mechanisms, null, "xmpp", host,
                    props, this );
            authenticate();
        }

        @Override
        public void authenticate( String username, String host,
                                  CallbackHandler cbh ) throws IOException, XMPPException
        {
            String[] mechanisms = { "DIGEST-MD5" };
            Map<String, String> props = new HashMap<String, String>();
            this.sc = Sasl.createSaslClient(mechanisms, null, "xmpp", host,
                    props, cbh);
            authenticate();
        }

        @Override
        protected String getName()
        {
            return NAME;
        }

        @Override
        public void challengeReceived( String challenge ) throws IOException, UnsupportedEncodingException {
            byte response[] = null;
            if( challenge != null )
            {
                String decodedResponse = new String(
                        org.jivesoftware.smack.util.Base64.decode( challenge ) );
                Map<String, String> parameters = getQueryMap( decodedResponse );

                String version = "1.0";
                String nonce = parameters.get( "nonce" );
                String method = parameters.get( "method" );

                Long callId = Long.valueOf( System.currentTimeMillis() );

                String composedResponse = String
                        .format(
                                "method=%s&nonce=%s&access_token=%s&api_key=%s&call_id=%s&v=%s",
                                URLEncoder.encode(method, "UTF-8"),
                                URLEncoder.encode( nonce, "UTF-8" ),
                                URLEncoder.encode( this.accessToken, "UTF-8" ),
                                URLEncoder.encode( this.apiKey, "UTF-8" ),
                                callId, URLEncoder.encode( version, "UTF-8" ) );
                response = composedResponse.getBytes();
            }

            String authenticationText = "";

            if( response != null )
            {
                authenticationText = org.jivesoftware.smack.util.Base64
                        .encodeBytes(
                                response,
                                org.jivesoftware.smack.util.Base64.DONT_BREAK_LINES );
            }

            SASLMechanism.Response stanza = new Response( authenticationText );

            getSASLAuthentication().send( stanza );
        }

        private Map<String, String> getQueryMap( String query )
        {
            String[] params = query.split( "&" );
            Map<String, String> map = new HashMap<String, String>();
            for( String param : params )
            {
                String name = param.split( "=" )[0];
                String value = param.split( "=" )[1];
                map.put( name, value );
            }
            return map;
        }
    }

    //We need to get the Facebook ID of the username with who we are trying to chat.
    private String getIdByUsername(String contactUsername){
        HttpURLConnection conn;
        BufferedReader rd;
        String line;
        String result = "";
        try {
            URL url = new URL("http://graph.facebook.com/" + contactUsername);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) {
                result += line;
            }
            rd.close();
        } catch (Exception e) {
            Log.i(TAG, "Error at getting ID for " + contactUsername);
            Log.e(TAG, e.toString());
            return null;
        }
        Log.i(TAG, "Get Method result: " + result + "\n");
        String contactAccountID = result.split(",")[0].split(":")[1].replace("\"", "");
        Log.i(TAG, "The ID for " + contactUsername + " is " + contactAccountID + "\n");
        return contactAccountID;
    }
}