#version 330 core

layout(location = 0) in vec3 position;

out vec3 color;

uniform mat4 uProj;
uniform mat4 uView;
uniform vec3 InputColor;
uniform mat4 uTransform;

void main() {
    gl_Position = uProj * uView * uTransform * vec4(position, 1.0);
    //color = vec3(position.x, position.y - position.x, position.y);
    color = InputColor;
}