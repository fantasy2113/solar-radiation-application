package org.josmer.application.interfaces;

import java.util.List;

public interface ILogRepository {

    List<String> infoLogs();

    List<String> errorLogs();
}
