#type vertex
#version 330 core
layout (location=0) in vec2 aPos;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aTexCoords;
layout (location=3) in float aTexId;

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;
out vec2 fTexCoords;
out float fTexId;

void main()
{
    fColor = aColor;
    fTexCoords = aTexCoords;
    fTexId = aTexId;
    gl_Position = uProjection * uView * vec4(aPos, 0, 1.0);
}

#type fragment
#version 330 core

in vec4 fColor;
in vec2 fTexCoords;
in float fTexId;

uniform sampler2D uTextures[8];

out vec4 color;

void main()
{
    // float avg = (fColor.r + fColor.g + fColor.b) / 3;
    // float noise = fract(sin(dot(fColor.xy, vec2(12.9898, 78.233))) * 43758.5453);

    // Sample what color is at fTexCoords position at the TEX_SAMPLER image and place in "color"
    // color = texture(TEX_SAMPLER, fTexCoords);
    if (fTexId > 0) {
        int id = int(fTexId);
        color = fColor * texture(uTextures[id], fTexCoords);
        // color = vec4(fTexCoords, 0, 1);
    } else {
        color = fColor;
    }
}
