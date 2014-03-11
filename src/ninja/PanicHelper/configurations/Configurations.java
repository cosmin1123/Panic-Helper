package ninja.PanicHelper.configurations;

import android.os.Environment;
import android.util.Log;
import ninja.PanicHelper.safetyMeasures.GPSTracker;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Configurations implements Serializable{

    private static final long serialVersionUID = 1L;

    private final String myLocationPattern = "\n\nMy location is:\n";
    private final String myLocation = "\n\nMy location is:\n" + GPSTracker.getLocationLink();

    /* Car crash settings */
    private boolean isCrashServiceActivated = false;
    private boolean isCrashYellService = false;
    private boolean isCrashLightService = false;

    private int crashWaitingTime = 30;
    private String crashMessage = "Help me! I was in a car accident!\n";

    /* Help button settings */
    private boolean isButtonYellService = false;
    private boolean isButtonLightService = false;
    private boolean sendGPS = false;

    private int buttonWaitingTime = 30;
    private float holdTime = 2.0f;
    private float gravity = 2.0f;
    private String buttonMessage = "Help me! I am hurt!\n";

    /* ???????????????????????????????????????????*/
    private int soundType = 0;
    private int lightType = 0;
    private boolean silentAlarmButton = false;
    private boolean lightAlarmButton = false;


    /* Facebook data */
    private boolean isLoggedIn = false;
    private String accessToken = null;

    private ArrayList<Contact> personList = new ArrayList<Contact>();

    private static Configurations configInstance;

    public static void checkIfLoad() {
        if (configInstance == null)
            Configurations.load();

    }

    public static String getMyLocationPattern() {
        return configInstance.myLocationPattern;
    }

    public static boolean isCrashServiceActivated() {
        checkIfLoad();
        return configInstance.isCrashServiceActivated;
    }

    public static void setCrashServiceActivated(boolean isCrashServiceActivated) {
        checkIfLoad();
        configInstance.isCrashServiceActivated = isCrashServiceActivated;
    }

    public static boolean isCrashYellService() {
        checkIfLoad();
        return configInstance.isCrashYellService;
    }

    public static void setCrashYellService(boolean isCrashYellService) {
        checkIfLoad();
        configInstance.isCrashYellService = isCrashYellService;
    }

    public static boolean isCrashLightService() {
        checkIfLoad();
        return configInstance.isCrashLightService;
    }

    public static void setCrashLightService(boolean isCrashLightService) {
        checkIfLoad();
        configInstance.isCrashLightService = isCrashLightService;
    }

    public static int getCrashWaitingTime() {
        checkIfLoad();
        return configInstance.crashWaitingTime;
    }

    public static void setCrashWaitingTime(int crashWaitingTime) {
        checkIfLoad();
        configInstance.crashWaitingTime = crashWaitingTime;
    }

    public static String getCrashMessage() {
        checkIfLoad();
        if (configInstance.sendGPS) {
            Log.d("testgps", "pun gps");
            return configInstance.crashMessage + configInstance.myLocation;
        }
        Log.d("testgps", "nu pun gps");
        return configInstance.crashMessage;
    }

    public static void setCrashMessage(String crashMessage) {
        checkIfLoad();
        configInstance.crashMessage = crashMessage;
    }

    public static boolean isButtonYellService() {
        checkIfLoad();
        return configInstance.isButtonYellService;
    }

    public static void setButtonYellService(boolean isButtonYellService) {
        checkIfLoad();
        configInstance.isButtonYellService = isButtonYellService;
    }

    public static boolean isButtonLightService() {
        checkIfLoad();
        return configInstance.isButtonLightService;
    }

    public static void setButtonLightService(boolean isButtonLightService) {
        checkIfLoad();
        configInstance.isButtonLightService = isButtonLightService;
    }

    public static boolean isSendGPS() {
        checkIfLoad();
        return configInstance.sendGPS;
    }

    public static void setSendGPS(boolean sendGPS) {
        checkIfLoad();
        configInstance.sendGPS = sendGPS;
    }

    public static int getButtonWaitingTime() {
        checkIfLoad();
        return configInstance.buttonWaitingTime;
    }

    public static void setButtonWaitingTime(int buttonWaitingTime) {
        checkIfLoad();
        configInstance.buttonWaitingTime = buttonWaitingTime;
    }

    public static float getHoldTime() {
        checkIfLoad();
        return configInstance.holdTime;
    }

    public static void setHoldTime(float holdTime) {
        checkIfLoad();
        configInstance.holdTime = holdTime;
    }

    public static float getGravity() {
        checkIfLoad();
        return configInstance.gravity;
    }

    public static void setGravity(float gravity) {
        checkIfLoad();
        configInstance.gravity = gravity;
    }

    public static String getButtonMessage() {
        checkIfLoad();
        if (configInstance.sendGPS) {
            Log.d("testgps", "pun gps");
            return configInstance.buttonMessage + configInstance.myLocation;
        }
        Log.d("testgps", "nu pun gps");
        return configInstance.buttonMessage;
    }

    public static void setButtonMessage(String buttonMessage) {
        checkIfLoad();
        configInstance.buttonMessage = buttonMessage;
    }

    public static int getSoundType() {
        checkIfLoad();
        return configInstance.soundType;
    }

    public static void setSoundType(int soundType) {
        checkIfLoad();
        configInstance.soundType = soundType;
    }

    public static int getLightType() {
        checkIfLoad();
        return configInstance.lightType;
    }

    public static void setLightType(int lightType) {
        checkIfLoad();
        configInstance.lightType = lightType;
    }

    public static boolean getSilentAlarmButton() {
        checkIfLoad();
        return configInstance.silentAlarmButton;
    }

    public static void setSilentAlarmButton(boolean silentAlarmButton) {
        checkIfLoad();
        configInstance.silentAlarmButton = silentAlarmButton;
    }

    public static boolean isLightAlarmButton() {
        checkIfLoad();
        return configInstance.lightAlarmButton;
    }

    public static void setLightAlarmButton(boolean lightAlarmButton) {
        checkIfLoad();
        configInstance.lightAlarmButton = lightAlarmButton;
    }

    public static boolean isAccident(float currentGravity) {
        checkIfLoad();
        return currentGravity >= configInstance.gravity;
    }

    public static boolean addContact(Contact c) {
        checkIfLoad();
        for(Contact t : configInstance.personList) {
            if(t.name.compareTo(c.name) == 0)
                return false;
        }
        configInstance.personList.add(c);
        return  true;
    }

    public static boolean addContactAt(Integer position, Contact c) {
        checkIfLoad();
        for(Contact t : configInstance.personList) {
            if(t.name.compareTo(c.name) == 0)
                return false;
        }

        configInstance.personList.add(position, c);
        return true;
    }

    public static String[] getContactNames() {
        checkIfLoad();

        String [] temp = new String[configInstance.personList.size()];
        int i = 0;
        for(Contact c : configInstance.personList) {
            temp[i] = c.name;
            i++;
        }
        return temp;
    }

    public static void removeContact(String name) {
        checkIfLoad();

        for(int i = 0 ; i < configInstance.personList.size(); i++) {
            if(configInstance.personList.get(i).name.compareTo(name) == 0) {
                configInstance.personList.remove(i);
            }
        }
    }

    public static Contact getContactByName(String name) {
        checkIfLoad();

        for(int i = 0 ; i < configInstance.personList.size(); i++) {
            if(configInstance.personList.get(i).name.compareTo(name) == 0) {
                return configInstance.personList.get(i);
            }
        }
        return null;
    }

    public static Integer getContactPositionByName(String name) {
        for(int i = 0 ; i < configInstance.personList.size(); i++) {
            if(configInstance.personList.get(i).name.compareTo(name) == 0) {
                return i;
            }
        }
        return -1;
    }

    public static LinkedList<Contact> getFirstFive() {
        checkIfLoad();

        LinkedList<Contact> temp = new LinkedList<Contact>();
        for(int i = 0 ; i < configInstance.personList.size(); i++) {
            temp.add(configInstance.personList.get(i));
            if(temp.size() == 5)
                return temp;
        }

        for (int i = temp.size(); i < 5; i++) {
            temp.add(new Contact("Empty", "", "", false, false, false));

        }
        return temp;
    }

    public static void save()
    {
        try
        {
            checkIfLoad();

            ObjectOutputStream os = new ObjectOutputStream(
                    new FileOutputStream(new File(Environment.getExternalStorageDirectory(), "/config.dat") ));

            os.writeObject(configInstance);
            os.close();

            if(configInstance == null)
                configInstance = new Configurations();
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
                new FileInputStream(new File(Environment.getExternalStorageDirectory(), "/config.dat") )));

            configInstance = (Configurations)os.readObject();
            os.close();

            if(configInstance == null)
                configInstance = new Configurations();

            Log.d("Write", "Load success");

        } catch (FileNotFoundException e) {
           configInstance = new Configurations();
            Log.d("Write", "File not found");
        } catch (StreamCorruptedException e) {
            Log.d("Write", "Corrupted Stream");
            configInstance = new Configurations();
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("Write", "IO Exception");
            configInstance = new Configurations();
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            configInstance = new Configurations();
            Log.d("Write", "Class not found");
            e.printStackTrace();
        }
    }

    public static void setLoggedIn(boolean isLoggedIn) {
        configInstance.isLoggedIn = isLoggedIn;
    }

    public static boolean isLoggedIn(){
        return configInstance.isLoggedIn;
    }

    public static void setAccessToken(String accessToken) {
        configInstance.accessToken = accessToken;
    }

    public static String getAccessToken(){
        return configInstance.accessToken;
    }
}
