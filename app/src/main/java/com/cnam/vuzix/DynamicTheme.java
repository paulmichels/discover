package com.cnam.vuzix;

import com.vuzix.hud.resources.DynamicThemeApplication;

/* Fait changer le theme du HUD selon la luminosité ambiante */

public class DynamicTheme extends DynamicThemeApplication {
    @Override
    protected int getNormalThemeResId() {
        return R.style.AppTheme;
    }

    @Override
    protected int getLightThemeResId() {
        return R.style.AppTheme_Light;
    }
}
