package configurations;

import android.os.Environment;
import android.util.Log;

import java.io.*;

public class Configurations implements Serializable{
    private float gravity = 2.0f;

    private static Configurations configInstance;


    public static void setGravity(float newGravity) {
        configInstance.gravity = newGravity;
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
}
