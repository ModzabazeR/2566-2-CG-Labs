#version 330

layout (location = 0) in vec3 pos;
layout (location = 1) in vec2 aTexCoord;
layout (location = 2) in vec3 aNormal;

// create a uniform (global) variable for the model matrix
uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

out vec4 vCol;
out vec2 TexCoord;
out vec3 Normal;

out vec3 FragPos;

void main()
{
    gl_Position = projection * view * model * vec4(pos, 1.0);
    FragPos = vec3(model * vec4(pos, 1.0)); // get the position of the vertex in world space
    vCol = vec4(clamp(pos, 0.0f, 1.0f), 1.0f);
    TexCoord = aTexCoord;
    Normal = aNormal;
}