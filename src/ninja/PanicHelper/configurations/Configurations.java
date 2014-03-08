package ninja.PanicHelper.configurations;

import android.os.Environment;
import android.util.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Configurations implements Serializable{

    private static final long serialVersionUID = 1L;

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

    private ArrayList<Contact> personList = new ArrayList<Contact>();

    private static Configurations configInstance;

    public static void checkIfLoad() {
        if (configInstance == null)
            Configurations.load();

    }

    public static void setGravity(float newGravity) {
        checkIfLoad();
        configInstance.gravity = newGravity;
    }

    public static void setSoundType(int soundType) {
        checkIfLoad();
        configInstance.soundType = soundType;
    }

    public static void setLightType(int lightType) {
        checkIfLoad();
        configInstance.lightType = lightType;
    }

    public static void setButtonAlarmTimer(int buttonAlarmTimer) {
        checkIfLoad();
        configInstance.buttonAlarmTimer = buttonAlarmTimer;
    }

    public static int getButtonAlarmTimer() {
        checkIfLoad();
        return configInstance.buttonAlarmTimer;
    }

    public static void setAcceleratorAlarmTimer(int acceleratorAlarmTimer) {
        checkIfLoad();
        configInstance.acceleratorAlarmTimer = acceleratorAlarmTimer;
    }

    public static int getAcceleratorAlarmTimer() {
        checkIfLoad();
        return configInstance.acceleratorAlarmTimer;
    }

    public static void setHoldAlarmTimer(float holdAlarmTimer) {
        checkIfLoad();
        configInstance.holdAlarmTimer = holdAlarmTimer;
    }

    public static void setServiceActivated(boolean isServiceActivated) {
        checkIfLoad();
        configInstance.isServiceActivated = isServiceActivated;
    }

    public static boolean isServiceActive() {
        checkIfLoad();

        return configInstance.isServiceActivated;
    }


    public static void setSendGPS(boolean sendGPS) {
        checkIfLoad();
        configInstance.sendGPS = sendGPS;
    }

    public static void setSilentAlarmButton(boolean silentAlarmButton) {
        checkIfLoad();
        configInstance.silentAlarmButton = silentAlarmButton;
    }

    public static boolean getSilentAlarmButton() {
        checkIfLoad();
        return configInstance.silentAlarmButton;
    }

    public static void setLightAlarmButton(boolean lightAlarmButton) {
        checkIfLoad();
        configInstance.lightAlarmButton = lightAlarmButton;
    }

    public static void setButtonMessage(String buttonMessage) {
        checkIfLoad();
        configInstance.buttonMessage = buttonMessage;
    }

    public static void setAcceleratorMessage(String acceleratorMessage) {
        checkIfLoad();
        configInstance.acceleratorMessage = acceleratorMessage;
    }

    public static void setConfigInstance(Configurations configInstance) {
        checkIfLoad();
        Configurations.configInstance = configInstance;
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
        return  true;
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
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("Write", "IO Exception");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            Log.d("Write", "Class not found");
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
