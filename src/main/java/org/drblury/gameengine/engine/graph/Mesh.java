package org.drblury.gameengine.engine.graph;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

public class Mesh {
    private static final Vector3f DEFAULT_COLOUR = new Vector3f(1.0f, 1.0f, 1.0f);

    private Vector3f colour;

    private final int vaoId;

    private final List<Integer> vboIdList;

    private final int vertexCount;

    private Texture texture;

    public Mesh(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
        FloatBuffer posBuffer = null;
        FloatBuffer normBuffer = null;
        FloatBuffer textureCoordsBuffer = null;
        IntBuffer indicesBuffer = null;
        try {
            colour = Mesh.DEFAULT_COLOUR;
            vertexCount = indices.length;
            vboIdList = new ArrayList<>();

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // Position VBO
            int vboId = glGenBuffers();
            posBuffer = MemoryUtil.memAllocFloat(positions.length);
            posBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            // Texture VBO
            vboId = glGenBuffers();
            textureCoordsBuffer = MemoryUtil.memAllocFloat(textureCoords.length);
            textureCoordsBuffer.put(textureCoords).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, textureCoordsBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

            // Vertex normals VBO
            vboId = glGenBuffers();
            normBuffer = MemoryUtil.memAllocFloat(normals.length);
            normBuffer.put(normals).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, normBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(2);
            glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

            // Index VBO
            vboId = glGenBuffers();
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } finally {
            if (posBuffer != null) {
                MemoryUtil.memFree(posBuffer);
            }
            if (textureCoordsBuffer != null) {
                MemoryUtil.memFree(textureCoordsBuffer);
            }
            if (normBuffer != null) {
                MemoryUtil.memFree(normBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }
    }


    public void cleanUp() {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIdList) {
            glDeleteBuffers(vboId);
        }

        // Delete the texture
        if (texture != null) {
            texture.cleanup();
        }

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    public void render() {
        if (texture != null) {
            // Activate firs texture bank
            glActiveTexture(GL_TEXTURE0);
            // Bind the texture
            glBindTexture(GL_TEXTURE_2D, texture.getId());
        }

        // Draw the mesh
        glBindVertexArray(this.getVaoId());

        glDrawElements(GL_TRIANGLES, this.getVertexCount(), GL_UNSIGNED_INT, 0);

        // Restore state
        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public boolean isTextured() {
        return this.texture != null;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setColour(Vector3f colour) {
        this.colour = colour;
    }

    public Vector3f getColour() {
        return this.colour;
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }


}