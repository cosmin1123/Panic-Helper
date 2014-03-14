package ninja.PanicHelper.configurations;

import android.os.Environment;
import android.util.Log;
import ninja.PanicHelper.safetyMeasures.GPSTracker;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Configurations implements Serializable{

    private static final long serialVersionUID = 1L;

    /* Car crash settings */
    private boolean isCrashServiceActivated = false;
    private boolean isCrashYellService = false;
    private boolean isCrashLightService = false;
    private boolean isCrashPostOnWall = false;
    private boolean isCrashVoiceRec = false;

    private int crashWaitingTime = 30;
    private String crashMessage = "Help me! I was in a car accident!\n";

    /* Help button settings */
    private boolean isButtonYellService = false;
    private boolean isButtonLightService = false;
    private boolean isButtonPostOnWall = false;
    private boolean isButtonVoiceRec = false;

    private int buttonWaitingTime = 30;
    private int impactSpeed = 50;
    private float gravity = 2.0f;
    private String buttonMessage = "Help me! I am hurt!\n";
    private int buttonHoldTime = 5;

    /* Facebook data */
    private boolean isLoggedIn = false;
    private String accessToken = null;

    private ArrayList<Contact> personList = new ArrayList<Contact>();

    private static Configurations configInstance;

    public static void checkIfLoad() {
        if (configInstance == null)
            Configurations.load();

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

    public static int getImpactSpeed() {
        checkIfLoad();
        return configInstance.impactSpeed;
    }

    public static void setImpactSpeed(int impactSpeed) {
        configInstance.impactSpeed = impactSpeed;
    }

    public static String getCrashMessage() {
        checkIfLoad();
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

    public static int getButtonWaitingTime() {
        checkIfLoad();
        return configInstance.buttonWaitingTime;
    }

    public static void setButtonWaitingTime(int buttonWaitingTime) {
        checkIfLoad();
        configInstance.buttonWaitingTime = buttonWaitingTime;
    }

    public static float getGravity() {
        checkIfLoad();
        return 10 * configInstance.impactSpeed / 36; // formula fizica
    }

    public static void setGravity(float gravity) {
        checkIfLoad();
        configInstance.gravity = gravity;
    }

    public static String getButtonMessage() {
        checkIfLoad();
        return configInstance.buttonMessage;
    }

    public static void setButtonMessage(String buttonMessage) {
        checkIfLoad();
        configInstance.buttonMessage = buttonMessage;
    }

    public static int getButtonHoldTime() {
        checkIfLoad();
        return configInstance.buttonHoldTime;
    }

    public static void setButtonHoldTime(int buttonHoldTime) {
        checkIfLoad();
        configInstance.buttonHoldTime = buttonHoldTime;
    }

    public static boolean isCrashPostOnWall() {
        checkIfLoad();
        return configInstance.isCrashPostOnWall;
    }

    public static void setCrashPostOnWall(boolean isCrashPostOnWall) {
        checkIfLoad();
        configInstance.isCrashPostOnWall = isCrashPostOnWall;
    }

    public static boolean isButtonPostOnWall() {
        checkIfLoad();
        return configInstance.isButtonPostOnWall;
    }

    public static void setButtonPostOnWall(boolean isButtonPostOnWall) {
        checkIfLoad();
        configInstance.isButtonPostOnWall = isButtonPostOnWall;
    }

    public static boolean isCrashVoiceRec() {
        checkIfLoad();
        return configInstance.isCrashVoiceRec;
    }

    public static void setCrashVoiceRec(boolean isCrashVoiceRec) {
        checkIfLoad();
        configInstance.isCrashVoiceRec = isCrashVoiceRec;
    }

    public static boolean isButtonVoiceRec() {
        checkIfLoad();
        return configInstance.isButtonVoiceRec;
    }

    public static void setButtonVoiceRec(boolean isButtonVoiceRec) {
        checkIfLoad();
        configInstance.isButtonVoiceRec = isButtonVoiceRec;
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
