#version 330 core


uniform sampler2D atlas;

in vec4 f_color;
in vec2 f_uv;

out vec4 out_Color;

void main() {
  vec4 color = texture(atlas, f_uv);
  out_Color = vec4(color.xyz, color.x);
}
