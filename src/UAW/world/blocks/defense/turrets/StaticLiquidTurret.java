package UAW.world.blocks.defense.turrets;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.bullet.BulletType;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.world.blocks.defense.turrets.LiquidTurret;

public class StaticLiquidTurret extends LiquidTurret {

    public TextureRegion base, liquid, top;
    public Color color;

    public StaticLiquidTurret(String name) {
        super(name);
        ammoUseEffect = Fx.steam;
        hasLiquids = true;
        liquidCapacity = 150f;
        hasItems = false;
        shootCone = 360;
        rotate = false;
    }

    public void load() {
        super.load();
        base = Core.atlas.find(name);
        liquid = Core.atlas.find(name + "-liquid");
        top = Core.atlas.find(name + "-top");
    }
    public class StaticLiquidTurretBuild extends LiquidTurretBuild {

        @Override
        public void draw () {
            Color liquidColor = liquids.current().color;
            Draw.z(Layer.turret - 0.001f);
            Draw.rect(base, x, y);
            Drawf.liquid(liquid, x, y, liquids.total() / liquidCapacity, liquidColor);
            Draw.rect(top, x, y);
        }

        @Override
        protected void bullet(BulletType type, float angle){
            float lifeScl = type.scaleVelocity ? Mathf.clamp(Mathf.dst(x, y , targetPos.x, targetPos.y) / type.range(), minRange / type.range(), range / type.range()) : 1f;
            type.create(this, team, x, y, angle, 1f + Mathf.range(velocityInaccuracy), lifeScl);
        }

        @Override
        protected void effects(){
            Effect shoot = shootEffect == Fx.none ? peekAmmo().shootEffect : shootEffect;
            Effect smoke = smokeEffect == Fx.none ? peekAmmo().smokeEffect : smokeEffect;

            shoot.at(x,y);
            smoke.at(x, y);
            shootSound.at(x, y, Mathf.random(0.9f, 1.1f));

            if(shootShake > 0){
                Effect.shake(shootShake, shootShake, this);
            }
        }
    }
}




