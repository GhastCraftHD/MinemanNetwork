package me.mineman.network.constant;

import com.velocitypowered.api.proxy.Player;
import me.mineman.network.rank.Rank;
import net.kyori.adventure.text.Component;

public class Message {

    public static final Component RESTARTING = Component.text("The Network is restarting", MineColor.NETWORK)
            .appendNewline()
            .append(Component.text("Try reconnecting again in 30 seconds", MineColor.ACCENT));


    public static Component rankGetResponse(Player player, Rank rank){
        return Prefix.NETWORK.append(
                Component.text(player.getUsername(), rank.getColor())
                        .append(Component.text(" has the ", MineColor.DEFAULT)
                                .append(Component.text(rank.getDisplay(), rank.getColor())
                                        .append(Component.text(" rank", MineColor.DEFAULT))))
        );
    }

    public static final Component SURVIVAL_DENY_JOIN = Component.text("You need to be a ", MineColor.ERROR)
            .append(Component.text("Member", Rank.MEMBER.getColor()))
            .append(Component.text(" to participate in the survival project", MineColor.ERROR));

    public static Component couldNotFindPlayer(String name){
        return Prefix.NETWORK.append(
                Component.text("Could not find player ", MineColor.ERROR)
                        .append(Component.text(name, MineColor.ACCENT))
        );
    }

    public static Component couldNotFindPlayerFromUsername(){
        return Prefix.NETWORK.append(
                Component.text("No player with that username could be found", MineColor.ERROR)
        );
    }

    public static Component illegalRank(){
        return Prefix.NETWORK.append(
                Component.text("This rank is invalid", MineColor.ERROR)
        );
    }

    public static Component changedRankSender(String target, Rank rank){
        return Prefix.NETWORK.append(
                Component.text("Successfully changed ", MineColor.SUCCESS)
                        .append(Component.text(target, rank.getColor())
                                .append(Component.text("'s rank to ", MineColor.SUCCESS))
                                .append(Component.text(rank.getDisplay(), rank.getColor())))

        );
    }

    public static Component changedRankTarget(String sender, Rank senderRank, Rank newRank){
        return Prefix.NETWORK.append(
                Component.text(sender, senderRank.getColor())
                        .append(Component.text(" changed your rank to ", MineColor.SUCCESS)
                                .append(Component.text(newRank.getDisplay(), newRank.getColor())))
        );
    }

    public static Component changedRankSelf(Rank rank){
        return Prefix.NETWORK.append(
                Component.text("You changed your rank to ", MineColor.SUCCESS)
                        .append(Component.text(rank.getDisplay(), rank.getColor()))
        );
    }

    public static Component banned(String reason){
        return Component.text("This account was permanently banned from the ", MineColor.ERROR)
                .append(Component.text("Mineman network", MineColor.ACCENT))
                .append(Component.newline())
                .append(Component.newline())
                .append(Component.text("Reason: ", MineColor.WARNING))
                .append(Component.text(reason, MineColor.DEFAULT));
    }

    public static Component bannedPlayer(String name, String reason){
        return Prefix.NETWORK
                .append(Component.text("Permanently banned ", MineColor.SUCCESS)
                .append(Component.text(name, MineColor.ACCENT))
                .append(Component.text(" for reason: ", MineColor.SUCCESS))
                .append(Component.text(reason, MineColor.ACCENT)));
    }

    public static Component specifyPlayer(){
        return Prefix.NETWORK.append(
                Component.text("Please specify a player", MineColor.ERROR)
        );
    }

    public static Component pardonedPlayer(String name){
        return Prefix.NETWORK.append(
                Component.text("Pardoned ", MineColor.SUCCESS)
                    .append(Component.text(name, MineColor.ACCENT))
        );
    }

    public static Component playerAlreadyBanned(String name) {
        return Prefix.NETWORK.append(
                Component.text(name, MineColor.ACCENT)
                        .append(Component.text(" is already permanently banned", MineColor.ERROR))
        );
    }

    public static Component playerIsNotBanned(String name) {
        return Prefix.NETWORK.append(
                Component.text(name, MineColor.ACCENT)
                        .append(Component.text(" is not banned", MineColor.ERROR))
        );
    }

    public static Component pingResponse(long ping){
        return Prefix.NETWORK.append(
                Component.text("Your current ping is: ", MineColor.SUCCESS)
                        .append(Component.text(ping + "ms", MineColor.ACCENT))
        );
    }

    public static Component noServerProvided(){
        return Prefix.LOBBY.append(
                Component.text("Please provide a servername", MineColor.ERROR)
        );
    }

    public static Component unableToConnect(String servername){
        return Prefix.LOBBY.append(
                Component.text("Unable to connect you to server ", MineColor.ERROR)
                        .append(Component.text(servername, MineColor.ACCENT))
        );
    }

}
