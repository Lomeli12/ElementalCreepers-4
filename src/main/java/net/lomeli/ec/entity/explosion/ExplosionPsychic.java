package net.lomeli.ec.entity.explosion;

import java.util.*;

import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ExplosionPsychic {
    /**
     * whether or not the explosion sets fire to blocks around it
     */
    public boolean isFlaming;

    /**
     * whether or not this explosion spawns smoke particles
     */
    public boolean isSmoking = true;
    public double explosionX;
    public double explosionY;
    public double explosionZ;
    public Entity exploder;
    public float explosionSize;
    public int explosionPower;
    /**
     * A list of ChunkPositions of blocks affected by this explosion
     */
    @SuppressWarnings("rawtypes")
    public List affectedBlockPositions = new ArrayList();
    private int field_77289_h = 16;
    private Random explosionRNG = new Random();
    private World worldObj;
    @SuppressWarnings("rawtypes")
    private Map field_77288_k = new HashMap();

    public ExplosionPsychic(World world, Entity entity, double xPos, double yPos, double zPos, float size, int power) {
        this.worldObj = world;
        this.exploder = entity;
        this.explosionSize = size;
        this.explosionX = xPos;
        this.explosionY = yPos;
        this.explosionZ = zPos;
        this.explosionPower = power;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void doExplosionA() {
        float f = this.explosionSize;
        int i;
        int j;
        int k;
        double d0;
        double d1;
        double d2;

        for (i = 0; i < this.field_77289_h; ++i) {
            for (j = 0; j < this.field_77289_h; ++j) {
                for (k = 0; k < this.field_77289_h; ++k) {
                    if (i == 0 || i == this.field_77289_h - 1 || j == 0 || j == this.field_77289_h - 1 || k == 0 || k == this.field_77289_h - 1) {
                        double d3 = i / (this.field_77289_h - 1.0F) * 2.0F - 1.0F;
                        double d4 = j / (this.field_77289_h - 1.0F) * 2.0F - 1.0F;
                        double d5 = k / (this.field_77289_h - 1.0F) * 2.0F - 1.0F;
                        double d6 = Math.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
                        d3 /= d6;
                        d4 /= d6;
                        d5 /= d6;
                        float f1 = this.explosionSize * (0.7F + this.worldObj.rand.nextFloat() * 0.6F);
                        d0 = this.explosionX;
                        d1 = this.explosionY;
                        d2 = this.explosionZ;

                        for (float f2 = 0.3F; f1 > 0.0F; f1 -= f2 * 0.75F) {
                            d0 += d3 * f2;
                            d1 += d4 * f2;
                            d2 += d5 * f2;
                        }
                    }
                }
            }
        }

        this.explosionSize *= 2.0F;
        i = MathHelper.floor_double(this.explosionX - this.explosionSize - 1.0D);
        j = MathHelper.floor_double(this.explosionX + this.explosionSize + 1.0D);
        k = MathHelper.floor_double(this.explosionY - this.explosionSize - 1.0D);
        int l1 = MathHelper.floor_double(this.explosionY + this.explosionSize + 1.0D);
        int i2 = MathHelper.floor_double(this.explosionZ - this.explosionSize - 1.0D);
        int j2 = MathHelper.floor_double(this.explosionZ + this.explosionSize + 1.0D);
        List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this.exploder, AxisAlignedBB.fromBounds(i, k, i2, j, l1, j2));
        Vec3 vec3 = new Vec3(this.explosionX, this.explosionY, this.explosionZ);

        for (int k2 = 0; k2 < list.size(); ++k2) {
            Entity entity = (Entity) list.get(k2);
            double d7 = entity.getDistance(this.explosionX, this.explosionY, this.explosionZ) / this.explosionSize;

            if (d7 <= 1.0D) {
                d0 = entity.posX - this.explosionX;
                d1 = entity.posY + entity.getEyeHeight() - this.explosionY;
                d2 = entity.posZ - this.explosionZ;
                double d8 = MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);

                if (d8 != 0.0D) {
                    d0 /= d8;
                    d1 /= d8;
                    d2 /= d8;
                    double d9 = this.worldObj.getBlockDensity(vec3, entity.getEntityBoundingBox());
                    double d10 = (1.0D - d7) * d9;
                    double d11 = EnchantmentProtection.func_92092_a(entity, d10);
                    entity.attackEntityFrom(DamageSource.generic, 0.0001F);
                    entity.motionX += (d0 * d11);
                    entity.motionY += (d1 * d11) * explosionPower;
                    entity.motionZ += (d2 * d11);

                    if (entity instanceof EntityPlayer)
                        this.field_77288_k.put(entity, new Vec3(d0 * d10, d1 * d10, d2 * d10));
                }
            }
        }

        this.explosionSize = f;
    }
}
