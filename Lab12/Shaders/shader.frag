#version 330

out vec4 colour;
in vec4 vCol;
in vec2 TexCoord;
in vec3 FragPos;
in vec3 Normal;

uniform vec3 lightColour;
uniform sampler2D texture2D;
uniform vec3 lightPos;
uniform vec3 viewPos;

vec3 ambientLight()
{
    float ambientStrength = 0.3f;
    vec3 ambient = ambientStrength * lightColour;
    return ambient;
}

vec3 diffuseLight()
{

    float diffuseStrength = 0.8f;
    vec3 lightDir = normalize(lightPos - FragPos); // light direction
    vec3 norm = normalize(Normal); // normal

    float diff = max(dot(lightDir, norm), 0.0f); // diffuse intensity. use max to prevent negative values
    vec3 diffuse = diffuseStrength * diff * lightColour; // final diffuse colour
    return diffuse;
}

vec3 specularLight()
{
    float specularStrength = 0.8f;
    float shininess = 64.0f;

    vec3 lightDir = normalize(lightPos - FragPos);
    vec3 norm = normalize(Normal);
    vec3 reflectDir = reflect(-lightDir, norm);
    vec3 viewDir = normalize(viewPos - FragPos);

    float spec = pow(max(dot(viewDir, reflectDir), 0.0f), shininess);

    vec3 specular = lightColour * spec * specularStrength;

    return specular;
}

void main()
{
    // colour = texture(texture2D, TexCoord) * vec4(ambientLight() + diffuseLight(), 1.0f);

    // Phong Shading
    colour = texture(texture2D, TexCoord) * vec4(ambientLight() + diffuseLight() + specularLight(), 1.0f);
}