package femtocraft.power.TileEntity;

import net.minecraftforge.common.ForgeDirection;

public class FemtopowerConsumerTest extends FemtopowerConsumer {
	private int amountPerTick;
	
	
	public FemtopowerConsumerTest() {
		super();
		amountPerTick = 10;
	}

	@Override
	 public void updateEntity() {
		if(!this.worldObj.isRemote)
			consume(amountPerTick);
		super.updateEntity();
	}
	
	@Override
	public float getFillPercentageForCharging(ForgeDirection from) {
		float val = getFillPercentage();
		return val < .25f ? val : .25f;
	}

	@Override
	public float getFillPercentageForOutput(ForgeDirection to) {
		float val = getFillPercentage();
		return val < .25f ? val : .25f;
	}
}