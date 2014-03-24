package com.noto0648.lightflower;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.RegistrySimple;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Noto on 14/03/24.
 */
public class BlockLightFlower extends BlockContainer implements IPlantable
{
    public static List<ItemStack> itemMap;

    public BlockLightFlower()
    {
        super(Material.plants);
        this.setCreativeTab(LightFlowers.instance.tab);
        this.setBlockTextureName("lightFlower:lightFlower");
        this.itemMap = new ArrayList<ItemStack>();
        this.setStepSound(Block.soundTypeGrass);
        this.setLightLevel(1.0F);
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
    {
        TileEntity te = world.getTileEntity(x, y, z);
        if(te != null && te instanceof TileEntityLightFlower)
        {
            Block block = Block.getBlockFromName(((TileEntityLightFlower) te).getBlockName());
            if(block != null && block instanceof IPlantable)
            {
                return ((IPlantable) block).getPlantType(world, x, y, z);
            }
        }
        return EnumPlantType.Plains;
    }

    @Override
    public Block getPlant(IBlockAccess world, int x, int y, int z)
    {
        return this;
    }

    @Override
    public int getPlantMetadata(IBlockAccess world, int x, int y, int z)
    {
        TileEntity te = world.getTileEntity(x, y, z);
        if(te != null && te instanceof TileEntityLightFlower)
        {
            return ((TileEntityLightFlower) te).getBlockDamage();
        }
        return world.getBlockMetadata(x, y, z);
    }

    private String getOriginalName(Block block, String[] array, Map<String, Block> map)
    {
        for(int i = 0; i < array.length; i++)
        {
            Block bl = map.get(array[i]);
            if(Block.isEqualTo(block, bl))
            {
                return array[i];
            }
        }
        return "";
    }

    public void init()
    {
        List<String> nameMap = new ArrayList<String>();
        List<String> easyMap = new ArrayList<String>();

        Iterator iterator = Block.blockRegistry.iterator();
        Map originalNameMap = null;
        try
        {
            originalNameMap = ObfuscationReflectionHelper.getPrivateValue(RegistrySimple.class, Block.blockRegistry, 1);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        if(originalNameMap == null)return;

        String[] nameArray = (String[])originalNameMap.keySet().toArray(new String[0]);

        for (Iterator i = iterator; i.hasNext();)
        {
            Object obj = i.next();
            if(obj != null && obj instanceof Block && obj instanceof IPlantable && !(obj instanceof BlockLightFlower))
            {
                IPlantable block = (IPlantable)obj;
                if(((Block)obj).getRenderType() != 1) continue;
                String originalName = getOriginalName((Block)obj, nameArray, originalNameMap);
                try
                {
                    for(int j = 0; j < 15; j++)
                    {
                        ItemStack is = new ItemStack((Block)obj, 1, j);

                        if(is != null && is.getUnlocalizedName() != null && !is.getUnlocalizedName().equalsIgnoreCase("") && (!nameMap.contains(is.getUnlocalizedName()) ||(!easyMap.contains(originalName))))
                        {
                            ItemStack newStack = new ItemStack(this, 1, itemMap.size());
                            newStack.setStackDisplayName("LightFlower (" + is.getDisplayName() + ")");
                            if(!newStack.hasTagCompound())
                            {
                                newStack.setTagCompound(new NBTTagCompound());
                            }
                            //newStack.getTagCompound().setInteger("originalId", Block.getIdFromBlock((Block) obj));
                            newStack.getTagCompound().setInteger("originalMeta", j);
                            newStack.getTagCompound().setString("originalName", originalName);
                            nameMap.add(is.getUnlocalizedName());
                            itemMap.add(newStack);
                            easyMap.add(originalName);

                            GameRegistry.addRecipe(newStack, new Object[]{"X", "C", 'X', Items.glowstone_dust, 'C', is });
                        }
                    }
                }
                catch (NullPointerException e)
                {

                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public ItemStack getItemStack(String name, int stack, int damage)
    {
        ItemStack is = new ItemStack(Block.getBlockFromName(name), stack, damage);
        for(int i = 0; i < itemMap.size(); i++)
        {
            ItemStack newStack = itemMap.get(i);
            if(newStack.hasTagCompound())
            {
                String newName = newStack.getTagCompound().getString("originalName");
                int newMeta = newStack.getTagCompound().getInteger("originalMeta");
                if(name.equalsIgnoreCase(newName) && damage == newMeta)
                {
                    return  newStack.copy();
                }
            }
        }
        return is;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block p_149749_5_, int p_149749_6_)
    {
        TileEntity te = world.getTileEntity(x, y, z);
        if(te != null && te instanceof TileEntityLightFlower)
        {
            int damage = ((TileEntityLightFlower) te).getBlockDamage();
            String name = ((TileEntityLightFlower) te).getBlockName();
            this.dropBlockAsItem(world, x, y, z, getItemStack(name, 1, damage));
        }
        super.breakBlock(world, x, y, z, p_149749_5_, p_149749_6_ );
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();


        return ret;
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List par3List)
    {
        for(int i = 0; i < itemMap.size(); i++)
        {
            par3List.add(itemMap.get(i).copy());
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return Blocks.sapling.getIcon(p_149691_1_, 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess par1World, int par2, int par3, int par4, int par5Side)
    {
        TileEntity te = par1World.getTileEntity(par2, par3, par4);
        if(te != null && te instanceof TileEntityLightFlower)
        {
            Block block = Block.getBlockFromName(((TileEntityLightFlower) te).getBlockName());
            if(block != null)
            {
                return block.getIcon(par5Side, ((TileEntityLightFlower) te).getBlockDamage());
            }
        }
        return Blocks.sapling.getIcon(par5Side, 0);
    }

    @Override
    public int getRenderType()
    {
        return LightFlowers.instance.renderID;
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2)
    {
        return new TileEntityLightFlower();
    }

    @Override
    public int damageDropped(int p_149692_1_)
    {
        return p_149692_1_;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
    {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public int getRenderColor(int p_149741_1_)
    {
        return this.getBlockColor();
    }

    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess par1World, int par2, int par3, int par4)
    {
        TileEntity te = par1World.getTileEntity(par2, par3, par4);
        if(te != null && te instanceof TileEntityLightFlower)
        {
            Block block = Block.getBlockFromName(((TileEntityLightFlower) te).getBlockName());
            if(block != null)
            {
                if(block == Blocks.tallgrass)
                {
                    return grassColor(par1World, par2, par3, par4);
                }
                return block.colorMultiplier(par1World, par2, par3, par4);
            }
        }
        return 16777215;
    }

    @SideOnly(Side.CLIENT)
    public int grassColor(IBlockAccess p_149720_1_, int p_149720_2_, int p_149720_3_, int p_149720_4_)
    {
        int l = 0;
        int i1 = 0;
        int j1 = 0;

        for (int k1 = -1; k1 <= 1; ++k1)
        {
            for (int l1 = -1; l1 <= 1; ++l1)
            {
                int i2 = p_149720_1_.getBiomeGenForCoords(p_149720_2_ + l1, p_149720_4_ + k1).getBiomeGrassColor(p_149720_2_ + l1, p_149720_3_, p_149720_4_ + k1);
                l += (i2 & 16711680) >> 16;
                i1 += (i2 & 65280) >> 8;
                j1 += i2 & 255;
            }
        }

        return (l / 9 & 255) << 16 | (i1 / 9 & 255) << 8 | j1 / 9 & 255;
    }
}
