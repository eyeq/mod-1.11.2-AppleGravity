package eyeq.applegravity;

import eyeq.applegravity.item.ItemGravity;
import eyeq.util.client.model.UModelCreator;
import eyeq.util.client.model.UModelLoader;
import eyeq.util.client.model.gson.ItemmodelJsonFactory;
import eyeq.util.client.renderer.ResourceLocationFactory;
import eyeq.util.client.resource.ULanguageCreator;
import eyeq.util.client.resource.lang.LanguageResourceManager;
import eyeq.util.oredict.CategoryTypes;
import eyeq.util.oredict.UOreDictionary;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.io.File;

import static eyeq.applegravity.AppleGravity.MOD_ID;

@Mod(modid = MOD_ID, version = "1.0", dependencies = "after:eyeq_util")
@Mod.EventBusSubscriber
public class AppleGravity {
    public static final String MOD_ID = "eyeq_applegravity";

    @Mod.Instance(MOD_ID)
    public static AppleGravity instance;

    private static final ResourceLocationFactory resource = new ResourceLocationFactory(MOD_ID);

    public static Item moonApple;
    public static Item moonPear;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        addRecipes();
        if(event.getSide().isServer()) {
            return;
        }
        renderItemModels();
        createFiles();
    }

    @SubscribeEvent
    protected static void registerItems(RegistryEvent.Register<Item> event) {
        moonApple = new ItemGravity(false).setUnlocalizedName("moonApple");
        moonPear = new ItemGravity(true).setUnlocalizedName("moonPear");

        GameRegistry.register(moonApple, resource.createResourceLocation("moon_apple"));
        GameRegistry.register(moonPear, resource.createResourceLocation("moon_pear"));
    }

    public static void addRecipes() {
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(moonApple),
                UOreDictionary.OREDICT_END_STONE, CategoryTypes.PREFIX_FRUIT.getDictionaryName("apple")));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(moonPear),
                UOreDictionary.OREDICT_END_STONE, CategoryTypes.PREFIX_FRUIT.getDictionaryName("pear")));
    }

	@SideOnly(Side.CLIENT)
    public static void renderItemModels() {
        UModelLoader.setCustomModelResourceLocation(moonApple);
        UModelLoader.setCustomModelResourceLocation(moonPear);
    }

    public static void createFiles() {
        File project = new File("../1.11.2-AppleGravity");

        LanguageResourceManager language = new LanguageResourceManager();

        language.register(LanguageResourceManager.EN_US, moonApple, "Moon Apple");
        language.register(LanguageResourceManager.JA_JP, moonApple, "月りんご");

        language.register(LanguageResourceManager.EN_US, moonPear, "Moon Pear");
        language.register(LanguageResourceManager.JA_JP, moonPear, "月なし");

        ULanguageCreator.createLanguage(project, MOD_ID, language);

        UModelCreator.createItemJson(project, moonApple, ItemmodelJsonFactory.ItemmodelParent.GENERATED);
        UModelCreator.createItemJson(project, moonPear, ItemmodelJsonFactory.ItemmodelParent.GENERATED);
    }
}
