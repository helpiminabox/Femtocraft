package femtocraft.power.tiles;

import femtocraft.managers.research.EnumTechLevel;
import net.minecraftforge.common.ForgeDirection;

public class TileEntityPowerConsumerTest extends TileEntityPowerConsumer {
	private int amountPerTick;

	public TileEntityPowerConsumerTest() {
		super();
		amountPerTick = 10;
		setTechLevel(EnumTechLevel.MICRO);
	}

	@Override
	public void femtocraftServerUpdate() {
		super.femtocraftServerUpdate();
		consume(amountPerTick);
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
