package org.josmer.application.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.josmer.application.crawler.Insert;
import org.josmer.application.enums.RadiationTypes;
import org.josmer.application.interfaces.IRadiationRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class InsertHandler implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger(InsertHandler.class.getName());
    @Autowired
    private IRadiationRepository radiationRepository;

    @Override
    public void run() {
        LOGGER.info("global inserting...");
        Insert.insertData(RadiationTypes.GLOBAL);
        LOGGER.info("direct inserting...");
        Insert.insertData(RadiationTypes.DIRECT);
        LOGGER.info("diffuse inserting...");
        Insert.insertData(RadiationTypes.DIFFUSE);
    }
}
