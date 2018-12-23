package io.anuke.mindustry.world.blocks.production;

import io.anuke.arc.graphics.g2d.TextureRegion;
import io.anuke.mindustry.Vars;
import io.anuke.mindustry.graphics.Palette;
import io.anuke.mindustry.graphics.Shaders;
import io.anuke.mindustry.world.Tile;
import io.anuke.arc.Graphics;
import io.anuke.arc.graphics.g2d.Draw;
import io.anuke.arc.graphics.g2d.Lines;
import io.anuke.arc.math.Mathf;

public class PhaseWeaver extends PowerSmelter{
    protected TextureRegion bottomRegion;
    protected TextureRegion weaveRegion;

    public PhaseWeaver(String name){
        super(name);
    }

    @Override
    public void load(){
        super.load();

        bottomRegion = Core.atlas.find(name + "-bottom");
        weaveRegion = Core.atlas.find(name + "-weave");
    }

    @Override
    public TextureRegion[] getIcon(){
        if(icon == null){
            icon = new TextureRegion[]{Core.atlas.find(name + "-bottom"), Core.atlas.find(name)};
        }
        return icon;
    }

    @Override
    public void draw(Tile tile){
        PowerSmelterEntity entity = tile.entity();

        Draw.rect(bottomRegion, tile.drawx(), tile.drawy());

        float progress = 0.5f;

        Shaders.build.region = weaveRegion;
        Shaders.build.progress = progress;
        Shaders.build.color.set(Palette.accent);
        Shaders.build.color.a = entity.heat;
        Shaders.build.time = -entity.time / 10f;

        Graphics.shader(Shaders.build, false);
        Shaders.build.apply();
        Draw.rect(weaveRegion, tile.drawx(), tile.drawy(), entity.time);
        Graphics.shader();

        Draw.color(Palette.accent);
        Draw.alpha(entity.heat);

        Lines.lineAngleCenter(
                tile.drawx() + Mathf.sin(entity.time, 6f, Vars.tilesize / 3f * size),
                tile.drawy(),
                90,
                size * Vars.tilesize / 2f);

        Draw.reset();

        Draw.rect(region, tile.drawx(), tile.drawy());
    }
}
