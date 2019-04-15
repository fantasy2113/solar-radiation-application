package de.josmer.application.library.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.OptionalInt;

public class Authentication {

    private static final Logger LOGGER = LoggerFactory.getLogger(Authentication.class.getName());
    private String token;
    private int userId;

    public Authentication(final String decodedToken, final String delimiter) {
        init(decodedToken, delimiter);
    }

    private void init(final String decodedToken, final String delimiter) {
        try {
            final String[] extractedData = extractData(decodedToken, delimiter);
            this.token = extractedData[0];
            this.userId = Integer.parseInt(extractedData[1]);
        } catch (Exception ex) {
            LOGGER.info(ex.getMessage());
            setDefault();
        }
    }

    private String[] extractData(final String decodedToken, final String delimiter) {
        if (decodedToken == null || delimiter == null) {
            return new String[]{};
        }
        return decodedToken.split(delimiter);
    }

    public Optional<String> getToken() {
        return Optional.ofNullable(this.token);
    }

    public OptionalInt getUserId() {
        if (!isId(this.userId)) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(this.userId);
    }

    private boolean isId(final int id) {
        return id > 0;
    }

    private void setDefault() {
        this.token = null;
        this.userId = -1;
    }
}
