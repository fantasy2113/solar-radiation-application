package de.josmer.application.library.handler;

import de.josmer.application.library.security.Token;

public class TokenHandler extends Handler {
    @Override
    public void run() {
        Token.init();
    }
}
