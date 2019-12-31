#version 330 core

uniform sampler2D sprite;

in vec4 color;
in vec2 uv;

out vec4 o_color;

void main() {
	o_color = color * texture(sprite, uv);
}