package de.josmer.app.library.handler;

import de.josmer.app.library.security.Token;

public class TokenHandler extends AHandler {
    @Override
    public void run() {
        Token.init();
    }
}
