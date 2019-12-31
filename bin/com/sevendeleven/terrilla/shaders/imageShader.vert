#version 330 core

uniform mat4 camera;
uniform mat4 transform;

layout (location = 0) in vec2 v_pos;
layout (location = 1) in vec4 v_color;
layout (location = 2) in vec2 v_uv;

out vec4 f_color;
out vec2 f_uv;

void main() {
  gl_Position = camera*transform*vec4(v_pos, 0.5, 1.0);
  f_color = v_color;
  f_uv = v_uv;
}
