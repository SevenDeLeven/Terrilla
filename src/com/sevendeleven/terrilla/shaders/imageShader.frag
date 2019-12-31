#version 330 core

uniform sampler2D img;

in vec4 f_color;
in vec2 f_uv;

out vec4 out_Color;

void main() {
  out_Color = f_color*texture(img, f_uv);
}
