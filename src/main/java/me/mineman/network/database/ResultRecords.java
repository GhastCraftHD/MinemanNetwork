package me.mineman.network.database;

public class ResultRecords {

    public record RelationResult(String id, String in, String out){}

    public record DatabaseRankName(String name){
        @Override
        public String toString() {
            return name;
        }
    }

    public record BanResult(String id, String in, String out){}

    public record DatabaseBan(String reason, String uuid){}

    public record DeleteResult(String[] strings){}

}
