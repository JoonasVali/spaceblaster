//SpriteBatch will use texture unit 0
uniform sampler2D u_texture;

//"in" varyings from our vertex shader
varying vec4 vColor;
varying vec2 vTexCoord;
uniform vec2 mouse;
uniform float time;

void main() {
    //sample the texture
    vec4 texColor = texture2D(u_texture, vTexCoord);

    //invert the red, green and blue channels
    //texColor.rgb = 1.0 - texColor.rgb;
    texColor.r = 1 / (time % 100);

    //final color
    gl_FragColor = vColor * texColor;
}
