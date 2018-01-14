package lifesgame.tapstudios.ca.lifesgame.fragment.introFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lifesgame.tapstudios.ca.lifesgame.R;

public class IntroFragment3 extends Fragment {
    private View introFragment1;

    public IntroFragment3() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        introFragment1 = inflater.inflate(R.layout.fragment_intro_fragment_3, container, false);
        return introFragment1;
    }
}
