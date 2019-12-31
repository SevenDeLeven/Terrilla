#version 330 core

uniform mat4 transform;
uniform mat4 camera;

layout(location = 0) in vec2 vposition;
layout(location = 1) in vec4 vcolor;
layout(location = 2) in vec2 vuv;

out vec4 color;
out vec2 uv;

void main() {
	gl_Position = camera * transform * vec4(vposition, 0.5, 1.0);
	color = vcolor;
	uv = vuv;
}