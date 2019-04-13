package de.josmer.app.lib.handler;

import de.josmer.app.lib.interfaces.IHandler;

import java.time.LocalDate;
import java.time.LocalLocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class Handler implements IHandler {
    @Override
    public void start() {
        ScheduledExecutorService tokenService = Executors.newScheduledThreadPool(1);
        long midnight = LocalLocalDateTime.now().until(LocalDate.now().plusDays(1).atStartOfDay(), ChronoUnit.MINUTES);
        tokenService.scheduleAtFixedRate(this, midnight, 1440, TimeUnit.MINUTES);
    }
}
