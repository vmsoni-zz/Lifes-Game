package lifesgame.tapstudios.ca.lifesgame.model;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.io.Serializable;

/**
 * Created by viditsoni on 2017-12-29.
 */

public class User implements Serializable {
    GoogleSignInAccount googleSignInAccount;
    public User(GoogleSignInAccount googleSignInAccount) {
        this.googleSignInAccount = googleSignInAccount;
    }

    public GoogleSignInAccount getGoogleSignInAccount() {
        return googleSignInAccount;
    }
}
