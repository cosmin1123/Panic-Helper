package ninja.PanicHelper.configurations;

import android.os.Environment;
import android.util.Log;

import java.io.*;

public class Configurations implements Serializable{

    private int soundType = 0;
    private int lightType = 0;

    private float gravity = 2.0f;
    private int buttonAlarmTimer = 30;
    private int acceleratorAlarmTimer = 30;
    private float holdAlarmTimer = 2.0f;

    /* Facebook data */
    private boolean isLoggedIn = false;
    private String accessToken = null;

    private boolean isServiceActivated = false;
    private boolean sendGPS = false;
    private boolean silentAlarmButton = false;
    private boolean lightAlarmButton = false;

    private String buttonMessage = "Mesaj buton";
    private String acceleratorMessage = "Mesaj accelerometru";

    private static Configurations configInstance;

    public static void setGravity(float newGravity) {
        configInstance.gravity = newGravity;
    }

    public static void setSoundType(int soundType) {
        configInstance.soundType = soundType;
    }

    public static void setLightType(int lightType) {
        configInstance.lightType = lightType;
    }

    public static void setButtonAlarmTimer(int buttonAlarmTimer) {
        configInstance.buttonAlarmTimer = buttonAlarmTimer;
    }

    public static int getButtonAlarmTimer() {
        return configInstance.buttonAlarmTimer;
    }

    public static void setAcceleratorAlarmTimer(int acceleratorAlarmTimer) {
        configInstance.acceleratorAlarmTimer = acceleratorAlarmTimer;
    }

    public static int getAcceleratorAlarmTimer() {
        return configInstance.acceleratorAlarmTimer;
    }

    public static void setHoldAlarmTimer(float holdAlarmTimer) {
        configInstance.holdAlarmTimer = holdAlarmTimer;
    }

    public static void setServiceActivated(boolean isServiceActivated) {
        configInstance.isServiceActivated = isServiceActivated;
    }

    public static boolean isServiceActive() {
        return configInstance.isServiceActivated;
    }


    public static void setSendGPS(boolean sendGPS) {
        configInstance.sendGPS = sendGPS;
    }

    public static void setSilentAlarmButton(boolean silentAlarmButton) {
        configInstance.silentAlarmButton = silentAlarmButton;
    }

    public static void setLightAlarmButton(boolean lightAlarmButton) {
        configInstance.lightAlarmButton = lightAlarmButton;
    }

    public static void setButtonMessage(String buttonMessage) {
        configInstance.buttonMessage = buttonMessage;
    }

    public static void setAcceleratorMessage(String acceleratorMessage) {
        configInstance.acceleratorMessage = acceleratorMessage;
    }

    public static void setConfigInstance(Configurations configInstance) {
        Configurations.configInstance = configInstance;
    }

    public static boolean isAccident(float currentGravity) {
        return currentGravity >= configInstance.gravity;
    }

    public static void save()
    {
        try
        {
            ObjectOutputStream os = new ObjectOutputStream(
                    new FileOutputStream(new File(Environment.getExternalStorageDirectory(), "/data.dat") ));

            os.writeObject(configInstance);
            os.close();
            Log.d("Write","Sucess");
        }

        catch(IOException e)
        {
            Log.d("Write", e.toString());
        }

    }

    public static void load() {
        try {
            ObjectInputStream os = new ObjectInputStream((
                new FileInputStream(new File(Environment.getExternalStorageDirectory(), "/data.dat") )));

            configInstance = (Configurations)os.readObject();
            os.close();

            if(configInstance == null)
                configInstance = new Configurations();

            Log.d("Write", "Load success");
        } catch (FileNotFoundException e) {
           configInstance = new Configurations();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public boolean isLoggedIn(){
        return isLoggedIn;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken(){
        return accessToken;
    }
}
