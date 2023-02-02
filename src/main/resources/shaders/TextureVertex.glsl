#version 330 core

out vec3 color;
out vec2 fTexCoords;

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 aTexCoords;

uniform mat4 uProj;
uniform mat4 uView;
uniform vec3 InputColor;
uniform mat4 uTransform;
uniform sampler2D InputTexture;

void main()
{
    color = InputColor;
    fTexCoords = aTexCoords;
    gl_Position = uProj * uView * uTransform * vec4(position, 1.0);
}