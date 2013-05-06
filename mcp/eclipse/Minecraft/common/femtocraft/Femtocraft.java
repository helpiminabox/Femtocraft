package femtocraft;

import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import femtocraft.blocks.femtoStone;
import femtocraft.blocks.microStone;
import femtocraft.blocks.nanoStone;
import femtocraft.blocks.unidentifiedAlloy;
import femtocraft.cooking.blocks.CuttingBoard;
import femtocraft.core.items.ingotFarenite;
import femtocraft.core.items.ingotPlatinum;
import femtocraft.core.items.ingotThorium;
import femtocraft.core.items.ingotTitanium;
import femtocraft.core.ore.oreFarenite;
import femtocraft.core.ore.orePlatinum;
import femtocraft.core.ore.oreThorium;
import femtocraft.core.ore.oreTitanium;
import femtocraft.farming.produce.Tomato;
import femtocraft.farming.seeds.tomatoSeed;
import femtocraft.industry.blocks.BlockMicroFurnace;
import femtocraft.power.FemtopowerCable;
import femtocraft.power.FemtopowerConsumerBlock;
import femtocraft.power.FemtopowerGenerator;
import femtocraft.proxy.ClientProxyFemtocraft;
import femtocraft.proxy.CommonProxyFemtocraft;

@Mod(modid = Femtocraft.ID, version = Femtocraft.VERSION)
@NetworkMod(channels = { Femtocraft.ID }, packetHandler = FemtocraftPacketHandler.class, clientSideRequired = true, serverSideRequired = false)
public class Femtocraft {
	public static final String ID = "Femtocraft";
	public static final String VERSION = "0.1.0";

	@Instance(ID)
	public static Femtocraft instance;

	@SidedProxy(clientSide = "femtocraft.proxy.ClientProxyFemtocraft", serverSide = "femtocraft.proxy.CommonProxyFemtocraft")
	public static CommonProxyFemtocraft proxy;

	public static CreativeTabs femtocraftTab = new FemtocraftCreativeTab("Femtocraft");
	
	public static Logger logger;

	//blocks
	public static Block oreTitanium;
	public static Block orePlatinum;
	public static Block oreThorium;
	public static Block oreFarenite;
	public static Block microStone;
	public static Block nanoStone;
	public static Block femtoStone;
	public static Block unidentifiedAlloy;
	public static FemtopowerCable FemtopowerCable;
	public static Block FemtopowerGeneratorTest;
	public static Block FemtopowerConsumerTest;
	public static Block FemtocraftMicroFurnaceUnlit;
	public static Block FemtocraftMicroFurnaceLit;
	
	//items
	public static Item ingotTitanium;
	public static Item ingotPlatinum;
	public static Item ingotThorium;
	public static Item ingotFarenite;
	
	//Produce
	public static Item tomatoSeed;
	public static Item tomato;
	
	//Cooking
	public static Block cuttingBoard;
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		logger = Logger.getLogger(ID);
		logger.setParent(FMLLog.getLogger());

		Configuration config = new Configuration(
				event.getSuggestedConfigurationFile());
		Configs.load(config);
		
		Femtocraft.proxy.registerTileEntities();
		Femtocraft.proxy.registerRendering();
		Femtocraft.proxy.registerBlockRenderers();
		
