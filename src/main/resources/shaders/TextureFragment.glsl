#version 330 core

uniform sampler2D InputTexture;


in vec2 fTexCoords;
in vec3 color;
out vec4 outColor;

void main()
{
    outColor = texture(InputTexture, fTexCoords);
    //outColor = vec4(color,1);
}