#undef GLFW_DLL
#include <iostream>

#include <GL/glew.h>
#include <GLFW/glfw3.h>

#include <vector>

#include "Libs/Shader.h"
#include "Libs/Window.h"
#include "Libs/Mesh.h"
#include "Libs/stb_image.h"

#include <glm/glm.hpp>
#include <glm/gtc/matrix_transform.hpp>
#include <glm/gtc/type_ptr.hpp> 

const GLint WIDTH = 800, HEIGHT = 600;

Window mainWindow;
std::vector<Mesh*> meshList;
std::vector<Shader*> shaderList;

static const char* vShader = "Shaders/shader.vert";
static const char* fShader = "Shaders/shader.frag";
static const char* lightvShader = "Shaders/lightShader.vert";
static const char* lightfShader = "Shaders/lightShader.frag";
static const char* bgvShader = "Shaders/bgShader.vert";
static const char* bgfShader = "Shaders/bgShader.frag";
Shader* depthShader;

GLuint uniformModel = 0, uniformProjection = 0, uniformView = 0;
unsigned int texture;

void CreateOBJ()
{
    Mesh *obj1 = new Mesh();
    bool loaded = obj1->CreateMeshFromOBJ("Models/suzanne.obj");
    if (loaded)
    {
        meshList.push_back(obj1);
    }
    else
    {
        std::cout<<"Failed to load model"<<std::endl;
    }
}

void CreateShaders()
{
    Shader* shader1 = new Shader();
    shader1->CreateFromFiles(vShader, fShader);
    shaderList.push_back(shader1);

    Shader* shader2 = new Shader();
    shader2->CreateFromFiles(lightvShader, lightfShader);
    shaderList.push_back(shader2);

    Shader* shader3 = new Shader();
    shader3->CreateFromFiles(bgvShader, bgfShader);
    shaderList.push_back(shader3);
}

glm::vec3 lightColour = glm::vec3(1.0f, 1.0f, 1.0f);
glm::vec3 lightPos = glm::vec3(0.0f, 0.0f, -5.0f);

void RenderScene(glm::mat4 view, glm::mat4 projection)
{
    glm::vec3 pyramidPositions[] =
    {
        glm::vec3(0.0f, 0.0f, -2.5f),
        glm::vec3( 2.0f, 5.0f, -15.0f),
        glm::vec3(-1.5f, -2.2f, -2.5f),
        glm::vec3(-3.8f, -2.0f, -12.3f),
        glm::vec3( 2.4f, -0.4f, -3.5f),
        glm::vec3(-1.7f, 3.0f, -7.5f),
        glm::vec3( 1.3f, -2.0f, -2.5f),
        glm::vec3( 1.5f, 2.0f, -2.5f),
        glm::vec3( 1.5f, 0.2f, -1.5f),
        glm::vec3(-1.3f, 1.0f, -1.5f)
        };

    //Object
    for (int i = 0; i < 10; i++)
    {
        glm::mat4 model (1.0f);

        model = glm::translate(model, pyramidPositions[i]);
        model = glm::rotate(model, glm::radians(2.0f * i) ,glm::vec3(1.0f, 0.3f, 0.5f));
        model = glm::scale(model, glm::vec3(0.8f, 0.8f, 1.0f));

        glUniformMatrix4fv(uniformModel, 1, GL_FALSE, glm::value_ptr(model));
        glUniformMatrix4fv(uniformView, 1, GL_FALSE, glm::value_ptr(view));
        glUniformMatrix4fv(uniformProjection, 1, GL_FALSE, glm::value_ptr(projection));

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture);
        meshList[0]->RenderMesh();
    }
}

