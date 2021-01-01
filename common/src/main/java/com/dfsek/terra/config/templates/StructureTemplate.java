package com.dfsek.terra.config.templates;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.api.loot.LootTable;
import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.math.Range;
import com.dfsek.terra.api.structures.script.StructureScript;
import com.dfsek.terra.api.util.GlueList;
import com.dfsek.terra.procgen.GridSpawn;

import java.util.List;
import java.util.Map;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class StructureTemplate extends AbstractableTemplate implements ConfigTemplate {
    @Value("id")
    private String id;

    @Value("scripts")
    @Abstractable
    private ProbabilityCollection<StructureScript> structure;

    @Value("spawn.start")
    @Abstractable
    private Range y;

    @Value("spawn")
    @Abstractable
    private GridSpawn spawn;

    @Value("loot")
    @Abstractable
    private Map<String, LootTable> loot;

    @Value("features")
    @Abstractable
    @Default
    private List<Void> features = new GlueList<>();

    public Map<String, LootTable> getLoot() {
        return loot;
    }

    public String getID() {
        return id;
    }

    public ProbabilityCollection<StructureScript> getStructures() {
        return structure;
    }

    public Range getY() {
        return y;
    }

    public List<Void> getFeatures() {
        return features;
    }

    public GridSpawn getSpawn() {
        return spawn;
    }
}
