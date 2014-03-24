package com.noto0648.lightflower;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;

/**
 * Created by Noto on 14/03/24.
 */
@Mod(modid = "lightFlower", name = "Light Flower2", version = "2.0.0")
public class LightFlowers
{
    @SidedProxy(clientSide = "com.noto0648.lightflower.ClientProxy", serverSide = "com.noto0648.lightflower.ServerProxy")
    public static ServerProxy proxy;

    @Mod.Instance("lightFlower")
    public static LightFlowers instance;

    public BlockLightFlower lightFlower;
    public CreativeTabs tab = new CreativeTabLightFlower();

    @SideOnly(Side.CLIENT)
    public int renderID = RenderingRegistry.getNextAvailableRenderId();

    @Mod.EventHandler
    public void load(FMLInitializationEvent event)
    {
        proxy.registers();
        lightFlower.init();
        GameRegistry.registerTileEntity(TileEntityLightFlower.class, "NotoMod.tileLightFlower");

    }

    @Mod.EventHandler
    public void preLoad(FMLPreInitializationEvent event)
    {
        lightFlower = (BlockLightFlower)new BlockLightFlower().setBlockName("NotoMod.lightFlower");
        GameRegistry.registerBlock(lightFlower, ItemLightFlower.class, "NotoMod.lightFlower");
    }
}
