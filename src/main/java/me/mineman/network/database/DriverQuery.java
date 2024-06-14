package me.mineman.network.database;

import com.surrealdb.driver.SyncSurrealDriver;

@FunctionalInterface
public interface DriverQuery<T>{
     T execute(SyncSurrealDriver driver);
}
