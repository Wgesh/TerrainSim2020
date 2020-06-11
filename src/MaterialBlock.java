public class MaterialBlock {
    
    private final String type; // stone, dirt, grass, air
    private boolean hillBase = false; // if the block is a hill base
    private int hillStrength = 0; // decides how high a hill protrudes over this block
    
    public MaterialBlock(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
    
    public boolean isHill() {
        return hillBase;
    }
    
    public int getHillStrength() {
        return hillStrength;
    }
    
    public void setHill(boolean hillBase, int hillStrength) {
        this.hillBase = hillBase;
        this.hillStrength = hillStrength;
    }
}
