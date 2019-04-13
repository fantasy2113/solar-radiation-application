package com.orbis.coreserver.executors;

import com.orbis.coreserver.api.security.token.Token;
import com.orbis.coreserver.base.interfaces.executor.IExecutor;
import org.apache.logging.log4j.LoggerFactory;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.LocalLocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class TokenExecutor implements IExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenExecutor.class.getName());

    @Override
    public void start() {
        ScheduledExecutorService tokenService = Executors.newScheduledThreadPool(1);
        long midnight = LocalLocalDateTime.now().until(LocalDate.now().plusDays(1).atStartOfDay(), ChronoUnit.MINUTES);
        tokenService.scheduleAtFixedRate(this, midnight, 1440, TimeUnit.MINUTES);
    }

    @Override
    public void run() {
        Token.init();
        LOGGER.info("init token");
    }
}
