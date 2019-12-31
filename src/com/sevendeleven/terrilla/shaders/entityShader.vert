#version 330 core

uniform mat4 camera;
uniform mat4 transform;

layout (location = 0) in vec2 position;
layout (location = 1) in vec4 color;
layout (location = 2) in vec2 uv;

out vec4 o_color;
out vec2 o_uv;

void main(void) {
	gl_Position = camera*transform*vec4(position.xy, 0.5, 1.0);
	o_color = vec4(color);
	o_uv = uv;
}
