package com.noto0648.lightflower;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by Noto on 14/03/24.
 */

public class CreativeTabLightFlower extends CreativeTabs
{
    @SideOnly(Side.CLIENT)
    private int counter;

    @SideOnly(Side.CLIENT)
    private int damageIndex;

    public CreativeTabLightFlower()
    {
        super("lightFlower");
    }

    @Override
    public Item getTabIconItem()
    {
        return Items.diamond;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getIconItemStack()
    {
        counter++;
        if(counter == 100)
        {
            counter = 0;
            damageIndex++;
            damageIndex %= BlockLightFlower.itemMap.size();
        }
        return new ItemStack(LightFlowers.instance.lightFlower, 1, damageIndex);
    }
}
