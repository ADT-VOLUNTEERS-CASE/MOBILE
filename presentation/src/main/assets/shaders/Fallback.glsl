uniform shader content;
half4 main(float2 p) {
    return content.eval(p);
}