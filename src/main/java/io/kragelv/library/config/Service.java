package io.kragelv.library.config;

import io.kragelv.library.service.ServiceFactory;
import io.kragelv.library.service.impl.ServiceFactoryImpl;

public class Service {

    private static final ServiceFactoryImpl serviceFactory = new ServiceFactoryImpl();

    private Service() { }

    public static ServiceFactory getFactory() {
        return serviceFactory;
    }
}
