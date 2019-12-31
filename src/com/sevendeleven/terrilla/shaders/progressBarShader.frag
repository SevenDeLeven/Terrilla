#version 330 core
#define PI 3.14159265358979

uniform float progress;
uniform vec4 color;

in vec4 f_color;
in vec2 f_uv;

out vec4 out_Color;

vec4 getColor() {
  vec2 diff = f_uv-vec2(0.5,0.5);
  float dist = distance(f_uv, vec2(0.5, 0.5));
  if (dist <= 1) {
    if (dist >= 0.95) {
      return vec4(1,1,1,1);
    } else if (dist != 0 && asin(diff.x/dist) <= progress*2*PI) {
      return vec4(1,1,1,1);
    } else if (dist == 0 && progress != 0) {
      return vec4(1,1,1,1);
    }
  }
  return vec4(0,0,0,0);
}

void main() {
  out_Color = f_color*color*getColor();
}
