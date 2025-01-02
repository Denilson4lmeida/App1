package sounds;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Sounds {

    public void tocarSom(String caminhoDoArquivo) {
        try {
            File arquivoDeSom = new File(caminhoDoArquivo);
            if (arquivoDeSom.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(arquivoDeSom);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
            } else {
                System.out.println("Arquivo de som não encontrado: " + caminhoDoArquivo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
