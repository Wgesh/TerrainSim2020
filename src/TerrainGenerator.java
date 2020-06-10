/*
* TODO: deal with if grass generates underground
* TODO: make things adjustable in code
* TODO: code in hills and mountains
* TODO: code in water spots?
* TODO: different material landscapes
* TODO: grass must place within certain range of the grassLevel var
*/

public class TerrainGenerator {
    
    //private static ArrayList<MaterialBlock> blocks = new ArrayList<>();
    private static MaterialBlock[][] blocks; // stores all blocks and positions
    
    private static int totalBlocks; // the total amount of spaces within a grid
    private static int tempXPos = 0; // storing where blocks will be placed
    private static int tempZPos; // storing where blocks will be placed
    private static int randomDirtStoneCoefficient; // decides probability of dirt generating
    private static int grassLevel; // the grass level within the grid, where air is above and dirt generated procedurally underneath
    
    public static boolean parameterCheck(int x, int z, int grassParam, int hillFreqParam, int hillHeightParam) {
        if ((x >= 15 && z >= 15) && (x <=  600 && z <= 600) && (grassParam >= 0 && grassParam < z) && (hillFreqParam >= 0 && hillFreqParam <= 100) &&
                (hillHeightParam >= 0 && hillHeightParam <= 100)) {
            return true;
        } else {
            return false;
        }
    }
    
    public static void generateTerrain(int x, int z, int grassParam, int hillFreqParam, int hillHeightParam /*possibly y*/ /*other parameters: hill size, water stuff, thickness of layers etc*/) {
        blocks = new MaterialBlock[x][z];
        totalBlocks = x * z;
        
        tempZPos = z - 1;
        
        grassLevel = (int)((double)z * 0.6); // set grass level to 60% above bottom
        
        for (int i = 0; i < totalBlocks; i ++) {
            dropBlock();
            tempXPos ++;
            tempZPos = z - 1;
            if (tempXPos > x - 1) {
                tempXPos = 0;
            }
        }
    }
    
    private static void dropBlock() {
        
        MaterialBlock block;
        
        // keep finding pos for block until another block directly underneath or hit bottom of grid
        while (blocks[tempXPos][tempZPos - 1] == null) {
            tempZPos --;
            
            if (tempZPos == 0) {
                break;
            }
        }
        
        // decide probability of dirt placement
        if (tempZPos < grassLevel - 11) { // 1/35 chance at 11+ blocks under grassLevel
            randomDirtStoneCoefficient = 35;
        } else if (tempZPos < grassLevel - 8) { // 1/6 chance at 8-10 blocks under grassLevel
            randomDirtStoneCoefficient = 6;
        } else if (tempZPos < grassLevel - 5) { // 1/3 chance at 5-7 blocks under ""
            randomDirtStoneCoefficient = 3;
        } else { // 1/1 chance at 4-0 blocks under ""
            randomDirtStoneCoefficient = 1;
        }

        // choose the type of block
        if (tempZPos > 0) {
            if (blocks[tempXPos][tempZPos - 1].getType().equals("grass") || blocks[tempXPos][tempZPos - 1].getType().equals("air")) {
                block = new MaterialBlock("air");
            } else if (checkDirtThickness()) {
                block = new MaterialBlock("grass");
            } else if ((int)(Math.random()*randomDirtStoneCoefficient)+1 == 1) {
                block = new MaterialBlock("dirt");
            } else {
                block = new MaterialBlock("stone");
            }
        } else {
            if ((int)(Math.random()*randomDirtStoneCoefficient)+1 == 1) {
                block = new MaterialBlock("dirt");
            } else {
                block = new MaterialBlock("stone");
            }
        }
        
        // setblock into array
        blocks[tempXPos][tempZPos] = block;
        
        
    }
    
    private static boolean checkDirtThickness() {
        for (int i = 0; i < 5; i ++) {
            if (tempZPos - i < 0) {
                return false;
            }
            
            if (blocks[tempXPos][tempZPos - i] == null) {
                
            } else {
                // if any block within 4 blocks under the given coordinate is not dirt return false
                if (!(blocks[tempXPos][tempZPos - i].getType().equals("dirt"))) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static MaterialBlock[][] blocksArray() {
        return blocks;
    }
    
    public static void printBlocks(int x, int z) {
        System.out.println("");
        for (int i = z - 1; i > -1; i --) {
            for (int k = 0; k < x; k ++) {
                if (blocks[k][i].getType().equals("air")) {
                    System.out.print(blocks[k][i].getType() + "   ");
                } else if (blocks[k][i].getType().equals("dirt")) {
                    System.out.print(blocks[k][i].getType() + "  ");
                } else {
                    System.out.print(blocks[k][i].getType() + " ");
                }
                
            }
            System.out.println("");
        }
    }
}
