package net.kitawa.more_stuff.util.block_colors;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;

public class HybernaticFoliageColor {

    // Three noise fields — structure, fine detail, and lateral warp
    // Using different seeds so they're uncorrelated
    public static final SimplexNoise GRASS_NOISE    = createNoise(11111L);
    public static final SimplexNoise CRYSTAL_NOISE  = createNoise(22222L);

    // Per-noise secondary and warp layers are derived by offsetting coords,
    // not by creating new SimplexNoise instances — keeps memory low
    private static final double DETAIL_OFFSET = 1000.0;
    private static final double WARP_OFFSET   = 2000.0;

    // ── Nebula palette ────────────────────────────────────────────────────────
    private static final int NEBULA_BLUE      = 0x4488FF;
    private static final int NEBULA_CYAN      = 0x00CCEE;
    private static final int NEBULA_PINK      = 0xFF66BB;
    private static final int NEBULA_PURPLE    = 0xAA44FF;
    private static final int NEBULA_VIOLET    = 0x7722DD;
    private static final int NEBULA_MAGENTA   = 0xDD44AA;  // bridges pink→violet in low zone
    private static final int NEBULA_INDIGO    = 0x5533CC;

    public static SimplexNoise createNoise(long seed) {
        return new SimplexNoise(RandomSource.create(seed));
    }

    // ── FBM ──────────────────────────────────────────────────────────────────

    private static double fbm3D(SimplexNoise noise, double x, double y, double z,
                                int octaves, double lacunarity, double gain) {
        double value = 0, amp = 1.0, freq = 1.0;
        for (int i = 0; i < octaves; i++) {
            value += amp * noise.getValue(x * freq, y * freq, z * freq);
            freq  *= lacunarity;
            amp   *= gain;
        }
        return value;
    }

    // ── GAUSSIAN KERNEL ───────────────────────────────────────────────────────

    private static double[] gaussianKernel(int radius) {
        double sigma = radius * 0.6 + 0.5;
        double[] kernel = new double[radius * 2 + 1];
        double sum = 0;
        for (int i = -radius; i <= radius; i++) {
            double v = Math.exp(-(i * i) / (2 * sigma * sigma));
            kernel[i + radius] = v;
            sum += v;
        }
        for (int i = 0; i < kernel.length; i++) kernel[i] /= sum;
        return kernel;
    }

    // ── OKLAB ─────────────────────────────────────────────────────────────────

    private static double[] rgbToOklab(int rgb) {
        double r = linearize((rgb >> 16) & 0xFF);
        double g = linearize((rgb >>  8) & 0xFF);
        double b = linearize( rgb        & 0xFF);
        double l = 0.4122214708*r + 0.5363325363*g + 0.0514459929*b;
        double m = 0.2119034982*r + 0.6806995451*g + 0.1073969566*b;
        double s = 0.0883024619*r + 0.2817188376*g + 0.6299787005*b;
        double lc = Math.cbrt(l), mc = Math.cbrt(m), sc = Math.cbrt(s);
        return new double[]{
                0.2104542553*lc + 0.7936177850*mc - 0.0040720468*sc,
                1.9779984951*lc - 2.4285922050*mc + 0.4505937099*sc,
                0.0259040371*lc + 0.7827717662*mc - 0.8086757660*sc
        };
    }

    private static int oklabToRgb(double L, double a, double b) {
        double lc = L + 0.3963377774*a + 0.2158037573*b;
        double mc = L - 0.1055613458*a - 0.0638541728*b;
        double sc = L - 0.0894841775*a - 1.2914855480*b;
        double lv = lc*lc*lc, mv = mc*mc*mc, sv = sc*sc*sc;
        double r  =  4.0767416621*lv - 3.3077115913*mv + 0.2309699292*sv;
        double g  = -1.2684380046*lv + 2.6097574011*mv - 0.3413193965*sv;
        double bl = -0.0041960863*lv - 0.7034186147*mv + 1.7076147010*sv;
        return (clamp(delinearize(r)) << 16) | (clamp(delinearize(g)) << 8) | clamp(delinearize(bl));
    }

    private static double linearize(int c) {
        double v = c / 255.0;
        return v <= 0.04045 ? v / 12.92 : Math.pow((v + 0.055) / 1.055, 2.4);
    }

    private static double delinearize(double v) {
        return v <= 0.0031308 ? v * 12.92 : 1.055 * Math.pow(v, 1.0/2.4) - 0.055;
    }

    private static int clamp(double v) {
        return (int) Math.max(0, Math.min(255, Math.round(v * 255.0)));
    }

    // ── NEBULA COLOR ─────────────────────────────────────────────────────────

