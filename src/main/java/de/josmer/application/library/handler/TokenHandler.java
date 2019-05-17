package de.josmer.application.library.handler;

public class TokenHandler extends AHandler {

    @Override
    public void run() {
        Token.init();
    }
}
