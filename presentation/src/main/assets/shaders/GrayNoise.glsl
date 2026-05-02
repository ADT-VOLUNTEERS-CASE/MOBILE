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

    float time = iTime * 0.8;
    v += a * noise(p + time * 0.2);
    v += a * 0.5 * noise(p * 2.1 - time * 0.4);
    v += a * 0.25 * noise(p * 4.3 + time * 0.6);
    return v;
}

half4 main(float2 fragCoord) {
    float2 uv = fragCoord / iResolution.xy;

    float speed = iTime * 0.4;
    float2 p = uv * 3.0;

    float2 q = float2(fbm(p + float2(speed, speed * 0.5)), fbm(p + 1.0));
    float2 r = float2(fbm(p + q + float2(speed * 0.3, speed * 0.2)), fbm(p + q));
    float f = fbm(p + r);

    // --- Palette ---
    half3 background = half3(0.08, 0.08, 0.08);
    half3 midTone    = half3(0.18, 0.18, 0.18);
    half3 highlight  = half3(0.35, 0.35, 0.35);

    half3 color = mix(background, midTone, f);
    color = mix(color, highlight, length(q) * 0.5);

    color *= 1.2;

    float vignette = 1.0 - length(uv - 0.5) * 0.7;
    color *= clamp(vignette, 0.3, 1.0);

    return half4(color, 1.0);
}