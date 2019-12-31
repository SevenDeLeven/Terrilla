#version 330 core

uniform sampler2D tex;
uniform vec2 size;
uniform bool hovered;

in vec4 f_color;
in vec2 f_uv;

layout (location = 0) out vec4 out_Color;

void main(void) {
  float tex_x = f_uv.x * size.x;
  float tex_y = f_uv.y * size.y;

  if (tex_x <= 15) {
    if (tex_y <= 15) {
      out_Color = texture(tex, vec2(tex_x / 64.0, tex_y / 64.0));
    } else if (tex_y >= 15 && tex_y <= size.y - 15) {
      out_Color = texture(tex, vec2(tex_x / 64.0, 32.0 / 64.0));
    } else if (tex_y >= size.y - 15) {
      out_Color = texture(tex, vec2(tex_x / 64.0, (tex_y-(size.y-15))/64.0+(49.0/64.0)));
    }
  } else if (tex_x > 15 && tex_x < size.x-15) {
    if (tex_y <= 15) {
      out_Color = texture(tex, vec2(32.0 / 64.0, tex_y / 64.0));
    } else if (tex_y >= 15 && tex_y <= size.y - 15) {
      out_Color = texture(tex, vec2(32.0 / 64.0, 32.0 / 64.0));
    } else if (tex_y >= size.y - 15) {
      out_Color = texture(tex, vec2(32.0 / 64.0, (tex_y-(size.y-15))/64.0+(49.0/64.0)));
    }
  } else if (tex_x >= size.x-15) {
    if (tex_y <= 15) {
      out_Color = texture(tex, vec2((tex_x-(size.x-15))/64.0+(49.0/64.0), tex_y / 64.0));
    } else if (tex_y >= 15 && tex_y <= size.y - 15) {
      out_Color = texture(tex, vec2((tex_x-(size.x-15))/64.0+(49.0/64.0), 32.0 / 64.0));
    } else if (tex_y >= size.y - 15) {
      out_Color = texture(tex, vec2((tex_x-(size.x-15))/64.0+(49.0/64.0), (tex_y-(size.y-15))/64.0+(49.0/64.0)));
    }
  } else {
    out_Color = vec4(0,0,0,1);
  }
  if (hovered) {
    out_Color *= 1.2;
  }
}
