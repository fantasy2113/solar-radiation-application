package de.josmer.app.lib.handler;

import de.josmer.app.lib.security.Token;

public class TokenHandler extends Handler {
    @Override
    public void run() {
        Token.init();
    }
}
