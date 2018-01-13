package lifesgame.tapstudios.ca.lifesgame.activity;

/**
 * Created by viditsoni on 2018-01-01.
 */

import com.github.orangegangsters.lollipin.lib.managers.AppLockActivity;

/**
 * Created by oliviergoutay on 1/14/15.
 */
public class CustomPinActivity extends AppLockActivity {

    @Override
    public void showForgotDialog() {

    }

    @Override
    public void onPinFailure(int attempts) {

    }

    @Override
    public void onPinSuccess(int attempts) {

    }

    @Override
    public int getPinLength() {
        return super.getPinLength();//you can override this method to change the pin length from the default 4
    }
}