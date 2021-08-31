package TGamingStudio.Prison.Util;

import TGamingStudio.Prison.Prison;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.time.Instant;
import java.util.HashMap;

public class MinedBlocks {
    Prison Prison;
    private HashMap<Block, Long> Blocks = new HashMap<>();
    private HashMap<Block, Material> Types = new HashMap<>();

    public MinedBlocks(Prison instance) {
        Prison = instance;
        Prison.getServer().getScheduler().scheduleSyncRepeatingTask(Prison, new Runnable() {
            @Override
            public void run() {
                for (Block B : Blocks.keySet()) {
                    if (Blocks.get(B) < Instant.now().toEpochMilli()) {
                        B.setType(Types.get(B));
                        Blocks.remove(B); Types.remove(B); return;
                    }
                }
            }
        }, 0L, 5L);
    }

    public void ResetAll() {
        for (Block B : Blocks.keySet())
            B.setType(Types.get(B));
        Prison.getLogger().info("Disabling plugin, resetting " + Blocks.size() + " blocks.");
        Blocks.clear();
        Types.clear();
    }

    public void AddBlock(Block Block) {
        int Time = 10;
        if(Prison.getMineableManager().getMineable(Block.getType()) != null)
            Time = Prison.getMineableManager().getMineable(Block.getType()).getTime();
        Blocks.put(Block, Instant.now().toEpochMilli() + Time * 1000);
        Types.put(Block, Block.getType());
    }
}
