import java.awt.Color;
import java.awt.Graphics;

public class Drawer {
    
    public static void drawTerrain(Graphics g, int panelHeight, int panelWidth, MaterialBlock[][] blocks, int x, int z) {
        g.setColor(new Color(115, 225, 255)); // blue for sky
        g.fillRect(0, 0, panelWidth, panelHeight);

        int blockSizeX = panelWidth / x;
        int blockSizeZ = panelHeight / z;
        
        for (int i = 0; i < z; i ++) {
            for (int k = 0; k < x; k ++) {
                if (blocks[k][i].getType().equals("stone")) {
                    g.setColor(new Color(92, 92, 92)); // gray for stone
                    g.fillRect(blockSizeX * k, panelHeight - (blockSizeZ * i), blockSizeX, blockSizeZ);
                } else if (blocks[k][i].getType().equals("dirt")) {
                    g.setColor(new Color(99, 58, 7)); // brown for dirt
                    g.fillRect(blockSizeX * k, panelHeight - (blockSizeZ * i), blockSizeX, blockSizeZ);
                } else if (blocks[k][i].getType().equals("grass")) {
                    g.setColor(new Color(26, 150, 9)); // green for grass
                    g.fillRect(blockSizeX * k, panelHeight - (blockSizeZ * i), blockSizeX, blockSizeZ);
                } else { // air basically
                    
                }   
            }
        }
    }
    
}
