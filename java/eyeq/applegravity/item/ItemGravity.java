package eyeq.applegravity.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class ItemGravity extends Item {
    private final boolean isRepulsive;

    public ItemGravity(boolean isRepulsive) {
        this.isRepulsive = isRepulsive;
        this.setCreativeTab(CreativeTabs.TOOLS);
    }

    @Override
    public void onUsingTick(ItemStack itemStack, EntityLivingBase player, int count) {
        World world = player.getEntityWorld();
        if(world.isRemote) {
            return;
        }
        for(Entity entity : world.getEntitiesWithinAABB(Entity.class, player.getEntityBoundingBox().expand(16.0, 16.0, 16.0))) {
            double motionX = (player.posX - entity.posX) / 16.0;
            double motionY = (player.posY + player.getEyeHeight() / 2.0 - entity.posY) / 16.0;
            double motionZ = (player.posZ - entity.posZ) / 16.0;
            double accele = Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
            double d = 1.0D - accele;
            if(d > 0.0D) {
                d = d * d / accele;
                if(isRepulsive) {
                    d *= -1;
                }
                entity.motionX += motionX * d;
                entity.motionY += motionY * d;
                entity.motionZ += motionZ * d;
            }
        }
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack itemStack = player.getHeldItem(hand);
        player.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
    }
}
