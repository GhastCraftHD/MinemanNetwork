package me.mineman.network.rank;

public record DatabaseRank(String name, String[] permissions) {

    @Override
    public String toString(){
        return name;
    }
}
