package me.mineman.network.database;

import com.surrealdb.driver.model.QueryResult;
import me.mineman.network.Network;
import me.mineman.network.player.DatabasePlayer;
import me.mineman.network.player.DatabasePlayerWithId;
import me.mineman.network.rank.Rank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FrequentQueryExecutor {

    private final Network main;
    private final Database database;

    public FrequentQueryExecutor(Network main, Database database) {
        this.main = main;
        this.database = database;
    }

    public DatabasePlayerWithId getPlayerEntry(UUID uuid){
        Map<String, String> args = Map.of(
                "table", Database.PLAYER,
                "uuid", uuid.toString()
        );
        List<QueryResult<DatabasePlayerWithId>> results = database.executeQuery(
                driver -> driver.query("select * from type::table($table) where uuid = type::string($uuid);", args, DatabasePlayerWithId.class)
        );
        return results.get(0).getResult().get(0);
    }

    public boolean hasDatabaseEntry(UUID uuid){
        Map<String, String> args = new HashMap<>();
        args.put("table", Database.PLAYER);
        args.put("uuid", uuid.toString());
        List<QueryResult<DatabasePlayer>> results = database.executeQuery(
                driver -> driver.query("select * from type::table($table) where uuid = type::string($uuid);", args, DatabasePlayer.class));
        return !results.get(0).getResult().isEmpty();
    }

    public void createDatabaseEntry(UUID uuid){
        database.executeQuery(driver -> driver.create("player", new DatabasePlayer(uuid, "", new String[]{})));
        main.getRankSystem().setRank(uuid, Rank.GUEST);
    }

    @Deprecated
    public String getNicknameFromDatabase(UUID uuid){
        Map<String, String> args = new HashMap<>();
        args.put("table", Database.PLAYER);
        args.put("uuid", uuid.toString());
        List<QueryResult<DatabasePlayer>> results = database.executeQuery(
                driver -> driver.query("select * from type::table($table) where uuid = type::string($uuid)", args, DatabasePlayer.class)
        );
        if(results.get(0).getResult().isEmpty()) return "";

        return results.get(0).getResult().get(0).nickname();
    }

    @Deprecated
    public void setNicknameInDatabase(UUID uuid, String nickname){
        Map<String, String> args = new HashMap<>();
        args.put("table", Database.PLAYER);
        args.put("uuid", uuid.toString());

        List<QueryResult<DatabasePlayerWithId>> results = database.executeQuery(
                driver -> driver.query("select * from type::table($table) where uuid = type::string($uuid)", args, DatabasePlayerWithId.class)
        );

        if(results.get(0).getResult().isEmpty()) return;

        database.executeQuery(
                driver -> driver.update(results.get(0).getResult().get(0).id(), Map.of("nickname", nickname))
        );
    }


    public void banPlayer(UUID uuid, UUID bannedUUID, String reason){
        Map<String, String> args = new HashMap<>();
        args.put("uuid", uuid.toString());
        args.put("bannedUUID", bannedUUID.toString());

        if(isBanned(uuid)) return;

        database.executeQuery(
                driver -> driver.create("ban", new ResultRecords.DatabaseBan(reason, bannedUUID.toString()))
        );

        database.executeQuery(
                driver -> driver.query("relate (array::first(select id from player where uuid = type::string($uuid)))->has_banned->(array::first(select id from ban where uuid = type::string($bannedUUID)));", args, ResultRecords.BanResult.class)
        );

    }

    public boolean isBanned(UUID uuid){
        Map<String, String> args = new HashMap<>();
        args.put("uuid", uuid.toString());

        List<QueryResult<ResultRecords.DatabaseBan>> results = database.executeQuery(
                driver -> driver.query("select * from ban where uuid = type::string($uuid)", args, ResultRecords.DatabaseBan.class)
        );

        return !results.get(0).getResult().isEmpty();
    }

    public String getBanReason(UUID uuid){
        Map<String, String> args = new HashMap<>();
        args.put("uuid", uuid.toString());

        List<QueryResult<ResultRecords.DatabaseBan>> results = database.executeQuery(
                driver -> driver.query("select * from ban where uuid = type::string($uuid)", args, ResultRecords.DatabaseBan.class)
        );

        return results.get(0).getResult().get(0).reason();
    }

    public void pardonPlayer(UUID bannedUUID){
        Map<String, String> args = new HashMap<>();
        args.put("bannedUUID", bannedUUID.toString());
        database.executeQuery(
                driver -> driver.query("delete ban where uuid = type::string($bannedUUID);", args, ResultRecords.DeleteResult.class)
        );
    }

}
