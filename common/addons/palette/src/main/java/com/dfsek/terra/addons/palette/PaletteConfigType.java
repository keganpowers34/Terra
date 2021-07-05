package com.dfsek.terra.addons.palette;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.addons.palette.palette.PaletteImpl;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.registry.OpenRegistry;
import com.dfsek.terra.api.world.generator.Palette;

import java.lang.reflect.Type;
import java.util.function.Supplier;

public class PaletteConfigType implements ConfigType<PaletteTemplate, Palette> {
    private final PaletteFactory factory = new PaletteFactory();
    private final ConfigPack pack;
    private final TerraPlugin main;

    public PaletteConfigType(ConfigPack pack, TerraPlugin main) {
        this.pack = pack;
        this.main = main;
    }

    @Override
    public PaletteTemplate getTemplate(ConfigPack pack, TerraPlugin main) {
        return new PaletteTemplate();
    }

    @Override
    public ConfigFactory<PaletteTemplate, Palette> getFactory() {
        return factory;
    }

    @Override
    public Class<Palette> getTypeClass() {
        return Palette.class;
    }

    @Override
    public Supplier<OpenRegistry<Palette>> registrySupplier() {
        return () -> pack.getRegistryFactory().create(registry -> (TypeLoader<Palette>) (t, c, loader) -> {
            if(((String) c).startsWith("BLOCK:"))
                return new PaletteImpl.Singleton(main.getWorldHandle().createBlockData(((String) c).substring(6))); // Return single palette for BLOCK: shortcut.
            return registry.load(t, c, loader);
        });
    }
}
