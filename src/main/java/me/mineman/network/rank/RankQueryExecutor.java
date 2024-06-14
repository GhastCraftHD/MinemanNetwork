package me.mineman.network.rank;

import com.surrealdb.driver.model.QueryResult;
import me.mineman.network.database.Database;
import me.mineman.network.database.ResultRecords;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RankQueryExecutor {

    private final Database database;

    public RankQueryExecutor(Database database) {
        this.database = database;
    }

    public Rank getRankFromDatabase(UUID uuid){
        Map<String, String> args = new HashMap<>();
        args.put("table", Database.PLAYER);
        args.put("uuid", uuid.toString());
        List<QueryResult<ResultRecords.DatabaseRankName>> results = database.executeQuery(
                driver -> driver.query("select array::first(->has_rank->rank.name) as name from type::table($table) where uuid = type::string($uuid)", args, ResultRecords.DatabaseRankName.class)
        );
        String name = results.get(0).getResult().get(0).toString();
        return Rank.valueOf(name);
    }


    public void setRankInDatabase(UUID uuid, Rank rank){
        Map<String, String> args = new HashMap<>();
        args.put("uuid", uuid.toString());
        args.put("rank", rank.name());
        if(database.getFrequentQueryExecutor().hasDatabaseEntry(uuid)){
            database.executeQuery(
                    driver -> driver.query("delete array::first((select id from player where uuid = type::string($uuid)))->has_rank", args, ResultRecords.DeleteResult.class));
        }
        database.executeQuery(
                driver -> driver.query("relate (array::first(select id from player where uuid = type::string($uuid)))->has_rank->(array::first(select id from rank where name = type::string($rank)));", args, ResultRecords.RelationResult.class));

    }

}
