package ninja.PanicHelper;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Cataaa on 3/4/14.
 */
public class HomeFragment extends Fragment {
    public static View fragmentView;
    public HomeFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.fragment_home, container, false);
        return fragmentView;
    }

    public static View getViewById(int target) {
        return fragmentView.findViewById(target);
    }


}
