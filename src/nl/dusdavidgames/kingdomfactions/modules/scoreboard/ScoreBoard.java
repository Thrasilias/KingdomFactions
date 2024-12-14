package nl.dusdavidgames.kingdomfactions.modules.scoreboard;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import lombok.Getter;
import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;

public class ScoreBoard {
    private static final String BLANK_LINE = " "; // Constant blank line
    private Scoreboard board;
    private String name;
    private @Getter HashMap<Integer, String> lineId = new HashMap<>();
    private KingdomFactionsPlayer owner; 

    public ScoreBoard(String name, KingdomFactionsPlayer owner) {
        this.owner = owner;
        this.board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = board.registerNewObjective("Scoreboard", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + name);
        this.name = name;
    }

    public void addLine(String line, int i) {
        Score score = board.getObjective(DisplaySlot.SIDEBAR).getScore(line);
        score.setScore(i);
        lineId.put(i, line);
    }

    public void addBlankLine(int i) {
        Score score = board.getObjective(DisplaySlot.SIDEBAR).getScore(BLANK_LINE);
        score.setScore(i);
        lineId.put(i, BLANK_LINE);
    }

    public void editLine(int line, String newInput) {
        board.resetScores(lineId.get(line));
        Score score = board.getObjective(DisplaySlot.SIDEBAR).getScore(newInput);
        score.setScore(line);
        lineId.put(line, newInput);
    }

    public void setName(String newName) {
        board.getObjective(DisplaySlot.SIDEBAR).setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + newName);
    }

    public Scoreboard getBoard() {
        return board;
    }

    public String getName() {
        return name;
    }

    public void refreshTags() {
        for (KingdomFactionsPlayer p : PlayerModule.getInstance().getPlayers()) {
            refreshPlayer(p);
            p.getScoreboard().refreshPlayer(this.owner);
        }
    }

    @SuppressWarnings("deprecation")
    public void refreshPlayer(KingdomFactionsPlayer p) {
        String teamName = p.getKingdom().getType().toString() + getExtraTeamSuffix(p);
        Team team = board.getTeam(teamName);
        if (team == null) {
            team = board.registerNewTeam(teamName);
        }
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);

        if (p.isStaff()) {
            team.setPrefix(p.getKingdom().getType().getColor() + "" + ChatColor.BOLD);
        } else {
            team.setPrefix(p.getKingdom().getType().getColor() + "");
        }
        team.addPlayer(p.getPlayer());
    }

    private String getExtraTeamSuffix(KingdomFactionsPlayer player) {
        return player.isStaff() ? "S" : "";
    }
}
