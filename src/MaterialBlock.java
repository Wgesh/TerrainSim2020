public class MaterialBlock {
    
    private final String type; // dirt, stone, water, air
    private final boolean hillBase; // if the block is a hill base
    private final int hillStrength; // decides how high a hill protrudes over this block
    
    public MaterialBlock(String type, boolean hillBase, int hillStrength) {
        this.type = type;
        this.hillBase = hillBase;
        this.hillStrength = hillStrength;
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
}
