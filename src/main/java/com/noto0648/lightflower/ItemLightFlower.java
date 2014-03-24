package com.noto0648.lightflower;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

/**
 * Created by Noto on 14/03/24.
 */
public class ItemLightFlower extends ItemBlock
{
    public ItemLightFlower(Block bk)
    {
        super(bk);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        return super.getUnlocalizedName() + "" + itemStack.getItemDamage();
    }


    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int par1)
    {
        return BlockLightFlower.itemMap.get(par1).getIconIndex();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconIndex(ItemStack par1ItemStack)
    {
        if(par1ItemStack.hasTagCompound())
        {
            if(par1ItemStack.getTagCompound().hasKey("originalName") && par1ItemStack.getTagCompound().hasKey("originalMeta"))
            {
                String name = par1ItemStack.getTagCompound().getString("originalName");
                int meta = par1ItemStack.getTagCompound().getInteger("originalMeta");
                ItemStack is = new ItemStack(Block.getBlockFromName(name), 1, meta);
                return is.getIconIndex();
            }
        }
        return this.getIconFromDamage(par1ItemStack.getItemDamage());
    }


    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        Block block = par3World.getBlock(par4, par5, par6);

        if (block == Blocks.snow_layer && (par3World.getBlockMetadata(par4, par5, par6) & 7) < 1)
        {
            par7 = 1;
        }
        else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush && !block.isReplaceable(par3World, par4, par5, par6))
        {
            if (par7 == 0)
            {
                --par5;
            }

            if (par7 == 1)
            {
                ++par5;
            }

            if (par7 == 2)
            {
                --par6;
            }

            if (par7 == 3)
            {
                ++par6;
            }

            if (par7 == 4)
            {
                --par4;
            }

            if (par7 == 5)
            {
                ++par4;
            }
        }

        if (par1ItemStack.stackSize == 0)
        {
            return false;
        }
        else if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack))
        {
            return false;
        }
        else if (par5 == 255 && this.field_150939_a.getMaterial().isSolid())
        {
            return false;
        }
        else if (par3World.canPlaceEntityOnSide(this.field_150939_a, par4, par5, par6, false, par7, par2EntityPlayer, par1ItemStack))
        {
            int i1 = this.getMetadata(par1ItemStack.getItemDamage());
            int j1 = this.field_150939_a.onBlockPlaced(par3World, par4, par5, par6, par7, par8, par9, par10, i1);


            if (placeBlockAt(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10, j1))
            {
                par3World.playSoundEffect((double)((float)par4 + 0.5F), (double)((float)par5 + 0.5F), (double)((float)par6 + 0.5F), this.field_150939_a.stepSound.func_150496_b(), (this.field_150939_a.stepSound.getVolume() + 1.0F) / 2.0F, this.field_150939_a.stepSound.getPitch() * 0.8F);
                --par1ItemStack.stackSize;
                TileEntity te = par3World.getTileEntity(par4, par5, par6);
                if(te != null && te instanceof TileEntityLightFlower)
                {
                    if(par1ItemStack.hasTagCompound())
                    {
                        if(par1ItemStack.getTagCompound().hasKey("originalName") && par1ItemStack.getTagCompound().hasKey("originalMeta"))
                        {
                            String name = par1ItemStack.getTagCompound().getString("originalName");
                            int meta = par1ItemStack.getTagCompound().getInteger("originalMeta");
                            ((TileEntityLightFlower) te).setBlockName(name);
                            ((TileEntityLightFlower) te).setBlockDamage(meta);
                        }
                    }
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }
}
