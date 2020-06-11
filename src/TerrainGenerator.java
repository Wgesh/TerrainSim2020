/*
* TODO: ?
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
    private static int hillFreqParamTemp; // frequency of hills
    private static double hillHeightParamTemp; // height of hills
    private static int hillWidthParamTemp; // width of hills
    
    // checks all parameters
    public static boolean parameterCheck(int x, int z, int grassParam, double flatParam, int hillFreqParam, double hillHeightParam, int hillWidthParam) {
        if ((x >= 15 && z >= 15) && (x <=  600 && z <= 600) && (grassParam >= 0 && grassParam < z - 2) && (flatParam >= 0.0 && flatParam <= 1.0) && 
                (hillFreqParam >= 0 && hillFreqParam <= 100) && (hillHeightParam >= 0.0 && hillHeightParam <= 1.0) && (hillWidthParam >= 0 && hillWidthParam <= 40)) {
            return true;
        } else {
            return false;
        }
    }
    
    // generates terrain in the blocks array
    public static void generateTerrain(int x, int z, int grassParam, double flatParam, int hillFreqParam, double hillHeightParam, int hillWidthParam) {
        blocks = new MaterialBlock[x][z];
        totalBlocks = x * z;
        
        // set variables so they can be used throughout the class
        tempZPos = z - 1;
        
        grassLevel = grassParam;
        flatParamTemp = flatParam;
        
        hillFreqParamTemp = hillFreqParam;
        hillHeightParamTemp = hillHeightParam;
        hillWidthParamTemp = hillWidthParam;
        
        for (int i = 0; i < totalBlocks; i ++) {
            if (i > x) { // once past the bottom layer check if block being placed is over a hill base
                grassLevel = grassLevel + blocks[tempXPos][0].getHillStrength() + 1;
            }
            
            dropBlock();
            
            grassLevel = grassParam; // reset the grassLevel back to regular
            
            if (i == x) { // after first row done, generate where hills be
                hillSetup(x);
            }
            
            tempXPos ++;
            tempZPos = z - 1;
            if (tempXPos == x) { // once reached right side of grid dimensions, go back to left
                tempXPos = 0;
            }
        }
    }
    
    // "drops" a block into a position and decides what type it should be
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
                } else if (grassPlaceCheck()) {
                    block = new MaterialBlock("grass");
                } else if ((int)(Math.random()*randomDirtStoneCoefficient)+1 == 1) {
                    block = new MaterialBlock("dirt");
                } else if ((int)(Math.random()*28)+1 == 1) {
                    block = new MaterialBlock("gravel");
                } else {
                    block = new MaterialBlock("stone");
                }
            } else {
                if ((int)(Math.random()*randomDirtStoneCoefficient)+1 == 1) {
                    block = new MaterialBlock("dirt");
                } else if ((int)(Math.random()*28)+1 == 1) {
                    block = new MaterialBlock("gravel");
                } else {
                    block = new MaterialBlock("stone");
                }
            }

        
        // setblock into array
        blocks[tempXPos][tempZPos] = block;
        
        
    }
    
    // defines hills
    private static void hillSetup(int x) {
        
        int hillWidthCounter = 0; // keeps hill within hillWidthParamTemp
        boolean hillAscend = true; // ascending edge of hill or not
        boolean flipped = false; // if hill ascend has been flipped or not for the current hill
        
        for (int i = 0; i < x; i ++) { // go through whole bottom layer
            if (i > 0 && blocks[i - 1][0].isHill() && hillWidthCounter < hillWidthParamTemp) { // hill started directly to left?
                if (hillAscend) { // ascending edge of hill
                    // decide how steep the next block should be
                    if (Math.random() <= hillHeightParamTemp - 0.2) { // even if hill height 1 dont always jump up 2 blocks
                        blocks[i][0].setHill(true, blocks[i - 1][0].getHillStrength() + 2);
                        hillWidthCounter ++;
                    } else if (Math.random() <= hillHeightParamTemp) {
                        blocks[i][0].setHill(true, blocks[i - 1][0].getHillStrength() + 1);
                        hillWidthCounter ++;
                    } else {
                        blocks[i][0].setHill(true, blocks[i - 1][0].getHillStrength());
                        hillWidthCounter ++;
                    }
                } else { // descending edge of hill
                    // decide how steep the next block should be
                    if (blocks[i - 1][0].getHillStrength() == 1) {
                        blocks[i][0].setHill(true, 1);
                        hillWidthCounter ++;
                    } else if (Math.random() <= hillHeightParamTemp - 0.2 && blocks[i - 1][0].getHillStrength() >= 3) { // make sure hill doesnt go negative
                        blocks[i][0].setHill(true, blocks[i - 1][0].getHillStrength() - 2);
                        hillWidthCounter ++;
                    }
                    else if(Math.random() <= hillHeightParamTemp) {
                        blocks[i][0].setHill(true, blocks[i - 1][0].getHillStrength() - 1);
                        hillWidthCounter ++;
                    } else {
                        blocks[i][0].setHill(true, blocks[i - 1][0].getHillStrength());
                        hillWidthCounter ++;
                    }
                }
                
                // flips to descend mode once around the middle of hill
                if (hillWidthCounter >= (hillWidthParamTemp / 2) + ((int)(Math.random()*4)-2) && flipped == false) {
                    hillAscend = !hillAscend;
                    flipped = !flipped;
                }
                
            } else if ((((int)(Math.random()*(202 - hillFreqParamTemp*2))+1) == 1) && hillFreqParamTemp > 0) { // randomly decide to start hill
                blocks[i][0].setHill(true, 1);
                
                hillWidthCounter ++;
            } else { // reset variables
                hillWidthCounter = 0;
                hillAscend = true;
                flipped = false;
            }
        }
    }
    
    // checks if block placed should be grass
    private static boolean grassPlaceCheck() { // checks whether to place grass or not
   
            if (tempZPos >= grassLevel + 2) {
                return true; // forces grass placement
            } else if (tempXPos > 0) {
                if (blocks[tempXPos - 1][tempZPos].getType().equals("grass") && (Math.random() <= flatParamTemp && !blocks[tempXPos][0].isHill())){
                    return true; // randomly decides grass placement based off flatParamTemp
                } else if (tempZPos >= grassLevel - 1) {
                    // random grass placement
                    if ((int)(Math.random()*6)+1 == 1) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else if (tempZPos >= grassLevel - 1) {
                // random grass placement 
                if ((int)(Math.random()*6)+1 == 1) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
  
    }
    
    // returns blocks array
    public static MaterialBlock[][] blocksArray() {
        return blocks;
    }
    
    // prints blocks into output for testing
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
    
    // prints bottom layer blocks to output for testing
    public static void printBottomlayer(int x) {
        System.out.println("");
        for (int k = 0; k < x; k ++) {
            System.out.print(blocks[k][0].getHillStrength() + "   ");
        }
    }
}