		NetworkRegistry.instance().registerGuiHandler(this, new GuiHandlerFemtocraft());
	}
	
	@Init
	public void init(FMLInitializationEvent event) {
		proxy.registerRendering();
		
		if(Configs.worldGen) {
			GameRegistry.registerWorldGenerator(new FemtocraftWorldGenerator());
		}
		
		//Change the creative tab name
		LanguageRegistry.instance().addStringLocalization("itemGroup.Femtocraft", "en_US", "Femtocraft");
		
		// item = new Item(Configs.itemId);

		//blocks
		
		oreTitanium = new oreTitanium(Configs.oreTitaniumID, 0).setUnlocalizedName("oreTitanium").setHardness(3.0f).setStepSound(Block.soundStoneFootstep).setResistance(1f);
		 MinecraftForge.setBlockHarvestLevel(oreTitanium, "pickaxe", 2);
		 GameRegistry.registerBlock(oreTitanium, "oreTitanium");
		 LanguageRegistry.addName(oreTitanium, "Titanium Ore");
		 OreDictionary.registerOre("oreTitanium", new ItemStack(oreTitanium));
		 
		 orePlatinum = new orePlatinum(Configs.orePlatinumID, 1).setUnlocalizedName("orePlatinum").setHardness(3.0f).setStepSound(Block.soundStoneFootstep).setResistance(1f);
		 MinecraftForge.setBlockHarvestLevel(orePlatinum, "pickaxe", 2);
		 GameRegistry.registerBlock(orePlatinum, "orePlatinum");
		 LanguageRegistry.addName(orePlatinum, "Platinum Ore");
		 OreDictionary.registerOre("orePlatinum", new ItemStack(orePlatinum));
		 
		 oreThorium = new oreThorium(Configs.oreThoriumID, 2).setUnlocalizedName("oreThorium").setHardness(3.0f).setStepSound(Block.soundStoneFootstep).setResistance(1f);
		 MinecraftForge.setBlockHarvestLevel(oreThorium, "pickaxe", 2);
		 GameRegistry.registerBlock(oreThorium, "oreThorium");
		 LanguageRegistry.addName(oreThorium, "Thorium Ore");
		 OreDictionary.registerOre("oreThorium", new ItemStack(oreThorium));
		 
		 oreFarenite = new oreFarenite(Configs.oreFareniteID, 3).setUnlocalizedName("oreFarenite").setHardness(3.0f).setStepSound(Block.soundStoneFootstep).setResistance(1f);
		 MinecraftForge.setBlockHarvestLevel(oreFarenite, "pickaxe", 2);
		 GameRegistry.registerBlock(oreFarenite, "oreFarenite");
		 LanguageRegistry.addName(oreFarenite, "Farenite Ore");
		 OreDictionary.registerOre("oreFarenite", new ItemStack(oreFarenite));
		 
		 nanoStone = new nanoStone(Configs.nanoStoneID, 4).setUnlocalizedName("nanoStone").setHardness(7.0f).setStepSound(Block.soundMetalFootstep).setResistance(12f);
		 GameRegistry.registerBlock(nanoStone, "nanoStone");
		 LanguageRegistry.addName(nanoStone, "Nanostone");
		 
		 microStone = new microStone(Configs.microStoneID, 5).setUnlocalizedName("microStone").setHardness(6.0f).setStepSound(Block.soundMetalFootstep).setResistance(9f);
		 GameRegistry.registerBlock(microStone, "microStone");
		 LanguageRegistry.addName(microStone,  "Microstone");
		 
		 femtoStone = new femtoStone(Configs.femtoStoneID, 6).setUnlocalizedName("femtoStone").setHardness(8.0f).setStepSound(Block.soundMetalFootstep).setResistance(15f);
		 GameRegistry.registerBlock(femtoStone, "femtoStone");
		 LanguageRegistry.addName(femtoStone, "Femtostone");
		 
		 unidentifiedAlloy = new unidentifiedAlloy(Configs.unidentifiedAlloyID, 7).setUnlocalizedName("unidentifiedAlloy").setBlockUnbreakable().setStepSound(Block.soundMetalFootstep).setResistance(20f);
		 GameRegistry.registerBlock(unidentifiedAlloy, "unidentifiedAlloy");
		 LanguageRegistry.addName(unidentifiedAlloy, "Unidentified Alloy");
		 
		 FemtopowerCable = (FemtopowerCable) new FemtopowerCable(Configs.FemtopowerCableID, Material.rock).setUnlocalizedName("FemtopowerCable").setHardness(1.0f).setStepSound(Block.soundStoneFootstep);
		 GameRegistry.registerBlock(FemtopowerCable, "FemtopowerCable");
		 LanguageRegistry.addName(FemtopowerCable, "Femtopower Cable");
		 
		 FemtopowerGeneratorTest = new FemtopowerGenerator(Configs.FemtopowerGeneratorTestID, Material.rock).setUnlocalizedName("FemtopowerGenerator").setHardness(3.5f).setStepSound(Block.soundStoneFootstep);
		 GameRegistry.registerBlock(FemtopowerGeneratorTest, "FemtopowerGenerator");
		 LanguageRegistry.addName(FemtopowerGeneratorTest, "Femtopower Generator");
		 
		 FemtopowerConsumerTest = new FemtopowerConsumerBlock(Configs.FemtopowerConsumerTestBlockID, Material.rock).setUnlocalizedName("FemtopowerConsumer").setHardness(3.5f).setStepSound(Block.soundStoneFootstep);
		 GameRegistry.registerBlock(FemtopowerConsumerTest, "FemtopowerConsumer");
		 LanguageRegistry.addName(FemtopowerConsumerTest, "Femtopower Consumer");
		 
		 FemtocraftMicroFurnaceUnlit = new BlockMicroFurnace(Configs.FemtocraftMicroFurnaceUnlitID, false).setUnlocalizedName("FemtocraftMicroFurnace").setHardness(3.5f).setStepSound(Block.soundStoneFootstep).setCreativeTab(femtocraftTab);
		 GameRegistry.registerBlock(FemtocraftMicroFurnaceUnlit, "FemtocraftMicroFurnace");
		 LanguageRegistry.addName(FemtocraftMicroFurnaceUnlit, "Micro-Furnace");
		 
		 FemtocraftMicroFurnaceLit = new BlockMicroFurnace(Configs.FemtocraftMicroFurnaceLitID, true).setLightValue(0.875F).setUnlocalizedName("FemtocraftMicroFurnace").setHardness(3.5f).setStepSound(Block.soundStoneFootstep);
		 
		 //items
		 
		 ingotTitanium = new ingotTitanium(Configs.ingotTitaniumID).setUnlocalizedName("ingotTitanium");
		 LanguageRegistry.addName(ingotTitanium, "Titanium Ingot");
		 OreDictionary.registerOre("ingotTitanium", new ItemStack(ingotTitanium));
		
		 ingotPlatinum = new ingotPlatinum(Configs.ingotPlatinumID).setUnlocalizedName("ingotPlatinum");
		 LanguageRegistry.addName(ingotPlatinum, "Platinum Ingot");
		 OreDictionary.registerOre("ingotPlatinum", new ItemStack(ingotPlatinum));
		 
		 ingotThorium = new ingotThorium(Configs.ingotThoriumID).setUnlocalizedName("ingotThorium");
		 LanguageRegistry.addName(ingotThorium, "Thorium Ingot");
		 OreDictionary.registerOre("ingotThorium", new ItemStack(ingotThorium));
		 
		 ingotFarenite = new ingotFarenite(Configs.ingotFareniteID).setUnlocalizedName("ingotFarenite");
		 LanguageRegistry.addName(ingotFarenite, "Farenite");
		 OreDictionary.registerOre("ingotFarenite", new ItemStack(ingotFarenite));
		 
		 //Produce
		 tomatoSeed = new tomatoSeed(Configs.tomatoSeedID).setUnlocalizedName("tomatoSeed");
		 LanguageRegistry.addName(tomatoSeed, "Tomato Seeds");
		 GameRegistry.registerItem(tomatoSeed, "Tomato Seeds");
		 
		 tomato = new Tomato(Configs.tomatoID).setUnlocalizedName("tomato");
		 LanguageRegistry.addName(tomato, "Tomato");
		 GameRegistry.registerItem(tomato, "Tomato");
		 
		 //Cooking
		 cuttingBoard = new CuttingBoard(Configs.cuttingBoardID).setUnlocalizedName("cuttingBoard");
		 LanguageRegistry.addName(cuttingBoard, "Cutting Board");
		 GameRegistry.registerBlock(cuttingBoard, "Cutting Board");
		 
		 
		 registerRecipes();
		 
		 ClientProxyFemtocraft.setCustomRenderers();
		 
		// GameRegistry.registerTileEntity(TileEntity.class, "myTile");

		// GameRegistry.addRecipe(new ItemStack(itemId), new Object[] {});

		// EntityRegistry.registerModEntity(entity.class, "myEntity", 0, this, 32, 10, true)
	}

	@PostInit
	public void postInit(FMLPostInitializationEvent event) {

	}
	
	private void registerRecipes() {
		GameRegistry.addSmelting(oreTitanium.blockID, new ItemStack(ingotTitanium), 0.1f);
		GameRegistry.addSmelting(orePlatinum.blockID, new ItemStack(ingotPlatinum), 0.1f);
		GameRegistry.addSmelting(oreThorium.blockID, new ItemStack(ingotThorium), 0.1f);
	}

}
