#version 330 core

uniform mat4 camera;
uniform mat4 transform;
uniform vec2 pos;
uniform vec2 size;

in vec2 v_pos;
in vec4 v_color;
in vec2 v_uv;

out vec4 f_color;
out vec2 f_uv;
float calculate(float u, float t, float z) {
  return ((z*u)+t)/2048.0;
}
void main() {
  gl_Position = camera*transform*vec4(v_pos, 0.5, 1.0);
  f_color = v_color;
  f_uv = vec2(calculate(v_uv.x, pos.x, size.x), calculate(v_uv.y, pos.y, size.y));
}
