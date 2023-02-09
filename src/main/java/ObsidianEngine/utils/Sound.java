package ObsidianEngine.utils;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.libc.LibCStdlib.free;

public class Sound {
    private int bufferId;
    private int sourceId;
    private String filePath;
    private boolean isPlaying = false;

    public Sound(String filePath, boolean loops){
        this.filePath = filePath;

        stackPush();
        IntBuffer channelsB = stackMallocInt(1);
        stackPush();
        IntBuffer sampleRateB = stackMallocInt(1);

        ShortBuffer rawAudio = stb_vorbis_decode_filename(filePath,channelsB,sampleRateB);

        if(rawAudio == null){
            System.out.println("[SOUND NOT FOUND] " + filePath);
            stackPop();
            stackPop();
            return;
        }

        int channels = channelsB.get();
        int sampleRate = sampleRateB.get();

        stackPop();
        stackPop();

        int format = -1;
        if(channels == 1){
            format = AL_FORMAT_MONO16;
        }
        else if(channels == 2){
            format = AL_FORMAT_STEREO16;
        }

        bufferId = alGenBuffers();
        alBufferData(bufferId,format,rawAudio,sampleRate);

        sourceId = alGenSources();
        alSourcei(sourceId,AL_BUFFER,bufferId);
        alSourcei(sourceId,AL_LOOPING,loops ? 1 : 0);
        alSourcei(sourceId,AL_POSITION,0);
        alSourcef(sourceId,AL_GAIN,0.3f);

        free(rawAudio);
    }

    public void delete(){
        alDeleteBuffers(bufferId);
        alDeleteSources(sourceId);
    }

    public void play(){
        int state = alGetSourcei(sourceId,AL_SOURCE_STATE);
        if(state == AL_STOPPED){
            isPlaying = false;
            alSourcei(sourceId,AL_POSITION,0);
        }

        if(!isPlaying){
            alSourcePlay(sourceId);
            isPlaying = true;
        }
    }

    public void stop(){
        if(isPlaying){
            alSourceStop(sourceId);
            isPlaying = false;
        }
    }
}
