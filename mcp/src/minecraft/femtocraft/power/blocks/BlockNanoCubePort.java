package femtocraft.power.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import femtocraft.Femtocraft;
import femtocraft.core.blocks.TileContainer;
import femtocraft.core.multiblock.MultiBlockInfo;
import femtocraft.power.multiblock.MultiBlockNanoCube;
import femtocraft.power.tiles.TileEntityNanoCubeFrame;
import femtocraft.power.tiles.TileEntityNanoCubePort;

public class BlockNanoCubePort extends TileContainer {
	public Icon portInput;
	public Icon portOutput;

	public BlockNanoCubePort(int par1) {
		super(par1, Material.iron);
		setCreativeTab(Femtocraft.femtocraftTab);
		setUnlocalizedName("BlockNanoCubePort");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess par1iBlockAccess, int par2,
			int par3, int par4, int par5) {
		TileEntity te = par1iBlockAccess.getBlockTileEntity(par2, par3, par4);
		if (te != null && te instanceof TileEntityNanoCubePort) {
			TileEntityNanoCubePort port = (TileEntityNanoCubePort) te;
			return port.output ? portOutput : portInput;
		}
		return super.getBlockTexture(par1iBlockAccess, par2, par3, par4, par5);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.minecraft.block.Block#registerIcons(net.minecraft.client.renderer
	 * .texture.IconRegister)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister) {
		this.blockIcon = portInput = par1IconRegister.registerIcon(Femtocraft.ID.toLowerCase()
				+ ":" + "BlockNanoCubePort_input");
		portOutput = par1IconRegister.registerIcon(Femtocraft.ID.toLowerCase()
				+ ":" + "BlockNanoCubePort_output");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * femtocraft.core.blocks.TileContainer#createNewTileEntity(net.minecraft
	 * .world.World)
	 */
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityNanoCubePort();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.minecraft.block.Block#onPostBlockPlaced(net.minecraft.world.World,
	 * int, int, int, int)
	 */
	@Override
	public void onPostBlockPlaced(World par1World, int par2, int par3,
			int par4, int par5) {
		MultiBlockNanoCube.instance.formMultiBlockWithBlock(par1World, par2,
				par3, par4);
		super.onPostBlockPlaced(par1World, par2, par3, par4, par5);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * femtocraft.core.blocks.TileContainer#breakBlock(net.minecraft.world.World
	 * , int, int, int, int, int)
	 */
	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4,
			int par5, int par6) {
		TileEntity te = par1World.getBlockTileEntity(par2, par3, par4);
		if (te != null && te instanceof TileEntityNanoCubePort) {
			MultiBlockInfo info = ((TileEntityNanoCubePort) te).getInfo();
			MultiBlockNanoCube.instance.breakMultiBlock(par1World, info.x(),
					info.y(), info.z());

		}
		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}
}
