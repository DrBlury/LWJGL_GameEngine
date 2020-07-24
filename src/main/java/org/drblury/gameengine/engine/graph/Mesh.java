package org.drblury.gameengine.engine.graph;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.system.MemoryUtil;

public class Mesh {

    private final int vaoId;

    private final int posVboId;

    private final int textureVboId;

    private final int idxVboId;

    private final int vertexCount;

    Texture texture;

    public Mesh(float[] positions, float[] textureCoords, int[] indices, Texture texture) {
        FloatBuffer posBuffer = null;
        FloatBuffer textureCoordsBuffer = null;
        IntBuffer indicesBuffer = null;
        this.texture = texture;
        try {
            vertexCount = indices.length;

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // Position VBO
            posVboId = glGenBuffers();
            posBuffer = MemoryUtil.memAllocFloat(positions.length);
            posBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, posVboId);
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            // Colour VBO
            textureVboId = glGenBuffers();
            textureCoordsBuffer = MemoryUtil.memAllocFloat(textureCoords.length);
            textureCoordsBuffer.put(textureCoords).flip();
            glBindBuffer(GL_ARRAY_BUFFER, textureVboId);
            glBufferData(GL_ARRAY_BUFFER, textureCoordsBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

            // Index VBO
            idxVboId = glGenBuffers();
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId);
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
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void cleanUp() {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(posVboId);
        glDeleteBuffers(textureVboId);
        glDeleteBuffers(idxVboId);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    public void render() {
        // Activate first texture unit
        glActiveTexture(GL_TEXTURE0);
        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, texture.getId());

        // Draw the mesh
        glBindVertexArray(this.getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.getVertexCount(), GL_UNSIGNED_INT, 0);

        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }
}