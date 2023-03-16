package io.redstudioragnarok.recrystallizedwing.items;

import io.redstudioragnarok.recrystallizedwing.config.RCWConfig;
import io.redstudioragnarok.recrystallizedwing.utils.RCWUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import static io.redstudioragnarok.recrystallizedwing.RCW.crystalWing;

public class BurntWing extends Item {

    public BurntWing() {
        setCreativeTab(CreativeTabs.TRANSPORTATION);

        maxStackSize = 1;

        if (RCWConfig.common.burntwingdurability > 0)
            this.setMaxDamage(RCWConfig.common.burntwingdurability - 1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer player, final EnumHand hand) {
        ItemStack itemStack = player.getHeldItem(hand);

        if (!world.isRemote) {
            // If in the nether replace by a normal crystal wing and use it, which if in the nether will replace it by a burning crystal wing
            if (player.dimension == -1)
                return crystalWing.onItemRightClick(world, player, hand);

            RCWUtils.randomTeleport(world, player);

            if (RCWConfig.common.burntwingdurability > 0)
                itemStack.damageItem(1, player);

            player.getCooldownTracker().setCooldown(this, RCWConfig.common.burntwingcooldown);

            return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
        }

        return new ActionResult<>(EnumActionResult.PASS, itemStack);
    }
}
