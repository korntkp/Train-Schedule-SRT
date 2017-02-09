package io.github.shredktp.trainschedulesrt;

import android.content.Context;

/**
 * Created by Korshreddern on 08-Feb-17.
 */

public class Contextor {
    private static Contextor instance;
    private Context context;

    public static Contextor getInstance() {
        if (instance == null) instance = new Contextor();
        return instance;
    }

    private Contextor() {
    }

    public void init(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
}
