uniform float iTime;
uniform float2 iResolution;

float hash(float2 p) {
    float3 p3 = fract(float3(p.xyx) * 0.1031);
    p3 += dot(p3, p3.yzx + 33.33);
    return fract((p3.x + p3.y) * p3.z);
}

float noise(float2 p) {
    float2 i = floor(p);
    float2 f = fract(p);
    float2 u = f * f * (3.0 - 2.0 * f);
    return mix(mix(hash(i + float2(0.0, 0.0)), hash(i + float2(1.0, 0.0)), u.x),
               mix(hash(i + float2(0.0, 1.0)), hash(i + float2(1.0, 1.0)), u.x), u.y);
}

float fbm(float2 p) {
    float v = 0.0;
    float a = 0.5;
    v += a * noise(p);
    v += a * 0.5 * noise(p * 2.1);
    v += a * 0.25 * noise(p * 4.3);
    return v;
}

half4 main(float2 fragCoord) {
    float2 uv = fragCoord / iResolution.xy;

    float angle = iTime * 6.283185;
    float2 timeOffset = float2(cos(angle), sin(angle)) * 0.6;

    float2 p = uv * 2.0;

    float2 q = float2(fbm(p + timeOffset * 0.5), fbm(p + 1.0));
    float f = fbm(p + q + timeOffset);

    // --- Palette ---
    half3 background = half3(0.01, 0.03, 0.02);
    half3 emerald    = half3(0.05, 0.25, 0.15);
    half3 highlight  = half3(0.10, 0.50, 0.30);

    half3 color = mix(background, emerald, f);
    color = mix(color, highlight, length(q) * 0.4);

    float verticalMask = smoothstep(-0.5, 1.0, uv.y);
    color *= verticalMask;
    color += background * 5.5;
    color *= 1.1;

    float vignette = 1.0 - length(uv - float2(0.5, 0.7)) * 0.5;
    color *= clamp(vignette, 0.2, 1.0);

    return half4(color, 1.0);
}
