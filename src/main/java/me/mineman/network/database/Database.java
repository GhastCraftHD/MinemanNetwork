package me.mineman.network.database;

import com.surrealdb.connection.SurrealWebSocketConnection;
import com.surrealdb.driver.SyncSurrealDriver;
import me.mineman.network.Network;

import java.io.Serializable;

public class Database {

    public static final String RANK = "rank";
    public static final String PLAYER = "player";
    public static final String HAS_RANK = "has_rank";
    public static final String BAN = "ban";
    public static final String HAS_BANNED = "has_banned";

    private final Network main;

    private SurrealWebSocketConnection connection;
    private SyncSurrealDriver driver;

    private final CredentialManager credentialManager;
    private final DatabaseCredentials credentials;
    private final FrequentQueryExecutor frequentQueryExecutor;

    public Database(Network main){
        this.main = main;
        this.frequentQueryExecutor = new FrequentQueryExecutor(main, this);

        this.credentialManager = new CredentialManager(main);
        this.credentials = credentialManager.getDatabaseCredentials();

    }

    private void connect(){
        disconnect();

        connection = new SurrealWebSocketConnection("localhost", 3306, false);
        connection.connect(5);
        driver = new SyncSurrealDriver(connection);
        driver.signIn(credentials.user(), credentials.pass());
        driver.use(credentials.namespace(), credentials.database());
    }

    private boolean isConnected(){
        return connection != null;
    }

    private void disconnect(){
        if(isConnected()){
            connection.disconnect();
        }
    }

    public <T> T executeQuery(DriverQuery<T> operation){
        connect();
        T result = operation.execute(driver);
        disconnect();
        return result;
    }

    public FrequentQueryExecutor getFrequentQueryExecutor() {
        return frequentQueryExecutor;
    }
}