int main()
{
    mainWindow = Window(WIDTH, HEIGHT, 3, 3); // 3 คือ OpenGL version 3.3
    mainWindow.initialise();

    // CreateTriangle();
    CreateOBJ();
    CreateShaders();

    depthShader = new Shader();
    depthShader->CreateFromFiles("Shaders/depthShader.vert", "Shaders/depthShader.frag");

    glm::mat4 projection = glm::perspective(45.0f, (GLfloat)mainWindow.getBufferWidth() / (GLfloat)mainWindow.getBufferHeight(), 0.1f, 100.0f);
    // glm::mat4 projection = glm::ortho(-4.0f, 4.0f, -3.0f, 3.0f, 0.1f, 100.0f);

    glGenTextures(1, &texture);
    glBindTexture(GL_TEXTURE_2D, texture);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

    int width, height, nrChannels;
    unsigned char *data = stbi_load("Textures/uvmap.png", &width, &height, &nrChannels,
    0);
    if (data)
    {
        //bind image with texture
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE,
        data);
        glGenerateMipmap(GL_TEXTURE_2D);
    }
    else
    {
        std::cout<<"Failed to load texture"<<std::endl;
    }
    stbi_image_free(data);

    //shadow
    GLuint depthMapFBO;
    glGenFramebuffers(1, &depthMapFBO);

    const unsigned int SHADOW_WIDTH = 1024, SHADOW_HEIGHT = 1024;

    GLuint depthMap;
    glGenTextures(1, &depthMap);
    glBindTexture(GL_TEXTURE_2D, depthMap);
    glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, SHADOW_WIDTH, SHADOW_HEIGHT, 0,
    GL_DEPTH_COMPONENT, GL_FLOAT, NULL);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);

    glBindFramebuffer(GL_FRAMEBUFFER, depthMapFBO);
    glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthMap, 0);
    glDrawBuffer(GL_NONE);
    glReadBuffer(GL_NONE);
    glBindFramebuffer(GL_FRAMEBUFFER, 0);

    //Loop until window closed
    while (!mainWindow.getShouldClose())
    {
        //Get + Handle user input events
        glfwPollEvents();

        //Clear window
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        //draw here
        shaderList[0]->UseShader();
        uniformModel = shaderList[0]->GetUniformLocation("model"); // เป็นบุรุษไปรษณีย์ที่จะส่งของไปให้
        uniformProjection = shaderList[0]->GetUniformLocation("projection");
        uniformView = shaderList[0]->GetUniformLocation("view");

        lightPos.x = 1.0f + sin(glfwGetTime()) * 2.0f;
        lightPos.y = sin(glfwGetTime() / 2.0f) * 1.0f;
        lightPos.z = cos(glfwGetTime()) * 2.0f;

        glm::mat4 view(1.0f);
        glm::vec3 cameraPosition = glm::vec3(0.0f, 0.0f, 4.0f);
        glm::vec3 cameraTarget = glm::vec3(0.0f, 0.0f, 0.0f);
        glm::vec3 up = glm::vec3(0.0f, 1.0f, 0.0f);

        glm::vec3 cameraDirection = glm::normalize (cameraTarget - cameraPosition);

        glm::vec3 cameraRight = glm::normalize(glm::cross(cameraDirection, up));
        glm::vec3 cameraUp = glm::cross(cameraRight, cameraDirection);

        view = glm::lookAt(cameraPosition, cameraPosition + cameraDirection, cameraUp);

        // first pass: depth map
        glViewport(0, 0, 1024, 1024);
        glBindFramebuffer(GL_FRAMEBUFFER, depthMapFBO);
        glClear(GL_DEPTH_BUFFER_BIT);
        glm::mat4 lightProjection = glm::ortho(-20.0f, 20.0f, -20.0f, 20.0f, 0.1f, 20.0f);
        glm::mat4 lightView = glm::lookAt(lightPos, glm::vec3(0.0f, 0.0f, -2.5f), up);
        depthShader->UseShader();
        uniformModel = depthShader->GetUniformLocation("model");
        uniformView = depthShader->GetUniformLocation("view");
        uniformProjection = depthShader->GetUniformLocation("projection");

        glCullFace(GL_FRONT);
        RenderScene(lightView, lightProjection);
        glCullFace(GL_BACK);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);

        //second pass: scene
        glViewport(0, 0, mainWindow.getBufferWidth(), mainWindow.getBufferHeight());
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        shaderList[0]->UseShader();
        uniformModel = shaderList[0]->GetUniformLocation("model");
        uniformView = shaderList[0]->GetUniformLocation("view");
        uniformProjection = shaderList[0]->GetUniformLocation("projection");

        RenderScene(view, projection);

        // light
        glUniform3fv(shaderList[0]->GetUniformLocation("lightColour"), 1, (GLfloat *)&lightColour);
        glUniform3fv(shaderList[0]->GetUniformLocation("lightPos"), 1, (GLfloat *)&lightPos);
        glUniform3fv(shaderList[0]->GetUniformLocation("viewPos"), 1, (GLfloat *)&cameraPosition);

        // bg
        shaderList[2]->UseShader();

        Mesh *background = new Mesh();
        background->CreateMeshFromOBJ("Models/cube.obj");

        glm::mat4 bgModel(1.0f);
        bgModel = glm::translate(bgModel, glm::vec3(0.0f, 0.0f, -8.0f));
        bgModel = glm::scale(bgModel, glm::vec3(3.5f, 3.5f, 0.1f));

        glUniformMatrix4fv(shaderList[2]->GetUniformLocation("model"), 1, GL_FALSE, glm::value_ptr(bgModel));
        glUniformMatrix4fv(shaderList[2]->GetUniformLocation("projection"), 1, GL_FALSE, glm::value_ptr(projection));
        glUniformMatrix4fv(shaderList[2]->GetUniformLocation("view"), 1, GL_FALSE, glm::value_ptr(view));
        glUniform3fv(shaderList[2]->GetUniformLocation("bgColour"), 1, glm::value_ptr(glm::vec3(1.0f, 0.5f, 0.5f)));
        glUniformMatrix4fv(shaderList[2]->GetUniformLocation("lightProjection"), 1, GL_FALSE, glm::value_ptr(lightProjection));
        glUniformMatrix4fv(shaderList[2]->GetUniformLocation("lightView"), 1, GL_FALSE, glm::value_ptr(lightView));

        glBindTexture(GL_TEXTURE_2D, depthMap);
        background->RenderMesh();

        glUseProgram(0);
        //end draw

        mainWindow.swapBuffers();
    }

    return 0;
}
