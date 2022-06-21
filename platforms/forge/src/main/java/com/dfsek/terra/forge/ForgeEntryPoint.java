/*
 * This file is part of Terra.
 *
 * Terra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terra.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.dfsek.terra.forge;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfsek.terra.forge.data.Codecs;
import com.dfsek.terra.forge.util.LifecycleUtil;

@Mod("terra")
public class ForgeEntryPoint {
    public static final String MODID = "terra";
    
    private static final Logger logger = LoggerFactory.getLogger(ForgeEntryPoint.class);
    
    private static final PlatformImpl TERRA_PLUGIN = new PlatformImpl();
    
    
    public static PlatformImpl getPlatform() {
        return TERRA_PLUGIN;
    }
    
    public static void register() { // register the things
        Registry.register(Registry.CHUNK_GENERATOR, new Identifier("terra:terra"), Codecs.FABRIC_CHUNK_GENERATOR_WRAPPER);
        Registry.register(Registry.BIOME_SOURCE, new Identifier("terra:terra"), Codecs.TERRA_BIOME_SOURCE);
    }
    
    public ForgeEntryPoint() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        modEventBus.addListener(this::commonSetup);
    }
    
    private void commonSetup(FMLCommonSetupEvent event) {
        logger.info("Initializing Terra Forge mod...");
        LifecycleUtil.initialize();
    }
}
