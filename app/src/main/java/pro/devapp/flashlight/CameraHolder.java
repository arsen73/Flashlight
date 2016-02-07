package pro.devapp.flashlight;

import android.hardware.Camera;

import java.util.List;

/**
 * Class for camera object
 */
public class CameraHolder {
    private static Camera camera;
    private static boolean is_process = false;
    private static boolean is_run = false;

    public static Boolean isProcess(){
        return is_process;
    }

    public static Camera getCamera(){
        if(camera == null)
            camera = android.hardware.Camera.open();
        return camera;
    }

    public static void start(){
        getCamera();
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(parameters);
        camera.startPreview();
        is_run = true;
    }

    public static void stop(){
        if(camera != null && !is_process) {
            is_process = true;
            camera.stopPreview();
        }
        is_process = false;
        is_run = false;
    }

    public static void release(){
        if(camera != null && !is_process) {
            is_process = true;
            camera.stopPreview();
            camera.release();
            camera = null;
        }
        is_process = false;
    }

    public static boolean isOn(){
        return camera != null && is_run;
    }

    public static boolean hasFlash(){
        Camera.Parameters params = getCamera().getParameters();
        List<String> flashModes = params.getSupportedFlashModes();
        if(flashModes == null) {
            return false;
        }

        for(String flashMode : flashModes) {
            if(Camera.Parameters.FLASH_MODE_ON.equals(flashMode)) {
                return true;
            }
        }

        return false;
    }

}
