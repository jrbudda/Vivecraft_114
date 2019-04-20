package org.vivecraft.utils;

import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class ExtendedModelManager extends ModelManager{
	ArrayList<String> models=new ArrayList<>();

	public ExtendedModelManager(TextureMap textures) {
		super(textures);
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		
		
		ModelBakery modelbakery = new ModelBakery(resourceManager, (TextureMap) MCReflection.ModelManager_texmap.get(this));
		putModels(modelbakery);

		
		
		Map<ModelResourceLocation, IBakedModel> registry=modelbakery.setupModelRegistry();
		
		MCReflection.ModelManager_modelRegistry.set(this,registry);

		MCReflection.ModelManager_defaultModel.set(this,registry.get(ModelBakery.MODEL_MISSING));
		
		getBlockModelShapes().reloadModels();
	}

	private void putModels(ModelBakery bakery){
		for (String entry : models){
			ResourceLocation resourcelocation = new ResourceLocation(entry);
			
			//Set<ResourceLocation> queue= (Set<ResourceLocation>)
			//	new MCReflection.ReflectionField(ModelBlock.class,"field_209609_E").get(bakery);
			
			//queue.add(resourcelocation);
			
			/*//Method mgetDef=MCReflection.getDeclaredMethod(ModelBakery.class,"getModelBlockDefinition","a","func_177586_a",ResourceLocation.class);
			MCReflection.ReflectionMethod mgetDef=new MCReflection.ReflectionMethod(ModelBakery.class,"func_177586_a",ResourceLocation.class);
			ModelBlockDefinition modelblockdefinition =(ModelBlockDefinition) mgetDef.invoke(bakery,resourcelocation);
			
			
			//Field variants=MCReflection.getDeclaredField(ModelBlockDefinition.class,"mapVariants","b","field_178332_b");
			MCReflection.ReflectionField variants=new MCReflection.ReflectionField(ModelManager.class,"field_178332_b");
			Map<String,VariantList> map=(Map<String,VariantList>) variants.get(modelblockdefinition);

			for(String var: map.keySet()) {
				//Method mRegister=MCReflection.getDeclaredMethod(ModelBakery.class,"registerVariant","a","func_177569_a", ModelBlockDefinition.class,ModelResourceLocation.class);
				MCReflection.ReflectionMethod mRegister=new MCReflection.ReflectionMethod(ModelBakery.class,"func_177569_a", ModelBlockDefinition.class,ModelResourceLocation.class);
				mRegister.invoke(bakery, modelblockdefinition, new ModelResourceLocation(resourcelocation, var));
			}*/
			
		}
	}

	public void registerModel(String baseId){
		models.add(baseId);
	}

}
