#version 330 core

uniform sampler2D tex;

in vec4 o_color;
in vec2 o_uv;

layout (location = 0) out vec4 out_Color;

void main(void) {
	out_Color = texture(tex, o_uv) * o_color;
}
