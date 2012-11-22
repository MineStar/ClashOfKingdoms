package de.minestar.clashofkingdoms.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import de.minestar.clashofkingdoms.COKCore;
import de.minestar.clashofkingdoms.data.COKGame;
import de.minestar.clashofkingdoms.data.TeamData;
import de.minestar.clashofkingdoms.enums.EnumTeam;
import de.minestar.clashofkingdoms.manager.GameManager;
import de.minestar.clashofkingdoms.utils.BlockVector;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class AdminListener implements Listener {

    private final GameManager gameManager;
    private BlockVector vector = new BlockVector("", 0, 0, 0);

    public AdminListener() {
        this.gameManager = COKCore.gameManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        // left & rightclick a block
        if (!event.getAction().equals(Action.LEFT_CLICK_BLOCK) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        Player player = event.getPlayer();

        // is the player in a game?
        if (!this.gameManager.isPlayerInAnyGame(player.getName())) {
            return;
        }

        COKGame game = this.gameManager.getGameByPlayer(player.getName());

        if (!game.getPlayer(player.getName()).isInTeam(EnumTeam.REF)) {
            return;
        }

        Block block = event.getClickedBlock();
        ItemStack stackInHand = player.getItemInHand();
        if (stackInHand == null) {
            return;
        }

        this.vector.update(block.getLocation());

        // HANDLE WOOLS
        if (stackInHand.getType().equals(Material.WOOL)) {
            // game must be running or be paused
            if (game.isRunning()) {
                PlayerUtils.sendError(player, COKCore.NAME, "Game is currently running!");
                event.setUseInteractedBlock(Result.DENY);
                event.setUseItemInHand(Result.DENY);
                event.setCancelled(true);
                return;
            }

            if (stackInHand.getDurability() == EnumTeam.RED.getSubID()) {
                // RED WOOL
                TeamData teamData = game.getTeamData(EnumTeam.RED);
                if (teamData.isRealBaseBlock(vector)) {
                    teamData.unregisterBaseBlock(vector);
                    PlayerUtils.sendInfo(player, COKCore.NAME, "Baseblock unregistered!");
                } else {
                    teamData.registerBaseBlock(vector, game.getSettings().getBaseHeight());
                    PlayerUtils.sendInfo(player, COKCore.NAME, "Baseblock registered!");
                }
                event.setUseInteractedBlock(Result.DENY);
                event.setUseItemInHand(Result.DENY);
                event.setCancelled(true);
                return;
            } else if (stackInHand.getDurability() == EnumTeam.BLU.getSubID()) {
                // RED WOOL
                TeamData teamData = game.getTeamData(EnumTeam.BLU);
                if (teamData.isRealBaseBlock(vector)) {
                    teamData.unregisterBaseBlock(vector);
                    PlayerUtils.sendInfo(player, COKCore.NAME, "Baseblock unregistered!");
                } else {
                    teamData.registerBaseBlock(vector, game.getSettings().getBaseHeight());
                    PlayerUtils.sendInfo(player, COKCore.NAME, "Baseblock registered!");
                }
                event.setUseInteractedBlock(Result.DENY);
                event.setUseItemInHand(Result.DENY);
                event.setCancelled(true);
                return;
            }
        }
    }
}
