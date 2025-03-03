package io.kragelv.library.config;

import io.kragelv.library.dao.DAOFactory;
import io.kragelv.library.dao.impl.DAOFactoryJDBCImpl;

public class DAO {
    
    private static final DAOFactoryJDBCImpl jdbcFactory = new DAOFactoryJDBCImpl();
    
    private DAO() {}

    public static DAOFactory getFactory() {
        return jdbcFactory;
    } 
}
