package com.noto0648.lightflower;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Created by Noto on 14/03/24.
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy
{
    @Override
    public void registers()
    {
        RenderingRegistry.registerBlockHandler(new RenderLightFlower());
    }
}
