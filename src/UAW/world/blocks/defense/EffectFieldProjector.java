package UAW.world.blocks.defense;

import UAW.world.blocks.defense.turrets.StaticLiquidTurret;
import arc.graphics.Color;
import arc.math.Angles;
import arc.math.Mathf;
import mindustry.content.Fx;
import mindustry.core.World;
import mindustry.entities.Effect;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Posc;
import mindustry.gen.Sounds;
import mindustry.logic.LAccess;
import mindustry.world.meta.*;

import static mindustry.Vars.tilesize;

public class EffectFieldProjector extends StaticLiquidTurret {

    public Color color;
    public int effectIntensity = 64;

    public EffectFieldProjector(String name) {
        super(name);
        group = BlockGroup.projectors;
        ammoUseEffect = Fx.steam;
        hasPower = hasLiquids = true;
        reloadTime = 45f;
        liquidCapacity = 150f;
        hasItems = false;
        emitLight = true;
        lightRadius = 50f;
        shootCone = 360;
        spread = 36;
        shots = 64;
        loopSound = Sounds.shield;
        shootSound = Sounds.plasmaboom;
        logicConfigurable = canOverdrive = rotate = extinguish = false;
    }
    @Override
    public void setStats(){
        super.setStats();
        stats.remove(Stat.inaccuracy);
        stats.remove(Stat.reload);
    }

    public class EffectFieldProjectorBuild extends StaticLiquidTurretBuild {

        @Override
        public boolean canControl(){
            return false;
        }

        @Override
        protected void effects() {
            Effect shoot = shootEffect == Fx.none ? peekAmmo().shootEffect : shootEffect;
            Effect smoke = smokeEffect == Fx.none ? peekAmmo().smokeEffect : smokeEffect;
            for (int i = 0; i < (effectIntensity * 2); i++) {
                smoke.at(
                        x + Angles.trnsx(Mathf.random(360), Mathf.random(range)),
                        y + Angles.trnsx(Mathf.random(360), Mathf.random(range))
                );
            }
            shoot.at(x,y);
            ammoUseEffect.at(x, y);
            shootSound.at(x, y, Mathf.random(0.9f, 1.1f));
        }

        @Override
        protected void bullet(BulletType type, float angle) {
            float lifeScl = type.scaleVelocity ? Mathf.clamp(Mathf.dst(x, y, targetPos.x, targetPos.y) / type.range(), minRange / type.range(), range / type.range()) : 1f;
            float rndX = Angles.trnsx(Mathf.random(360), Mathf.random(range - 4));
            float rndY = Angles.trnsy(Mathf.random(360), Mathf.random(range - 4));
            type.create(this, team, x + rndX, y + rndY, angle, 1f, lifeScl);
        }
    }
}



