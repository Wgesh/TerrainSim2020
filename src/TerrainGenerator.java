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
    private static double flatParamTemp; // how flat the top grass should be
    
    public static boolean parameterCheck(int x, int z, int grassParam, double flatParam, int hillFreqParam, int hillHeightParam) {
        if ((x >= 15 && z >= 15) && (x <=  600 && z <= 600) && (grassParam >= 0 && grassParam < z - 2) && (flatParam >= 0.0 && flatParam <= 1.0) && 
                (hillFreqParam >= 0 && hillFreqParam <= 100) && (hillHeightParam >= 0 && hillHeightParam <= 100)) {
            return true;
        } else {
            return false;
        }
    }
    
    public static void generateTerrain(int x, int z, int grassParam, double flatParam, int hillFreqParam, int hillHeightParam /*possibly y*/ /*other parameters: hill size, water stuff, thickness of layers etc*/) {
        blocks = new MaterialBlock[x][z];
        totalBlocks = x * z;
        
        tempZPos = z - 1;
        
        grassLevel = grassParam;
        flatParamTemp = flatParam;
        
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
                block = new MaterialBlock("air", false, 0);
            } else if (grassPlaceCheck()) {
                block = new MaterialBlock("grass", false, 0);
            } else if ((int)(Math.random()*randomDirtStoneCoefficient)+1 == 1) {
                block = new MaterialBlock("dirt", false, 0);
            } else {
                block = new MaterialBlock("stone", false, 0);
            }
        } else {
            if ((int)(Math.random()*randomDirtStoneCoefficient)+1 == 1) {
                block = new MaterialBlock("dirt", false, 0);
            } else {
                block = new MaterialBlock("stone", false, 0);
            }
        }
        
        // setblock into array
        blocks[tempXPos][tempZPos] = block;
        
        
    }
    
    private static void hillSetup() {
        
    }
    
    private static boolean grassPlaceCheck() { // checks whether to place grass or not
        try {
            if (tempZPos >= grassLevel + 2) {
                return true;
            } else if (blocks[tempXPos - 1][tempZPos].getType().equals("grass") && (Math.random() <= flatParamTemp)) {
                    return true;
            } else if (tempZPos >= grassLevel - 1) {
                if ((int)(Math.random()*6)+1 == 1) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            if (tempZPos >= grassLevel - 1) {
                return true;
            } else {
                return false;
            }
        }
    }
    
    public static MaterialBlock[][] blocksArray() {
        return blocks;
    }
    
    public static void printBlocks(int x, int z) { // used for initial testing
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
