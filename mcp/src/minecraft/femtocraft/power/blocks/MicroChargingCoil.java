package femtocraft.power.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import femtocraft.Femtocraft;
import femtocraft.api.IChargingBase;
import femtocraft.api.IChargingCoil;
import femtocraft.managers.research.TechLevel;
import femtocraft.proxy.ClientProxyFemtocraft;

public class MicroChargingCoil extends Block implements IChargingCoil {
	public Icon coilConnector;
	public Icon coilConnectorTop;

	public MicroChargingCoil(int par1) {
		super(par1, Material.iron);
		setCreativeTab(Femtocraft.femtocraftTab);
		setUnlocalizedName("MicroChargingCoil");
		setHardness(1.0f);
		setStepSound(Block.soundMetalFootstep);
		setBlockBounds(4.f/16.f, 0, 4.f/16.f, 12.f/16.f, 1, 12.f/16.f);
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public int getRenderType() {
		return ClientProxyFemtocraft.FemtocraftChargingCoilRenderID;
	}
	

	@Override
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
		return canBlockStay(par1World, par2, par3, par4);
	}

	@Override
	public boolean canBlockStay(World par1World, int par2, int par3, int par4) {
		Block block = Block.blocksList[par1World.getBlockId(par2, par3 - 1, par4)];
		if(block == null) return false;
		if(!((block instanceof IChargingCoil) || (block instanceof IChargingBase))) return false;
		
		if(!par1World.isAirBlock(par2 -1, par3, par4)) return false;
		if(!par1World.isAirBlock(par2 +1, par3, par4)) return false;
		if(!par1World.isAirBlock(par2, par3, par4 - 1)) return false;
		if(!par1World.isAirBlock(par2, par3, par4 + 1)) return false;

		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister) {
		coilConnector  = par1IconRegister.registerIcon(Femtocraft.ID.toLowerCase() + ":" + "MicroChargingCoil_connector");
		coilConnectorTop  = par1IconRegister.registerIcon(Femtocraft.ID.toLowerCase() + ":" + "MicroChargingCoil_coilConnectorTop");
	}

	@Override
	public boolean isBlockSolid(IBlockAccess par1iBlockAccess, int par2,
			int par3, int par4, int par5) {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public float powerPerTick(World world, int x, int y, int z) {
		return .1f;
	}

	@Override
	public TechLevel techLevel(World world, int x, int y, int z) {
		return TechLevel.MICRO;
	}

	@Override
	public void onNeighborBlockChange(World par1World, int par2, int par3,
			int par4, int par5) {
		if (!canBlockStay(par1World, par2, par3, par4))
        {
            dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
            par1World.setBlockToAir(par2, par3, par4);
        }
	}

}
