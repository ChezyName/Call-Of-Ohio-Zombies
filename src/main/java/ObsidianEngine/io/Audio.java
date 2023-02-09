package ObsidianEngine.io;


import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.ALC11.*;
import static org.lwjgl.openal.ALC.*;

public class Audio {
    public static Audio AudioManager;
    private long AudioContext;
    private long AudioDevice;

    public Audio(){
        String defaultDeviceName = alcGetString(0,ALC_DEVICE_SPECIFIER);
        AudioDevice = alcOpenDevice(defaultDeviceName);
        AudioContext = alcCreateContext(AudioDevice,new int[] {0});
        alcMakeContextCurrent(AudioContext);

        ALCCapabilities capabilitiesA = createCapabilities(AudioDevice);
        ALCapabilities capabilitiesB = AL.createCapabilities(capabilitiesA);

        if(!AL.getCapabilities().OpenAL10) assert false : "Audio Library not supported.";
    }

    public static Audio getAudioManager(){
        if(AudioManager == null) AudioManager = new Audio();
        return AudioManager;
    }

    public void Destroy(){
        alcDestroyContext(AudioContext);
        alcCloseDevice(AudioDevice);
    }
}
