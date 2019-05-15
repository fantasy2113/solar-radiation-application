package de.josmer.application.library.handler;

import de.josmer.application.controller.security.Token;

public class TokenHandler extends AHandler {

    @Override
    public void run() {
        Token.init();
    }
}
