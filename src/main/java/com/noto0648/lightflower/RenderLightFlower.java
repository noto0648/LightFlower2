package com.noto0648.lightflower;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

/**
 * Created by Noto on 14/03/24.
 */
@SideOnly(Side.CLIENT)
public class RenderLightFlower implements ISimpleBlockRenderingHandler
{
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
    {
        if (getRenderId() == modelId)
        {
            RenderHelper.disableStandardItemLighting();
            //renderBlockTriangleInInv(block);
            RenderHelper.enableStandardItemLighting();
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        if (getRenderId() == modelId)
        {
            TileEntity te = world.getTileEntity(x, y, z);
            if(te != null && te instanceof TileEntityLightFlower)
            {
                //ItemStack is = new ItemStack(Block.getBlockFromName(((TileEntityLightFlower) te).getBlockName()), 1, ((TileEntityLightFlower) te).getBlockDamage());
                //renderer.setOverrideBlockTexture(is.getIconIndex());
                this.renderCrossedSquares(block, x, y, z, renderer);
            }
            return true;
        }
        return false;
    }

    public boolean renderCrossedSquares(Block p_147746_1_, int p_147746_2_, int p_147746_3_, int p_147746_4_, RenderBlocks renderer)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(p_147746_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147746_2_, p_147746_3_, p_147746_4_));
        int l = p_147746_1_.colorMultiplier(renderer.blockAccess, p_147746_2_, p_147746_3_, p_147746_4_);
        float f = (float)(l >> 16 & 255) / 255.0F;
        float f1 = (float)(l >> 8 & 255) / 255.0F;
        float f2 = (float)(l & 255) / 255.0F;
        if (EntityRenderer.anaglyphEnable)
        {
            float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
            float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
            float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
            f = f3;
            f1 = f4;
            f2 = f5;
        }

        tessellator.setColorOpaque_F(f, f1, f2);
        double d1 = (double)p_147746_2_;
        double d2 = (double)p_147746_3_;
        double d0 = (double)p_147746_4_;
        long i1;

        if (p_147746_1_ == Blocks.tallgrass)
        {
            i1 = (long)(p_147746_2_ * 3129871) ^ (long)p_147746_4_ * 116129781L ^ (long)p_147746_3_;
            i1 = i1 * i1 * 42317861L + i1 * 11L;
            d1 += ((double)((float)(i1 >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D;
            d2 += ((double)((float)(i1 >> 20 & 15L) / 15.0F) - 1.0D) * 0.2D;
            d0 += ((double)((float)(i1 >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D;
        }
        else if (p_147746_1_ == Blocks.red_flower || p_147746_1_ == Blocks.yellow_flower)
        {
            i1 = (long)(p_147746_2_ * 3129871) ^ (long)p_147746_4_ * 116129781L ^ (long)p_147746_3_;
            i1 = i1 * i1 * 42317861L + i1 * 11L;
            d1 += ((double)((float)(i1 >> 16 & 15L) / 15.0F) - 0.5D) * 0.3D;
            d0 += ((double)((float)(i1 >> 24 & 15L) / 15.0F) - 0.5D) * 0.3D;
        }
        IIcon iicon = p_147746_1_.getIcon(renderer.blockAccess, p_147746_2_, p_147746_3_, p_147746_4_, 0);

        //IIcon iicon = renderer.getBlockIconFromSideAndMetadata(p_147746_1_, 0, renderer.blockAccess.getBlockMetadata(p_147746_2_, p_147746_3_, p_147746_4_));
        renderer.drawCrossedSquares(iicon, d1, d2, d0, 1.0F);
        return true;
    }


    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return false;
    }

    @Override
    public int getRenderId()
    {
        return LightFlowers.instance.renderID;
    }


}
