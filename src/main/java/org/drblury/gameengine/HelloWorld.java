package org.drblury.gameengine;
import org.lwjgl.Version;
import org.lwjgl.opengl.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class HelloWorld {
    Long windowHandle;
    Window window;
    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        window = new Window();
        window.init();
        windowHandle = window.windowHandle;
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
        System.out.println("Bye!");
    }



    private void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Set the clear color
        glClearColor(1.0f, 1.0f, 0.0f, 0.0f);

        // Run the rendering loop until the user has attempted to close the window
        while ( !glfwWindowShouldClose(windowHandle) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            glfwSwapBuffers(windowHandle); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();

            // Setup resize callback
            glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) -> {
                this.window.width = width;
                this.window.height = height;
                this.window.resize();
            });
        }
    }

    public static void main(String[] args) {
        new HelloWorld().run();
    }

}