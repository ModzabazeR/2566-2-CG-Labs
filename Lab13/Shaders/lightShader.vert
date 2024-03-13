#version 330

layout (location = 0) in vec3 pos;

// create a uniform (global) variable for the model matrix
uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main()
{
    gl_Position = projection * view * model * vec4(pos, 1.0);
}