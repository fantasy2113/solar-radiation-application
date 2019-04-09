package org.josmer.application.handler;

import org.josmer.application.crawler.Insert;
import org.josmer.application.enums.RadiationTypes;
import org.josmer.application.interfaces.IRadiationRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class InsertHandler implements Runnable {
    @Autowired
    private IRadiationRepository radiationRepository;

    @Override
    public void run() {
        Insert.insertData(RadiationTypes.GLOBAL);
        Insert.insertData(RadiationTypes.DIRECT);
        Insert.insertData(RadiationTypes.DIFFUSE);
    }
}
