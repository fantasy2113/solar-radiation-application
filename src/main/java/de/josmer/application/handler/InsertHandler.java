package de.josmer.application.handler;

import de.josmer.application.crawler.Insert;
import de.josmer.application.enums.RadiationTypes;
import de.josmer.application.interfaces.IRadiationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class InsertHandler implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(InsertHandler.class.getName());
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