    /**
     * Three noise layers:
     *   n      — large-scale nebula structure (which zone we're in)
     *   detail — fine variation that warps zone boundaries
     *   warp   — a third independent field that drives color selection in the low zone,
     *            replacing the flat highlight colors with genuine nebula variation
     */
    public static int foliageColor(SimplexNoise noise, double x, double y, double z) {
        double scale = 0.00625;

        double n      = (fbm3D(noise, x*scale, y*scale, z*scale, 5, 2.1, 0.55) + 1.0) / 2.0;
        double detail = (fbm3D(noise, x*scale*3.7 + DETAIL_OFFSET, y*scale*3.7, z*scale*3.7, 3, 2.0, 0.5) + 1.0) / 2.0;
        double warp   = (fbm3D(noise, x*scale*1.9 + WARP_OFFSET,   y*scale*1.9, z*scale*1.9, 4, 2.0, 0.5) + 1.0) / 2.0;

        double warped = n + (detail - 0.5) * 0.25;
        warped = Math.max(0, Math.min(1, warped));

        // High zone — vivid cyan/blue peaks
        if (warped > 0.80) {
            double t = (warped - 0.80) / 0.20;
            return lerpColor(NEBULA_PURPLE, NEBULA_CYAN, t);
        }

        // Inner nebula
        if (warped > 0.62) {
            double t = (warped - 0.62) / 0.18;
            if      (t < 0.33) return lerpColor(NEBULA_PURPLE,  NEBULA_PINK,  t / 0.33);
            else if (t < 0.66) return lerpColor(NEBULA_PINK,    NEBULA_CYAN,  (t - 0.33) / 0.33);
            else               return lerpColor(NEBULA_CYAN,    NEBULA_BLUE,  (t - 0.66) / 0.34);
        }

        // Mid zone
        if (warped > 0.38) {
            double t = (warped - 0.38) / 0.24;
            if (t < 0.5) return lerpColor(NEBULA_VIOLET,  NEBULA_PURPLE, t / 0.5);
            else         return lerpColor(NEBULA_PURPLE,   NEBULA_BLUE,   (t - 0.5) / 0.5);
        }

        // Low zone — warp noise cycles through nebula colors
        double t = warped / 0.38;
        double hueShift = (warp + t * 0.4) % 1.0;
        if      (hueShift < 0.25) return lerpColor(NEBULA_INDIGO,   NEBULA_VIOLET,   hueShift / 0.25);
        else if (hueShift < 0.50) return lerpColor(NEBULA_VIOLET,   NEBULA_MAGENTA,  (hueShift - 0.25) / 0.25);
        else if (hueShift < 0.75) return lerpColor(NEBULA_MAGENTA,  NEBULA_PURPLE,   (hueShift - 0.50) / 0.25);
        else                      return lerpColor(NEBULA_PURPLE,    NEBULA_INDIGO,   (hueShift - 0.75) / 0.25);
    }

    public static int blendedFoliageColor(SimplexNoise noise, BlockAndTintGetter world, BlockPos pos) {
        int rawR = Minecraft.getInstance().options.biomeBlendRadius().get();
        int r = Math.min(rawR, 3);
        if (r <= 0) return foliageColor(noise, pos.getX(), pos.getY(), pos.getZ());

        double[] kernel = gaussianKernel(r);
        double[] labX_L = new double[r*2+1], labX_a = new double[r*2+1], labX_b = new double[r*2+1];

        for (int dz = -r; dz <= r; dz++) {
            double sumL = 0, sumA = 0, sumB = 0;
            for (int dx = -r; dx <= r; dx++) {
                int col      = foliageColor(noise, pos.getX()+dx, pos.getY(), pos.getZ()+dz);
                double[] lab = rgbToOklab(col);
                double w     = kernel[dx + r];
                sumL += w * lab[0]; sumA += w * lab[1]; sumB += w * lab[2];
            }
            labX_L[dz+r] = sumL; labX_a[dz+r] = sumA; labX_b[dz+r] = sumB;
        }

        double finalL = 0, finalA = 0, finalB = 0;
        for (int dz = -r; dz <= r; dz++) {
            double w = kernel[dz + r];
            finalL += w * labX_L[dz+r]; finalA += w * labX_a[dz+r]; finalB += w * labX_b[dz+r];
        }

        return oklabToRgb(finalL, finalA, finalB);
    }

    private static int lerpColor(int a, int b, double t) {
        int ar = (a>>16)&0xFF, ag = (a>>8)&0xFF, ab = a&0xFF;
        int br = (b>>16)&0xFF, bg = (b>>8)&0xFF, bb = b&0xFF;
        return ((int)(ar+(br-ar)*t) << 16) | ((int)(ag+(bg-ag)*t) << 8) | (int)(ab+(bb-ab)*t);
    }
}