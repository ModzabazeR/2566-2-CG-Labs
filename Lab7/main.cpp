#undef GLFW_DLL
#include <iostream>
#include <stdio.h>
#include <string>
#include <string.h>

#include <GL/glew.h>
#include <GLFW/glfw3.h>

#include <vector>
#include <cmath>

#include "Libs/Shader.h"
#include "Libs/Window.h"
#include "Libs/Mesh.h"

#include <glm/glm.hpp>
#include <glm/gtc/matrix_transform.hpp>
#include <glm/gtc/type_ptr.hpp> 

const GLint WIDTH = 800, HEIGHT = 600;

Window mainWindow;
std::vector<Mesh*> meshList;
std::vector<Shader> shaderList;

//Vertex Shader
static const char* vShader = "Shaders/shader.vert";

//Fragment Shader
static const char* fShader = "Shaders/shader.frag";

void CreateTriangle()
{
    GLfloat vertices[] =
    {
        // x, y, z
        -1.0f, -1.0f, 0.0f, // 0
        0.0f, -1.0f, 1.0f, // 1
        1.0f, -1.0f, 0.0f, // 2
        0.0f, 1.0f, 0.0f, // 3
    };

    // index draw
    unsigned int indices[] = 
    {
        0, 3, 1,
        1, 3, 2,
        2, 3, 0,
        0, 1, 2,
    };

    Mesh *obj1 = new Mesh();
    obj1->CreateMesh(vertices, indices, 12, 12);
    meshList.push_back(obj1);
}

void CreateShaders()
{
    Shader* shader1 = new Shader();
    shader1->CreateFromFiles(vShader, fShader);
    shaderList.push_back(*shader1);
}

int main()
{
    mainWindow = Window(WIDTH, HEIGHT, 3, 3); // 3 คือ OpenGL version 3.3
    mainWindow.initialise();

    CreateTriangle();
    CreateShaders();

    GLuint uniformModel = 0, uniformProjection = 0;

    glm::mat4 projection = glm::perspective(45.0f, (GLfloat)mainWindow.getBufferWidth() / (GLfloat)mainWindow.getBufferHeight(), 0.1f, 100.0f);
    // glm::mat4 projection = glm::ortho(-4.0f, 4.0f, -3.0f, 3.0f, 0.1f, 100.0f);

    //for secret room 3 enter - https://forms.gle/U9VE4pkYAPNvUW1H9 

    //Loop until window closed
    while (!mainWindow.getShouldClose())
    {
        //Get + Handle user input events
        glfwPollEvents();

        //Clear window
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        //draw here
        shaderList[0].UseShader();
        uniformModel = shaderList[0].GetUniformLocation("model"); // เป็นบุรุษไปรษณีย์ที่จะส่งของไปให้
        uniformProjection = shaderList[0].GetUniformLocation("projection");

        glm::mat4 model(1.0f); // 1.0f คือเป็น identity matrix 4x4

        model = glm::scale(model, glm::vec3(0.4f, 0.4f, 1.0f)); // ขยาย 0.4 เท่า
        model = glm::translate(model, glm::vec3(-0.5f, 0.0f, -2.0f)); // ย้ายไปทางซ้าย 0.5 หน่วย

        glUniformMatrix4fv(uniformModel, 1, GL_FALSE, glm::value_ptr(model)); // ส่ง model ไปให้บุรุษไปรษณีย์
        glUniformMatrix4fv(uniformProjection, 1, GL_FALSE, glm::value_ptr(projection));

        //Object
        meshList[0]->RenderMesh();

        glUseProgram(0);
        //end draw

        //magic word - SAKURA

        mainWindow.swapBuffers();
    }

    return 0;
}
