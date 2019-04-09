package com.orbis.coreserver.config;

import com.orbis.coreserver.base.interfaces.executor.IExecutor;
import org.apache.logging.log4j.LoggerFactory;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CoreServerExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoreServerExecutor.class.getName());
    private final Set<Class<? extends IExecutor>> executorsClasses;

    public CoreServerExecutor() {
        this.executorsClasses = new Reflections("com.orbis.coreserver.executors").getSubTypesOf(IExecutor.class);
    }

    public void startAll() {
        List<IExecutor> executors = getExecutors();
        executors.forEach(IExecutor::start);
        executors.stream().map(e -> e.getClass().getSimpleName() + " - started").forEach(LOGGER::info);
    }

    private List<IExecutor> getExecutors() {
        List<IExecutor> executors = new ArrayList<>();
        for (Class<? extends IExecutor> executorsClass : executorsClasses) {
            try {
                executors.add(executorsClass.newInstance());
            } catch (InstantiationException | IllegalAccessException ex) {
                LOGGER.info(ex.getMessage());
            }
        }
        return executors;
    }

}
