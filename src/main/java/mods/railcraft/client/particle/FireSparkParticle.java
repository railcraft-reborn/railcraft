package mods.railcraft.client.particle;

import mods.railcraft.particle.FireSparkParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;

public class FireSparkParticle extends TextureSheetParticle {

    private final Vec3 destination;
    private final double maxHorizontalDist;
    private final float lavaParticleScale;

    private FireSparkParticle(ClientLevel level, double x, double y, double z, double dx,
        double dy, double dz, FireSparkParticleOptions options, SpriteSet sprites) {
        super(level, x, y, z, dx, dy, dz);
        this.destination = options.destination();

        this.maxHorizontalDist = getHorizontalDistSq(destination);
        calculateVector(maxHorizontalDist);
        scale(0.5f);

        this.rCol = this.gCol = this.bCol = 1.0f;

        this.quadSize *= random.nextFloat() * 2.0f + 0.2f;
        this.lavaParticleScale = quadSize;
        setLifetime(2000);
        this.hasPhysics = false;
        this.pickSprite(sprites);
    }

    private double getHorizontalDistSq(Vec3 point) {
        double xDiff = this.x - point.x;
        double zDiff = this.z - point.z;
        return xDiff * xDiff + zDiff * zDiff;
    }

    private void calculateVector(double dist) {
        Vec3 vecParticle = new Vec3(this.x, this.y, this.z);

        Vec3 vel = destination.subtract(vecParticle);
        vel = vel.normalize();

        float velScale = 0.1f;
        this.xd = vel.x * velScale;
        this.yd = vel.y * velScale + 0.4 * (dist / maxHorizontalDist);
        this.zd = vel.z * velScale;
    }


    @Override
    protected int getLightColor(float partialTick) {
        int brightness = super.getLightColor(partialTick);
        int j = brightness >> 16 & 255;
        return 240 | j << 16;
    }

    @Override
    public float getQuadSize(float scaleFactor) {
        float f6 = (this.age + scaleFactor) / lifetime;
        return this.quadSize = lavaParticleScale * (1 - f6 * f6);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            float f = (float) this.age / (float) lifetime;

            if (level.random.nextFloat() > f) {
                level.addParticle(ParticleTypes.SMOKE, x, y, z, xd, yd, zd);
            }

            double dist = destination.distanceToSqr(x, y, z);
            if (dist <= 0.1) {
                this.remove();
            } else {
                calculateVector(getHorizontalDistSq(destination));
                this.move(this.xd, this.yd, this.zd);
            }
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<FireSparkParticleOptions> {

        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(FireSparkParticleOptions options, ClientLevel level,
            double x, double y, double z, double dx, double dy, double dz) {
            return new FireSparkParticle(level, x, y, z, dx, dy, dz, options, this.sprites);
        }
    }
}
